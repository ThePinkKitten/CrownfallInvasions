/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  javax.annotation.Nullable
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.InteractionResult
 *  net.minecraft.world.InteractionResultHolder
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.tooltip.TooltipComponent
 *  net.minecraft.world.item.CreativeModeTab
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.Item$Properties
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.TooltipFlag
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  org.zeith.hammerlib.api.items.ITabItem
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 */
package org.zeith.improvableskills.custom.items;

import java.util.Collection;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import javax.annotation.Nullable;
import net.minecraft.ChatFormatting;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.tooltip.TooltipComponent;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import org.zeith.hammerlib.api.items.ITabItem;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.api.tooltip.SkillTooltip;
import org.zeith.improvableskills.cfg.ConfigsIS;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.ItemsIS;
import org.zeith.improvableskills.net.PacketScrollLevelupSkill;
import org.zeith.improvableskills.net.PacketScrollUnlockedSkill;

public class ItemSkillScroll
extends Item
implements ITabItem {
    private static final Map<String, PlayerSkillBase> SKILL_MAP = new HashMap<String, PlayerSkillBase>();

    public ItemSkillScroll() {
        super(new Item.Properties().m_41487_(1));
        ImprovableSkills.TAB.add((ItemLike)this);
    }

    @Nullable
    public String getCreatorModId(ItemStack stack) {
        PlayerSkillBase v = ItemSkillScroll.getSkillFromScroll(stack);
        if (v != null) {
            return ImprovableSkills.SKILLS().getKey((Object)v).m_135827_();
        }
        return "improvableskills";
    }

    @Nullable
    public static PlayerSkillBase getSkillFromScroll(ItemStack stack) {
        if (!stack.m_41619_() && stack.m_41720_() instanceof ItemSkillScroll && stack.m_41782_() && stack.m_41783_().m_128425_("Skill", 8)) {
            String skill = stack.m_41783_().m_128461_("Skill");
            if (SKILL_MAP.containsKey(skill)) {
                return SKILL_MAP.get(skill);
            }
            PlayerSkillBase b = (PlayerSkillBase)ImprovableSkills.SKILLS().getValue(new ResourceLocation(stack.m_41783_().m_128461_("Skill")));
            SKILL_MAP.put(skill, b);
            return b;
        }
        return null;
    }

    public static ItemStack of(PlayerSkillBase base) {
        if (base.getScrollState().hasScroll()) {
            ItemStack stack = new ItemStack((ItemLike)ItemsIS.SKILL_SCROLL);
            CompoundTag tag = new CompoundTag();
            tag.m_128359_("Skill", base.getRegistryName().toString());
            stack.m_41751_(tag);
            return stack;
        }
        return ItemStack.f_41583_;
    }

    public static void getItems(Collection<ItemStack> items) {
        ImprovableSkills.SKILLS().getValues().stream().filter(skill -> skill.getScrollState().hasScroll()).sorted(Comparator.comparing(PlayerSkillBase::getUnlocalizedName)).forEach(skill -> items.add(ItemSkillScroll.of(skill)));
    }

    public CreativeModeTab getItemCategory() {
        return ImprovableSkills.TAB.tab();
    }

    public void fillItemCategory(CreativeModeTab tab, Set<ItemStack> items) {
        if (this.allowedIn(tab)) {
            ItemSkillScroll.getItems(items);
        }
    }

    public void m_7373_(ItemStack stack, Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        PlayerSkillBase base = ItemSkillScroll.getSkillFromScroll(stack);
        if (base == null) {
            return;
        }
        tooltip.add((Component)base.getLocalizedName(SyncSkills.getData()).m_130940_(ChatFormatting.GRAY));
        if (flagIn.m_7050_()) {
            tooltip.add((Component)Component.m_237113_((String)(" - " + base.getRegistryName())).m_130940_(ChatFormatting.DARK_GRAY));
        }
        if (ImprovableSkills.PROXY.hasShiftDown()) {
            tooltip.add((Component)Component.m_237113_((String)I18n.m_118938_((String)("recipe." + base.getRegistryName().m_135827_() + ":skill." + base.getRegistryName().m_135815_()), (Object[])new Object[0]).replace('&', '\u00a7')).m_130940_(ChatFormatting.GRAY));
        } else {
            tooltip.add((Component)Component.m_237113_((String)I18n.m_118938_((String)"text.improvableskills:shiftfrecipe", (Object[])new Object[0]).replace('&', '\u00a7')).m_130940_(ChatFormatting.GRAY));
        }
    }

    public InteractionResultHolder<ItemStack> m_7203_(Level worldIn, Player playerIn, InteractionHand handIn) {
        ItemStack held = playerIn.m_21120_(handIn);
        if (worldIn.f_46443_) {
            return new InteractionResultHolder(InteractionResult.PASS, (Object)held);
        }
        return PlayerDataManager.handleDataSafely(playerIn, data -> {
            PlayerSkillBase base = ItemSkillScroll.getSkillFromScroll(held);
            if (base == null) {
                return new InteractionResultHolder(InteractionResult.PASS, (Object)held);
            }
            if (!data.hasSkillScroll(base) && data.unlockSkillScroll(base, true)) {
                ItemStack used = held.m_41777_();
                held.m_41774_(1);
                playerIn.m_6674_(handIn);
                worldIn.m_5594_(null, playerIn.m_20183_(), SoundEvents.f_11887_, SoundSource.PLAYERS, 0.5f, 1.0f);
                int slot = handIn == InteractionHand.OFF_HAND ? -2 : playerIn.m_150109_().f_35977_;
                Network.sendTo((IPacket)new PacketScrollUnlockedSkill(slot, used, base.getRegistryName()), (Player)playerIn);
                return new InteractionResultHolder(InteractionResult.SUCCESS, (Object)held);
            }
            if (data.getSkillLevel(base) < base.getMaxLevel()) {
                data.setSkillLevel(base, data.getSkillLevel(base) + 1);
                ItemStack used = held.m_41777_();
                held.m_41774_(1);
                playerIn.m_6674_(handIn);
                worldIn.m_5594_(null, playerIn.m_20183_(), SoundEvents.f_11887_, SoundSource.PLAYERS, 0.5f, 1.0f);
                int slot = handIn == InteractionHand.OFF_HAND ? -2 : playerIn.m_150109_().f_35977_;
                Network.sendTo((IPacket)new PacketScrollLevelupSkill(slot, used, base.getRegistryName()), (Player)playerIn);
                return new InteractionResultHolder(InteractionResult.SUCCESS, (Object)held);
            }
            return new InteractionResultHolder(InteractionResult.PASS, (Object)held);
        }, new InteractionResultHolder(InteractionResult.PASS, (Object)held));
    }

    public void m_6883_(ItemStack pStack, Level pLevel, Entity pEntity, int pSlotId, boolean pIsSelected) {
        if (ConfigsIS.autouseScrolls && pEntity instanceof ServerPlayer) {
            ServerPlayer playerIn = (ServerPlayer)pEntity;
            PlayerDataManager.handleDataSafely((Player)playerIn, data -> {
                PlayerSkillBase base = ItemSkillScroll.getSkillFromScroll(pStack);
                if (base == null) {
                    return;
                }
                if (!data.hasSkillScroll(base) && data.unlockSkillScroll(base, true)) {
                    ItemStack used = pStack.m_41777_();
                    pStack.m_41774_(1);
                    pLevel.m_5594_(null, playerIn.m_20183_(), SoundEvents.f_11887_, SoundSource.PLAYERS, 0.5f, 1.0f);
                    Network.sendTo((IPacket)new PacketScrollUnlockedSkill(pSlotId, used, base.getRegistryName()), (ServerPlayer)playerIn);
                } else if (data.getSkillLevel(base) < base.getMaxLevel()) {
                    data.setSkillLevel(base, data.getSkillLevel(base) + 1);
                    ItemStack used = pStack.m_41777_();
                    pStack.m_41774_(1);
                    pLevel.m_5594_(null, playerIn.m_20183_(), SoundEvents.f_11887_, SoundSource.PLAYERS, 0.5f, 1.0f);
                    Network.sendTo((IPacket)new PacketScrollLevelupSkill(pSlotId, used, base.getRegistryName()), (ServerPlayer)playerIn);
                }
            });
        }
        super.m_6883_(pStack, pLevel, pEntity, pSlotId, pIsSelected);
    }

    public Optional<TooltipComponent> m_142422_(ItemStack stack) {
        return Optional.ofNullable(ItemSkillScroll.getSkillFromScroll(stack)).map(SkillTooltip::new);
    }
}

