/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.enchantment.Enchantment
 *  net.minecraftforge.event.entity.living.LivingEvent
 */
package org.zeith.improvableskills.api.evt;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraftforge.event.entity.living.LivingEvent;

public class EntityEnchantmentLevelEvent
extends LivingEvent {
    private int max;
    private final Enchantment ench;

    public EntityEnchantmentLevelEvent(LivingEntity entity, Enchantment ench) {
        super(entity);
        this.ench = ench;
    }

    public Enchantment getEnchantment() {
        return this.ench;
    }

    public void max(int with) {
        this.max = Math.max(this.max, with);
    }

    public int getMax() {
        return this.max;
    }
}

