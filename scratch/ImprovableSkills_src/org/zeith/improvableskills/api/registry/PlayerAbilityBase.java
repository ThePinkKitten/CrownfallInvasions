/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.common.util.LazyOptional
 *  net.minecraftforge.fml.loading.FMLEnvironment
 *  net.minecraftforge.fml.loading.FMLLoader
 *  org.zeith.hammerlib.api.fml.IRegisterListener
 */
package org.zeith.improvableskills.api.registry;

import java.util.NoSuchElementException;
import java.util.function.Consumer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLLoader;
import org.zeith.hammerlib.api.fml.IRegisterListener;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.OwnedTexture;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.client.IClientAbilityExtensions;
import org.zeith.improvableskills.api.registry.IHasRegistryName;

public class PlayerAbilityBase
implements IHasRegistryName,
IRegisterListener {
    public OwnedTexture<PlayerAbilityBase> tex = new OwnedTexture<PlayerAbilityBase>(this);
    private ResourceLocation id;
    protected LazyOptional<Integer> color = LazyOptional.of(() -> this.getRegistryName().toString().hashCode());
    private boolean registered;
    private Object renderProperties;

    public PlayerAbilityBase() {
        this.initClient();
    }

    public void onPostRegistered() {
        this.registered = true;
    }

    public boolean registered() {
        return this.registered;
    }

    @Override
    public ResourceLocation getRegistryName() {
        if (this.id == null) {
            this.id = ImprovableSkills.ABILITIES().getKey((Object)this);
        }
        return this.id;
    }

    @Override
    public String textureFolder() {
        return "abilities";
    }

    @OnlyIn(value=Dist.CLIENT)
    public void onClickClient(Player player, int mouseButton) {
    }

    public String getUnlocalizedName() {
        return this.getUnlocalizedName(this.getRegistryName());
    }

    public String getUnlocalizedName(ResourceLocation id) {
        return "ability." + id.toString();
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
        return "ability." + this.getRegistryName().toString();
    }

    public MutableComponent getLocalizedDesc(PlayerSkillData data) {
        return Component.m_237115_((String)(this.getUnlocalizedDesc(data) + ".desc"));
    }

    public void setColor(int color) {
        this.color = LazyOptional.of(() -> color);
    }

    public int getColor() {
        return (Integer)this.color.orElseThrow(NoSuchElementException::new);
    }

    public void onUnlocked(PlayerSkillData data) {
    }

    public void tick(PlayerSkillData data) {
    }

    public boolean showDisabledIcon(PlayerSkillData data) {
        return false;
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

    public void initializeClient(Consumer<IClientAbilityExtensions> consumer) {
    }
}

