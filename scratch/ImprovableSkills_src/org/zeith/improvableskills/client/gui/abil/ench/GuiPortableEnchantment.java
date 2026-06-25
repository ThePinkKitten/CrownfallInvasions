/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.google.common.collect.Lists
 *  com.mojang.blaze3d.platform.Lighting
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.blaze3d.vertex.VertexConsumer
 *  com.mojang.math.Axis
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.inventory.AbstractContainerScreen
 *  net.minecraft.client.gui.screens.inventory.EnchantmentNames
 *  net.minecraft.client.model.BookModel
 *  net.minecraft.client.model.geom.ModelLayers
 *  net.minecraft.client.renderer.GameRenderer
 *  net.minecraft.client.renderer.RenderType
 *  net.minecraft.client.renderer.texture.OverlayTexture
 *  net.minecraft.client.resources.language.I18n
 *  net.minecraft.client.resources.sounds.SimpleSoundInstance
 *  net.minecraft.client.resources.sounds.SoundInstance
 *  net.minecraft.core.Holder
 *  net.minecraft.network.chat.CommonComponents
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.FormattedText
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.sounds.SoundEvents
 *  net.minecraft.util.Mth
 *  net.minecraft.util.RandomSource
 *  net.minecraft.world.entity.player.Inventory
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.Slot
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.item.enchantment.Enchantment
 */
package org.zeith.improvableskills.client.gui.abil.ench;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.EnchantmentNames;
import net.minecraft.client.model.BookModel;
import net.minecraft.client.model.geom.ModelLayers;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.client.resources.sounds.SoundInstance;
import net.minecraft.core.Holder;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.client.gui.abil.ench.ContainerPortableEnchantment;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.client.rendering.ote.OTESparkle;

