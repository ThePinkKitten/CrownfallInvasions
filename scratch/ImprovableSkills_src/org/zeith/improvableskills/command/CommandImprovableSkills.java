/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.CommandDispatcher
 *  com.mojang.brigadier.Message
 *  com.mojang.brigadier.arguments.ArgumentType
 *  com.mojang.brigadier.arguments.BoolArgumentType
 *  com.mojang.brigadier.arguments.IntegerArgumentType
 *  com.mojang.brigadier.builder.LiteralArgumentBuilder
 *  com.mojang.brigadier.builder.RequiredArgumentBuilder
 *  com.mojang.brigadier.context.CommandContext
 *  com.mojang.brigadier.exceptions.SimpleCommandExceptionType
 *  net.minecraft.commands.CommandBuildContext
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.Commands
 *  net.minecraft.commands.arguments.EntityArgument
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.util.java.Cast
 *  org.zeith.hammerlib.util.shaded.json.JSONObject
 */
package org.zeith.improvableskills.command;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.Message;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.cfg.ConfigsIS;
import org.zeith.improvableskills.command.SuggestionProvidersIS3;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.net.NetSkillCalculator;

public class CommandImprovableSkills {
    public static void register(CommandDispatcher<CommandSourceStack> dispatcher, CommandBuildContext context) {
        dispatcher.register((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.m_82127_((String)"improvableskills").requires(executor -> executor.m_6761_(2))).then(CommandImprovableSkills.reloadConfigs())).then(((LiteralArgumentBuilder)Commands.m_82127_((String)"book").then(CommandImprovableSkills.unlockSkillBook())).then(CommandImprovableSkills.lockSkillBook()))).then(((LiteralArgumentBuilder)((LiteralArgumentBuilder)((LiteralArgumentBuilder)Commands.m_82127_((String)"skills").then(CommandImprovableSkills.giveSkill())).then(CommandImprovableSkills.revokeSkill())).then(CommandImprovableSkills.unlockSkill())).then(CommandImprovableSkills.lockSkill()))).then(((LiteralArgumentBuilder)Commands.m_82127_((String)"abilities").then(CommandImprovableSkills.giveAbility())).then(CommandImprovableSkills.revokeAbility())));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> unlockSkillBook() {
        return (LiteralArgumentBuilder)Commands.m_82127_((String)"unlock").then(((RequiredArgumentBuilder)Commands.m_82129_((String)"targets", (ArgumentType)EntityArgument.m_91470_()).executes(src -> {
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null || pd.hasCraftedSkillsBook()) continue;
                pd.hasCraftedSkillBook = true;
                pd.sync();
                ++updated;
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)("Skills Book has been unlocked for " + updated + " players."))), true);
            return updated;
        })).then(Commands.m_82129_((String)"silent", (ArgumentType)BoolArgumentType.bool()).executes(src -> {
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null || pd.hasCraftedSkillsBook()) continue;
                pd.hasCraftedSkillBook = true;
                if (BoolArgumentType.getBool((CommandContext)src, (String)"silent")) {
                    pd.hasCraftedSkillBookPrev = true;
                }
                pd.sync();
                ++updated;
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)("Skills Book has been unlocked for " + updated + " players."))), true);
            return updated;
        })));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> lockSkillBook() {
        return (LiteralArgumentBuilder)Commands.m_82127_((String)"lock").then(Commands.m_82129_((String)"targets", (ArgumentType)EntityArgument.m_91470_()).executes(src -> {
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null || !pd.hasCraftedSkillsBook()) continue;
                pd.hasCraftedSkillBook = false;
                pd.sync();
                ++updated;
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)("Skills Book has been unlocked for " + updated + " players."))), true);
            return updated;
        }));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> reloadConfigs() {
        return (LiteralArgumentBuilder)Commands.m_82127_((String)"reload").executes(src -> {
            ConfigsIS.config.load();
            ConfigsIS.reloadCustom(ConfigsIS.config);
            ConfigsIS.reloadCosts();
            if (ConfigsIS.config.hasChanged()) {
                ConfigsIS.config.save();
            }
            NetSkillCalculator.pack().build().sendToAll();
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)"Configs have been reloaded.")), true);
            return 1;
        });
    }

    public static LiteralArgumentBuilder<CommandSourceStack> unlockSkill() {
        return (LiteralArgumentBuilder)Commands.m_82127_((String)"unlock").then(((RequiredArgumentBuilder)Commands.m_82129_((String)"targets", (ArgumentType)EntityArgument.m_91470_()).then(Commands.m_82127_((String)"only").then(Commands.m_82129_((String)"skill", (ArgumentType)((ArgumentType)SuggestionProvidersIS3.SKILL_ARGUMENT.get())).suggests(SuggestionProvidersIS3.skillSuggestions()).executes(src -> {
            ResourceKey key = (ResourceKey)src.getArgument("skill", ResourceKey.class);
            PlayerSkillBase skill = (PlayerSkillBase)ImprovableSkills.SKILLS().getValue(key.m_135782_());
            if (skill == null) {
                throw new SimpleCommandExceptionType((Message)Component.m_237113_((String)("Skill " + JSONObject.quote((String)key.m_135782_().toString()) + " not found."))).create();
            }
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null || !pd.unlockSkillScroll(skill, true)) continue;
                ++updated;
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)"Skill ").m_7220_((Component)skill.getLocalizedName()).m_130946_(" unlocked to " + updated + " players.")), true);
            return updated;
        })))).then(Commands.m_82127_((String)"everything").executes(src -> {
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null) continue;
                boolean anyUnlocked = false;
                for (PlayerSkillBase skill : ImprovableSkills.SKILLS().getValues()) {
                    if (!pd.unlockSkillScroll(skill, false)) continue;
                    anyUnlocked = true;
                }
                if (!anyUnlocked) continue;
                pd.sync();
                ++updated;
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)("All skills have been unlocked for " + updated + " players."))), true);
            return updated;
        })));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> lockSkill() {
        return (LiteralArgumentBuilder)Commands.m_82127_((String)"lock").then(((RequiredArgumentBuilder)Commands.m_82129_((String)"targets", (ArgumentType)EntityArgument.m_91470_()).then(Commands.m_82127_((String)"only").then(Commands.m_82129_((String)"skill", (ArgumentType)((ArgumentType)SuggestionProvidersIS3.SKILL_ARGUMENT.get())).suggests(SuggestionProvidersIS3.skillSuggestions()).executes(src -> {
            ResourceKey key = (ResourceKey)src.getArgument("skill", ResourceKey.class);
            PlayerSkillBase skill = (PlayerSkillBase)ImprovableSkills.SKILLS().getValue(key.m_135782_());
            if (skill == null) {
                throw new SimpleCommandExceptionType((Message)Component.m_237113_((String)("Skill " + JSONObject.quote((String)key.m_135782_().toString()) + " not found."))).create();
            }
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null || !pd.lockSkillScroll(skill, false)) continue;
                pd.setSkillLevel(skill, 0);
                ++updated;
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)"Skill ").m_7220_((Component)skill.getLocalizedName()).m_130946_(" locked to " + updated + " players.")), true);
            return updated;
        })))).then(Commands.m_82127_((String)"everything").executes(src -> {
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null) continue;
                boolean anyLocked = false;
                for (PlayerSkillBase skill : ImprovableSkills.SKILLS().getValues()) {
                    if (!pd.lockSkillScroll(skill, false)) continue;
                    pd.setSkillLevelNoSync(skill, 0);
                    anyLocked = true;
                }
                if (!anyLocked) continue;
                pd.sync();
                ++updated;
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)("All skills have been locked for " + updated + " players."))), true);
            return updated;
        })));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> revokeSkill() {
        return (LiteralArgumentBuilder)Commands.m_82127_((String)"revoke").then(((RequiredArgumentBuilder)Commands.m_82129_((String)"targets", (ArgumentType)EntityArgument.m_91470_()).then(Commands.m_82127_((String)"only").then(Commands.m_82129_((String)"skill", (ArgumentType)((ArgumentType)SuggestionProvidersIS3.SKILL_ARGUMENT.get())).suggests(SuggestionProvidersIS3.skillSuggestions()).executes(src -> {
            ResourceKey key = (ResourceKey)src.getArgument("skill", ResourceKey.class);
            PlayerSkillBase skill = (PlayerSkillBase)ImprovableSkills.SKILLS().getValue(key.m_135782_());
            int level = 0;
            if (skill == null) {
                throw new SimpleCommandExceptionType((Message)Component.m_237113_((String)("Skill " + JSONObject.quote((String)key.m_135782_().toString()) + " not found."))).create();
            }
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null) continue;
                pd.setSkillLevel(skill, level);
                pd.lockSkillScroll(skill, true);
                ++updated;
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)"Skill ").m_7220_((Component)skill.getLocalizedName()).m_130946_(" revoked from " + updated + " players.")), true);
            return updated;
        })))).then(Commands.m_82127_((String)"everything").executes(src -> {
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null) continue;
                for (PlayerSkillBase skill : ImprovableSkills.SKILLS().getValues()) {
                    pd.setSkillLevel(skill, 0);
                    pd.lockSkillScroll(skill, false);
                }
                pd.sync();
                ++updated;
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)("All skills have been revoked from " + updated + " players."))), true);
            return updated;
        })));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> giveSkill() {
        return (LiteralArgumentBuilder)Commands.m_82127_((String)"give").then(((RequiredArgumentBuilder)Commands.m_82129_((String)"targets", (ArgumentType)EntityArgument.m_91470_()).then(Commands.m_82127_((String)"only").then(Commands.m_82129_((String)"skill", (ArgumentType)((ArgumentType)SuggestionProvidersIS3.SKILL_ARGUMENT.get())).suggests(SuggestionProvidersIS3.skillSuggestions()).then(Commands.m_82129_((String)"level", (ArgumentType)IntegerArgumentType.integer((int)0)).executes(src -> {
            ResourceKey key = (ResourceKey)src.getArgument("skill", ResourceKey.class);
            PlayerSkillBase skill = (PlayerSkillBase)ImprovableSkills.SKILLS().getValue(key.m_135782_());
            int level = IntegerArgumentType.getInteger((CommandContext)src, (String)"level");
            if (skill == null) {
                throw new SimpleCommandExceptionType((Message)Component.m_237113_((String)("Skill " + JSONObject.quote((String)key.m_135782_().toString()) + " not found."))).create();
            }
            if (level > skill.getMaxLevel()) {
                throw new SimpleCommandExceptionType((Message)Component.m_237113_((String)("Unable to set skill level to " + level + ", as the max level for ")).m_7220_((Component)skill.getLocalizedName()).m_130946_(" is " + skill.getMaxLevel() + ".")).create();
            }
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null) continue;
                pd.setSkillLevel(skill, level);
                if (level > 0 && skill.getScrollState().hasScroll()) {
                    pd.unlockSkillScroll(skill, true);
                }
                ++updated;
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)"Skill ").m_7220_((Component)skill.getLocalizedName()).m_130946_(" set to level " + level + " for " + updated + " players.")), true);
            return updated;
        }))))).then(Commands.m_82127_((String)"everything").executes(src -> {
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null) continue;
                for (PlayerSkillBase skill : ImprovableSkills.SKILLS().getValues()) {
                    pd.setSkillLevel(skill, skill.getMaxLevel());
                    if (!skill.getScrollState().hasScroll()) continue;
                    pd.unlockSkillScroll(skill, false);
                }
                ++updated;
                pd.sync();
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)("All skills have been given at their max levels for " + updated + " players."))), true);
            return updated;
        })));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> revokeAbility() {
        return (LiteralArgumentBuilder)Commands.m_82127_((String)"revoke").then(((RequiredArgumentBuilder)Commands.m_82129_((String)"targets", (ArgumentType)EntityArgument.m_91470_()).then(Commands.m_82127_((String)"only").then(Commands.m_82129_((String)"ability", (ArgumentType)((ArgumentType)SuggestionProvidersIS3.ABILITY_ARGUMENT.get())).suggests(SuggestionProvidersIS3.abilitySuggestions()).executes(src -> {
            ResourceKey key = (ResourceKey)src.getArgument("ability", ResourceKey.class);
            PlayerAbilityBase abil = (PlayerAbilityBase)ImprovableSkills.ABILITIES().getValue(key.m_135782_());
            if (abil == null) {
                throw new SimpleCommandExceptionType((Message)Component.m_237113_((String)("Ability " + JSONObject.quote((String)key.m_135782_().toString()) + " not found."))).create();
            }
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null) continue;
                pd.lockAbility(abil, true);
                ++updated;
                pd.sync();
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)"Ability ").m_7220_((Component)abil.getLocalizedName()).m_130946_(" revoked for " + updated + " players.")), true);
            return updated;
        })))).then(Commands.m_82127_((String)"everything").executes(src -> {
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null) continue;
                for (PlayerAbilityBase skill : ImprovableSkills.ABILITIES().getValues()) {
                    pd.lockAbility(skill, false);
                }
                ++updated;
                pd.sync();
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)("All abilities have been revoked for " + updated + " players."))), true);
            return updated;
        })));
    }

    public static LiteralArgumentBuilder<CommandSourceStack> giveAbility() {
        return (LiteralArgumentBuilder)Commands.m_82127_((String)"give").then(((RequiredArgumentBuilder)Commands.m_82129_((String)"targets", (ArgumentType)EntityArgument.m_91470_()).then(Commands.m_82127_((String)"only").then(Commands.m_82129_((String)"ability", (ArgumentType)((ArgumentType)SuggestionProvidersIS3.ABILITY_ARGUMENT.get())).suggests(SuggestionProvidersIS3.abilitySuggestions()).executes(src -> {
            ResourceKey key = (ResourceKey)src.getArgument("ability", ResourceKey.class);
            PlayerAbilityBase abil = (PlayerAbilityBase)ImprovableSkills.ABILITIES().getValue(key.m_135782_());
            if (abil == null) {
                throw new SimpleCommandExceptionType((Message)Component.m_237113_((String)("Ability " + JSONObject.quote((String)key.m_135782_().toString()) + " not found."))).create();
            }
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null) continue;
                pd.unlockAbility(abil, true);
                ++updated;
                pd.sync();
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)"Ability ").m_7220_((Component)abil.getLocalizedName()).m_130946_(" unlocked for " + updated + " players.")), true);
            return updated;
        })))).then(Commands.m_82127_((String)"everything").executes(src -> {
            int updated = 0;
            for (ServerPlayer player : EntityArgument.m_91477_((CommandContext)src, (String)"targets")) {
                PlayerSkillData pd = PlayerDataManager.getDataFor((Player)player);
                if (pd == null) continue;
                for (PlayerAbilityBase skill : ImprovableSkills.ABILITIES().getValues()) {
                    pd.unlockAbility(skill, false);
                }
                ++updated;
                pd.sync();
            }
            ((CommandSourceStack)src.getSource()).m_288197_(Cast.constant((Object)Component.m_237113_((String)("All abilities have been unlocked for " + updated + " players."))), true);
            return updated;
        })));
    }
}

