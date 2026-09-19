package com.cosmicbarri.crystal_gacha.item;

import com.cosmicbarri.crystal_gacha.Config;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.*;

public class CrystalCache {
    private static final Map<Rarity, List<Item>> CACHE = new EnumMap<>(Rarity.class);
    public static void buildCache() {
        CACHE.clear();
        for (Rarity rarity : Rarity.values()) {
            CACHE.put(rarity, new ArrayList<>());
        }

        List<? extends String> itemBlacklist = Config.ITEM_BLACKLIST.get();
        List<? extends String> modBlacklist = Config.MOD_BLACKLIST.get();
        boolean spawners = Config.SPAWNERS.get();

        for (Map.Entry<ResourceKey<Item>, Item> entry : ForgeRegistries.ITEMS.getEntries()) {
            ResourceLocation id = entry.getKey().location();
            Item item = entry.getValue();
            String itemId = id.toString();
            String namespace = id.getNamespace();

            if (modBlacklist.contains(namespace)) continue;
            if (itemBlacklist.contains(itemId)) continue;
            if (!spawners) if (item instanceof SpawnEggItem) continue;

            Rarity rarity = item.getDefaultInstance().getRarity();
            CACHE.get(rarity).add(item);
        }
    }

    public static Optional<Item> getRandomItem(Rarity rarity, RandomSource random) {
        List<Item> pool = CACHE.get(rarity);
        if (pool == null || pool.isEmpty()) return Optional.empty();
        return Optional.of(pool.get(random.nextInt(pool.size())));
    }
}