package com.cosmicbarri.crystal_gacha;

import net.minecraftforge.common.ForgeConfigSpec;
import java.util.List;

public class Config {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> ITEM_BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<List<? extends String>> MOD_BLACKLIST;
    public static final ForgeConfigSpec.ConfigValue<Boolean> SPAWNERS;

    static {
        BUILDER.push("gacha crystal blacklisted drops");
        ITEM_BLACKLIST = BUILDER.comment("Below is all blacklisted items.").defineList("item_blacklist", List.of("crystal_gacha:common_crystal", "crystal_gacha:uncommon_crystal", "crystal_gacha:rare_crystal", "crystal_gacha:epic_crystal", "minecraft:barrier", "minecraft:command_block", "minecraft:vault", "minecraft:spawner", "minecraft:trial_spawner", "minecraft:end_portal_frame", "minecraft:bedrock", "minecraft:command_block_minecart", "minecraft:chain_command_block", "minecraft:repeating_command_block", "minecraft:debug_stick", "minecraft:structure_block", "minecraft:structure_void", "minecraft:light", "minecraft:jigsaw", "minecraft:knowledge_book"), entry -> true)
        ;
        MOD_BLACKLIST = BUILDER.comment("Below is all blacklisted mods. For example if you put here \"irons_spellbooks\" all the Iron's Spells 'n Spellbooks items will be blacklisted. You can do that with any mod.").defineList("mod_blacklist", List.of(), entry -> true)
        ;
        SPAWNERS = BUILDER.comment("True or false if you want spawn eggs to be dropped (true if you want, false if you don't).").define("spawners", false);
        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
