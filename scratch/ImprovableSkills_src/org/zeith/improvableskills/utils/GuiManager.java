/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.MenuProvider
 *  net.minecraft.world.SimpleMenuProvider
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.MenuType
 */
package org.zeith.improvableskills.utils;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;

public class GuiManager {
    public static void openGuiCallback(MenuType<?> menu, Player playerIn) {
        GuiManager.openGuiCallback(menu, playerIn, (Component)Component.m_237113_((String)""));
    }

    public static void openGuiCallback(MenuType<?> menu, Player playerIn, Component label) {
        playerIn.m_5893_((MenuProvider)new SimpleMenuProvider((windowId, inv, player) -> menu.m_39985_(windowId, inv), label));
    }
}

