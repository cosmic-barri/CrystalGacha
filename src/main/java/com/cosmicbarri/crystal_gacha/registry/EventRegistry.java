package com.cosmicbarri.crystal_gacha.registry;

import com.cosmicbarri.crystal_gacha.item.CrystalCache;
import net.minecraftforge.event.server.ServerAboutToStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber
public class EventRegistry {
    @SubscribeEvent
    public static void onServerAboutToStart(ServerAboutToStartEvent event) {
        CrystalCache.buildCache();
    }
}