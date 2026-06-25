/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.BoneMealItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelReader
 *  net.minecraft.world.level.block.Block
 *  net.minecraft.world.level.block.BonemealableBlock
 *  net.minecraft.world.level.block.state.BlockState
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
package org.zeith.improvableskills.custom.skills;

import java.util.ArrayList;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BoneMealItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.BonemealableBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.init.TagsIS3;

public class SkillGrowth
extends PlayerSkillBase {
    public SkillGrowth() {
        super(20);
        this.setupScroll();
        this.getLoot().chance.n = 4;
        this.getLoot().setLootTable(BuiltInLootTables.f_78686_);
        this.setColor(55931);
        this.xpCalculator.xpValue = 3;
        this.xpCalculator.setBaseFormula("((%lvl%)^%xpv%)*0.9+32");
    }

    @Override
    public void tick(PlayerSkillData data, boolean isActive) {
        short lvl;
        if (isActive && (lvl = data.getSkillLevel(this)) > 0 && data.player.f_19797_ % ((this.maxLvl - lvl) * 3 + 80) == 0) {
            SkillGrowth.growAround(data.player, 2 + lvl / 4, (int)Math.sqrt(lvl) / 2 + 1);
        }
    }

    public static void growAround(Player ent, int rad, int max) {
        Level world = ent.m_9236_();
        ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
        for (int x = -rad; x <= rad; ++x) {
            for (int z = -rad; z <= rad; ++z) {
                for (int y = -rad / 2; y <= rad / 2; ++y) {
                    BlockPos pos = ent.m_20183_().m_7918_(x, y, z);
                    BlockState state = world.m_8055_(pos);
                    Block b = state.m_60734_();
                    if (!(b instanceof BonemealableBlock)) continue;
                    BonemealableBlock gr = (BonemealableBlock)b;
                    if (state.m_204336_(TagsIS3.Blocks.GROWTH_SKILL_BLOCKLIST) || !gr.m_7370_((LevelReader)world, pos, state, world.f_46443_) || !gr.m_214167_(world, world.f_46441_, pos, state)) continue;
                    positions.add(pos);
                }
            }
        }
        int co = Math.min(world.f_46441_.m_188503_(max), positions.size());
        for (int i = 0; i < co; ++i) {
            BlockPos pos = (BlockPos)positions.remove(world.f_46441_.m_188503_(positions.size()));
            if (!BoneMealItem.applyBonemeal((ItemStack)new ItemStack((ItemLike)Items.f_42499_), (Level)world, (BlockPos)pos, (Player)ent)) continue;
            world.m_46796_(2005, pos, 0);
        }
    }
}

