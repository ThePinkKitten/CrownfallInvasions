/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.Event
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Constant
 *  org.spongepowered.asm.mixin.injection.ModifyConstant
 *  org.spongepowered.asm.mixin.injection.Slice
 */
package org.zeith.improvableskills.mixins;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;
import org.spongepowered.asm.mixin.injection.Slice;
import org.zeith.improvableskills.api.evt.EntityEnchantmentLevelEvent;

@Mixin(value={EnchantmentHelper.class})
public class EnchantmentHelperMixin {
    @ModifyConstant(method={"getEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/entity/LivingEntity;)I"}, constant={@Constant(intValue=0)}, slice={@Slice(from=@At(value="RETURN"), to=@At(value="INVOKE", target="Lnet/minecraft/world/item/enchantment/EnchantmentHelper;getItemEnchantmentLevel(Lnet/minecraft/world/item/enchantment/Enchantment;Lnet/minecraft/world/item/ItemStack;)I"))})
    private static int getEnchantmentLevel_IS3(int var, Enchantment enchantment, LivingEntity entity) {
        if (entity != null) {
            EntityEnchantmentLevelEvent evt = new EntityEnchantmentLevelEvent(entity, enchantment);
            evt.max(var);
            MinecraftForge.EVENT_BUS.post((Event)evt);
            return evt.getMax();
        }
        return var;
    }
}

