/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.network.chat.MutableComponent
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 */
package org.zeith.improvableskills.api.registry;

import java.util.List;
import java.util.function.Supplier;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.IHasRegistryName;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;

public abstract class PageletBase
implements IHasRegistryName {
    protected Object icon;
    public Component title;
    private ResourceLocation id;

    public boolean isRight() {
        return true;
    }

    @Override
    public ResourceLocation getRegistryName() {
        if (this.id == null) {
            this.id = ImprovableSkills.PAGELETS().getKey((Object)this);
        }
        return this.id;
    }

    @OnlyIn(value=Dist.CLIENT)
    public GuiTabbable<?> createTab(PlayerSkillData data) {
        return null;
    }

    @Override
    public String textureFolder() {
        return "pagelets";
    }

    public void reload() {
    }

    @OnlyIn(value=Dist.CLIENT)
    public boolean hasTab() {
        return true;
    }

    @OnlyIn(value=Dist.CLIENT)
    public void onClick() {
    }

    public PageletBase setIcon(Object icon) {
        this.icon = icon;
        return this;
    }

    public PageletBase setTitle(Component title) {
        this.title = title;
        return this;
    }

    @OnlyIn(value=Dist.CLIENT)
    public Object getIcon() {
        if (this.icon == null) {
            return ItemStack.f_41583_;
        }
        Object object = this.icon;
        if (object instanceof Supplier) {
            Supplier supp = (Supplier)object;
            this.icon = supp.get();
        }
        return this.icon;
    }

    public void addTitle(List<Component> text) {
        if (this.getTitle() != null) {
            text.add((Component)this.getTitle());
        } else {
            text.add((Component)Component.m_237113_((String)"Unnamed!"));
        }
    }

    public MutableComponent getTitle() {
        return this.title.m_6881_();
    }

    @OnlyIn(value=Dist.CLIENT)
    public boolean isVisible(PlayerSkillData data) {
        return true;
    }

    @OnlyIn(value=Dist.CLIENT)
    public boolean doesPop() {
        return false;
    }
}

