/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.event.entity.player.PlayerEvent$BreakSpeed
 *  net.minecraftforge.event.entity.player.PlayerEvent$ItemCraftedEvent
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  org.zeith.hammerlib.util.java.Cast
 *  org.zeith.hammerlib.util.java.tuples.Tuple1$Mutable1
 *  org.zeith.hammerlib.util.java.tuples.Tuples
 */
package org.zeith.improvableskills;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.hammerlib.util.java.tuples.Tuple1;
import org.zeith.hammerlib.util.java.tuples.Tuples;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.IDigSpeedAffectorSkill;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.custom.items.ItemSkillsBook;
import org.zeith.improvableskills.data.PlayerDataManager;

@Mod.EventBusSubscriber
public class SkillEvents {
    @SubscribeEvent
    public static void breakSpeed(PlayerEvent.BreakSpeed e) {
        BlockPos pos = e.getPosition().orElse(null);
        if (pos == null) {
            return;
        }
        PlayerDataManager.handleDataSafely(e.getEntity(), data -> {
            Player p = data.player;
            ItemStack item = p.m_21205_();
            Tuple1.Mutable1 tot = Tuples.mutable((Object)Float.valueOf(1.0f));
            ImprovableSkills.SKILLS().getValues().stream().flatMap(s -> Cast.optionally((Object)s, IDigSpeedAffectorSkill.class).stream()).filter(skill -> data.isSkillActive((PlayerSkillBase)((Object)skill))).forEach(d -> tot.setA((Object)Float.valueOf(((Float)tot.a()).floatValue() + d.getDigMultiplier(item, pos, (PlayerSkillData)data))));
            e.setNewSpeed(e.getNewSpeed() * ((Float)tot.a()).floatValue());
        });
    }

    @SubscribeEvent
    public static void crafting(PlayerEvent.ItemCraftedEvent e) {
        Player player = e.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer mp = (ServerPlayer)player;
            if (!e.getCrafting().m_41619_() && e.getCrafting().m_41720_() instanceof ItemSkillsBook) {
                PlayerDataManager.handleDataSafely((Player)mp, data -> {
                    data.hasCraftedSkillBook = true;
                    data.sync();
                });
            }
        }
    }
}

