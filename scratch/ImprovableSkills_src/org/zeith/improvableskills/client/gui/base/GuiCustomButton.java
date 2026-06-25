/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.Font
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.Button$OnPress
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.client.sounds.SoundManager
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvent
 *  net.minecraft.util.Mth
 *  org.zeith.hammerlib.client.texture.HttpTextureDownloader
 */
package org.zeith.improvableskills.client.gui.base;

import com.mojang.blaze3d.systems.RenderSystem;
import java.util.function.Supplier;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.util.Mth;
import org.zeith.hammerlib.client.texture.HttpTextureDownloader;

public class GuiCustomButton
extends Button {
    protected static final ResourceLocation CBUTTON_TEXTURES = new ResourceLocation("improvableskills", "textures/gui/icons.png");
    public SoundEvent customClickSound;
    public final int id;
    private static final ResourceLocation texture = new ResourceLocation("improvableskills", "textures/builtin/zeitheron.png");

    public GuiCustomButton(int id, int x, int y, int widthIn, int heightIn, String buttonText, Button.OnPress action) {
        this(id, x, y, widthIn, heightIn, (Component)Component.m_237113_((String)buttonText), action);
    }

    public GuiCustomButton(int id, int x, int y, int widthIn, int heightIn, Component buttonText, Button.OnPress action) {
        super(x, y, widthIn, heightIn, buttonText, action, Supplier::get);
        this.id = id;
    }

    public static AbstractTexture getZeithAvatar() {
        return HttpTextureDownloader.create((ResourceLocation)texture, (String)"https://h.zeith.org/discord/fetchAvatar/376091478142746625");
    }

    public GuiCustomButton setCustomClickSound(SoundEvent customClickSound) {
        this.customClickSound = customClickSound;
        return this;
    }

    protected void renderBg(GuiGraphics gfx, Minecraft minecraft, int mouseX, int mouseY) {
        int i = this.getTextureYCustom();
        gfx.m_280218_(CBUTTON_TEXTURES, this.m_252754_(), this.m_252907_(), 0, i * 20, this.f_93618_ / 2, this.f_93619_);
        gfx.m_280218_(CBUTTON_TEXTURES, this.m_252754_() + this.f_93618_ / 2, this.m_252907_(), 200 - this.f_93618_ / 2, i * 20, this.f_93618_ / 2, this.f_93619_);
    }

    public void m_87963_(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        if (this.f_93624_) {
            Minecraft minecraft = Minecraft.m_91087_();
            Font font = minecraft.f_91062_;
            RenderSystem.setShaderTexture((int)0, (ResourceLocation)CBUTTON_TEXTURES);
            gfx.m_280246_(1.0f, 1.0f, 1.0f, this.f_93625_);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();
            RenderSystem.enableDepthTest();
            this.renderBg(gfx, minecraft, mouseX, mouseY);
            int j = this.getFGColor();
            gfx.m_280653_(font, this.m_6035_(), this.m_252754_() + this.f_93618_ / 2, this.m_252907_() + (this.f_93619_ - 8) / 2, j | Mth.m_14167_((float)(this.f_93625_ * 255.0f)) << 24);
            gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
        }
    }

    public int getTextureYCustom() {
        int i = 1;
        if (!this.f_93623_) {
            i = 0;
        } else if (this.m_198029_()) {
            i = 2;
        }
        return i;
    }

    public void m_7435_(SoundManager soundManager) {
        if (this.customClickSound == null) {
            super.m_7435_(soundManager);
        } else {
            soundManager.m_120367_((SoundInstance)SimpleSoundInstance.m_119752_((SoundEvent)this.customClickSound, (float)1.0f));
        }
    }
}

