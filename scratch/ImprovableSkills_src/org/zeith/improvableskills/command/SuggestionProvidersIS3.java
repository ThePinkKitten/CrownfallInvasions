/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.brigadier.suggestion.SuggestionProvider
 *  com.mojang.brigadier.suggestion.SuggestionsBuilder
 *  net.minecraft.commands.CommandSourceStack
 *  net.minecraft.commands.arguments.ResourceKeyArgument
 *  net.minecraft.resources.ResourceKey
 *  net.minecraft.resources.ResourceLocation
 *  org.zeith.hammerlib.util.java.ResettableLazy
 */
package org.zeith.improvableskills.command;

import com.mojang.brigadier.suggestion.SuggestionProvider;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.arguments.ResourceKeyArgument;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.util.java.ResettableLazy;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;

public class SuggestionProvidersIS3 {
    public static final ResettableLazy<ResourceKeyArgument<PlayerSkillBase>> SKILL_ARGUMENT = ResettableLazy.of(() -> ResourceKeyArgument.m_212386_((ResourceKey)ImprovableSkills.SKILLS().getRegistryKey()));
    public static final ResettableLazy<ResourceKeyArgument<PlayerAbilityBase>> ABILITY_ARGUMENT = ResettableLazy.of(() -> ResourceKeyArgument.m_212386_((ResourceKey)ImprovableSkills.ABILITIES().getRegistryKey()));

    public static SuggestionProvider<CommandSourceStack> abilitySuggestions() {
        return (src, builder) -> {
            ImprovableSkills.ABILITIES().getKeys().stream().map(ResourceLocation::toString).filter(s -> s.startsWith(builder.getRemaining())).forEach(arg_0 -> ((SuggestionsBuilder)builder).suggest(arg_0));
            return builder.buildFuture();
        };
    }

    public static SuggestionProvider<CommandSourceStack> skillSuggestions() {
        return (src, builder) -> {
            ImprovableSkills.SKILLS().getKeys().stream().map(ResourceLocation::toString).filter(s -> s.startsWith(builder.getRemaining())).forEach(arg_0 -> ((SuggestionsBuilder)builder).suggest(arg_0));
            return builder.buildFuture();
        };
    }
}

