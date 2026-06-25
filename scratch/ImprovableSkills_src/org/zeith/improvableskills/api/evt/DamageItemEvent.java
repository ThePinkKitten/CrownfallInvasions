/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.event.entity.EntityEvent
 */
package org.zeith.improvableskills.api.evt;

import java.util.Optional;
import javax.annotation.Nullable;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent;

public class DamageItemEvent
extends EntityEvent {
    private final ItemStack item;
    private final int originalDamage;
    private int newDamage;

    public DamageItemEvent(ItemStack item, LivingEntity entity, int originalDamage) {
        super((Entity)entity);
        this.item = item;
        this.originalDamage = this.newDamage = originalDamage;
    }

    public ItemStack getItem() {
        return this.item;
    }

    public int getOriginalDamage() {
        return this.originalDamage;
    }

    public int getNewDamage() {
        return this.newDamage;
    }

    public void setNewDamage(int newDamage) {
        this.newDamage = newDamage;
    }

    public RandomSource getRandom() {
        return Optional.ofNullable(this.getEntity()).map(LivingEntity::m_217043_).orElseGet(RandomSource::m_216327_);
    }

    @Nullable
    public LivingEntity getEntity() {
        return (LivingEntity)super.getEntity();
    }
}

