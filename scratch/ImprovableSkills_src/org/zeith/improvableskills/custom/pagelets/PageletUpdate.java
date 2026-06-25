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
 *  net.minecraftforge.fml.ModList
 *  net.minecraftforge.fml.VersionChecker$Status
 *  net.minecraftforge.fml.loading.FMLLoader
 *  net.minecraftforge.forgespi.language.IModInfo
 *  org.apache.maven.artifact.versioning.ComparableVersion
 *  org.zeith.hammerlib.util.java.Threading
 *  org.zeith.hammerlib.util.java.net.HttpRequest
 *  org.zeith.hammerlib.util.shaded.json.JSONObject
 *  org.zeith.hammerlib.util.shaded.json.JSONTokener
 */
package org.zeith.improvableskills.custom.pagelets;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.VersionChecker;
import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.ComparableVersion;
import org.zeith.hammerlib.util.java.Threading;
import org.zeith.hammerlib.util.java.net.HttpRequest;
import org.zeith.hammerlib.util.shaded.json.JSONObject;
import org.zeith.hammerlib.util.shaded.json.JSONTokener;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PageletBase;
import org.zeith.improvableskills.client.gui.GuiUpdateBook;
import org.zeith.improvableskills.client.gui.base.GuiTabbable;

public class PageletUpdate
extends PageletBase {
    public final ResourceLocation texture = new ResourceLocation("improvableskills", "textures/gui/update.png");
    public static VersionChecker.Status level;
    public static String changes;
    public static String latest;
    public static String homepage;
    public static String liveURL;
    public static String liveTitle;
    Thread reloadThread;

    public PageletUpdate() {
        this.setTitle((Component)Component.m_237115_((String)"pagelet.improvableskills:update"));
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public boolean isRight() {
        return false;
    }

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public boolean doesPop() {
        return true;
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

    @Override
    @OnlyIn(value=Dist.CLIENT)
    public GuiTabbable<?> createTab(PlayerSkillData data) {
        return new GuiUpdateBook(this);
    }

    @Override
    public void reload() {
        if (this.reloadThread != null) {
            return;
        }
        this.reloadThread = Threading.createAndStart(() -> {
            try {
                String lat;
                JSONObject o = (JSONObject)new JSONTokener(HttpRequest.get((CharSequence)"https://api.modrinth.com/updates/9fT7HUaI/forge_updates.json").body()).nextValue();
                String mcVersion = FMLLoader.versionInfo().mcVersion();
                changes = "";
                homepage = o.getString("homepage");
                JSONObject promos = o.getJSONObject("promos");
                String rec = promos.optString(mcVersion + "-recommended");
                latest = lat = promos.optString(mcVersion + "-latest");
                IModInfo mod = (IModInfo)ModList.get().getModFileById("improvableskills").getMods().get(0);
                ComparableVersion current = new ComparableVersion(mod.getVersion().toString());
                ComparableVersion recommended = new ComparableVersion(rec);
                int diff = recommended.compareTo(current);
                if (diff == 0) {
                    level = VersionChecker.Status.UP_TO_DATE;
                } else if (diff < 0) {
                    ComparableVersion latest;
                    level = VersionChecker.Status.AHEAD;
                    if (lat != null && current.compareTo(latest = new ComparableVersion(lat)) < 0) {
                        level = VersionChecker.Status.OUTDATED;
                    }
                } else {
                    level = VersionChecker.Status.OUTDATED;
                }
                liveURL = null;
                liveTitle = null;
                JSONObject dev = o.optJSONObject("dev");
                if (dev != null && dev.getBoolean("live")) {
                    liveURL = dev.getString("url");
                    String txt = HttpRequest.get((CharSequence)liveURL).body();
                    txt = txt.substring(txt.indexOf("<title>") + 7);
                    if ((txt = txt.substring(0, txt.indexOf("</title>"))).toLowerCase().endsWith(" - youtube")) {
                        txt = txt.substring(0, txt.length() - 10);
                    }
                    if (txt.toLowerCase().endsWith(" - twitch")) {
                        txt = txt.substring(0, txt.length() - 9);
                    }
                    liveTitle = txt;
                }
            }
            catch (Throwable err) {
                err.printStackTrace();
            }
            this.reloadThread = null;
        });
    }

    public void joinReload() {
        if (this.reloadThread != null) {
            try {
                this.reloadThread.join();
            }
            catch (Exception exception) {
                // empty catch block
            }
        }
    }

    @Override
    public boolean isVisible(PlayerSkillData data) {
        return level == VersionChecker.Status.OUTDATED;
    }
}

