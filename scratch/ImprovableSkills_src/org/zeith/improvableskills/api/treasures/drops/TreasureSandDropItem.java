/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.NonNullList
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 */
package org.zeith.improvableskills.api.treasures.drops;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.api.treasures.TreasureContext;
import org.zeith.improvableskills.api.treasures.TreasureDropBase;
import org.zeith.improvableskills.api.treasures.drops.Stackable;
import org.zeith.improvableskills.init.SkillsIS;

public class TreasureSandDropItem
extends TreasureDropBase {
    public final NonNullList<Stackable> items = NonNullList.m_122779_();
    public int minLvl;
    public Predicate<PlayerSkillBase> skill = SkillsIS.TREASURE_OF_SANDS::is;

    public TreasureSandDropItem() {
    }

    public TreasureSandDropItem(int lvl, Stackable ... items) {
        this.minLvl = lvl;
        this.items.addAll(Arrays.asList(items));
    }

    public TreasureSandDropItem(int lvl, Object ... items) {
        this.minLvl = lvl;
        for (int i = 0; i < items.length; ++i) {
            Object o = items[i];
            if (o == null) {
                throw new NullPointerException("Item at index " + i + " is null.");
            }
            if (o instanceof ItemLike) {
                ItemLike l = (ItemLike)o;
                this.items.add((Object)Stackable.of(new ItemStack(l)));
                continue;
            }
            if (o instanceof ItemStack) {
                this.items.add((Object)Stackable.of(((ItemStack)o).m_41777_()));
                continue;
            }
            if (o instanceof Stackable) {
                this.items.add((Object)((Stackable)o));
                continue;
            }
            throw new IllegalArgumentException("Item at index " + i + " is not supported!");
        }
    }

    @Override
    public void drop(TreasureContext ctx, List<ItemStack> drops) {
        for (Stackable s : this.items) {
            if (s == null) continue;
            drops.add(s.transform(ctx.rand()));
        }
    }

    @Override
    public TreasureDropBase copy() {
        TreasureSandDropItem l = (TreasureSandDropItem)super.copy();
        l.minLvl = this.minLvl;
        l.items.addAll(this.items);
        return this;
    }

    @Override
    public boolean canDrop(TreasureContext ctx) {
        return this.skill.test(ctx.caller()) && ctx.data().getSkillLevel(ctx.caller()) >= this.minLvl;
    }
}

