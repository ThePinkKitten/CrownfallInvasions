/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.core.Vec3i
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 *  net.minecraft.world.level.storage.loot.LootParams
 *  net.minecraft.world.level.storage.loot.LootParams$Builder
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParamSets
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParams
 *  net.minecraft.world.phys.Vec3
 */
package org.zeith.improvableskills.api.treasures.drops;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.Vec3;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.treasures.TreasureContext;
import org.zeith.improvableskills.api.treasures.TreasureDropBase;

public class TreasureSandDropLootTableItem
extends TreasureDropBase {
    public ResourceLocation dropTable = BuiltInLootTables.f_78764_;
    public int minLvl;

    public TreasureSandDropLootTableItem() {
    }

    public TreasureSandDropLootTableItem(ResourceLocation table, int minLvl) {
        this.dropTable = table;
        this.minLvl = minLvl;
    }

    @Override
    public void drop(TreasureContext ctx, List<ItemStack> drops) {
        PlayerSkillData data = ctx.data();
        ServerLevel srv = (ServerLevel)ctx.level();
        LootParams lctx = new LootParams.Builder(srv).m_287286_(LootContextParams.f_81460_, (Object)Vec3.m_82512_((Vec3i)ctx.pos())).m_287286_(LootContextParams.f_81455_, (Object)data.player).m_287239_(data.player.m_36336_()).m_287235_(LootContextParamSets.f_81411_);
        ObjectArrayList gen = srv.m_7654_().m_278653_().m_278676_(this.dropTable).m_287195_(lctx);
        if (!gen.isEmpty()) {
            drops.add(((ItemStack)gen.get(data.player.m_217043_().m_188503_(gen.size()))).m_41777_());
            return;
        }
    }

    @Override
    public TreasureDropBase copy() {
        TreasureSandDropLootTableItem l = (TreasureSandDropLootTableItem)super.copy();
        l.dropTable = this.dropTable;
        l.minLvl = this.minLvl;
        return l;
    }

    @Override
    public boolean canDrop(TreasureContext ctx) {
        return ctx.caller() != null && ctx.caller().getRegistryName().toString().equals("improvableskills:treasure_sands") && ctx.data().getSkillLevel(ctx.caller()) >= this.minLvl;
    }
}

