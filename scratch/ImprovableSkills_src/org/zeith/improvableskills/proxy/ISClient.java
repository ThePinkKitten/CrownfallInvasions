/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.ChatFormatting
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.gui.GuiGraphics
 *  net.minecraft.client.gui.components.Button
 *  net.minecraft.client.gui.components.events.GuiEventListener
 *  net.minecraft.client.gui.screens.MenuScreens
 *  net.minecraft.client.gui.screens.Screen
 *  net.minecraft.client.gui.screens.inventory.AnvilScreen
 *  net.minecraft.client.gui.screens.inventory.CraftingScreen
 *  net.minecraft.client.gui.screens.inventory.InventoryScreen
 *  net.minecraft.core.particles.ParticleOptions
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.network.chat.Style
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.ItemLike
 *  net.minecraft.world.level.Level
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent
 *  net.minecraftforge.client.event.RegisterColorHandlersEvent$Item
 *  net.minecraftforge.client.event.RegisterGuiOverlaysEvent
 *  net.minecraftforge.client.event.RegisterParticleProvidersEvent
 *  net.minecraftforge.client.event.ScreenEvent$Init$Post
 *  net.minecraftforge.client.event.ScreenEvent$Render$Post
 *  net.minecraftforge.client.gui.overlay.IGuiOverlay
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.IEventBus
 *  net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent
 *  org.zeith.hammerlib.api.proxy.IClientProxy
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 */
package org.zeith.improvableskills.proxy;

import java.util.ArrayList;
import java.util.Optional;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.events.GuiEventListener;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.AnvilScreen;
import net.minecraft.client.gui.screens.inventory.CraftingScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Style;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RegisterClientTooltipComponentFactoriesEvent;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import org.zeith.hammerlib.api.proxy.IClientProxy;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.api.tooltip.AbilityTooltip;
import org.zeith.improvableskills.api.tooltip.SkillTooltip;
import org.zeith.improvableskills.cfg.ConfigsIS;
import org.zeith.improvableskills.client.gui.abil.ench.GuiEnchPowBook;
import org.zeith.improvableskills.client.gui.abil.ench.GuiPortableEnchantment;
import org.zeith.improvableskills.client.gui.base.GuiCustomButton;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.client.rendering.particle.SparkleParticle;
import org.zeith.improvableskills.client.rendering.tooltip.AbilityTooltipRenderer;
import org.zeith.improvableskills.client.rendering.tooltip.SkillTooltipRenderer;
import org.zeith.improvableskills.custom.items.ItemAbilityScroll;
import org.zeith.improvableskills.custom.items.ItemSkillScroll;
import org.zeith.improvableskills.custom.particles.ParticleDataSparkle;
import org.zeith.improvableskills.init.GuiHooksIS;
import org.zeith.improvableskills.init.ItemsIS;
import org.zeith.improvableskills.init.ParticleTypesIS;
import org.zeith.improvableskills.init.SoundsIS;
import org.zeith.improvableskills.net.PacketOpenSkillsBook;
import org.zeith.improvableskills.proxy.ISServer;

