/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.vertex.PoseStack
 *  net.minecraft.client.renderer.texture.TextureAtlasSprite
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.InventoryMenu
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  org.zeith.hammerlib.client.utils.FXUtils
 *  org.zeith.hammerlib.client.utils.RenderUtils
 *  org.zeith.hammerlib.client.utils.UV
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 */
package org.zeith.improvableskills.custom.abilities;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.client.utils.FXUtils;
import org.zeith.hammerlib.client.utils.RenderUtils;
import org.zeith.hammerlib.client.utils.UV;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.improvableskills.api.OwnedTexture;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.net.PacketOpenPortableAnvil;

public class AbilityAnvil
extends PlayerAbilityBase {
    public AbilityAnvil() {
        this.setColor(16475136);
        this.tex = new OwnedTexture<PlayerAbilityBase>((PlayerAbilityBase)this){

            @Override
            @OnlyIn(value=Dist.CLIENT)
            public UV toUV(boolean hovered) {
                if (this.texHov == null || this.texNorm == null) {
                    ResourceLocation res = ((PlayerAbilityBase)this.owner).getRegistryName();
                    this.texNorm = new ResourceLocation(res.m_135827_(), "textures/abilities/" + res.m_135815_() + "_normal.png");
                    this.texHov = new ResourceLocation(res.m_135827_(), "textures/abilities/" + res.m_135815_() + "_hovered.png");
                }
                return hovered ? new UVMagma(this.texHov) : new UV(this.texNorm, 0.0f, 0.0f, 256.0f, 256.0f);
            }
        };
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public void onClickClient(Player player, int mouseButton) {
        Network.sendToServer((IPacket)new PacketOpenPortableAnvil());
    }

    @OnlyIn(value=Dist.CLIENT)
    static class UVMagma
    extends UV {
        final ResourceLocation tex = new ResourceLocation("minecraft:block/lava_flow");

        public UVMagma(ResourceLocation path) {
            super(path, 0.0f, 0.0f, 256.0f, 256.0f);
        }

        @OnlyIn(value=Dist.CLIENT)
        public void render(PoseStack pose, float x, float y) {
            FXUtils.bindTexture((ResourceLocation)InventoryMenu.f_39692_);
            RenderUtils.drawTexturedModalRect((PoseStack)pose, (float)(x + 20.0f), (float)(y + 20.0f), (TextureAtlasSprite)RenderUtils.getMainSprite((ResourceLocation)this.tex), (float)(this.width - 40.0f), (float)(this.height - 40.0f));
            super.render(pose, x, y);
        }
    }
}

