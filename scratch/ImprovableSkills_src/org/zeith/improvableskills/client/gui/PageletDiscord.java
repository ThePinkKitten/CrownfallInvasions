/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package org.zeith.improvableskills.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PageletBase;
import org.zeith.improvableskills.client.gui.GuiDiscord;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;

public class PageletDiscord
extends PageletBase {
    public final ResourceLocation texture = new ResourceLocation("improvableskills", "textures/gui/discord.png");

    public PageletDiscord() {
        this.setTitle((Component)Component.m_237115_((String)"pagelet.improvableskills:discord1"));
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public GuiTabbable<?> createTab(PlayerSkillData data) {
        return new GuiDiscord(this);
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public Object getIcon() {
        Object o = super.getIcon();
        if (!(o instanceof AbstractTexture)) {
            o = Minecraft.m_91087_().m_91097_().m_118506_(this.texture);
            this.setIcon(o);
        }
        return o;
    }

    @Override
    public boolean isRight() {
        return false;
    }
}

