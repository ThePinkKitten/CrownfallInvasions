/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  org.zeith.hammerlib.client.utils.UV
 */
package org.zeith.improvableskills.custom.pagelets;

import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PageletBase;
import org.zeith.improvableskills.client.gui.GuiXPBank;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;

public class PageletXPStorage
extends PageletBase {
    public final ResourceLocation texture = new ResourceLocation("improvableskills", "textures/gui/xp_bank.png");

    public PageletXPStorage() {
        this.setTitle((Component)Component.m_237115_((String)"pagelet.improvableskills:xp_bank"));
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public Object getIcon() {
        Object o = super.getIcon();
        if (!(o instanceof UV)) {
            o = new UV(this.texture, 0.0f, 0.0f, 256.0f, 256.0f);
            this.setIcon(o);
        }
        return o;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public GuiTabbable<?> createTab(PlayerSkillData data) {
        return new GuiXPBank(this);
    }

    @Override
    public void reload() {
    }

    @Override
    public boolean isVisible(PlayerSkillData data) {
        return data.enableXPBank;
    }
}

