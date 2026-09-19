package com.cosmicbarri.crystal_gacha.registry;

import com.cosmicbarri.crystal_gacha.Config;
import com.cosmicbarri.crystal_gacha.CrystalGacha;
import net.minecraftforge.fml.event.lifecycle.FMLConstructModEvent;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.eventbus.api.SubscribeEvent;

@Mod.EventBusSubscriber(modid = CrystalGacha.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ConfigRegistry {
    @SubscribeEvent
    public static void register(FMLConstructModEvent event) {
        event.enqueueWork(() -> ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC, "gacha-config.toml"));
    }
}