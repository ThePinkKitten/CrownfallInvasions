/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.renderer.texture.AbstractTexture
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  net.minecraftforge.fml.loading.FMLLoader
 *  org.zeith.hammerlib.util.ZeithLinkRepository
 *  org.zeith.hammerlib.util.java.Hashers
 *  org.zeith.hammerlib.util.java.net.HttpRequest
 *  org.zeith.hammerlib.util.mcf.ModHelper
 */
package org.zeith.improvableskills.custom.pagelets;

import java.nio.charset.StandardCharsets;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.loading.FMLLoader;
import org.zeith.hammerlib.util.ZeithLinkRepository;
import org.zeith.hammerlib.util.java.Hashers;
import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.mcf.ModHelper;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PageletBase;
import org.zeith.improvableskills.client.gui.GuiNewsBook;
import org.zeith.improvableskills.data.ClientData;

public class PageletNews
extends PageletBase {
    public final ResourceLocation texture = new ResourceLocation("improvableskills", "textures/gui/news.png");
    boolean popping;
    String changes;

    public PageletNews() {
        this.setTitle((Component)Component.m_237115_((String)"pagelet.improvableskills:news"));
        this.popping = true;
    }

    @Override
    public boolean isRight() {
        return false;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public Object getIcon() {
        Object o = super.getIcon();
        if (!(o instanceof AbstractTexture)) {
            o = Minecraft.m_91087_().m_91097_().m_118506_(this.texture);
            this.setIcon(o);
        }
        return o;
    }

    public String getChanges() {
        return this.changes;
    }

    @Override
    public void reload() {
        this.popping = false;
        try {
            String url = (String)ZeithLinkRepository.findLink((String)"mods/improvableskills/news").orElseThrow();
            this.changes = new String(HttpRequest.get((CharSequence)url).userAgent("ImprovableSkills v" + ModHelper.getModVersion((String)"improvableskills") + "; Minecraft v" + FMLLoader.versionInfo().mcVersion()).connectTimeout(30000).bytes(), StandardCharsets.UTF_8).replace("\r", "");
            String rem = Hashers.SHA256.hashify(this.changes);
            if (!ClientData.readData("news.sha").map(rem::equals).orElse(false).booleanValue()) {
                this.popping = true;
            }
        }
        catch (Exception exception) {
            // empty catch block
        }
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public boolean doesPop() {
        return this.popping;
    }

    @OnlyIn(value=Dist.CLIENT)
    public GuiNewsBook createTab(PlayerSkillData data) {
        return new GuiNewsBook(this);
    }
}