public class GuiPortableEnchantment
extends AbstractContainerScreen<ContainerPortableEnchantment> {
    private static final ResourceLocation ENCHANTING_TABLE_LOCATION = new ResourceLocation("textures/gui/container/enchanting_table.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE1 = new ResourceLocation("improvableskills", "textures/gui/enchanting_table_book_1.png");
    private static final ResourceLocation ENCHANTMENT_TABLE_BOOK_TEXTURE2 = new ResourceLocation("improvableskills", "textures/gui/enchanting_table_book_2.png");
    private final RandomSource random = RandomSource.m_216327_();
    private BookModel bookModel;
    public int time;
    public float flip;
    public float oFlip;
    public float flipT;
    public float flipA;
    public float open;
    public float oOpen;
    private ItemStack last = ItemStack.f_41583_;

    public GuiPortableEnchantment(ContainerPortableEnchantment container, Inventory playerInv, Component label) {
        super((AbstractContainerMenu)container, playerInv, label);
    }

    protected void m_7856_() {
        super.m_7856_();
        this.bookModel = new BookModel(this.f_96541_.m_167973_().m_171103_(ModelLayers.f_171271_));
    }

    public void m_181908_() {
        super.m_181908_();
        this.tickBook();
        for (int section = 0; section < 3; ++section) {
            if (((ContainerPortableEnchantment)this.f_97732_).f_39446_[section] <= 0 || this.random.m_188503_(6) != 0) continue;
            String ln = I18n.m_118938_((String)"text.improvableskills:enchpower", (Object[])new Object[]{(int)SyncSkills.getData().enchantPower});
            float w = this.random.m_188501_();
            float x1 = (float)(this.f_97735_ + 60 + (108 - this.f_96547_.m_92895_(ln)) / 2) + w * (float)this.f_96547_.m_92895_(ln);
            Objects.requireNonNull(this.f_96547_);
            float y1 = this.f_97736_ + 3 + 9;
            float x2 = (float)(this.f_97735_ + 60) + w * 108.0f;
            float y2 = (float)(this.f_97736_ + 14 + 19 * section) + this.random.m_188501_() * 19.0f;
            OnTopEffects.effects.add(new OTESparkle(x1, y1, x2, y2, 50 + this.random.m_188503_(30), -8372020));
        }
    }

    /*
     * Unable to fully structure code
     */
    public boolean m_6375_(double mouseX, double mouseY, int mouseButton) {
        block5: {
            i = (this.f_96543_ - this.f_97726_) / 2;
            j = (this.f_96544_ - this.f_97727_) / 2;
            if (mouseButton != 0) break block5;
            mouseX -= (double)this.f_97735_;
            mouseY -= (double)this.f_97736_;
            v0 = new Object[]{(int)SyncSkills.getData().enchantPower};
            ln = I18n.m_118938_((String)"text.improvableskills:enchpower", (Object[])v0);
            if (!(mouseX >= (double)(60 + (108 - this.f_96547_.m_92895_(ln)) / 2)) || !(mouseY > 3.0) || !(mouseX < (double)(60 + (108 - this.f_96547_.m_92895_(ln)) / 2 + this.f_96547_.m_92895_(ln)))) ** GOTO lbl-1000
            Objects.requireNonNull(this.f_96547_);
            if (mouseY < (double)(3 + 9)) {
                v1 = true;
            } else lbl-1000:
            // 2 sources

            {
                v1 = mouseOverChant = false;
            }
            if (mouseOverChant) {
                this.f_96541_.m_91106_().m_120367_((SoundInstance)SimpleSoundInstance.m_263171_((Holder)SoundEvents.f_12490_, (float)(0.7f + this.random.m_188501_() * 0.1f)));
                this.f_96541_.f_91072_.m_105208_(((ContainerPortableEnchantment)this.f_97732_).f_38840_, 122);
                return true;
            }
            mouseX += (double)this.f_97735_;
            mouseY += (double)this.f_97736_;
        }
        for (k = 0; k < 3; ++k) {
            d0 = mouseX - (double)(i + 60);
            d1 = mouseY - (double)(j + 14 + 19 * k);
            if (!(d0 >= 0.0) || !(d1 >= 0.0) || !(d0 < 108.0) || !(d1 < 19.0) || !((ContainerPortableEnchantment)this.f_97732_).m_6366_((Player)this.f_96541_.f_91074_, k)) continue;
            this.f_96541_.f_91072_.m_105208_(((ContainerPortableEnchantment)this.f_97732_).f_38840_, k);
            for (m = 0; m < 10; ++m) {
                x1 = (float)(i + 23) + this.random.m_188501_() * 22.0f;
                y1 = (float)(j + 23) + this.random.m_188501_() * 12.0f;
                x2 = (float)(i + ((Slot)((ContainerPortableEnchantment)this.f_97732_).f_38839_.get((int)0)).f_40220_) + this.random.m_188501_() * 16.0f;
                y2 = (float)(j + ((Slot)((ContainerPortableEnchantment)this.f_97732_).f_38839_.get((int)0)).f_40221_) + this.random.m_188501_() * 16.0f;
                OnTopEffects.effects.add(new OTESparkle(x1, y1, x2, y2, 40 - this.random.m_188503_(30), -16742401));
            }
            return true;
        }
        return super.m_6375_(mouseX, mouseY, mouseButton);
    }

    /*
     * Unable to fully structure code
     */
    protected void m_280003_(GuiGraphics gfx, int mouseX, int mouseY) {
        ln = Component.m_237113_((String)I18n.m_118938_((String)"text.improvableskills:enchpower", (Object[])new Object[]{(int)SyncSkills.getData().enchantPower})).m_130940_(ChatFormatting.UNDERLINE);
        if ((mouseX -= this.f_97735_) < 60 + (108 - this.f_96547_.m_92852_((FormattedText)ln)) / 2 || (mouseY -= this.f_97736_) <= 3 || mouseX >= 60 + (108 - this.f_96547_.m_92852_((FormattedText)ln)) / 2 + this.f_96547_.m_92852_((FormattedText)ln)) ** GOTO lbl-1000
        Objects.requireNonNull(this.f_96547_);
        if (mouseY < 3 + 9) {
            v0 = true;
        } else lbl-1000:
        // 2 sources

        {
            v0 = mouseOverChant = false;
        }
        if (mouseOverChant) {
            ln = ln.m_130940_(ChatFormatting.BLUE);
        }
        gfx.m_280614_(this.f_96547_, (Component)ln, 60 + (108 - this.f_96547_.m_92852_((FormattedText)ln)) / 2, 3, 0x404040, false);
        super.m_280003_(gfx, mouseX, mouseY);
    }

    protected void m_7286_(GuiGraphics gfx, float partial, int mouseX, int mouseY) {
        Lighting.m_84930_();
        RenderSystem.setShader(GameRenderer::m_172817_);
        RenderSystem.setShaderColor((float)1.0f, (float)1.0f, (float)1.0f, (float)1.0f);
        gfx.m_280218_(ENCHANTING_TABLE_LOCATION, this.f_97735_, this.f_97736_, 0, 0, this.f_97726_, this.f_97727_);
        this.renderBook(gfx, this.f_97735_, this.f_97736_, partial);
        EnchantmentNames.m_98734_().m_98735_((long)((ContainerPortableEnchantment)this.f_97732_).m_39493_());
        int k = ((ContainerPortableEnchantment)this.f_97732_).m_39492_();
        for (int l = 0; l < 3; ++l) {
            int i1 = this.f_97735_ + 60;
            int j1 = i1 + 20;
            int k1 = ((ContainerPortableEnchantment)this.f_97732_).f_39446_[l];
            if (k1 == 0) {
                gfx.m_280218_(ENCHANTING_TABLE_LOCATION, i1, this.f_97736_ + 14 + 19 * l, 0, 185, 108, 19);
                continue;
            }
            String s = "" + k1;
            int l1 = 86 - this.f_96547_.m_92895_(s);
            FormattedText formattedtext = EnchantmentNames.m_98734_().m_98737_(this.f_96547_, l1);
            int i2 = 6839882;
            if ((k < l + 1 || this.f_96541_.f_91074_.f_36078_ < k1) && !this.f_96541_.f_91074_.m_150110_().f_35937_ || ((ContainerPortableEnchantment)this.f_97732_).f_39447_[l] == -1) {
                gfx.m_280218_(ENCHANTING_TABLE_LOCATION, i1, this.f_97736_ + 14 + 19 * l, 0, 185, 108, 19);
                gfx.m_280218_(ENCHANTING_TABLE_LOCATION, i1 + 1, this.f_97736_ + 15 + 19 * l, 16 * l, 239, 16, 16);
                gfx.m_280554_(this.f_96547_, formattedtext, j1, this.f_97736_ + 16 + 19 * l, l1, (i2 & 0xFEFEFE) >> 1);
                i2 = 4226832;
            } else {
                int j2 = mouseX - (this.f_97735_ + 60);
                int k2 = mouseY - (this.f_97736_ + 14 + 19 * l);
                if (j2 >= 0 && k2 >= 0 && j2 < 108 && k2 < 19) {
                    gfx.m_280218_(ENCHANTING_TABLE_LOCATION, i1, this.f_97736_ + 14 + 19 * l, 0, 204, 108, 19);
                    i2 = 0xFFFF80;
                } else {
                    gfx.m_280218_(ENCHANTING_TABLE_LOCATION, i1, this.f_97736_ + 14 + 19 * l, 0, 166, 108, 19);
                }
                gfx.m_280218_(ENCHANTING_TABLE_LOCATION, i1 + 1, this.f_97736_ + 15 + 19 * l, 16 * l, 223, 16, 16);
                gfx.m_280554_(this.f_96547_, formattedtext, j1, this.f_97736_ + 16 + 19 * l, l1, i2);
                i2 = 8453920;
            }
            gfx.m_280488_(this.f_96547_, s, j1 + 86 - this.f_96547_.m_92895_(s), this.f_97736_ + 16 + 19 * l + 7, i2);
        }
    }

    private void renderBook(GuiGraphics gfx, int x, int y, float partial) {
        PoseStack pose = gfx.m_280168_();
        float f = Mth.m_14179_((float)partial, (float)this.oOpen, (float)this.open);
        float f1 = Mth.m_14179_((float)partial, (float)this.oFlip, (float)this.flip);
        Lighting.m_166384_();
        pose.m_85836_();
        pose.m_252880_((float)x + 33.0f, (float)y + 31.0f, 100.0f);
        pose.m_85841_(-40.0f, 40.0f, 40.0f);
        pose.m_252781_(Axis.f_252529_.m_252977_(25.0f));
        pose.m_252880_((1.0f - f) * 0.2f, (1.0f - f) * 0.1f, (1.0f - f) * 0.25f);
        pose.m_252781_(Axis.f_252436_.m_252977_(-(1.0f - f) * 90.0f - 90.0f));
        pose.m_252781_(Axis.f_252529_.m_252977_(180.0f));
        float f4 = Mth.m_14036_((float)(Mth.m_14187_((float)(f1 + 0.25f)) * 1.6f - 0.3f), (float)0.0f, (float)1.0f);
        float f5 = Mth.m_14036_((float)(Mth.m_14187_((float)(f1 + 0.75f)) * 1.6f - 0.3f), (float)0.0f, (float)1.0f);
        this.bookModel.m_102292_(0.0f, f4, f5, f);
        VertexConsumer vrtx = gfx.m_280091_().m_6299_(RenderType.m_110452_((ResourceLocation)ENCHANTMENT_TABLE_BOOK_TEXTURE1));
        this.bookModel.m_7695_(pose, vrtx, 0xF000F0, OverlayTexture.f_118083_, 0.0f, 0.53333336f, 1.0f, 1.0f);
        vrtx = gfx.m_280091_().m_6299_(RenderType.m_110452_((ResourceLocation)ENCHANTMENT_TABLE_BOOK_TEXTURE2));
        this.bookModel.m_7695_(pose, vrtx, 0xF000F0, OverlayTexture.f_118083_, 1.0f, 1.0f, 1.0f, 1.0f);
        gfx.m_280262_();
        pose.m_85849_();
        Lighting.m_84931_();
    }

    public void m_88315_(GuiGraphics gfx, int mouseX, int mouseY, float partial) {
        partial = this.f_96541_.m_91296_();
        this.m_280273_(gfx);
        super.m_88315_(gfx, mouseX, mouseY, partial);
        this.m_280072_(gfx, mouseX, mouseY);
        boolean flag = this.f_96541_.f_91074_.m_150110_().f_35937_;
        int i = ((ContainerPortableEnchantment)this.f_97732_).m_39492_();
        for (int j = 0; j < 3; ++j) {
            int k = ((ContainerPortableEnchantment)this.f_97732_).f_39446_[j];
            Enchantment enchantment = Enchantment.m_44697_((int)((ContainerPortableEnchantment)this.f_97732_).f_39447_[j]);
            int l = ((ContainerPortableEnchantment)this.f_97732_).f_39448_[j];
            int i1 = j + 1;
            if (!this.m_6774_(60, 14 + 19 * j, 108, 17, mouseX, mouseY) || k <= 0) continue;
            ArrayList list = Lists.newArrayList();
            list.add(Component.m_237110_((String)"container.enchant.clue", (Object[])new Object[]{enchantment == null ? "" : enchantment.m_44700_(l)}).m_130940_(ChatFormatting.WHITE));
            if (enchantment == null) {
                list.add(Component.m_237113_((String)""));
                list.add(Component.m_237115_((String)"forge.container.enchant.limitedEnchantability").m_130940_(ChatFormatting.RED));
            } else if (!flag) {
                list.add(CommonComponents.f_237098_);
                if (this.f_96541_.f_91074_.f_36078_ < k) {
                    list.add(Component.m_237110_((String)"container.enchant.level.requirement", (Object[])new Object[]{((ContainerPortableEnchantment)this.f_97732_).f_39446_[j]}).m_130940_(ChatFormatting.RED));
                } else {
                    MutableComponent mutablecomponent = i1 == 1 ? Component.m_237115_((String)"container.enchant.lapis.one") : Component.m_237110_((String)"container.enchant.lapis.many", (Object[])new Object[]{i1});
                    list.add(mutablecomponent.m_130940_(i >= i1 ? ChatFormatting.GRAY : ChatFormatting.RED));
                    MutableComponent mutablecomponent1 = i1 == 1 ? Component.m_237115_((String)"container.enchant.level.one") : Component.m_237110_((String)"container.enchant.level.many", (Object[])new Object[]{i1});
                    list.add(mutablecomponent1.m_130940_(ChatFormatting.GRAY));
                }
            }
            gfx.m_280666_(this.f_96547_, (List)list, mouseX, mouseY);
            break;
        }
    }

    public void tickBook() {
        ItemStack itemstack = ((ContainerPortableEnchantment)this.f_97732_).m_38853_(0).m_7993_();
        if (!ItemStack.m_41728_((ItemStack)itemstack, (ItemStack)this.last)) {
            this.last = itemstack;
            do {
                this.flipT += (float)(this.random.m_188503_(4) - this.random.m_188503_(4));
            } while (this.flip <= this.flipT + 1.0f && this.flip >= this.flipT - 1.0f);
        }
        ++this.time;
        this.oFlip = this.flip;
        this.oOpen = this.open;
        boolean flag = false;
        for (int i = 0; i < 3; ++i) {
            if (((ContainerPortableEnchantment)this.f_97732_).f_39446_[i] == 0) continue;
            flag = true;
            break;
        }
        this.open = flag ? (this.open += 0.2f) : (this.open -= 0.2f);
        this.open = Mth.m_14036_((float)this.open, (float)0.0f, (float)1.0f);
        float f1 = (this.flipT - this.flip) * 0.4f;
        float f = 0.2f;
        f1 = Mth.m_14036_((float)f1, (float)-0.2f, (float)0.2f);
        this.flipA += (f1 - this.flipA) * 0.9f;
        this.flip += this.flipA;
    }
}

