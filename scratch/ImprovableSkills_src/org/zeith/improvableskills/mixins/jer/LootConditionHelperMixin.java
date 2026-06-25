/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  jeresources.api.conditionals.Conditional
 *  jeresources.api.drop.LootDrop
 *  jeresources.api.util.LootConditionHelper
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.level.storage.loot.predicates.LootItemCondition
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 */
package org.zeith.improvableskills.mixins.jer;

import jeresources.api.conditionals.Conditional;
import jeresources.api.drop.LootDrop;
import jeresources.api.util.LootConditionHelper;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.improvableskills.compat.jer.ConditionalComponent;
import org.zeith.improvableskills.utils.loot.LootConditionSkillScroll;

@Mixin(value={LootConditionHelper.class}, remap=false)
public class LootConditionHelperMixin {
    @Inject(method={"applyCondition"}, at={@At(value="HEAD")}, cancellable=true)
    private static void applyCondition_IS3(LootItemCondition condition, LootDrop lootDrop, CallbackInfo ci) {
        if (condition instanceof LootConditionSkillScroll) {
            LootConditionSkillScroll cond = (LootConditionSkillScroll)condition;
            lootDrop.addConditional((Conditional)new ConditionalComponent((Component)Component.m_237110_((String)"improvableskills.jer.skill_condition", (Object[])new Object[]{cond.getContextComponent()}).m_130940_(ChatFormatting.AQUA)));
            ci.cancel();
        }
    }
}

