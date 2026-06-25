/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  org.zeith.hammerlib.annotations.OnlyIf
 *  org.zeith.hammerlib.annotations.RegistryName
 *  org.zeith.hammerlib.annotations.SimplyRegister
 */
package org.zeith.improvableskills.init;

import org.zeith.hammerlib.annotations.OnlyIf;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.improvableskills.cfg.ConfigsIS;
import org.zeith.improvableskills.custom.skills.SkillAcceleratedFurnace;
import org.zeith.improvableskills.custom.skills.SkillAlchemist;
import org.zeith.improvableskills.custom.skills.SkillAtkDmgMelee;
import org.zeith.improvableskills.custom.skills.SkillAtkDmgRanged;
import org.zeith.improvableskills.custom.skills.SkillAttackSpeed;
import org.zeith.improvableskills.custom.skills.SkillCutting;
import org.zeith.improvableskills.custom.skills.SkillDexterousArms;
import org.zeith.improvableskills.custom.skills.SkillDigging;
import org.zeith.improvableskills.custom.skills.SkillEnchanter;
import org.zeith.improvableskills.custom.skills.SkillEnderManipulator;
import org.zeith.improvableskills.custom.skills.SkillFastSwimmer;
import org.zeith.improvableskills.custom.skills.SkillGenericProtection;
import org.zeith.improvableskills.custom.skills.SkillGrowth;
import org.zeith.improvableskills.custom.skills.SkillHealth;
import org.zeith.improvableskills.custom.skills.SkillHuckster;
import org.zeith.improvableskills.custom.skills.SkillLadderKing;
import org.zeith.improvableskills.custom.skills.SkillLeaper;
import org.zeith.improvableskills.custom.skills.SkillLuckOfTheSea;
import org.zeith.improvableskills.custom.skills.SkillMining;
import org.zeith.improvableskills.custom.skills.SkillMobRepellent;
import org.zeith.improvableskills.custom.skills.SkillObsidianSkin;
import org.zeith.improvableskills.custom.skills.SkillPVP;
import org.zeith.improvableskills.custom.skills.SkillSilentFoot;
import org.zeith.improvableskills.custom.skills.SkillSoftLanding;
import org.zeith.improvableskills.custom.skills.SkillSoulSpeed;
import org.zeith.improvableskills.custom.skills.SkillTreasureSands;
import org.zeith.improvableskills.custom.skills.SkillXPPlus;

@SimplyRegister
public interface SkillsIS {
    @RegistryName(value="accelerated_furnace")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillAcceleratedFurnace ACCELERATED_FURNACE = new SkillAcceleratedFurnace();
    @RegistryName(value="fast_swimmer")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillFastSwimmer FAST_SWIMMER = new SkillFastSwimmer();
    @RegistryName(value="leaper")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillLeaper LEAPER = new SkillLeaper();
    @RegistryName(value="ladder_king")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillLadderKing LADDER_KING = new SkillLadderKing();
    @RegistryName(value="soft_landing")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillSoftLanding SOFT_LANDING = new SkillSoftLanding();
    @RegistryName(value="attack_speed")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillAttackSpeed ATTACK_SPEED = new SkillAttackSpeed();
    @RegistryName(value="mining")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillMining MINING = new SkillMining();
    @RegistryName(value="digging")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillDigging DIGGING = new SkillDigging();
    @RegistryName(value="cutting")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillCutting CUTTING = new SkillCutting();
    @RegistryName(value="obsidian_skin")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillObsidianSkin OBSIDIAN_SKIN = new SkillObsidianSkin();
    @RegistryName(value="luck_of_the_sea")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillLuckOfTheSea LUCK_OF_THE_SEA = new SkillLuckOfTheSea();
    @RegistryName(value="health")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillHealth HEALTH = new SkillHealth();
    @RegistryName(value="growth")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillGrowth GROWTH = new SkillGrowth();
    @RegistryName(value="alchemist")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillAlchemist ALCHEMIST = new SkillAlchemist();
    @RegistryName(value="generic_protection")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillGenericProtection GENERIC_PROTECTION = new SkillGenericProtection();
    @RegistryName(value="treasure_sands")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillTreasureSands TREASURE_OF_SANDS = new SkillTreasureSands();
    @RegistryName(value="atkdmg_melee")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillAtkDmgMelee DAMAGE_MELEE = new SkillAtkDmgMelee();
    @RegistryName(value="atkdmg_ranged")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillAtkDmgRanged DAMAGE_RANGED = new SkillAtkDmgRanged();
    @RegistryName(value="pvp")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillPVP PVP = new SkillPVP();
    @RegistryName(value="enchanter")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillEnchanter ENCHANTER = new SkillEnchanter();
    @RegistryName(value="ender_manipulator")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillEnderManipulator ENDER_MANIPULATOR = new SkillEnderManipulator();
    @RegistryName(value="xp_plus")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillXPPlus XP_PLUS = new SkillXPPlus();
    @RegistryName(value="silent_foot")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillSilentFoot SILENT_FOOT = new SkillSilentFoot();
    @RegistryName(value="dexterous_arms")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillDexterousArms DEXTEROUS_ARMS = new SkillDexterousArms();
    @RegistryName(value="soul_speed")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillSoulSpeed SOUL_SPEED = new SkillSoulSpeed();
    @RegistryName(value="huckster")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillHuckster HUCKSTER = new SkillHuckster();
    @RegistryName(value="mob_repellent")
    @OnlyIf(owner=ConfigsIS.class, member="enableSkill")
    public static final SkillMobRepellent MOB_REPELLENT = new SkillMobRepellent();
}

