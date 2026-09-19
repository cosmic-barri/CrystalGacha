package com.cosmicbarri.crystal_gacha;

import com.cosmicbarri.crystal_gacha.registry.EntityRegistry;
import com.cosmicbarri.crystal_gacha.registry.ItemRegistry;
import com.cosmicbarri.crystal_gacha.registry.TabRegistry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(CrystalGacha.MODID)
public class CrystalGacha {
    public static final String MODID = "crystal_gacha";

    public CrystalGacha() {
        IEventBus bus = FMLJavaModLoadingContext.get().getModEventBus();
        MinecraftForge.EVENT_BUS.register(this);
        EntityRegistry.REGISTRY.register(bus);
        ItemRegistry.REGISTRY.register(bus);
        TabRegistry.REGISTRY.register(bus);
    }
}
