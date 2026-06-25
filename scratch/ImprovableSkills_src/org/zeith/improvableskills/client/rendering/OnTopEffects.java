/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.systems.RenderSystem
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.client.event.ScreenEvent$Render$Post
 *  net.minecraftforge.client.gui.overlay.ForgeGui
 *  net.minecraftforge.client.gui.overlay.IGuiOverlay
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.event.TickEvent$ClientTickEvent
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.registries.IForgeRegistry
 *  org.zeith.hammerlib.util.java.tuples.Tuple2$Mutable2
 */
package org.zeith.improvableskills.client.rendering;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import java.util.ArrayList;
import java.util.List;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.registries.IForgeRegistry;
import org.zeith.hammerlib.util.java.tuples.Tuple2;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.registry.PageletBase;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;
import org.zeith.improvableskills.client.rendering.OTEffect;
import org.zeith.improvableskills.utils.ScaledResolution;

public class OnTopEffects
implements IGuiOverlay {
    public static List<OTEffect> effects = new ArrayList<OTEffect>();
    private ScaledResolution resolution;

    public OnTopEffects() {
        MinecraftForge.EVENT_BUS.addListener(this::tick);
        MinecraftForge.EVENT_BUS.addListener(this::renderInGui);
    }

    public void tick(TickEvent.ClientTickEvent e) {
        Minecraft mc = Minecraft.m_91087_();
        SyncSkills.doCheck((Player)mc.f_91074_);
        if (e.phase == TickEvent.Phase.END) {
            for (int i = 0; i < effects.size(); ++i) {
                OTEffect eff = effects.get(i);
                if (eff.expired) {
                    effects.remove(i);
                    continue;
                }
                eff.update();
            }
            ScaledResolution sr = new ScaledResolution(Minecraft.m_91087_());
            if (this.resolution == null) {
                this.resolution = sr;
            }
            if (sr.getScaledHeight() != this.resolution.getScaledHeight() || sr.getScaledWidth() != this.resolution.getScaledWidth()) {
                for (int i = 0; i < effects.size(); ++i) {
                    OTEffect eff = effects.get(i);
                    eff.resize(this.resolution, sr);
                }
            }
            this.resolution = sr;
            IForgeRegistry<PageletBase> pagelets = ImprovableSkills.PAGELETS();
            for (ResourceLocation key : GuiTabbable.EXTENSIONS.keySet()) {
                Tuple2.Mutable2<Float, Float> val = GuiTabbable.EXTENSIONS.get(key);
                Float target = (Float)val.a();
                Float current = (Float)val.b();
                float dif = Math.max(-0.125f, Math.min(0.125f, target.floatValue() - current.floatValue()));
                val.setB((Object)Float.valueOf(current.floatValue() + dif));
                PageletBase base = (PageletBase)pagelets.getValue(key);
                if (!((double)target.floatValue() < 0.5) || base == null || !base.doesPop()) continue;
                float v = (float)((System.currentTimeMillis() + (long)Math.abs(key.hashCode())) % 5000L) / 5000.0f;
                if (!(current.floatValue() < v)) continue;
                val.setB((Object)Float.valueOf(v));
            }
        }
    }

    public void renderInGui(ScreenEvent.Render.Post e) {
        Screen gs = e.getScreen();
        int mx = e.getMouseX();
        int my = e.getMouseY();
        float pt = Minecraft.m_91087_().getPartialTick();
        GuiGraphics gfx = e.getGuiGraphics();
        PoseStack pose = gfx.m_280168_();
        pose.m_85836_();
        pose.m_252880_(0.0f, 0.0f, 300.0f);
        for (int i = 0; i < effects.size(); ++i) {
            OTEffect eff = effects.get(i);
            if (eff.expired || !eff.renderGui) continue;
            eff.currentGui = gs;
            eff.mouseX = mx;
            eff.mouseY = my;
            RenderSystem.enableBlend();
            pose.m_85836_();
            eff.render(gfx, pt);
            pose.m_85849_();
        }
        pose.m_85849_();
    }

    public void render(ForgeGui gui, GuiGraphics gfx, float partialTick, int screenWidth, int screenHeight) {
        float pt = partialTick;
        PoseStack pose = gfx.m_280168_();
        for (int i = 0; i < effects.size(); ++i) {
            OTEffect eff = effects.get(i);
            if (eff.expired || !eff.renderHud) continue;
            pose.m_85836_();
            RenderSystem.enableBlend();
            eff.render(gfx, pt);
            pose.m_85849_();
        }
    }
}

