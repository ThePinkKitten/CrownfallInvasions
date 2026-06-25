/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.CraftingMenu
 *  net.minecraft.world.inventory.MenuType
 */
package org.zeith.improvableskills.client.gui.abil.crafting;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.inventory.MenuType;
import org.zeith.improvableskills.init.GuiHooksIS;

public class CraftingMenuPortable
extends CraftingMenu {
    public CraftingMenuPortable(int p_39353_, Inventory p_39354_) {
        super(p_39353_, p_39354_);
    }

    public CraftingMenuPortable(int p_39356_, Inventory p_39357_, ContainerLevelAccess p_39358_) {
        super(p_39356_, p_39357_, p_39358_);
    }

    public MenuType<?> m_6772_() {
        return GuiHooksIS.CRAFTING;
    }

    public boolean m_6875_(Player p_39368_) {
        return true;
    }
}

