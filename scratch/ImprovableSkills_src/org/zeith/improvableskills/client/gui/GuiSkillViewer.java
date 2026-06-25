/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.util.FormattedCharSequence
 *  net.minecraft.util.Mth
 *  net.minecraft.world.entity.player.Player
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.client.utils.TexturePixelGetter
 *  org.zeith.hammerlib.client.utils.UV
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 *  org.zeith.hammerlib.util.XPUtil
 */
package org.zeith.improvableskills.client.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.Objects;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.client.utils.TexturePixelGetter;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.hammerlib.util.XPUtil;
import org.zeith.improvableskills.api.IGuiSkillDataConsumer;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.client.IClientSkillExtensions;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.client.gui.GuiCentered;
import org.zeith.improvableskills.client.gui.GuiSkillsBook;
import org.zeith.improvableskills.client.gui.base.GuiCustomButton;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.client.rendering.ote.OTEFadeOutButton;
import org.zeith.improvableskills.client.rendering.ote.OTEFadeOutUV;
import org.zeith.improvableskills.client.rendering.ote.OTESparkle;
import org.zeith.improvableskills.client.rendering.ote.OTETooltip;
import org.zeith.improvableskills.init.SoundsIS;
import org.zeith.improvableskills.net.PacketLvlDownSkill;
import org.zeith.improvableskills.net.PacketLvlUpSkill;
import org.zeith.improvableskills.net.PacketSetSkillActivity;

