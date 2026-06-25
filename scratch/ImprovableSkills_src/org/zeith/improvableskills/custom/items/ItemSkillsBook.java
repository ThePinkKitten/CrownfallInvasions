/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 */
package org.zeith.improvableskills.custom.items;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.SoundsIS;
import org.zeith.improvableskills.net.PacketOpenSkillsBook;

public class ItemSkillsBook
extends Item {
    public ItemSkillsBook(Item.Properties props) {
        super(props);
    }

    public ItemSkillsBook() {
        this(new Item.Properties().m_41487_(1));
        ImprovableSkills.TAB.add((ItemLike)this);
    }

    public InteractionResultHolder<ItemStack> m_7203_(Level level, Player player, InteractionHand hand) {
        if (!level.m_5776_() && player instanceof ServerPlayer) {
            ServerPlayer mp = (ServerPlayer)player;
            PacketOpenSkillsBook.sync(mp);
        } else {
            level.m_5594_(player, player.m_20183_(), SoundsIS.PAGE_TURNS, SoundSource.PLAYERS, 0.25f, 1.0f);
        }
        return super.m_7203_(level, player, hand);
    }

    public void m_6883_(ItemStack stack, Level worldIn, Entity entityIn, int itemSlot, boolean isSelected) {
        if (entityIn instanceof ServerPlayer) {
            ServerPlayer mp = (ServerPlayer)entityIn;
            if (!worldIn.f_46443_) {
                PlayerDataManager.handleDataSafely((Player)mp, data -> {
                    data.hasCraftedSkillBook = true;
                });
            }
        }
        super.m_6883_(stack, worldIn, entityIn, itemSlot, isSelected);
    }
}

