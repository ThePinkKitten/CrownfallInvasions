/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.block.Blocks
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package org.zeith.improvableskills.custom.pagelets;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PageletBase;
import org.zeith.improvableskills.client.gui.GuiAbilityBook;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;

public class PageletAbilities
extends PageletBase {
    public PageletAbilities() {
        this.setIcon(new ItemStack((ItemLike)Blocks.f_50201_));
        this.setTitle((Component)Component.m_237115_((String)"pagelet.improvableskills:abilities"));
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public GuiTabbable<?> createTab(PlayerSkillData data) {
        return new GuiAbilityBook(this, data);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public boolean isVisible(PlayerSkillData data) {
        return data.getAbilityCount() > 0;
    }
}

