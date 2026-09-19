package com.cosmicbarri.crystal_gacha.registry;

import com.cosmicbarri.crystal_gacha.CrystalGacha;
import com.cosmicbarri.crystal_gacha.item.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ItemRegistry {
    public static final DeferredRegister<Item> REGISTRY = DeferredRegister.create(ForgeRegistries.ITEMS, CrystalGacha.MODID);
    public static final RegistryObject<Item> COMMON_CRYSTAL = REGISTRY.register("common_crystal", () -> new CrystalItem(Rarity.COMMON));
    public static final RegistryObject<Item> UNCOMMON_CRYSTAL = REGISTRY.register("uncommon_crystal", () -> new CrystalItem(Rarity.UNCOMMON));
    public static final RegistryObject<Item> RARE_CRYSTAL = REGISTRY.register("rare_crystal", () -> new CrystalItem(Rarity.RARE));
    public static final RegistryObject<Item> EPIC_CRYSTAL = REGISTRY.register("epic_crystal", () -> new CrystalItem(Rarity.EPIC));
    public static final RegistryObject<Item> GACHA_SPAWN_EGG = REGISTRY.register("crystal_gacha_spawn_egg", () -> new ForgeSpawnEggItem(EntityRegistry.GACHA,0x4a4847,0x2d4418, new Item.Properties()));
}
