/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 */
package org.zeith.improvableskills.custom.items;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.net.PacketScrollUnlockedSkill;

public class ItemCreativeSkillScroll
extends Item {
    public ItemCreativeSkillScroll() {
        super(new Item.Properties().m_41487_(1));
        ImprovableSkills.TAB.add((ItemLike)this);
    }

    public void m_7373_(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        tooltip.add((Component)Component.m_237115_((String)(this.m_5524_() + ".tooltip0")).m_130940_(ChatFormatting.GRAY));
        tooltip.add((Component)Component.m_237115_((String)(this.m_5524_() + ".tooltip1")).m_130940_(ChatFormatting.GRAY));
    }

    public InteractionResultHolder<ItemStack> m_7203_(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack held = playerIn.m_21120_(handIn);
        if (worldIn.f_46443_) {
            return new InteractionResultHolder(InteractionResult.PASS, (Object)held);
        }
        return PlayerDataManager.handleDataSafely(playerIn, data -> {
            Collection bases = ImprovableSkills.SKILLS().getValues();
            ArrayList<PlayerSkillBase> given = new ArrayList<PlayerSkillBase>();
            for (PlayerSkillBase base : bases) {
                if (!data.hasSkillScroll(base) && base.getScrollState().hasScroll()) {
                    data.unlockSkillScroll(base, false);
                    given.add(base);
                    continue;
                }
                if (data.getSkillLevel(base) >= base.getMaxLevel()) continue;
                data.setSkillLevel(base, data.getSkillLevel(base) + 1);
                given.add(base);
            }
            if (!given.isEmpty()) {
                ItemStack used = held.m_41777_();
                held.m_41774_(1);
                playerIn.m_6674_(handIn);
                worldIn.m_5594_(null, playerIn.m_20183_(), SoundEvents.f_11887_, SoundSource.PLAYERS, 0.5f, 1.0f);
                int slot = handIn == InteractionHand.OFF_HAND ? -2 : playerIn.m_150109_().f_35977_;
                Network.sendTo((IPacket)new PacketScrollUnlockedSkill(slot, used, (ResourceLocation[])given.stream().map(PlayerSkillBase::getRegistryName).toArray(ResourceLocation[]::new)), (Player)playerIn);
            }
            return new InteractionResultHolder(!given.isEmpty() ? InteractionResult.SUCCESS : InteractionResult.PASS, (Object)held);
        }, new InteractionResultHolder(InteractionResult.PASS, (Object)held));
    }
}

