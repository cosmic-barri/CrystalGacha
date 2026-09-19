package com.cosmicbarri.crystal_gacha.item;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CrystalItem extends Item {
    public CrystalItem(Rarity rarity) {
        super(new Properties().rarity(rarity));
    }

    public ItemStack getRandomPotion(RandomSource random, ItemStack type) {
        var potionsMap = BuiltInRegistries.POTION.asHolderIdMap();

        if (potionsMap.size() == 0) {
            return new ItemStack(type.getItem());
        }

        Potion randomPotion;
        do {
            var holder = potionsMap.byId(random.nextInt(potionsMap.size()));
            assert holder != null;
            randomPotion = holder.value();
        } while (randomPotion == Potions.EMPTY || randomPotion == Potions.WATER);

        ItemStack potionStack = new ItemStack(type.getItem());
        PotionUtils.setPotion(potionStack, randomPotion);

        return potionStack;
    }

    public ItemStack getRandomEnchantedBook(RandomSource random) {
        var enchantments = BuiltInRegistries.ENCHANTMENT.asHolderIdMap();

        if (enchantments.size() == 0) {
            return new ItemStack(Items.BOOK);
        }

        Holder<Enchantment> randomHolder = enchantments.byId(random.nextInt(enchantments.size()));
        assert randomHolder != null;
        Enchantment enchantment = randomHolder.value();

        int minLevel = enchantment.getMinLevel();
        int maxLevel = enchantment.getMaxLevel();
        int randomLevel = minLevel + random.nextInt((maxLevel - minLevel) + 1);

        ItemStack enchantedBook = new ItemStack(Items.ENCHANTED_BOOK);
        EnchantedBookItem.addEnchantment(enchantedBook, new EnchantmentInstance(enchantment, randomLevel));

        return enchantedBook;
    }

    public void dropRandomItem(Level level, double x, double y, double z) {
        if (level.isClientSide()) return;

        Rarity rarity = this.getDefaultInstance().getRarity();

        CrystalCache.getRandomItem(rarity, level.random).ifPresent(randomItem -> {
            ItemStack stack = new ItemStack(randomItem);

            if (stack.is(Items.ENCHANTED_BOOK)) {
                stack = getRandomEnchantedBook(level.random);
            } else if (stack.is(Items.POTION) || stack.is(Items.SPLASH_POTION) || stack.is(Items.LINGERING_POTION)) {
                stack = getRandomPotion(level.random, stack);
            }

            ItemEntity entity = new ItemEntity(level, x, y + 0.5, z, stack);
            entity.setDefaultPickUpDelay();
            level.addFreshEntity(entity);
            });
        level.playSound(null, new BlockPos((int) x, (int) y, (int) z), SoundEvents.AMETHYST_CLUSTER_BREAK, SoundSource.AMBIENT, 1, 1);
    }

    @Override
    public @NotNull InteractionResultHolder<ItemStack> use(@NotNull Level level, Player player, @NotNull InteractionHand hand) {
        ItemStack item = player.getItemInHand(hand);
        if (hand != InteractionHand.MAIN_HAND) return InteractionResultHolder.pass(item);
        if (!level.isClientSide) {
            dropRandomItem(level, player.getX(), player.getY(), player.getZ());
            item.shrink(1);
        }
        return InteractionResultHolder.sidedSuccess(item, level.isClientSide());
    }
}