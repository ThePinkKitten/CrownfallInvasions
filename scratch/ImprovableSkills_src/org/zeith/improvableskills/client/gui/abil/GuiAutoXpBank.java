/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.EditBox
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FormattedCharSequence
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.client.utils.UV
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 *  org.zeith.hammerlib.util.XPUtil
 */
package org.zeith.improvableskills.client.gui.abil;

import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.XPUtil;
import org.zeith.improvableskills.api.IGuiSkillDataConsumer;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.client.gui.GuiCentered;
import org.zeith.improvableskills.client.gui.base.GuiCustomButton;
import org.zeith.improvableskills.client.rendering.ote.OTEFadeOutButton;
import org.zeith.improvableskills.client.rendering.ote.OTEFadeOutUV;
import org.zeith.improvableskills.net.PacketSetAutoXpBankData;

public class GuiAutoXpBank
extends GuiCentered
implements IGuiSkillDataConsumer {
    public static final ResourceLocation TEXTURE = new ResourceLocation("improvableskills", "textures/gui/auto_xp_bank.png");
    protected PlayerSkillData data;
    public final UV main;
    protected GuiCustomButton toggleButton;
    protected EditBox keepLevels;

    public GuiAutoXpBank(PlayerSkillData data) {
        this.data = data;
        this.setSize(176, 85);
        this.main = new UV(TEXTURE, 0.0f, 0.0f, (float)this.xSize, (float)this.ySize);
    }

    @Override
    protected void m_7856_() {
        super.m_7856_();
        Objects.requireNonNull(this.f_96547_);
        this.keepLevels = (EditBox)this.m_142416_((GuiEventListener)new EditBox(this.f_96547_, this.guiLeft + 67, this.guiTop + 58, 43, 9, this.keepLevels, (Component)Component.m_237119_()));
        this.keepLevels.m_94151_(str -> {
            try {
                this.keepLevels.m_94202_(ChatFormatting.WHITE.m_126665_().intValue());
                float levels = Float.parseFloat(str.replace(',', '.'));
                if (levels < 0.0f) {
                    throw new IllegalArgumentException();
                }
                int xpt = XPUtil.getXPTotal((int)((int)levels), (float)(levels % 1.0f));
                if (this.data.autoXpBankThreshold != xpt) {
                    this.data.autoXpBankThreshold = xpt;
                    Network.sendToServer((IPacket)new PacketSetAutoXpBankData(this.data.autoXpBankThreshold));
                }
            }
            catch (Throwable ignored) {
                this.keepLevels.m_94202_(ChatFormatting.RED.m_126665_().intValue());
            }
        });
        this.keepLevels.m_94144_("%.03f".formatted(Float.valueOf((float)XPUtil.getLevelFromXPValue((int)this.data.autoXpBankThreshold) + XPUtil.getCurrentFromXPValue((int)this.data.autoXpBankThreshold))).replace(',', '.'));
        this.keepLevels.m_94182_(false);
        this.toggleButton = (GuiCustomButton)this.m_142416_((GuiEventListener)new GuiCustomButton(0, this.guiLeft + 25, this.guiTop + 25, 20, 20, (Component)Component.m_237113_((String)""), btn -> {
            this.data.autoXpBank = !this.data.autoXpBank;
            Network.sendToServer((IPacket)new PacketSetAutoXpBankData(this.data.autoXpBank));
            new OTEFadeOutButton(btn, 15);
            this.m_232761_();
            for (int i = 0; i < 3; ++i) {
                new OTEFadeOutUV(new UV(TEXTURE, 176.0f, this.data != null && this.data.autoXpBank ? 20.0f : 0.0f, 20.0f, 20.0f), 20.0f, 20.0f, btn.m_252754_(), btn.m_252907_(), 15 + i * 10);
            }
        }){

            @Override
            protected void renderBg(GuiGraphics gfx, Minecraft mc, int x, int y) {
                super.renderBg(gfx, mc, x, y);
                FXUtils.bindTexture((ResourceLocation)TEXTURE);
                RenderUtils.drawTexturedModalRect((GuiGraphics)gfx, (float)this.m_252754_(), (float)this.m_252907_(), (float)176.0f, (float)(GuiAutoXpBank.this.data != null && GuiAutoXpBank.this.data.autoXpBank ? 0.0f : 20.0f), (float)20.0f, (float)20.0f);
            }
        });
    }

    public Component getInformation(float range) {
        boolean magnetic = this.data != null && this.data.autoXpBank;
        String levels = "%.01f".formatted(Float.valueOf(range));
        return !magnetic ? Component.m_237115_((String)"text.improvableskills.auto_xp_bank.off") : Component.m_237110_((String)"text.improvableskills.auto_xp_bank.on", (Object[])new Object[]{Component.m_237113_((String)levels)});
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics gfx, float partialTime, int mouseX, int mouseY) {
        PoseStack pose = gfx.m_280168_();
        float value = this.data != null ? (float)XPUtil.getLevelFromXPValue((int)this.data.autoXpBankThreshold) + XPUtil.getCurrentFromXPValue((int)this.data.autoXpBankThreshold) : 0.0f;
        this.m_280273_(gfx);
        this.toggleButton.f_93623_ = this.data != null;
        this.main.render(pose, (float)this.guiLeft, (float)this.guiTop);
        pose.m_85836_();
        float scale = 0.75f;
        pose.m_252880_((float)(this.guiLeft + 48), (float)this.guiTop + 25.25f, 0.0f);
        pose.m_85841_(scale, scale, scale);
        int width = 102;
        width = (int)((float)width / scale);
        int y = 0;
        for (FormattedCharSequence comp : this.f_96547_.m_92923_((FormattedText)this.getInformation(value), width)) {
            gfx.m_280649_(this.f_96547_, comp, 0, y, 0xFFFFFF, true);
            y += 9;
        }
        pose.m_85849_();
    }

    @Override
    public void applySkillData(PlayerSkillData data) {
        this.data = data;
    }
}