public class GuiSkillViewer
extends GuiCentered
implements IGuiSkillDataConsumer {
    public static final ResourceLocation TEXTURE = new ResourceLocation("improvableskills", "textures/gui/skills_gui_overlay.png");
    public static final UV CROSS = new UV(TEXTURE, 196.0f, 24.0f, 20.0f, 20.0f);
    public static final UV TICK = new UV(TEXTURE, 196.0f, 44.0f, 20.0f, 20.0f);
    final GuiSkillsBook parent;
    public PlayerSkillData data;
    final Style fontStyle;
    final PlayerSkillBase skill;
    int mouseX;
    int mouseY;
    boolean forbidden;
    private static final ResourceLocation ALT_FONT = new ResourceLocation("minecraft", "alt");
    GuiCustomButton btnUpgrade;
    GuiCustomButton btnDegrade;
    GuiCustomButton btnBack;
    GuiCustomButton btnToggle;
    protected int prevLevel;
    protected int currentLevel;
    protected final Random random = new Random();

    public GuiSkillViewer(GuiSkillsBook parent, PlayerSkillBase skill) {
        this.parent = parent;
        this.skill = skill;
        this.data = parent.data;
        this.f_96541_ = Minecraft.m_91087_();
        this.fontStyle = skill.isVisible(parent.data) ? Style.f_131099_ : Style.f_131099_.m_131150_(ALT_FONT);
        this.xSize = 200;
        this.ySize = 150;
    }

    @Override
    public void m_7856_() {
        super.m_7856_();
        this.parent.m_7856_();
        short s = this.data.getSkillLevel(this.skill);
        this.currentLevel = s;
        this.prevLevel = s;
        int gl = this.guiLeft;
        int gt = this.guiTop;
        this.btnUpgrade = (GuiCustomButton)this.m_142416_((GuiEventListener)new GuiCustomButton(0, gl + 10, gt + 124, 75, 20, (Component)Component.m_237115_((String)"button.improvableskills:upgrade"), this::actionPerformed));
        this.btnDegrade = (GuiCustomButton)this.m_142416_((GuiEventListener)new GuiCustomButton(1, gl + 116, gt + 124, 75, 20, (Component)Component.m_237115_((String)"button.improvableskills:degrade"), this::actionPerformed));
        this.btnBack = (GuiCustomButton)this.m_142416_((GuiEventListener)new GuiCustomButton(2, gl + (this.xSize - 20) / 2, gt + 124, 20, 20, " ", this::actionPerformed).setCustomClickSound(SoundsIS.PAGE_TURNS));
        this.btnToggle = (GuiCustomButton)this.m_142416_((GuiEventListener)new GuiCustomButton(3, gl + this.xSize - 30, gt + 14, 20, 20, " ", this::actionPerformed).setCustomClickSound(SoundsIS.PAGE_TURNS));
    }

    public void m_86600_() {
        super.m_86600_();
        this.prevLevel = this.currentLevel;
        this.currentLevel = this.data.getSkillLevel(this.skill);
        int lvl = (this.skill.getMaxLevel() - this.currentLevel) * 10 + 10;
        if (this.currentLevel > 0 && this.random.nextInt(lvl + 10) == 0) {
            int[] rgbs = TexturePixelGetter.getAllColors((ResourceLocation)this.skill.tex.toUV((boolean)true).path);
            int col = rgbs[this.random.nextInt(rgbs.length)];
            double tx = (float)(this.guiLeft + 10) + (float)this.random.nextInt(64) / 2.0f;
            double ty = (float)(this.guiTop + 6) + (float)this.random.nextInt(64) / 2.0f;
            OnTopEffects.effects.add(new OTESparkle(tx, ty, tx, ty, 10, col));
        }
    }

    @Override
    public void m_88315_(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        PoseStack pose = gfx.m_280168_();
        this.mouseX = mouseX;
        this.mouseY = mouseY;
        this.forbidden = !this.skill.isVisible(this.parent.data);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        short nextSkillLevel = (short)(this.data.getSkillLevel(this.skill) + 1);
        int xp = this.skill.getXPToUpgrade(this.data, nextSkillLevel);
        int xp2 = this.skill.getXPToDowngrade(this.data, (short)(nextSkillLevel - 2));
        this.btnToggle.f_93623_ = nextSkillLevel > 1;
        boolean notMaxedOut = nextSkillLevel <= this.skill.getMaxLevel() && !this.forbidden;
        super.m_88315_(gfx, mouseX, mouseY, partialTicks);
        FXUtils.bindTexture((ResourceLocation)TEXTURE);
        pose.m_85836_();
        pose.m_252880_((float)(this.guiLeft + (this.xSize - 20) / 2 + 2), (float)(this.guiTop + 126), 200.0f);
        pose.m_85841_(1.5f, 1.5f, 1.0f);
        RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)195.0f, (float)10.0f, (float)10.0f, (float)11.0f);
        pose.m_85849_();
        boolean active = this.data.isSkillActive(this.skill);
        pose.m_85836_();
        pose.m_252880_((float)(this.btnToggle.m_252754_() + 1), (float)(this.btnToggle.m_252907_() + 1), 200.0f);
        pose.m_85841_(0.9f, 0.9f, 0.9f);
        RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)GuiSkillViewer.CROSS.posX, (float)(GuiSkillViewer.CROSS.posY + (float)(active ? 20 : 0)), (float)20.0f, (float)20.0f);
        pose.m_85849_();
        gfx.m_280653_(this.forbidden ? this.f_96541_.f_243022_ : this.f_96547_, (Component)Component.m_237110_((String)"text.improvableskills:totalXP", (Object[])new Object[]{XPUtil.getXPTotal((Player)this.f_96541_.f_91074_)}), this.guiLeft + this.xSize / 2, this.guiTop + this.ySize + 2, 0x88FF00);
        this.btnUpgrade.f_93623_ = true;
        if (this.btnUpgrade.m_5953_(mouseX, mouseY) && notMaxedOut) {
            OTETooltip.showTooltip(new Component[]{Component.m_237113_((String)("-" + xp + " XP"))});
        }
        if (this.btnDegrade.m_5953_(mouseX, mouseY)) {
            OTETooltip.showTooltip(new Component[]{Component.m_237113_((String)("+" + xp2 + " XP"))});
        }
        this.btnUpgrade.f_93623_ = notMaxedOut && this.skill.canUpgrade(this.data);
        boolean bl = this.btnDegrade.f_93623_ = this.data.getSkillLevel(this.skill) > 0 && !this.forbidden;
        if (this.btnBack.m_5953_(mouseX, mouseY)) {
            OTETooltip.showTooltip(new Component[]{Component.m_237115_((String)"gui.back")});
        }
        if (this.btnToggle.m_5953_(mouseX, mouseY)) {
            OTETooltip.showTooltip(new Component[]{Component.m_237110_((String)("gui.improvableskills.toggle_skill." + (active ? "enabled" : "disabled")), (Object[])new Object[]{this.skill.getLocalizedName()})});
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(GuiGraphics gfx, float partialTicks, int mouseX, int mouseY) {
        PoseStack pose = gfx.m_280168_();
        MutableComponent name = this.skill.getLocalizedName(this.data);
        this.m_280273_(gfx);
        gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
        pose.m_85836_();
        pose.m_252880_((float)this.guiLeft, (float)this.guiTop, 0.0f);
        FXUtils.bindTexture((String)"improvableskills", (String)"textures/gui/skill_viewer.png");
        RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)0.0f, (float)0.0f, (float)0.0f, (float)0.0f, (float)this.xSize, (float)this.ySize);
        gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
        float lev = Mth.m_14179_((float)this.f_96541_.getPartialTick(), (float)this.prevLevel, (float)this.currentLevel) / (float)this.skill.getMaxLevel();
        if (!IClientSkillExtensions.of(this.skill).slotRenderer().drawSlot(gfx, 10.0f, 6.0f, 32.0f, 32.0f, lev, partialTicks)) {
            this.skill.tex.toUV(false).render(pose, 10.0, 6.0, 32.0f, 32.0f);
            if (lev > 0.0f) {
                UV hov = this.skill.tex.toUV(true);
                gfx.m_280246_(1.0f, 1.0f, 1.0f, lev);
                hov.render(pose, 10.0, 6.0, 32.0f, 32.0f);
                gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
        gfx.m_280056_(this.f_96547_, I18n.m_118938_((String)"text.improvableskills:level", (Object[])new Object[]{this.data.getSkillLevel(this.skill), this.skill.getMaxLevel()}), 44, 30, 0x555555, false);
        float scale = Math.min((float)((this.xSize - 48) / this.f_96547_.m_92852_((FormattedText)name)), 1.5f);
        Objects.requireNonNull(this.f_96547_);
        double flh = 9.0f * scale;
        pose.m_85837_(44.0, 6.0 + (24.0 - flh) / 2.0, 0.0);
        pose.m_85841_(scale, scale, 1.0f);
        gfx.m_280614_(this.f_96547_, (Component)name, 0, 0, 0x555555, false);
        pose.m_85849_();
        int maxWid = 176;
        pose.m_85836_();
        pose.m_252880_(0.0f, 2.0f, 0.0f);
        for (FormattedCharSequence formattedcharsequence : this.f_96547_.m_92923_((FormattedText)this.skill.getLocalizedDesc(this.data), maxWid)) {
            gfx.m_280649_(this.f_96547_, formattedcharsequence, this.guiLeft + 12, this.guiTop + 42, 0xFFFFFF, false);
            pose.m_252880_(0.0f, 9.0f, 0.0f);
        }
        pose.m_85849_();
    }

    protected void actionPerformed(Button button) {
        double ty;
        double tx;
        int col;
        if (!(button instanceof GuiCustomButton)) {
            return;
        }
        GuiCustomButton b = (GuiCustomButton)button;
        new OTEFadeOutButton(b, b.id == 2 ? 2 : 20);
        this.m_232761_();
        if (b.id == 2) {
            this.f_96541_.m_91152_((Screen)this.parent);
            new OTEFadeOutUV(new UV(TEXTURE, 195.0f, 10.0f, 10.0f, 11.0f), 15.0f, 16.5f, this.guiLeft + (this.xSize - 20) / 2 + 2, this.guiTop + 126, 2);
        }
        if (b.id == 3) {
            boolean newState = !this.data.isSkillActive(this.skill);
            this.data.setSkillState(this.skill, newState);
            new OTEFadeOutUV(new UV(TEXTURE, GuiSkillViewer.CROSS.posX, GuiSkillViewer.CROSS.posY + (float)(newState ? 20 : 0), 20.0f, 20.0f), 18.0f, 18.0f, b.m_252754_() + 1, b.m_252907_() + 1, 20);
            Network.sendToServer((IPacket)new PacketSetSkillActivity(this.skill.getRegistryName(), newState));
        }
        if (b.id == 0) {
            int[] rgbs = TexturePixelGetter.getAllColors((ResourceLocation)this.skill.tex.toUV((boolean)true).path);
            col = rgbs[this.random.nextInt(rgbs.length)];
            tx = (float)(this.guiLeft + 10) + (float)this.random.nextInt(64) / 2.0f;
            ty = (float)(this.guiTop + 6) + (float)this.random.nextInt(64) / 2.0f;
            OnTopEffects.effects.add(new OTESparkle(this.mouseX, this.mouseY, tx, ty, 30, col));
            Network.sendToServer((IPacket)new PacketLvlUpSkill(this.skill));
        }
        if (b.id == 1) {
            int[] rgbs = TexturePixelGetter.getAllColors((ResourceLocation)this.skill.tex.toUV((boolean)true).path);
            col = rgbs[this.random.nextInt(rgbs.length)];
            tx = (float)(this.guiLeft + 10) + (float)this.random.nextInt(64) / 2.0f;
            ty = (float)(this.guiTop + 6) + (float)this.random.nextInt(64) / 2.0f;
            OnTopEffects.effects.add(new OTESparkle(tx, ty, this.mouseX, this.mouseY, 30, col));
            Network.sendToServer((IPacket)new PacketLvlDownSkill(this.skill));
        }
    }

    @Override
    public void applySkillData(PlayerSkillData data) {
        this.data = data;
        this.parent.data = data;
    }
}

