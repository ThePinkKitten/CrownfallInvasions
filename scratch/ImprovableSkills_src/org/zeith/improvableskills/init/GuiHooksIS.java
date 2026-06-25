/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.core.BlockPos
 *  net.minecraft.world.inventory.ContainerLevelAccess
 *  net.minecraft.world.inventory.MenuType
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.common.extensions.IForgeMenuType
 *  org.zeith.hammerlib.annotations.RegistryName
 *  org.zeith.hammerlib.annotations.SimplyRegister
 */
package org.zeith.improvableskills.init;

import net.minecraft.core.BlockPos;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.extensions.IForgeMenuType;
import org.zeith.hammerlib.annotations.RegistryName;
import org.zeith.hammerlib.annotations.SimplyRegister;
import org.zeith.improvableskills.client.gui.abil.anvil.AnvilMenuPortable;
import org.zeith.improvableskills.client.gui.abil.crafting.CraftingMenuPortable;
import org.zeith.improvableskills.client.gui.abil.ench.ContainerEnchPowBook;
import org.zeith.improvableskills.client.gui.abil.ench.ContainerPortableEnchantment;

@SimplyRegister
public interface GuiHooksIS {
    @RegistryName(value="enchantment")
    public static final MenuType<ContainerPortableEnchantment> ENCHANTMENT = IForgeMenuType.create((windowId, inv, data) -> new ContainerPortableEnchantment(windowId, inv));
    @RegistryName(value="repair")
    public static final MenuType<AnvilMenuPortable> REPAIR = IForgeMenuType.create((windowId, inv, data) -> new AnvilMenuPortable(windowId, inv));
    @RegistryName(value="enchantment_setup")
    public static final MenuType<ContainerEnchPowBook> ENCH_POWER_BOOK_IO = IForgeMenuType.create((windowId, inv, data) -> new ContainerEnchPowBook(windowId, inv));
    @RegistryName(value="crafting")
    public static final MenuType<CraftingMenuPortable> CRAFTING = IForgeMenuType.create((windowId, inv, data) -> new CraftingMenuPortable(windowId, inv, ContainerLevelAccess.m_39289_((Level)inv.f_35978_.m_9236_(), (BlockPos)inv.f_35978_.m_20183_())));
}

