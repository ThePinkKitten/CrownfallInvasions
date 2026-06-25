/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.NonNullList
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.EntityType
 *  net.minecraft.world.entity.ExperienceOrb
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.Mob
 *  net.minecraft.world.entity.npc.AbstractVillager
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.event.entity.living.BabyEntitySpawnEvent
 *  net.minecraftforge.event.entity.living.LivingExperienceDropEvent
 *  net.minecraftforge.event.entity.player.ItemFishedEvent
 *  net.minecraftforge.event.level.BlockEvent$BreakEvent
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.core.NonNullList;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.npc.AbstractVillager;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.living.BabyEntitySpawnEvent;
import net.minecraftforge.event.entity.living.LivingExperienceDropEvent;
import net.minecraftforge.event.entity.player.ItemFishedEvent;
import net.minecraftforge.event.level.BlockEvent;
import org.zeith.improvableskills.api.evt.CalculateAdditionalFurnaceExperienceMultiplier;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillXPPlus
extends PlayerSkillBase {
    public SkillXPPlus() {
        super(10);
        this.setupScroll();
        this.getLoot().chance.n = 3;
        this.getLoot().setLootTable(EntityType.f_20563_.m_20677_());
        this.setColor(9689652);
        this.xpCalculator.setBaseFormula("%lvl%^3+(%lvl%+1)*100");
        this.addListener(this::blockBreak);
        this.addListener(this::killEntity);
        this.addListener(this::babyEntitySpawn);
        this.addListener(this::itemFished);
        this.addListener(this::furnaceExtra);
    }

    private void blockBreak(BlockEvent.BreakEvent e) {
        PlayerDataManager.handleDataSafely(e.getPlayer(), data -> {
            int xp = e.getExpToDrop();
            if (xp <= 0 || !data.isSkillActive(this)) {
                return;
            }
            float xpp = data.getSkillProgress(this);
            e.setExpToDrop(Mth.m_14143_((float)((float)xp + data.player.m_9236_().f_46441_.m_188501_() * (float)xp * xpp)));
        });
    }

    private void killEntity(LivingExperienceDropEvent e) {
        LivingEntity ded = e.getEntity();
        int xp = e.getDroppedExperience();
        if (ded instanceof Player || xp <= 0) {
            return;
        }
        PlayerDataManager.handleDataSafely(e.getAttackingPlayer(), data -> {
            if (!data.isSkillActive(this)) {
                return;
            }
            float xpp = data.getSkillProgress(this);
            e.setDroppedExperience(Mth.m_14143_((float)((float)xp + data.player.m_9236_().f_46441_.m_188501_() * (float)xp * xpp)));
        });
    }

    private void babyEntitySpawn(BabyEntitySpawnEvent e) {
        if (e.getChild() instanceof AbstractVillager) {
            return;
        }
        PlayerDataManager.handleDataSafely(e.getCausedByPlayer(), data -> {
            if (!data.isSkillActive(this)) {
                return;
            }
            short xpp = data.getSkillLevel(this);
            if (xpp > 0) {
                int xp = 1 + data.player.m_9236_().f_46441_.m_188503_(xpp + 1);
                Mob c = e.getParentA();
                Level patt2613$temp = c.m_9236_();
                if (patt2613$temp instanceof ServerLevel) {
                    ServerLevel mp = (ServerLevel)patt2613$temp;
                    ExperienceOrb.m_147082_((ServerLevel)mp, (Vec3)c.m_20182_(), (int)xp);
                }
            }
        });
    }

    private void itemFished(ItemFishedEvent e) {
        PlayerDataManager.handleDataSafely(e.getEntity(), data -> {
            if (!data.isSkillActive(this)) {
                return;
            }
            short xpp = data.getSkillLevel(this);
            NonNullList drops = e.getDrops();
            if (xpp > 0) {
                for (int i = 0; i < drops.size(); ++i) {
                    Level patt3128$temp;
                    int xp = data.player.m_9236_().f_46441_.m_188503_(xpp + 1);
                    if (xp < 1 || !((patt3128$temp = data.player.m_9236_()) instanceof ServerLevel)) continue;
                    ServerLevel mp = (ServerLevel)patt3128$temp;
                    ExperienceOrb.m_147082_((ServerLevel)mp, (Vec3)data.player.m_20182_(), (int)xp);
                }
            }
        });
    }

    private void furnaceExtra(CalculateAdditionalFurnaceExperienceMultiplier e) {
        PlayerDataManager.handleDataSafely(e.getEntity(), data -> {
            if (!data.isSkillActive(this)) {
                return;
            }
            e.addExtraPercent(data.getSkillProgress(this));
        });
    }
}

