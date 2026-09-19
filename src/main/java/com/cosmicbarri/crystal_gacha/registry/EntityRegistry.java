package com.cosmicbarri.crystal_gacha.registry;

import com.cosmicbarri.crystal_gacha.CrystalGacha;
import com.cosmicbarri.crystal_gacha.entity.GachaEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
public class EntityRegistry {
    public static final DeferredRegister<EntityType<?>> REGISTRY = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, CrystalGacha.MODID);

    public static final RegistryObject<EntityType<GachaEntity>> GACHA = REGISTRY.register("crystal_gacha",
            () -> EntityType.Builder.<GachaEntity>of(GachaEntity::new, MobCategory.CREATURE).setShouldReceiveVelocityUpdates(true).setTrackingRange(64).setUpdateInterval(3).setCustomClientFactory(GachaEntity::new)
                    .sized(3.8f, 3.8f).build("crystal_gacha"));

    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(GACHA.get(), GachaEntity.createAttributes().build());
    }
}
