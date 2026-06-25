/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AnvilMenu
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.level.Level
 */
package org.zeith.improvableskills.client.gui.abil.anvil;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AnvilMenu;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import org.zeith.improvableskills.init.GuiHooksIS;

public class AnvilMenuPortable
extends AnvilMenu {
    public AnvilMenuPortable(int windowId, Inventory inventory) {
        super(windowId, inventory, ContainerLevelAccess.m_39289_((Level)inventory.f_35978_.m_9236_(), (BlockPos)inventory.f_35978_.m_20183_()));
    }

    public MenuType<?> m_6772_() {
        return GuiHooksIS.REPAIR;
    }

    public boolean m_6875_(Player player) {
        return true;
    }
}

