/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.NonNullList
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.tags.BlockTags
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.LevelAccessor
 *  net.minecraft.world.level.biome.Biome
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
package org.zeith.improvableskills.custom.skills;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.BlockTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.evt.HarvestDropsEvent;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.api.treasures.DropUtil;
import org.zeith.improvableskills.api.treasures.TreasureContext;
import org.zeith.improvableskills.api.treasures.TreasureDropBase;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.SoundsIS;

public class SkillTreasureSands
extends PlayerSkillBase {
    public SkillTreasureSands() {
        super(3);
        this.setupScroll();
        this.getLoot().chance.n = 8;
        this.getLoot().setLootTable(BuiltInLootTables.f_78764_);
        this.setColor(13413463);
        this.xpCalculator.setBaseFormula("(%lvl%+1)^7+200");
        this.addListener(this::hook);
    }

    private void hook(HarvestDropsEvent e) {
        BlockPos pos = e.getPos();
        LevelAccessor level = e.getLevel();
        NonNullList<ItemStack> drops = e.getDrops();
        int ps = drops.size();
        PlayerDataManager.handleDataSafely(e.getEntity(), data -> {
            TreasureContext ctx;
            TreasureDropBase dr;
            RandomSource rng;
            ServerLevel mp;
            if (level instanceof ServerLevel && (mp = (ServerLevel)level).m_8055_(pos).m_204336_(BlockTags.f_13029_) && ((Biome)mp.m_204166_(pos).get()).m_47554_() >= 2.0f && (rng = data.player.m_217043_()).m_188503_(100) < 4 * data.getSkillLevel(this) && data.isSkillActive(this) && (dr = DropUtil.chooseDrop(ctx = new TreasureContext.Builder().withCaller(this).withData((PlayerSkillData)data).withLocation((Level)mp, pos).withRNG(rng).build())) != null) {
                dr.drop(ctx, (List<ItemStack>)drops);
            }
        });
        if (drops.size() > ps) {
            level.m_247517_(null, e.getPos(), SoundsIS.TREASURE_FOUND, SoundSource.BLOCKS);
        }
    }
}

