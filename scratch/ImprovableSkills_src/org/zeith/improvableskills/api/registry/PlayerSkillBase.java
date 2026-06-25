/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.common.util.LazyOptional
 *  net.minecraftforge.eventbus.api.Event
 *  net.minecraftforge.fml.loading.FMLEnvironment
 *  net.minecraftforge.fml.loading.FMLLoader
 *  org.zeith.hammerlib.api.fml.IRegisterListener
 *  org.zeith.hammerlib.util.XPUtil
 */
package org.zeith.improvableskills.api.registry;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.hammerlib.util.XPUtil;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.OwnedTexture;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.SkillCostConfig;
import org.zeith.improvableskills.api.client.IClientSkillExtensions;
import org.zeith.improvableskills.api.loot.SkillLoot;
import org.zeith.improvableskills.api.registry.IHasRegistryName;

public class PlayerSkillBase
implements IHasRegistryName,
IRegisterListener {
    private SkillLoot loot;
    private List<Consumer<? extends Event>> forgeEvents = new ArrayList<Consumer<? extends Event>>();
    public SkillCostConfig xpCalculator = new SkillCostConfig(1);
    public OwnedTexture<PlayerSkillBase> tex = new OwnedTexture<PlayerSkillBase>(this);
    protected final int maxLvl;
    protected boolean lockedWithScroll;
    protected boolean generateScroll;
    protected LazyOptional<Integer> color = LazyOptional.of(() -> this.getRegistryName().toString().hashCode());
    private ResourceLocation id;
    private Object renderProperties;

    public PlayerSkillBase(int maxLvl) {
        this.maxLvl = maxLvl;
        this.initClient();
    }

    public float getLevelProgress(int level) {
        return (float)level / (float)this.getMaxLevel();
    }

    public int getMaxLevel() {
        return this.maxLvl;
    }

    public void setupScroll() {
        this.generateScroll = true;
        this.lockedWithScroll = true;
    }

    public void setColor(int color) {
        this.color = LazyOptional.of(() -> color);
    }

    public void tick(PlayerSkillData data, boolean isActive) {
    }

    @Override
    public ResourceLocation getRegistryName() {
        if (this.id == null) {
            this.id = ImprovableSkills.SKILLS().getKey((Object)this);
        }
        return this.id;
    }

    @Override
    public String textureFolder() {
        return "skills";
    }

    public String getUnlocalizedName(ResourceLocation id) {
        return "skill." + id.toString();
    }

    public String getUnlocalizedName() {
        return this.getUnlocalizedName(this.getRegistryName());
    }

    public String getUnlocalizedName(PlayerSkillData data) {
        return this.getUnlocalizedName();
    }

    public MutableComponent getLocalizedName(PlayerSkillData data) {
        return Component.m_237115_((String)(this.getUnlocalizedName(data) + ".name"));
    }

    public MutableComponent getLocalizedName() {
        return Component.m_237115_((String)(this.getUnlocalizedName() + ".name"));
    }

    public String getUnlocalizedDesc(PlayerSkillData data) {
        return "skill." + this.getRegistryName().toString();
    }

    public MutableComponent getLocalizedDesc(PlayerSkillData data) {
        return Component.m_237115_((String)(this.getUnlocalizedDesc(data) + ".desc"));
    }

    public int getXPToUpgrade(PlayerSkillData data, short targetLvl) {
        return this.xpCalculator.getXPToUpgrade(data, targetLvl);
    }

    public boolean canUpgrade(PlayerSkillData data) {
        short clvl = data.getSkillLevel(this);
        return clvl < this.maxLvl && (XPUtil.getXPTotal((Player)data.player) >= this.getXPToUpgrade(data, (short)(clvl + 1)) || data.player.m_7500_());
    }

    public void onUpgrade(short oldLvl, short newLvl, PlayerSkillData data) {
        if (oldLvl > newLvl) {
            XPUtil.setPlayersExpTo((Player)data.player, (int)(XPUtil.getXPTotal((Player)data.player) + this.getXPToDowngrade(data, newLvl)));
        } else {
            XPUtil.setPlayersExpTo((Player)data.player, (int)(XPUtil.getXPTotal((Player)data.player) - this.getXPToUpgrade(data, newLvl)));
        }
    }

    public boolean isDowngradable(PlayerSkillData data) {
        return true;
    }

    public int getXPToDowngrade(PlayerSkillData data, short to) {
        return this.getXPToUpgrade(data, to);
    }

    public void onDowngrade(PlayerSkillData data, short from) {
    }

    public void onPostRegistered() {
        for (Consumer<? extends Event> listener : this.forgeEvents) {
            MinecraftForge.EVENT_BUS.addListener(listener);
        }
    }

    public <T extends Event> void addListener(Consumer<T> consumer) {
        this.forgeEvents.add(consumer);
    }

    public EnumScrollState getScrollState() {
        return this.lockedWithScroll ? EnumScrollState.NORMAL : EnumScrollState.NONE;
    }

    public SkillLoot getLoot() {
        SkillLoot skillLoot = this.lockedWithScroll && this.generateScroll ? (this.loot == null ? (this.loot = this.createLoot()) : this.loot) : null;
        return skillLoot;
    }

    protected SkillLoot createLoot() {
        return new SkillLoot(this);
    }

    public boolean isVisible(PlayerSkillData data) {
        return !this.lockedWithScroll || data.hasSkillScroll(this);
    }

    public int getColor() {
        return (Integer)this.color.orElseThrow(NoSuchElementException::new);
    }

    public void onUnlocked(PlayerSkillData data) {
    }

    public boolean is(PlayerSkillBase skill) {
        return skill == this;
    }

    public Object getRenderPropertiesInternal() {
        return this.renderProperties;
    }

    private void initClient() {
        if (FMLEnvironment.dist == Dist.CLIENT && !FMLLoader.getLaunchHandler().isData()) {
            this.initializeClient(properties -> {
                if (properties == this) {
                    throw new IllegalStateException("Don't extend IItemRenderProperties in your item, use an anonymous class instead.");
                }
                this.renderProperties = properties;
            });
        }
    }

    public void initializeClient(Consumer<IClientSkillExtensions> consumer) {
    }

    public static enum EnumScrollState {
        NONE,
        NORMAL,
        SPECIAL;


        public boolean hasScroll() {
            return this.ordinal() > 0;
        }
    }
}

