/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 *  org.zeith.hammerlib.util.XPUtil
 */
package org.zeith.improvableskills.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.math.BigInteger;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.XPUtil;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.IGuiSkillDataConsumer;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.client.gui.base.GuiCustomButton;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.client.rendering.ote.OTEFadeOutButton;
import org.zeith.improvableskills.client.rendering.ote.OTEXpOrb;
import org.zeith.improvableskills.custom.pagelets.PageletXPStorage;
import org.zeith.improvableskills.init.PageletsIS;
import org.zeith.improvableskills.net.PacketDrawXP;
import org.zeith.improvableskills.net.PacketStoreXP;

public class GuiXPBank
extends GuiTabbable<PageletXPStorage>
implements IGuiSkillDataConsumer {
    public PlayerSkillData data;
    public double targetXP_X;
    public double targetXP_Y;
    public float currentXP;
    public float prevXP;
    public final ResourceLocation ICONS_TX = new ResourceLocation("improvableskills", "textures/gui/skills_gui_paper.png");

    public GuiXPBank(PageletXPStorage pagelet) {
        super(pagelet);
        this.data = SyncSkills.getData();
        this.xSize = 195;
        this.ySize = 168;
    }

    @Override
    public void m_7856_() {
        super.m_7856_();
        int guiLeft = this.guiLeft;
        int guiTop = this.guiTop;
        int sizeX = 20;
        int scrW = this.xSize - sizeX * 2;
        MutableComponent storeAll = Component.m_237115_((String)"text.improvableskills:storeall");
        MutableComponent draw10 = Component.m_237115_((String)"text.improvableskills:draw10lvls");
        MutableComponent draw1 = Component.m_237115_((String)"text.improvableskills:draw1lvl");
        GuiCustomButton btn = new GuiCustomButton(1, guiLeft + 21, guiTop + 39, 100, 20, (Component)storeAll, this::actionPerformed).setCustomClickSound(SoundEvents.f_11871_);
        this.m_142416_((GuiEventListener)btn);
        btn.m_93674_(this.f_96547_.m_92852_((FormattedText)storeAll) + 8);
        int wid = this.f_96547_.m_92852_((FormattedText)draw10);
        GuiCustomButton btn2 = new GuiCustomButton(3, guiLeft + sizeX + scrW - wid - 13, guiTop + btn.m_93694_() + 30, wid + 12, 20, (Component)draw10, this::actionPerformed).setCustomClickSound(SoundEvents.f_11871_);
        this.m_142416_((GuiEventListener)btn2);
        this.m_142416_((GuiEventListener)new GuiCustomButton(2, btn2.m_252754_(), guiTop + 28, btn2.m_5711_(), 20, (Component)draw1.m_130946_(" "), this::actionPerformed).setCustomClickSound(SoundEvents.f_11871_));
    }

    @Override
    public void applySkillData(PlayerSkillData data) {
        this.data = data;
    }

    @Override
    public void m_86600_() {
        super.m_86600_();
        BigInteger i = this.data.storageXp;
        int xp = i.intValue();
        float current = XPUtil.getCurrentFromXPValue((int)xp);
        if (this.currentXP == 0.0f && this.prevXP == 0.0f) {
            this.currentXP = this.prevXP = current;
        } else {
            this.prevXP = this.currentXP;
            this.currentXP = current;
        }
    }

    @Override
    protected void drawBack(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        PoseStack pose = gfx.m_280168_();
        this.setWhiteColor(gfx);
        this.gui1.render(pose, (float)this.guiLeft, (float)this.guiTop);
        int guiLeft = this.guiLeft;
        int guiTop = this.guiTop;
        int sizeX = 20;
        int sizeY = 26;
        gfx.m_280246_(0.8f, 0.8f, 0.8f, 1.0f);
        gfx.m_280218_(this.ICONS_TX, guiLeft + sizeX, guiTop + sizeY - 9, sizeX, sizeY, this.xSize - sizeX * 2, this.ySize - sizeY * 2 - 4 + 14);
        MutableComponent form = PageletsIS.XP_STORAGE.getTitle().m_6881_().m_130940_(ChatFormatting.BLACK).m_130946_(": " + this.data.storageXp + " XP");
        gfx.m_280614_(this.f_96547_, (Component)form, guiLeft + (this.xSize - this.f_96547_.m_92852_((FormattedText)form)) / 2, guiTop + 9, 4161280, false);
        this.setWhiteColor(gfx);
        FXUtils.bindTexture((String)"minecraft", (String)"textures/gui/icons.png");
        double bx = (double)(this.xSize / 2) - 72.8;
        double by = this.ySize - 50;
        BigInteger i = this.data.storageXp;
        int xp = i.intValue();
        pose.m_85836_();
        this.targetXP_X = (double)guiLeft + bx;
        this.targetXP_Y = (double)guiTop + by + 18.0;
        pose.m_85837_(this.targetXP_X, this.targetXP_Y, 0.0);
        pose.m_85841_(0.8f, 0.8f, 0.8f);
        RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)0.0f, (float)64.0f, (float)182.0f, (float)5.0f);
        RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)0.0f, (float)69.0f, (float)(182.0f * (this.prevXP + (this.currentXP - this.prevXP) * partialTicks)), (float)5.0f);
        pose.m_85849_();
        pose.m_85836_();
        pose.m_85837_((double)guiLeft + bx, (double)guiTop + by + 18.0, 0.0);
        pose.m_85841_(1.1f, 1.1f, 1.1f);
        int lvl = XPUtil.getLevelFromXPValue((int)xp);
        String text = lvl < 0 ? "TOO MUCH!!!" : Integer.toString(lvl);
        RenderSystem.setShaderColor((float)0.24705882f, (float)0.49803922f, (float)0.0f, (float)1.0f);
        gfx.drawString(this.f_96547_, text, (145.6f - (float)this.f_96547_.m_92895_(text)) / 2.0f * 0.9f, -8.0f, 4161280, false);
        this.setWhiteColor(gfx);
        pose.m_85849_();
        float r = (float)(System.currentTimeMillis() % 2000L) / 2000.0f;
        r = r > 0.5f ? 1.0f - r : r;
        gfx.m_280137_(this.f_96547_, I18n.m_118938_((String)"text.improvableskills:totalXP", (Object[])new Object[]{XPUtil.getXPTotal((Player)this.f_96541_.f_91074_)}), guiLeft + this.xSize / 2, guiTop + this.ySize + 4, (int)((r += 0.45f) * 255.0f) << 16 | 0xFF00 | 0);
        this.setBlueColor(gfx);
        this.gui2.render(pose, (double)guiLeft, (double)guiTop, (float)this.xSize, (float)this.ySize);
        this.setWhiteColor(gfx);
    }

    protected void actionPerformed(Button buttonIn) {
        if (!(buttonIn instanceof GuiCustomButton)) {
            return;
        }
        GuiCustomButton button = (GuiCustomButton)buttonIn;
        int id = button.id;
        new OTEFadeOutButton(button, id == 0 ? 2 : 20);
        if (id == 0) {
            this.f_96541_.m_91152_(this.parent);
        } else if (id == 1) {
            RandomSource rand = this.f_96541_.f_91074_.m_217043_();
            int lvls = this.f_96541_.f_91074_.f_36078_;
            for (int i = 0; i < Math.min(100, lvls); ++i) {
                double rtx = this.targetXP_X + (double)(rand.m_188501_() * 182.0f * 0.8f);
                double rty = this.targetXP_Y + (double)(rand.m_188501_() * 5.0f);
                double rx = (float)(this.guiLeft + this.xSize / 2) + (rand.m_188501_() - rand.m_188501_()) * 30.0f;
                float f = this.guiTop + this.ySize + 4;
                float f2 = rand.m_188501_();
                Objects.requireNonNull(this.f_96547_);
                double ry = f + f2 * 9.0f;
                OnTopEffects.effects.add(new OTEXpOrb(rx, ry, rtx, rty, 40));
            }
            Network.sendToServer((IPacket)new PacketStoreXP(XPUtil.getXPTotal((Player)this.f_96541_.f_91074_)));
        } else if (id == 2) {
            Network.sendToServer((IPacket)new PacketDrawXP(XPUtil.getXPValueToNextLevel((int)XPUtil.getLevelFromXPValue((int)XPUtil.getXPTotal((Player)this.f_96541_.f_91074_)))));
            if (this.data.storageXp.longValue() > 0L) {
                RandomSource rand = this.f_96541_.f_91074_.m_217043_();
                BigInteger i = this.data.storageXp;
                int xp = i.intValue();
                float current = XPUtil.getCurrentFromXPValue((int)xp);
                double rx = this.targetXP_X + (double)(rand.m_188501_() * 182.0f * current * 0.8f);
                double ry = this.targetXP_Y + (double)(rand.m_188501_() * 5.0f);
                double rtx = (float)(this.guiLeft + this.xSize / 2) + (rand.m_188501_() - rand.m_188501_()) * 30.0f;
                float f = this.guiTop + this.ySize + 4;
                float f3 = rand.m_188501_();
                Objects.requireNonNull(this.f_96547_);
                double rty = f + f3 * 9.0f;
                OnTopEffects.effects.add(new OTEXpOrb(rx, ry, rtx, rty, 40));
            }
        } else if (id == 3) {
            RandomSource rand = this.f_96541_.f_91074_.m_217043_();
            int xpLvl = XPUtil.getLevelFromXPValue((int)XPUtil.getXPTotal((Player)this.f_96541_.f_91074_));
            int xp = 0;
            for (int i = 0; i < 10; ++i) {
                BigInteger i0 = this.data.storageXp;
                int xp0 = i0.intValue();
                float current = XPUtil.getCurrentFromXPValue((int)xp0);
                if (this.data.storageXp.longValue() > 0L) {
                    double rx = this.targetXP_X + (double)(rand.m_188501_() * 182.0f * current * 0.8f);
                    double ry = this.targetXP_Y + (double)(rand.m_188501_() * 5.0f);
                    double rtx = (float)(this.guiLeft + this.xSize / 2) + (rand.m_188501_() - rand.m_188501_()) * 30.0f;
                    double d = this.guiTop + this.ySize + 4;
                    float f = rand.m_188501_();
                    Objects.requireNonNull(this.f_96547_);
                    double rty = d + (double)(f * 9.0f) * 0.8;
                    OnTopEffects.effects.add(new OTEXpOrb(rx, ry, rtx, rty, 40));
                }
                xp += XPUtil.getXPValueToNextLevel((int)(xpLvl + i));
            }
            Network.sendToServer((IPacket)new PacketDrawXP(xp));
        }
    }
}

