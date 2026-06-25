/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.fml.loading.FMLEnvironment
 *  org.zeith.hammerlib.annotations.SetupConfigs
 *  org.zeith.hammerlib.util.configured.ConfigFile
 *  org.zeith.hammerlib.util.configured.ConfiguredLib
 *  org.zeith.hammerlib.util.configured.data.IntValueRange
 *  org.zeith.hammerlib.util.configured.types.ConfigArray
 *  org.zeith.hammerlib.util.configured.types.ConfigBoolean
 *  org.zeith.hammerlib.util.configured.types.ConfigCategory
 *  org.zeith.hammerlib.util.configured.types.ConfigInteger
 *  org.zeith.hammerlib.util.configured.types.ConfigString
 */
package org.zeith.improvableskills.cfg;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;
import org.zeith.hammerlib.annotations.SetupConfigs;
import org.zeith.hammerlib.util.configured.ConfigFile;
import org.zeith.hammerlib.util.configured.ConfiguredLib;
import org.zeith.hammerlib.util.configured.data.IntValueRange;
import org.zeith.hammerlib.util.configured.types.ConfigArray;
import org.zeith.hammerlib.util.configured.types.ConfigBoolean;
import org.zeith.hammerlib.util.configured.types.ConfigCategory;
import org.zeith.hammerlib.util.configured.types.ConfigInteger;
import org.zeith.hammerlib.util.configured.types.ConfigString;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;

public class ConfigsIS {
    public static ConfigFile config;
    private static ConfigCategory gameplay;
    public static boolean xpBank;
    public static boolean addBookToInv;
    public static boolean parchmentGeneration;
    public static boolean dropScrollsAfterUnlock;
    public static boolean autouseScrolls;
    public static int parchmentRarity;
    public static List<String> blockedParchmentChests;

    @SetupConfigs
    public static void reloadCustom(ConfigFile cfgs) {
        config = cfgs;
        gameplay = (ConfigCategory)cfgs.setupCategory("Gameplay").withComment("Gameplay affecting features");
        xpBank = ((ConfigBoolean)((ConfigBoolean)gameplay.getElement(ConfiguredLib.BOOLEAN, "XP Storage")).withDefault(true).withComment("Should XP Bank be active in the book? Disabling this only hides the skill from the player.")).getValue();
        dropScrollsAfterUnlock = ((ConfigBoolean)((ConfigBoolean)gameplay.getElement(ConfiguredLib.BOOLEAN, "Drop Scrolls After Unlock")).withDefault(true).withComment("Should scrolls still drop for players that have unlocked the skill already, but haven't yet maxxed it out?")).getValue();
        autouseScrolls = ((ConfigBoolean)((ConfigBoolean)gameplay.getElement(ConfiguredLib.BOOLEAN, "Auto-Use Scrolls")).withDefault(false).withComment("Enabling this option will automatically use scrolls once they enter player's inventory.")).getValue();
        ConfigCategory parchmentFragment = (ConfigCategory)((ConfigCategory)gameplay.getElement(ConfiguredLib.CATEGORY, "Parchment Fragment")).withComment("Various configurations for parchment fragment");
        parchmentGeneration = ((ConfigBoolean)((ConfigBoolean)parchmentFragment.getElement(ConfiguredLib.BOOLEAN, "Do Generation")).withDefault(true).withComment("Should parchment fragment appear in naturally generated chests?")).getValue();
        parchmentRarity = ((ConfigInteger)parchmentFragment.getElement(ConfiguredLib.INT, "WorldGen Rarity")).withRange(IntValueRange.range((long)1L, (long)Integer.MAX_VALUE)).withDefault(10).withComment("How rare should parchment fragment be? Higher values make the fragment appear less frequently inside chests.").getValue().intValue();
        boolean init = !parchmentFragment.getValue().containsKey("Chest Blocklist");
        ConfigArray bc = ((ConfigArray)parchmentFragment.getElement(ConfiguredLib.STRING.arrayOf(), "Chest Blocklist")).withComment("Which chests should be blocked from generating fragments?");
        List lst = bc.getElements();
        if (init) {
            ResourceLocation[] locs;
            for (ResourceLocation i : locs = new ResourceLocation[]{BuiltInLootTables.f_78743_, BuiltInLootTables.f_78744_, BuiltInLootTables.f_78745_, BuiltInLootTables.f_78746_, BuiltInLootTables.f_78747_, BuiltInLootTables.f_78748_, BuiltInLootTables.f_78749_, BuiltInLootTables.f_78750_, BuiltInLootTables.f_78751_, BuiltInLootTables.f_78752_, BuiltInLootTables.f_78753_, BuiltInLootTables.f_78754_, BuiltInLootTables.f_78755_, BuiltInLootTables.f_78756_, BuiltInLootTables.f_78757_, BuiltInLootTables.f_78758_}) {
                lst.add(((ConfigString)bc.createElement()).withDefault(i.toString()));
            }
        }
        blockedParchmentChests = bc.getElements().stream().map(ConfigString::getValue).toList();
        if (FMLEnvironment.dist == Dist.CLIENT) {
            ConfigCategory clientSide = (ConfigCategory)cfgs.setupCategory("Client-side").withComment("Features that only matter when the mod is loaded on client.");
            addBookToInv = ((ConfigBoolean)((ConfigBoolean)clientSide.getElement(ConfiguredLib.BOOLEAN, "Add Book to Inventory")).withDefault(true).withComment("Should ImprovableSkills add it's Book of Skills into player's inventory?")).getValue();
        }
    }

    public static void reloadCosts() {
        ConfigCategory costs = (ConfigCategory)gameplay.setupSubCategory("Costs").withComment("Configure how expensive each skill is");
        for (PlayerSkillBase skill : ImprovableSkills.SKILLS()) {
            skill.xpCalculator.load(costs, skill.getRegistryName().toString().replace(":", "_"));
        }
    }

    public static boolean enableSkill(PlayerSkillBase skill, ResourceLocation id) {
        return ((ConfigBoolean)((ConfigBoolean)((ConfigCategory)config.setupCategory("Skills").withComment("What skills should be enabled?")).getElement(ConfiguredLib.BOOLEAN, id.toString())).withDefault(true).withComment("Should Skill \"" + skill.getUnlocalizedName(id) + "\" be added to the game?")).getValue();
    }

    public static boolean enableAbility(PlayerAbilityBase skill, ResourceLocation id) {
        return ((ConfigBoolean)((ConfigBoolean)((ConfigCategory)config.setupCategory("Abilities").withComment("What abilities should be enabled?")).getElement(ConfiguredLib.BOOLEAN, id.toString())).withDefault(true).withComment("Should Ability \"" + skill.getUnlocalizedName(id) + "\" be added to the game?")).getValue();
    }

    static {
        parchmentGeneration = true;
        dropScrollsAfterUnlock = true;
        autouseScrolls = false;
        parchmentRarity = 10;
        blockedParchmentChests = List.of();
    }
}

