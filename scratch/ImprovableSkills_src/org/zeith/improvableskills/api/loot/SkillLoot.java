/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.storage.loot.LootPool
 *  net.minecraft.world.level.storage.loot.LootTable
 *  net.minecraft.world.level.storage.loot.entries.EmptyLootItem
 *  net.minecraft.world.level.storage.loot.entries.LootItem
 *  net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer$Builder
 *  net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer$Builder
 *  net.minecraft.world.level.storage.loot.functions.LootItemFunction$Builder
 *  net.minecraft.world.level.storage.loot.functions.SetItemCountFunction
 *  net.minecraft.world.level.storage.loot.functions.SetNbtFunction
 *  net.minecraft.world.level.storage.loot.providers.number.ConstantValue
 *  net.minecraft.world.level.storage.loot.providers.number.NumberProvider
 *  org.zeith.hammerlib.core.adapter.LootTableAdapter
 */
package org.zeith.improvableskills.api.loot;

import java.util.List;
import java.util.function.Predicate;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.EmptyLootItem;
import net.minecraft.world.level.storage.loot.entries.LootItem;
import net.minecraft.world.level.storage.loot.entries.LootPoolEntryContainer;
import net.minecraft.world.level.storage.loot.entries.LootPoolSingletonContainer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.SetItemCountFunction;
import net.minecraft.world.level.storage.loot.functions.SetNbtFunction;
import net.minecraft.world.level.storage.loot.providers.number.ConstantValue;
import net.minecraft.world.level.storage.loot.providers.number.NumberProvider;
import org.zeith.hammerlib.core.adapter.LootTableAdapter;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.loot.RandomBoolean;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.custom.items.ItemSkillScroll;
import org.zeith.improvableskills.init.ItemsIS;
import org.zeith.improvableskills.utils.loot.LootConditionSkillScroll;

public class SkillLoot {
    public final PlayerSkillBase skill;
    public Predicate<ResourceLocation> lootTableChecker = r -> false;
    public RandomBoolean chance = new RandomBoolean();
    public boolean exclusive;

    public SkillLoot(PlayerSkillBase skill) {
        this.skill = skill;
    }

    public void setLootTable(ResourceLocation rl) {
        this.lootTableChecker = r -> r.equals((Object)rl);
    }

    public void setLootTables(ResourceLocation ... rl) {
        List<ResourceLocation> rls = List.of(rl);
        this.lootTableChecker = rls::contains;
    }

    public void addLootTable(ResourceLocation rl) {
        this.lootTableChecker = this.lootTableChecker.or(arg_0 -> ((ResourceLocation)rl).equals(arg_0));
    }

    public void addLootTables(ResourceLocation ... rl) {
        List<ResourceLocation> rls = List.of(rl);
        this.lootTableChecker = this.lootTableChecker.or(rls::contains);
    }

    public void apply(ResourceLocation id, LootTable table) {
        if (this.lootTableChecker != null && this.lootTableChecker.test(id)) {
            ImprovableSkills.LOG.info("Injecting scroll for skill '{}' into LootTable '{}'!", (Object)this.skill.getRegistryName().toString(), (Object)table.getLootTableId());
            try {
                LootPoolSingletonContainer.Builder entry = LootItem.m_79579_((ItemLike)ItemsIS.SKILL_SCROLL).m_79078_((LootItemFunction.Builder)SetNbtFunction.m_81187_((CompoundTag)ItemSkillScroll.of(this.skill).m_41783_())).m_79078_((LootItemFunction.Builder)SetItemCountFunction.m_165412_((NumberProvider)ConstantValue.m_165692_((float)1.0f)));
                List pools = LootTableAdapter.getPools((LootTable)table);
                pools.add(LootPool.m_79043_().m_79080_(() -> new LootConditionSkillScroll(1.0f, this.skill)).m_165133_((NumberProvider)ConstantValue.m_165692_((float)1.0f)).m_79076_((LootPoolEntryContainer.Builder)EmptyLootItem.m_79533_().m_79707_(this.chance.n - 1)).m_79076_((LootPoolEntryContainer.Builder)entry.m_79707_(1).m_79711_(60)).m_79082_());
            }
            catch (Throwable err) {
                ImprovableSkills.LOG.error("Failed to inject scroll for skill '{}' into LootTable '{}'!!!", (Object)this.skill.getRegistryName().toString(), (Object)table.getLootTableId(), (Object)err);
            }
        }
    }
}

