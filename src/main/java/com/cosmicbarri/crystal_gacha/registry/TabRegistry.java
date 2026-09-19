package com.cosmicbarri.crystal_gacha.registry;

import com.cosmicbarri.crystal_gacha.CrystalGacha;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class TabRegistry {
    public static final DeferredRegister<CreativeModeTab> REGISTRY = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CrystalGacha.MODID);
    public static final RegistryObject<CreativeModeTab> MOD_TAB = REGISTRY.register("crystal_gacha_tab",
            () -> CreativeModeTab.builder().title(Component.translatable("item_group.crystal_gacha.crystal_gacha_tab")).icon(() -> new ItemStack(ItemRegistry.EPIC_CRYSTAL.get()))
                    .displayItems((parameters, tabData) -> {
                        tabData.accept(ItemRegistry.GACHA_SPAWN_EGG.get().asItem());
                        tabData.accept(ItemRegistry.COMMON_CRYSTAL.get().asItem());
                        tabData.accept(ItemRegistry.UNCOMMON_CRYSTAL.get().asItem());
                        tabData.accept(ItemRegistry.RARE_CRYSTAL.get().asItem());
                        tabData.accept(ItemRegistry.EPIC_CRYSTAL.get().asItem());
                    }).build());
}
