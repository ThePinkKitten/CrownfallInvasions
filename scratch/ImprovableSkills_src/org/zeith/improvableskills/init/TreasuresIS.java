/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
package org.zeith.improvableskills.init;

import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.zeith.improvableskills.api.treasures.TreasureRegistry;
import org.zeith.improvableskills.api.treasures.drops.Stackable;
import org.zeith.improvableskills.api.treasures.drops.TreasureSandDropItem;
import org.zeith.improvableskills.api.treasures.drops.TreasureSandDropLootTableItem;

public interface TreasuresIS {
    public static void register() {
        TreasuresIS.registerSandTreasures();
    }

    private static void registerSandTreasures() {
        TreasureRegistry.registerDrop(new TreasureSandDropItem(1, Stackable.of(new ItemStack((ItemLike)Items.f_42749_), 1, 3))).setChance(0.7f);
        TreasureRegistry.registerDrop(new TreasureSandDropItem(1, new ItemStack((ItemLike)Items.f_42583_))).setChance(0.8f);
        TreasureRegistry.registerDrop(new TreasureSandDropItem(1, r -> TreasuresIS.damage(new ItemStack((ItemLike)TreasuresIS.select(r, Items.f_42426_, Items.f_42427_)), 125 - r.m_188503_(32)))).setChance(0.2f);
        TreasureRegistry.registerDrop(new TreasureSandDropItem(1, Stackable.of(new ItemStack((ItemLike)Items.f_42500_), 1, 3))).setChance(0.65f);
        TreasureRegistry.registerDrop(new TreasureSandDropItem(1, Items.f_42413_)).setChance(0.72f);
        TreasureRegistry.registerDrop(new TreasureSandDropItem(2, r -> TreasuresIS.damage(new ItemStack((ItemLike)TreasuresIS.select(r, Items.f_42384_, Items.f_42385_, Items.f_42383_)), 250 - r.m_188503_(64)))).setChance(0.25f);
        TreasureRegistry.registerDrop(new TreasureSandDropItem(2, Stackable.of(new ItemStack((ItemLike)Items.f_42587_), 1, 3))).setChance(0.6f);
        TreasureRegistry.registerDrop(new TreasureSandDropItem(2, r -> TreasuresIS.damage(new ItemStack((ItemLike)(r.m_188499_() ? Items.f_42467_ : Items.f_42464_)), 160 - r.m_188503_(69)))).setChance(0.1f);
        TreasureRegistry.registerDrop(new TreasureSandDropItem(3, new ItemStack((ItemLike)Items.f_42436_, 1))).setChance(0.15f);
        TreasureRegistry.registerDrop(new TreasureSandDropItem(3, Stackable.of(new ItemStack((ItemLike)Items.f_42415_), 1, 2))).setChance(0.52f);
        TreasureRegistry.registerDrop(new TreasureSandDropItem(3, Stackable.of(new ItemStack((ItemLike)Items.f_42534_, 1), 3, 7))).setChance(0.3f);
        TreasureRegistry.registerDrop(new TreasureSandDropItem(3, new ItemStack((ItemLike)Items.f_42437_, 1))).setChance(0.001f);
        TreasureRegistry.registerDrop(new TreasureSandDropLootTableItem(BuiltInLootTables.f_78764_, 3)).setChance(0.45f);
    }

    public static ItemStack damage(ItemStack stack, int rng) {
        stack.m_41721_(rng);
        return stack;
    }

    public static <T> T select(RandomSource rand, T ... vars) {
        return vars[rand.m_188503_(vars.length)];
    }
}

