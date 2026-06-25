/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.resources.ResourceLocation
 *  org.zeith.hammerlib.client.utils.UV
 */
package org.zeith.improvableskills.api;

import net.minecraft.resources.ResourceLocation;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.improvableskills.api.registry.IHasRegistryName;

public class OwnedTexture<V extends IHasRegistryName> {
    public final V owner;
    public ResourceLocation texNorm;
    public ResourceLocation texHov;

    public OwnedTexture(V owner) {
        this.owner = owner;
    }

    public V owner() {
        return this.owner;
    }

    public UV toUV(boolean hovered) {
        if (this.texHov == null || this.texNorm == null) {
            ResourceLocation res = this.owner.getRegistryName();
            String sub = this.owner.textureFolder();
            this.texNorm = new ResourceLocation(res.m_135827_(), "textures/" + sub + "/" + res.m_135815_() + "_normal.png");
            this.texHov = new ResourceLocation(res.m_135827_(), "textures/" + sub + "/" + res.m_135815_() + "_hovered.png");
        }
        return new UV(hovered ? this.texHov : this.texNorm, 0.0f, 0.0f, 256.0f, 256.0f);
    }
}

