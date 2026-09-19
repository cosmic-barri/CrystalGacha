package com.cosmicbarri.crystal_gacha.entity;

import com.cosmicbarri.crystal_gacha.registry.EntityRegistry;
import com.cosmicbarri.crystal_gacha.registry.ItemRegistry;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.ai.goal.FloatGoal;
import net.minecraft.world.entity.ai.goal.RandomLookAroundGoal;
import net.minecraft.world.entity.ai.goal.RandomStrollGoal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.network.NetworkHooks;
import net.minecraftforge.network.PlayMessages;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.GeoAnimatable;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;
import software.bernie.geckolib.util.GeckoLibUtil;

public class GachaEntity extends PathfinderMob implements GeoEntity, Saddleable {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation IDLE = RawAnimation.begin().thenLoop("idle");
    private static final RawAnimation WALK = RawAnimation.begin().thenLoop("walk");
    private static final RawAnimation SIT = RawAnimation.begin().thenPlay("sit");
    private static final RawAnimation EAT = RawAnimation.begin().thenLoop("eat");
    private static final RawAnimation SHAKE = RawAnimation.begin().thenLoop("shake");

    private static final EntityDataAccessor<Byte> DATA_FLAGS = SynchedEntityData.defineId(GachaEntity.class, EntityDataSerializers.BYTE);
    private static final int FLAG_TAMED = 2;
    private static final int FLAG_SADDLED = 4;
    private static final int FLAG_EATING = 8;
    private static final int FLAG_SITTING = 16;
    private static final int FLAG_SHAKING = 32;

    private int charge, purity, lastEat, shakeTimer;

    public GachaEntity(PlayMessages.SpawnEntity packet, Level level) {
        this(EntityRegistry.GACHA.get(), level);
    }

    public GachaEntity(EntityType<GachaEntity> type, Level world) {
        super(type, world);
        xpReward = 0;
        setNoAi(false);
        setMaxUpStep(1.1f);
        this.setPersistenceRequired();
        this.charge = 0;
        this.purity = 100;
        this.lastEat = 41;
        this.shakeTimer = 0;
    }

    @Override
    public @NotNull Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public void addAdditionalSaveData(@NotNull CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("charge", this.charge);
        compound.putInt("purity", this.purity);
        compound.putInt("shakeTimer", this.shakeTimer);
        compound.putInt("lastEat", this.lastEat);
        compound.putBoolean("hasSaddle", this.isSaddled());
        compound.putBoolean("isTamed", this.isTamed());
        compound.putBoolean("isShaking", this.isShaking());
        compound.putBoolean("isSitting", this.isSitting());
    }

