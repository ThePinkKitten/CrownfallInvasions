/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.Items
 *  org.zeith.hammerlib.api.inv.SimpleInventory
 *  org.zeith.hammerlib.client.screen.ScreenWTFMojang
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.client.utils.TexturePixelGetter
 *  org.zeith.hammerlib.util.java.Cast
 */
package org.zeith.improvableskills.client.gui.abil.ench;

import java.util.Objects;
import java.util.Random;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.zeith.hammerlib.api.inv.SimpleInventory;
import org.zeith.hammerlib.client.screen.ScreenWTFMojang;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.client.utils.TexturePixelGetter;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.client.gui.abil.ench.ContainerEnchPowBook;
import org.zeith.improvableskills.client.gui.base.GuiCustomButton;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.client.rendering.ote.OTEFadeOutButton;
import org.zeith.improvableskills.client.rendering.ote.OTESparkle;

public class GuiEnchPowBook
extends ScreenWTFMojang<ContainerEnchPowBook> {
    public static final int DEFAULT_GLINT_COLOR = -8372020;
    protected static final ResourceLocation OVERLAY = new ResourceLocation("improvableskills", "textures/gui/book_slot_overlay.png");
    protected static final ResourceLocation MAIN_GUI = new ResourceLocation("improvableskills", "textures/gui/enchanter_lvl.png");

    public GuiEnchPowBook(ContainerEnchPowBook ctr, Inventory inv, Component label) {
        super((AbstractContainerMenu)ctr, inv, label);
    }

    public void m_7856_() {
        super.m_7856_();
        this.m_142416_((GuiEventListener)new GuiCustomButton(0, this.f_97735_ + this.f_97726_ / 2 - 16, this.f_97736_ + this.f_97727_ / 2 - 52 - 12, 60, 20, "--> +", this::actionPerformed));
        this.m_142416_((GuiEventListener)new GuiCustomButton(1, this.f_97735_ + this.f_97726_ / 2 - 16, this.f_97736_ + this.f_97727_ / 2 - 52 + 12, 60, 20, (Component)Component.m_237115_((String)"gui.back"), this::actionPerformed));
    }

    public void m_88315_(GuiGraphics gfx, int mouseX, int mouseY, float partialTicks) {
        Cast.optionally(this.m_6702_().get(0), GuiCustomButton.class).ifPresent(b -> b.m_93666_((Component)Component.m_237113_((String)(GuiEnchPowBook.m_96638_() ? "<-- *" : "--> *"))));
        this.m_280273_(gfx);
        super.m_88315_(gfx, mouseX, mouseY, partialTicks);
        this.m_280072_(gfx, mouseX, mouseY);
        Slot slot = this.getSlotUnderMouse();
        if (slot != null && slot.f_40218_ instanceof SimpleInventory && !slot.m_6657_()) {
            gfx.m_280557_(this.f_96547_, (Component)Component.m_237113_((String)"Slot for ").m_130940_(ChatFormatting.GRAY).m_7220_((Component)Items.f_42517_.m_41466_().m_6881_().m_130940_(ChatFormatting.BOLD)), mouseX, mouseY);
        }
        String ln = I18n.m_118938_((String)"text.improvableskills:enchpower", (Object[])new Object[]{(int)SyncSkills.getData().enchantPower});
        gfx.m_280056_(this.f_96547_, ln, this.f_97735_ + (this.f_97726_ - this.f_96547_.m_92895_(ln)) / 2, this.f_97736_ + 3, 0x404040, false);
    }

    protected void renderBackground(GuiGraphics gfx, float partialTime, int mouseX, int mouseY) {
        gfx.m_280218_(MAIN_GUI, this.f_97735_, this.f_97736_, 0, 0, this.f_97726_, this.f_97727_);
        FXUtils.bindTexture((ResourceLocation)OVERLAY);
        RenderUtils.drawFullTexturedModalRect((GuiGraphics)gfx, (float)(this.f_97735_ + this.f_97726_ / 2 - 36), (float)(this.f_97736_ + 32), (float)16.0f, (float)16.0f);
    }

    private void actionPerformed(Button buttonIn) {
        if (!(buttonIn instanceof GuiCustomButton)) {
            return;
        }
        GuiCustomButton button = (GuiCustomButton)buttonIn;
        new OTEFadeOutButton(button, button.id == 1 ? 2 : 20);
        int id = button.id;
        if (id == 0 && GuiEnchPowBook.m_96638_()) {
            id = 11;
        }
        this.clickMenuButton(id);
        ContainerEnchPowBook thus = (ContainerEnchPowBook)this.f_97732_;
        if (thus != null) {
            Random r;
            PlayerSkillData data;
            ItemStack item;
            Slot sl = (Slot)thus.f_38839_.get(thus.f_38839_.size() - 1);
            if (id == 11) {
                item = thus.inventory.getStackInSlot(0);
                data = SyncSkills.getData();
                if ((item.m_41619_() || item.m_41720_() == Items.f_42517_ && item.m_41613_() < 64) && data != null && data.enchantPower > 0.0f) {
                    r = new Random();
                    int[] rgbs = TexturePixelGetter.getAllColors((ItemStack)Items.f_42517_.m_7968_());
                    int col = rgbs.length == 0 ? -8372020 : rgbs[r.nextInt(rgbs.length)];
                    String ln = I18n.m_118938_((String)"text.improvableskills:enchpower", (Object[])new Object[]{(int)SyncSkills.getData().enchantPower});
                    double tx = this.f_97735_ + (this.f_97726_ - this.f_96547_.m_92895_(ln)) / 2;
                    double ty = this.f_97736_ + 3;
                    for (int i = 0; i < 2; ++i) {
                        double d = tx + (double)(r.nextFloat() * (float)this.f_96547_.m_92895_(ln));
                        float f = r.nextFloat();
                        Objects.requireNonNull(this.f_96547_);
                        OnTopEffects.effects.add(new OTESparkle(d, ty + (double)(f * 9.0f), this.f_97735_ + sl.f_40220_ + r.nextInt(16), this.f_97736_ + sl.f_40221_ + r.nextInt(16), 30, col));
                    }
                }
            }
            if (id == 0) {
                item = thus.inventory.getStackInSlot(0);
                data = SyncSkills.getData();
                if (!item.m_41619_() && item.m_41720_() == Items.f_42517_ && data != null && data.enchantPower < 15.0f) {
                    r = new Random();
                    int col = -8372020;
                    String ln = I18n.m_118938_((String)"text.improvableskills:enchpower", (Object[])new Object[]{(int)SyncSkills.getData().enchantPower});
                    double tx = this.f_97735_ + (this.f_97726_ - this.f_96547_.m_92895_(ln)) / 2;
                    double ty = this.f_97736_ + 3;
                    for (int i = 0; i < 2; ++i) {
                        double d = this.f_97735_ + sl.f_40220_ + r.nextInt(16);
                        double d2 = this.f_97736_ + sl.f_40221_ + r.nextInt(16);
                        double d3 = tx + (double)(r.nextFloat() * (float)this.f_96547_.m_92895_(ln));
                        float f = r.nextFloat();
                        Objects.requireNonNull(this.f_96547_);
                        OnTopEffects.effects.add(new OTESparkle(d, d2, d3, ty + (double)(f * 9.0f), 30, col));
                    }
                }
            }
        }
    }
}

