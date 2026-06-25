/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.ints.IntArrayList
 *  it.unimi.dsi.fastutil.ints.IntList
 *  net.minecraft.Util
 *  net.minecraft.advancements.CriteriaTriggers
 *  net.minecraft.core.BlockPos
 *  net.minecraft.core.registries.BuiltInRegistries
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.stats.Stats
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.EnchantmentMenu
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.item.EnchantedBookItem
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.item.enchantment.EnchantmentHelper
 *  net.minecraft.world.item.enchantment.EnchantmentInstance
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.event.ForgeEventFactory
 */
package org.zeith.improvableskills.client.gui.abil.ench;

import it.unimi.dsi.fastutil.ints.IntArrayList;
import it.unimi.dsi.fastutil.ints.IntList;
import java.util.List;
import net.minecraft.Util;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.stats.Stats;
import net.minecraft.util.RandomSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.item.EnchantedBookItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.EnchantmentHelper;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraftforge.event.ForgeEventFactory;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.GuiHooksIS;
import org.zeith.improvableskills.utils.GuiManager;

public class ContainerPortableEnchantment
extends EnchantmentMenu {
    public Level worldIn;
    public Player player;
    public int color;
    int capturing;
    IntList capture = new IntArrayList();

    public ContainerPortableEnchantment(int windowId, Inventory inv) {
        super(windowId, inv, ContainerLevelAccess.m_39289_((Level)inv.f_35978_.m_9236_(), (BlockPos)inv.f_35978_.m_20183_()));
        this.player = inv.f_35978_;
        this.worldIn = inv.f_35978_.m_9236_();
    }

    public MenuType<?> m_6772_() {
        return GuiHooksIS.ENCHANTMENT;
    }

    public boolean m_6875_(Player playerIn) {
        return true;
    }

    public void m_6199_(Container p_39461_) {
        if (p_39461_ == this.f_39449_) {
            ItemStack itemstack = p_39461_.m_8020_(0);
            if (!itemstack.m_41619_() && itemstack.m_41792_()) {
                float j = PlayerDataManager.handleDataSafely(this.player, data -> Float.valueOf(data.enchantPower), Float.valueOf(0.0f)).floatValue();
                this.f_39451_.m_188584_((long)this.f_39452_.m_6501_());
                for (int k = 0; k < 3; ++k) {
                    this.f_39446_[k] = EnchantmentHelper.m_220287_((RandomSource)this.f_39451_, (int)k, (int)((int)j), (ItemStack)itemstack);
                    this.f_39447_[k] = -1;
                    this.f_39448_[k] = -1;
                    if (this.f_39446_[k] < k + 1) {
                        this.f_39446_[k] = 0;
                    }
                    this.f_39446_[k] = ForgeEventFactory.onEnchantmentLevelSet((Level)this.worldIn, (BlockPos)this.player.m_20183_(), (int)k, (int)((int)j), (ItemStack)itemstack, (int)this.f_39446_[k]);
                }
                for (int l = 0; l < 3; ++l) {
                    List<EnchantmentInstance> list;
                    if (this.f_39446_[l] <= 0 || (list = this.m_39471_(itemstack, l, this.f_39446_[l])).isEmpty()) continue;
                    EnchantmentInstance enchantmentinstance = list.get(this.f_39451_.m_188503_(list.size()));
                    this.f_39447_[l] = BuiltInRegistries.f_256876_.m_7447_((Object)enchantmentinstance.f_44947_);
                    this.f_39448_[l] = enchantmentinstance.f_44948_;
                }
                this.m_38946_();
            } else {
                for (int i = 0; i < 3; ++i) {
                    this.f_39446_[i] = 0;
                    this.f_39447_[i] = -1;
                    this.f_39448_[i] = -1;
                }
            }
        }
    }

    public boolean m_6366_(Player player, int id) {
        if (this.capturing > 0) {
            this.capture.add(id);
            --this.capturing;
            if (this.capture.size() == 3 && this.capturing == 0) {
                this.color = this.capture.getInt(0) << 16 | this.capture.getInt(1) << 8 | this.capture.getInt(2);
            }
            return true;
        }
        if (id == 121) {
            this.capturing = 3;
            return true;
        }
        if (id == 122) {
            if (!this.worldIn.f_46443_) {
                GuiManager.openGuiCallback(GuiHooksIS.ENCH_POWER_BOOK_IO, player);
            }
            return true;
        }
        if (id >= 0 && id < this.f_39446_.length) {
            ItemStack itemstack = this.f_39449_.m_8020_(0);
            ItemStack itemstack1 = this.f_39449_.m_8020_(1);
            int i = id + 1;
            if ((itemstack1.m_41619_() || itemstack1.m_41613_() < i) && !player.m_150110_().f_35937_) {
                return false;
            }
            if (this.f_39446_[id] <= 0 || itemstack.m_41619_() || (player.f_36078_ < i || player.f_36078_ < this.f_39446_[id]) && !player.m_150110_().f_35937_) {
                return false;
            }
            this.f_39450_.m_39292_((p_39481_, p_39482_) -> {
                ItemStack itemstack2 = itemstack;
                List<EnchantmentInstance> list = this.m_39471_(itemstack, id, this.f_39446_[id]);
                if (!list.isEmpty()) {
                    player.m_7408_(itemstack, i);
                    boolean flag = itemstack.m_150930_(Items.f_42517_);
                    if (flag) {
                        itemstack2 = new ItemStack((ItemLike)Items.f_42690_);
                        CompoundTag compoundtag = itemstack.m_41783_();
                        if (compoundtag != null) {
                            itemstack2.m_41751_(compoundtag.m_6426_());
                        }
                        this.f_39449_.m_6836_(0, itemstack2);
                    }
                    for (int j = 0; j < list.size(); ++j) {
                        EnchantmentInstance enchantmentinstance = list.get(j);
                        if (flag) {
                            EnchantedBookItem.m_41153_((ItemStack)itemstack2, (EnchantmentInstance)enchantmentinstance);
                            continue;
                        }
                        itemstack2.m_41663_(enchantmentinstance.f_44947_, enchantmentinstance.f_44948_);
                    }
                    if (!player.m_150110_().f_35937_) {
                        itemstack1.m_41774_(i);
                        if (itemstack1.m_41619_()) {
                            this.f_39449_.m_6836_(1, ItemStack.f_41583_);
                        }
                    }
                    player.m_36220_(Stats.f_12964_);
                    if (player instanceof ServerPlayer) {
                        CriteriaTriggers.f_10575_.m_27668_((ServerPlayer)player, itemstack2, i);
                    }
                    this.f_39449_.m_6596_();
                    this.f_39452_.m_6422_(player.m_36322_());
                    this.m_6199_(this.f_39449_);
                    p_39481_.m_5594_(null, p_39482_, SoundEvents.f_11887_, SoundSource.BLOCKS, 1.0f, p_39481_.f_46441_.m_188501_() * 0.1f + 0.9f);
                }
            });
            return true;
        }
        Util.m_143785_((String)(player.m_7755_() + " pressed invalid button id: " + id));
        return false;
    }

    private List<EnchantmentInstance> m_39471_(ItemStack stack, int seed, int cost) {
        this.f_39451_.m_188584_((long)(this.f_39452_.m_6501_() + seed));
        List list = EnchantmentHelper.m_220297_((RandomSource)this.f_39451_, (ItemStack)stack, (int)cost, (boolean)false);
        if (stack.m_150930_(Items.f_42517_) && list.size() > 1) {
            list.remove(this.f_39451_.m_188503_(list.size()));
        }
        return list;
    }
}