    @Override
    public void readAdditionalSaveData(@NotNull CompoundTag compound) {
        super.readAdditionalSaveData(compound);
        if (compound.contains("charge"))
            this.charge = compound.getInt("charge");
        if (compound.contains("purity"))
            this.purity = compound.getInt("purity");
        if (compound.contains("shakeTimer"))
            this.shakeTimer = compound.getInt("shakeTimer");
        if (compound.contains("lastEat"))
            this.lastEat = compound.getInt("lastEat");
        if (compound.contains("hasSaddle"))
            this.setFlag(FLAG_SADDLED, compound.getBoolean("hasSaddle"));
        if (compound.contains("isTamed"))
            this.setTamed(compound.getBoolean("isTamed"));
        if (compound.contains("isShaking"))
            this.setShaking(compound.getBoolean("isShaking"));
        if (compound.contains("isSitting"))
            this.setSitting(compound.getBoolean("isSitting"));
    }

    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_FLAGS, (byte)0);
    }

    protected boolean getFlag(int flag) {
        return (this.entityData.get(DATA_FLAGS) & flag) != 0;
    }

    protected void setFlag(int flag, boolean bool) {
        byte b0 = this.entityData.get(DATA_FLAGS);
        if (bool) {
            this.entityData.set(DATA_FLAGS, (byte)(b0 | flag));
        } else {
            this.entityData.set(DATA_FLAGS, (byte)(b0 & ~flag));
        }
    }

    private boolean isTamed() {
        return this.getFlag(FLAG_TAMED);
    }

    private void setTamed(boolean bool) {
        this.setFlag(FLAG_TAMED, bool);
    }

    private boolean isEating() {
        return this.getFlag(FLAG_EATING);
    }

    private void setEating(boolean bool) {
        this.setFlag(FLAG_EATING, bool);
    }

    private boolean isSitting() {
        return this.getFlag(FLAG_SITTING);
    }

    private void setSitting(boolean bool) {
        this.setFlag(FLAG_SITTING, bool);
    }

    private boolean isShaking() {
        return this.getFlag(FLAG_SHAKING);
    }

    private void setShaking(boolean bool) {
        this.setFlag(FLAG_SHAKING, bool);
    }

    @Override
    public boolean removeWhenFarAway(double distanceToClosestPlayer) {
        return false; //!this.isTamed();
    }

    @Override
    protected void registerGoals() {
        super.registerGoals();
        this.goalSelector.addGoal(0, new FloatGoal(this));
        this.goalSelector.addGoal(1, new RandomStrollGoal(this, 1){
            @Override
            public boolean canUse() {
                if (GachaEntity.this.isEating() || GachaEntity.this.isShaking() || GachaEntity.this.isSitting()) return false;
                return super.canUse();
            }
        });
        this.goalSelector.addGoal(2, new RandomLookAroundGoal(this){
            @Override
            public boolean canUse() {
                if (GachaEntity.this.isEating() || GachaEntity.this.isShaking() || GachaEntity.this.isSitting()) return false;
                return super.canUse();
            }
        });
    }

    @Override
    public @NotNull SoundEvent getHurtSound(@NotNull DamageSource ds) {
        return SoundEvents.STONE_HIT;
    }

    @Override
    public @NotNull SoundEvent getDeathSound() {
        return SoundEvents.STONE_BREAK;
    }

    @Override
    public boolean isPushable() {
        return false;
    }

    private void eat(ItemStack stack) {
        if (this.level().isClientSide) return;
        this.setSitting(false);
        this.lastEat = 0;
        this.setEating(true);
        this.navigation.stop();

        if (stack.getRarity() == Rarity.COMMON) {
            this.charge = this.charge + 1;
            this.purity = this.purity - 3;
        } else if (stack.getRarity() == Rarity.UNCOMMON) {
            this.charge = this.charge + 3;
            this.purity = this.purity - 2;
        } else if (stack.getRarity() == Rarity.RARE) {
            this.charge = this.charge + 5;
            this.purity = this.purity - 1;
        } else this.charge = this.charge + 7;

        stack.shrink(1);

        this.playSound(SoundEvents.STRIDER_EAT);

        if (this.charge >= 50) {
            this.shakeTimer = this.random.nextInt(100, 400);
            this.setShaking(true);
        }
    }

    private Item checkPurity() {
        if (this.purity >= 91 || this.random.nextInt(1, 100) >= 98) {
            return ItemRegistry.EPIC_CRYSTAL.get();
        } else if (this.purity >= 67 || this.random.nextInt(1, 100) >= 88) {
            return ItemRegistry.RARE_CRYSTAL.get();
        } else if (this.purity >= 30 || this.random.nextInt(1, 100) >= 78) {
            return ItemRegistry.UNCOMMON_CRYSTAL.get();
        } else return ItemRegistry.COMMON_CRYSTAL.get();
    }

    private void dropCrystal() {
        if (this.level().isClientSide || !this.isAlive()) return;
        if (!this.isTamed() && this.purity > 70) this.setTamed(true);
        ItemEntity crystal = new ItemEntity(this.level(), this.getX(), this.getY(), this.getZ(), new ItemStack(checkPurity()));
        this.level().addFreshEntity(crystal);
        this.setShaking(false);
        this.charge = 0;
        this.purity = 100;
    }

    @Override
    public void tick() {
        super.tick();
        if (this.shakeTimer == 1) dropCrystal();
        if (this.shakeTimer >= 1) this.shakeTimer--;
        if (this.lastEat <= 41) this.lastEat++;
        if (this.lastEat == 41) this.setEating(false);
    }

    private boolean canRide(Player player) {
        return this.isSaddled() && !this.isVehicle() && !player.isSecondaryUseActive() && player.getMainHandItem().isEmpty();
    }

    @Override
    public @NotNull InteractionResult mobInteract(@NotNull Player player, @NotNull InteractionHand hand) {
        if (this.isShaking() || hand != InteractionHand.MAIN_HAND) return InteractionResult.PASS;
        if (this.canRide(player)) {
            if (!this.level().isClientSide) {
                this.setSitting(false);
                player.startRiding(this);
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        } else if (player.isCrouching()) {
            if (!player.getMainHandItem().isEmpty()) {
                this.eat(player.getMainHandItem());
            } else {
                if (this.isTamed()) {
                    this.setSitting(!this.isSitting());
                }
            }
            return InteractionResult.sidedSuccess(this.level().isClientSide);
        }
        return InteractionResult.PASS;
    }

    public @Nullable Entity getRider() {
        return this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
    }

    @Override
    public double getPassengersRidingOffset() {
        return 3.25D;
    }

    @Override
    public void travel(@NotNull Vec3 dir) {
        Entity entity = this.getRider();
        if (entity instanceof LivingEntity passenger) {
            this.setYRot(entity.getYRot());
            this.yRotO = this.getYRot();
            this.setXRot(entity.getXRot() * 0.5F);
            this.setRot(this.getYRot(), this.getXRot());
            this.yBodyRot = entity.getYRot();
            this.yHeadRot = entity.getYRot();
            this.setSpeed((float) this.getAttributeValue(Attributes.MOVEMENT_SPEED));
            float forward = passenger.zza;
            float strafe = 0;
            super.travel(new Vec3(strafe, 0, forward));
            double d1 = this.getX() - this.xo;
            double d0 = this.getZ() - this.zo;
            float f1 = (float) Math.sqrt(d1 * d1 + d0 * d0) * 4;
            if (f1 > 1.0F)
                f1 = 1.0F;
            this.walkAnimation.setSpeed(this.walkAnimation.speed() + (f1 - this.walkAnimation.speed()) * 0.4F);
            this.walkAnimation.position(this.walkAnimation.position() + this.walkAnimation.speed());
            this.calculateEntityAnimation(true);
            return;
        }
        super.travel(dir);
    }

    public static AttributeSupplier.Builder createAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder = builder.add(Attributes.MOVEMENT_SPEED, 0.2);
        builder = builder.add(Attributes.MAX_HEALTH, 30);
        builder = builder.add(Attributes.ARMOR, 5);
        builder = builder.add(Attributes.ARMOR_TOUGHNESS, 5);
        builder = builder.add(Attributes.KNOCKBACK_RESISTANCE, 10);
        builder = builder.add(Attributes.ATTACK_DAMAGE, 0);
        builder = builder.add(Attributes.FOLLOW_RANGE, 16);
        return builder;
    }

    private <E extends GeoAnimatable> PlayState predicate(AnimationState<E> state) {
        if (this.isShaking())
            return state.setAndContinue(SHAKE);
        if (this.isEating())
            return state.setAndContinue(EAT);
        if (this.isSitting())
            return state.setAndContinue(SIT);
        if (state.isMoving())
            return state.setAndContinue(WALK);
        return state.setAndContinue(IDLE);
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>(this, "controller", 5, this::predicate));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean isSaddleable() {
        return this.isAlive() && this.isTamed() && !this.isSaddled();
    }

    @Override
    public void equipSaddle(@Nullable SoundSource ss) {
        this.playSound(SoundEvents.HORSE_SADDLE);
        this.setFlag(FLAG_SADDLED, true);
    }

    @Override
    public boolean isSaddled() {
        return this.getFlag(FLAG_SADDLED);
    }

    @Override
    protected void dropEquipment() {
        super.dropEquipment();
        if (this.isSaddled()) {
            this.spawnAtLocation(Items.SADDLE);
        }
    }
}