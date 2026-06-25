/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  com.mojang.math.Axis
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.monster.Monster
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 *  net.minecraft.world.phys.AABB
 *  net.minecraft.world.phys.Vec3
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.client.utils.UV
 *  org.zeith.hammerlib.util.AABBUtils
 */
package org.zeith.improvableskills.custom.skills;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import java.util.function.Consumer;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.hammerlib.util.AABBUtils;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.client.IClientSkillExtensions;
import org.zeith.improvableskills.api.client.ISlotRenderer;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;

public class SkillMobRepellent
extends PlayerSkillBase {
    public SkillMobRepellent() {
        super(5);
        this.setupScroll();
        this.getLoot().chance.n = 1;
        this.getLoot().addLootTable(BuiltInLootTables.f_78745_);
        this.setColor(12297971);
        this.xpCalculator.xpValue = 4;
        this.xpCalculator.setBaseFormula("(%lvl%+1)^%xpv%");
    }

    @Override
    public void initializeClient(Consumer<IClientSkillExtensions> consumer) {
        consumer.accept(new IClientSkillExtensions(){
            final ResourceLocation hoveredHands = ImprovableSkills.id("textures/skills/mob_repellent_hovered_hands.png");
            final ResourceLocation hoveredNoHands = ImprovableSkills.id("textures/skills/mob_repellent_hovered_no_hands.png");
            final ResourceLocation normalHands = ImprovableSkills.id("textures/skills/mob_repellent_normal_hands.png");
            final ResourceLocation normalNoHands = ImprovableSkills.id("textures/skills/mob_repellent_normal_no_hands.png");
            final UV normalUv = new UV(this.normalNoHands, 0.0f, 0.0f, 256.0f, 256.0f);
            final UV hovUv = new UV(this.hoveredNoHands, 0.0f, 0.0f, 256.0f, 256.0f);
            final ISlotRenderer renderer = (gfx, x, y, width, height, hoverProgress, partialTicks) -> {
                PoseStack pose = gfx.m_280168_();
                this.normalUv.render(pose, (double)x, (double)y, width, height);
                if (hoverProgress > 0.0f) {
                    gfx.m_280246_(1.0f, 1.0f, 1.0f, hoverProgress);
                    this.hovUv.render(pose, (double)x, (double)y, width, height);
                    gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
                }
                float mulX = width / 24.0f;
                float mulY = height / 24.0f;
                pose.m_85836_();
                pose.m_252880_(x + 10.55f * mulX, y + 11.5f * mulY, 0.0f);
                pose.m_252781_(Axis.f_252403_.m_252977_((hoverProgress *= 0.95f) * 180.0f));
                pose.m_252880_(-0.5f * mulX, -1.5f * mulY, 0.0f);
                pose.m_85841_(width / 256.0f, height / 256.0f, width / 256.0f);
                FXUtils.bindTexture((ResourceLocation)this.normalHands);
                RenderUtils.drawTexturedModalRect((GuiGraphics)gfx, (float)0.0f, (float)11.0f, (float)107.0f, (float)117.0f, (float)10.0f, (float)22.0f);
                if (hoverProgress > 0.0f) {
                    gfx.m_280246_(1.0f, 1.0f, 1.0f, hoverProgress);
                    FXUtils.bindTexture((ResourceLocation)this.hoveredHands);
                    RenderUtils.drawTexturedModalRect((GuiGraphics)gfx, (float)0.0f, (float)11.0f, (float)107.0f, (float)117.0f, (float)10.0f, (float)22.0f);
                    gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
                }
                pose.m_85849_();
                pose.m_85836_();
                pose.m_252880_(x + 13.45f * mulX, y + 11.5f * mulY, 0.0f);
                pose.m_252781_(Axis.f_252403_.m_252977_(-hoverProgress * 180.0f));
                pose.m_252880_(-0.5f * mulX, -1.5f * mulY, 0.0f);
                pose.m_85841_(width / 256.0f, height / 256.0f, width / 256.0f);
                FXUtils.bindTexture((ResourceLocation)this.normalHands);
                RenderUtils.drawTexturedModalRect((GuiGraphics)gfx, (float)0.0f, (float)11.0f, (float)139.0f, (float)117.0f, (float)10.0f, (float)22.0f);
                if (hoverProgress > 0.0f) {
                    gfx.m_280246_(1.0f, 1.0f, 1.0f, hoverProgress);
                    FXUtils.bindTexture((ResourceLocation)this.hoveredHands);
                    RenderUtils.drawTexturedModalRect((GuiGraphics)gfx, (float)0.0f, (float)11.0f, (float)139.0f, (float)117.0f, (float)10.0f, (float)22.0f);
                    gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
                }
                pose.m_85849_();
                return true;
            };

            @Override
            public ISlotRenderer slotRenderer() {
                return this.renderer;
            }
        });
    }

    @Override
    public void tick(PlayerSkillData data, boolean isActive) {
        Player player;
        float sp = data.getSkillProgress(this) * 5.0f;
        if (isActive && (player = data.getPlayer()) instanceof ServerPlayer) {
            ServerPlayer mp = (ServerPlayer)player;
            if (sp > 0.0f) {
                Level lvl = mp.m_9236_();
                Vec3 c = AABBUtils.getCenter((AABB)mp.m_20191_());
                for (Monster m : lvl.m_45976_(Monster.class, new AABB(c, c).m_82400_((double)sp))) {
                    Vec3 mc = AABBUtils.getCenter((AABB)m.m_20191_());
                    Vec3 dir = mc.m_82546_(c).m_82541_().m_82490_((double)0.15f);
                    m.m_5997_(dir.f_82479_, dir.f_82480_, dir.f_82481_);
                }
            }
        }
    }
}

