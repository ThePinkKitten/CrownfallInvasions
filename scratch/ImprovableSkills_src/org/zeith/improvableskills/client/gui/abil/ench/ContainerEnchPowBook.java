/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.sounds.SoundSource
 *  net.minecraft.world.Container
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  net.minecraft.world.level.ItemLike
 *  org.zeith.hammerlib.api.inv.SimpleInventory
 *  org.zeith.hammerlib.util.java.Threading
 */
package org.zeith.improvableskills.client.gui.abil.ench;

import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import org.zeith.hammerlib.api.inv.SimpleInventory;
import org.zeith.hammerlib.util.java.Threading;
import org.zeith.improvableskills.data.PlayerDataManager;
import org.zeith.improvableskills.init.GuiHooksIS;
import org.zeith.improvableskills.utils.GuiManager;

public class ContainerEnchPowBook
extends AbstractContainerMenu {
    public final SimpleInventory inventory = new SimpleInventory(1);

    public ContainerEnchPowBook(int windowId, Inventory playerInv) {
        super(GuiHooksIS.ENCH_POWER_BOOK_IO, windowId);
        for (int i = 0; i < 3; ++i) {
            for (int j = 0; j < 9; ++j) {
                this.m_38897_(new Slot((Container)playerInv, j + i * 9 + 9, 8 + j * 18, 82 + i * 18));
            }
        }
        for (int k = 0; k < 9; ++k) {
            this.m_38897_(new Slot((Container)playerInv, k, 8 + k * 18, 140));
        }
        this.m_38897_(new Slot((Container)this.inventory, 0, 52, 32){

            public boolean m_5857_(ItemStack stack) {
                return !stack.m_41619_() && stack.m_41720_() == Items.f_42517_;
            }
        });
    }

    public boolean m_6366_(Player playerIn, int id) {
        if (id == 11) {
            if (!playerIn.m_9236_().f_46443_) {
                PlayerDataManager.handleDataSafely(playerIn, data -> {
                    ItemStack item = this.inventory.getStackInSlot(0);
                    if ((item.m_41619_() || item.m_41720_() == Items.f_42517_ && item.m_41613_() < 64) && data.enchantPower > 0.0f) {
                        if (item.m_41619_()) {
                            this.inventory.m_6836_(0, new ItemStack((ItemLike)Items.f_42517_));
                        } else {
                            item.m_41769_(1);
                        }
                        data.enchantPower -= 1.0f;
                        data.sync();
                        playerIn.m_9236_().m_5594_(null, playerIn.m_20183_(), SoundEvents.f_11859_, SoundSource.PLAYERS, 1.0f, playerIn.m_9236_().f_46441_.m_188501_() * 0.1f + 1.5f);
                    }
                });
            }
            return true;
        }
        if (id == 0) {
            if (!playerIn.m_9236_().f_46443_) {
                PlayerDataManager.handleDataSafely(playerIn, data -> {
                    ItemStack item = this.inventory.getStackInSlot(0);
                    if (!item.m_41619_() && item.m_41720_() == Items.f_42517_ && data.enchantPower < 15.0f) {
                        item.m_41774_(1);
                        data.enchantPower += 1.0f;
                        data.sync();
                        playerIn.m_9236_().m_5594_(null, playerIn.m_20183_(), SoundEvents.f_11859_, SoundSource.PLAYERS, 1.0f, playerIn.m_9236_().f_46441_.m_188501_() * 0.1f + 1.5f);
                    }
                });
            }
            return true;
        }
        if (id == 1) {
            GuiManager.openGuiCallback(GuiHooksIS.ENCHANTMENT, playerIn);
            return true;
        }
        return false;
    }

    public boolean m_6875_(Player player) {
        return true;
    }

    public ItemStack m_7648_(Player player, int slotId) {
        ItemStack itemstack = ItemStack.f_41583_;
        Slot slot = (Slot)this.f_38839_.get(slotId);
        if (slot != null && slot.m_6657_()) {
            ItemStack itemstack1 = slot.m_7993_();
            itemstack = itemstack1.m_41777_();
            if (slot.f_40218_ == player.m_150109_() ? !this.m_38903_(itemstack1, 36, this.f_38839_.size(), true) : !this.m_38903_(itemstack1, 0, 36, true)) {
                return ItemStack.f_41583_;
            }
            if (itemstack1.m_41619_()) {
                slot.m_5852_(ItemStack.f_41583_);
            } else {
                slot.m_6654_();
            }
            if (itemstack1.m_41613_() == itemstack.m_41613_()) {
                return ItemStack.f_41583_;
            }
            slot.m_142406_(player, itemstack1);
        }
        return itemstack;
    }

    public void m_6877_(Player playerIn) {
        this.m_150411_(playerIn, (Container)this.inventory);
        super.m_6877_(playerIn);
        if (playerIn.m_20194_() != null) {
            Threading.createAndStart(() -> playerIn.m_20194_().execute(() -> GuiManager.openGuiCallback(GuiHooksIS.ENCHANTMENT, playerIn)));
        }
    }
}

