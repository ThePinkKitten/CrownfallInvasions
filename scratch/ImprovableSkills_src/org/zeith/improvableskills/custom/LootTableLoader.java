/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.storage.loot.LootPool
 *  net.minecraft.world.level.storage.loot.LootTable
 *  net.minecraft.world.level.storage.loot.entries.EmptyLootItem
 *  net.minecraft.world.level.storage.loot.entries.LootItem
 *  net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer$Builder
 *  net.minecraft.world.level.storage.loot.functions.LootItemFunction$Builder
 *  net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
 *  net.minecraft.world.level.storage.loot.providers.number.ConstantValue
 *  net.minecraft.world.level.storage.loot.providers.number.NumberProvider
 *  org.zeith.hammerlib.core.adapter.LootTableAdapter
 */
package org.zeith.improvableskills.custom;

import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.zeith.hammerlib.core.adapter.LootTableAdapter;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.loot.SkillLoot;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.cfg.ConfigsIS;
import org.zeith.improvableskills.init.ItemsIS;

public class LootTableLoader {
    public static void loadTable(ResourceLocation id, LootTable table) {
        for (PlayerSkillBase skill : ImprovableSkills.SKILLS()) {
            SkillLoot lt = skill.getLoot();
            if (lt == null) continue;
            lt.apply(id, table);
        }
        if (id.m_135815_().contains("chests/") && ConfigsIS.parchmentGeneration) {
            if (ConfigsIS.blockedParchmentChests.contains(id.toString())) {
                ImprovableSkills.LOG.debug("SKIPPING parchment injection for LootTable '" + table.getLootTableId() + "'!");
                return;
            }
            ImprovableSkills.LOG.info("Injecting parchment into LootTable '" + table.getLootTableId() + "'!");
            try {
                List pools = LootTableAdapter.getPools((LootTable)table);
                pools.add(LootPool.m_79043_().m_165133_((NumberProvider)ConstantValue.m_165692_((float)1.0f)).m_79076_((LootPoolEntryContainer.Builder)EmptyLootItem.m_79533_().m_79707_(ConfigsIS.parchmentRarity)).m_79076_((LootPoolEntryContainer.Builder)LootItem.m_79579_((ItemLike)ItemsIS.PARCHMENT_FRAGMENT).m_79078_((LootItemFunction.Builder)SetItemCountFunction.m_165412_((NumberProvider)ConstantValue.m_165692_((float)1.0f))).m_79707_(1).m_79711_(60)).m_79082_());
            }
            catch (Throwable err) {
                ImprovableSkills.LOG.error("Failed to inject parchment into LootTable '" + table.getLootTableId() + "'!!!");
                err.printStackTrace();
            }
        }
    }
}