public class ISClient
extends ISServer
implements IClientProxy {
    private Button openSkills;
    private boolean hovered;

    @Override
    public void register(IEventBus modBus) {
        super.register(modBus);
        modBus.addListener(this::registerOverlays);
        modBus.addListener(this::clientSetup);
        modBus.addListener(this::registerItemColors);
        modBus.addListener(this::registerParticles);
        modBus.addListener(this::registerTooltipImages);
        IEventBus mcfBus = MinecraftForge.EVENT_BUS;
        mcfBus.addListener(this::addInvButtons);
        mcfBus.addListener(this::renderInventory);
    }

    @Override
    public boolean hasShiftDown() {
        return Screen.m_96638_();
    }

    @Override
    public Player getClientPlayer() {
        return Minecraft.m_91087_().f_91074_;
    }

    @Override
    public void sparkle(Level level, double x, double y, double z, double xMove, double yMove, double zMove, int color, int maxAge) {
        level.m_7106_((ParticleOptions)new ParticleDataSparkle(Vec3.m_82501_((int)color).m_252839_(), 1.0f, maxAge), x, y, z, xMove, yMove, zMove);
    }

    private void registerTooltipImages(RegisterClientTooltipComponentFactoriesEvent e) {
        e.register(SkillTooltip.class, SkillTooltipRenderer::new);
        e.register(AbilityTooltip.class, AbilityTooltipRenderer::new);
    }

    private void registerParticles(RegisterParticleProvidersEvent e) {
        e.registerSpriteSet(ParticleTypesIS.SPARKLE, SparkleParticle.Provider::new);
    }

    private void registerItemColors(RegisterColorHandlersEvent.Item e) {
        e.register((stack, layer) -> {
            PlayerSkillBase b = ItemSkillScroll.getSkillFromScroll(stack);
            if (layer == 1 && b != null) {
                return 0xFF000000 | b.getColor();
            }
            return -1;
        }, new ItemLike[]{ItemsIS.SKILL_SCROLL});
        e.register((stack, layer) -> {
            PlayerAbilityBase b = ItemAbilityScroll.getAbilityFromScroll(stack);
            if (layer == 1 && b != null) {
                return 0xFF000000 | b.getColor();
            }
            return -1;
        }, new ItemLike[]{ItemsIS.ABILITY_SCROLL});
    }

    private void clientSetup(FMLClientSetupEvent e) {
        MenuScreens.m_96206_(GuiHooksIS.ENCH_POWER_BOOK_IO, GuiEnchPowBook::new);
        MenuScreens.m_96206_(GuiHooksIS.REPAIR, AnvilScreen::new);
        MenuScreens.m_96206_(GuiHooksIS.ENCHANTMENT, GuiPortableEnchantment::new);
        MenuScreens.m_96206_(GuiHooksIS.CRAFTING, CraftingScreen::new);
    }

    private void registerOverlays(RegisterGuiOverlaysEvent e) {
        e.registerAboveAll("ontop_effects", (IGuiOverlay)new OnTopEffects());
    }

    private void addInvButtons(ScreenEvent.Init.Post e) {
        Minecraft mc = Minecraft.m_91087_();
        SyncSkills.doCheck((Player)mc.f_91074_);
        Screen screen = e.getScreen();
        if (screen instanceof InventoryScreen) {
            InventoryScreen inv = (InventoryScreen)screen;
            if (ConfigsIS.addBookToInv) {
                PlayerSkillData data = SyncSkills.getData();
                this.openSkills = new GuiCustomButton(0, inv.getGuiLeft() + (inv.getXSize() - 16) / 2 - 1, inv.getGuiTop() + 24, 16, 16, (Component)Component.m_237113_((String)""), this::openSkillBook).setCustomClickSound(SoundsIS.PAGE_TURNS);
                e.addListener((GuiEventListener)this.openSkills);
                this.openSkills.m_93650_(0.0f);
                this.openSkills.f_93623_ = data.hasCraftedSkillsBook();
            }
        }
    }

    private void renderInventory(ScreenEvent.Render.Post e) {
        Screen screen = e.getScreen();
        if (screen instanceof InventoryScreen) {
            InventoryScreen inv = (InventoryScreen)screen;
            if (this.openSkills != null && ConfigsIS.addBookToInv) {
                GuiGraphics gfx = e.getGuiGraphics();
                int mx = e.getMouseX();
                int my = e.getMouseY();
                this.openSkills.m_252865_(inv.getGuiLeft() + (inv.getXSize() - 16) / 2 - 1);
                this.openSkills.m_253211_(inv.getGuiTop() + 24);
                PlayerSkillData data = SyncSkills.getData();
                this.hovered = this.openSkills.m_5953_((double)mx, (double)my);
                this.openSkills.f_93623_ = data.hasCraftedSkillsBook();
                gfx.m_280480_(ItemsIS.SKILLS_BOOK.m_7968_(), this.openSkills.m_252754_(), this.openSkills.m_252907_());
                if (this.openSkills.f_93623_) {
                    gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
                } else {
                    gfx.m_280246_(0.75f, 0.75f, 0.75f, 1.0f);
                }
                gfx.m_280480_(ItemsIS.SKILLS_BOOK.m_7968_(), this.openSkills.m_252754_(), this.openSkills.m_252907_());
                gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
                if (this.hovered) {
                    ArrayList<MutableComponent> arr = new ArrayList<MutableComponent>();
                    arr.add(ItemsIS.SKILLS_BOOK.m_41466_().m_6881_().m_130948_(Style.f_131099_.m_178520_(0xDDDDDD)));
                    if (!this.openSkills.f_93623_) {
                        arr.add(Component.m_237115_((String)"gui.improvableskills.locked"));
                    }
                    arr.add(Component.m_237113_((String)"Improvable Skills").m_130944_(new ChatFormatting[]{ChatFormatting.BLUE, ChatFormatting.ITALIC}));
                    gfx.m_280677_(Minecraft.m_91087_().f_91062_, arr, Optional.empty(), this.openSkills.m_252754_() + 12, this.openSkills.m_252907_() + 4);
                } else {
                    gfx.m_280488_(Minecraft.m_91087_().f_91062_, "", 0, 0, 0xFFFFFF);
                    gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
                }
                gfx.m_280246_(1.0f, 1.0f, 1.0f, 1.0f);
            }
        }
    }

    private void openSkillBook(Button e) {
        Network.sendToServer((IPacket)new PacketOpenSkillsBook());
    }
}

