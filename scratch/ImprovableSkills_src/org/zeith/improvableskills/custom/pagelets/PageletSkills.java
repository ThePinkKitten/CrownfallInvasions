/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package org.zeith.improvableskills.custom.pagelets;

import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PageletBase;
import org.zeith.improvableskills.client.gui.GuiSkillsBook;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;
import org.zeith.improvableskills.custom.items.ItemSkillScroll;
import org.zeith.improvableskills.init.SkillsIS;

public class PageletSkills
extends PageletBase {
    public PageletSkills() {
        this.setIcon(() -> ItemSkillScroll.of(SkillsIS.SILENT_FOOT));
        this.setTitle((Component)Component.m_237115_((String)"pagelet.improvableskills:skills"));
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public GuiTabbable<?> createTab(PlayerSkillData data) {
        return new GuiSkillsBook(this, data);
    }
}

