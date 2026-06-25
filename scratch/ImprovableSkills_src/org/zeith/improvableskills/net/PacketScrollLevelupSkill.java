/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Window
 *  net.minecraft.client.Minecraft
 *  net.minecraft.client.player.LocalPlayer
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec2
 *  net.minecraftforge.api.distmarker.Dist
 *  net.minecraftforge.api.distmarker.OnlyIn
 *  org.zeith.hammerlib.net.INBTPacket
 *  org.zeith.hammerlib.net.PacketContext
 */
package org.zeith.improvableskills.net;

import com.mojang.blaze3d.platform.Window;
import java.util.ArrayList;
import java.util.Random;
import net.minecraft.client.Minecraft;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.zeith.hammerlib.net.INBTPacket;
import org.zeith.hammerlib.net.PacketContext;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.client.rendering.ItemToBookHandler;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.client.rendering.ote.OTEBook;
import org.zeith.improvableskills.client.rendering.ote.OTEItemSkillScroll;

public class PacketScrollLevelupSkill
implements INBTPacket {
    private ResourceLocation skill;
    private ItemStack used;
    private int slot;

    public PacketScrollLevelupSkill(int slot, ItemStack used, ResourceLocation skill) {
        this.skill = skill;
        this.used = used;
        this.slot = slot;
    }

    public PacketScrollLevelupSkill() {
    }

    public void write(CompoundTag nbt) {
        nbt.m_128359_("s", this.skill.toString());
        nbt.m_128405_("i", this.slot);
        nbt.m_128365_("u", (Tag)this.used.serializeNBT());
    }

    public void read(CompoundTag nbt) {
        this.skill = ResourceLocation.m_135820_((String)nbt.m_128461_("s"));
        this.slot = nbt.m_128451_("i");
        this.used = ItemStack.m_41712_((CompoundTag)nbt.m_128469_("u"));
    }

    @OnlyIn(value=Dist.CLIENT)
    public void clientExecute(PacketContext net) {
        LocalPlayer sp = Minecraft.m_91087_().f_91074_;
        if (sp == null) {
            return;
        }
        PlayerSkillBase sk = (PlayerSkillBase)ImprovableSkills.SKILLS().getValue(this.skill);
        if (sk == null) {
            return;
        }
        ArrayList<PlayerSkillBase> base = new ArrayList<PlayerSkillBase>();
        base.add(sk);
        sp.m_213846_((Component)Component.m_237110_((String)"chat.improvableskills.page_upgraded", (Object[])new Object[]{sk.getLocalizedName(SyncSkills.getData())}));
        if (this.slot >= 9) {
            return;
        }
        Random rand = new Random();
        Minecraft mc = Minecraft.m_91087_();
        Window sr = mc.m_91268_();
        Vec2 v = ItemToBookHandler.getPosOfSlot(this.slot);
        OTEBook.show(150 + 10 * base.size());
        OnTopEffects.effects.add(new OTEItemSkillScroll(v.f_82470_, v.f_82471_, (float)(sr.m_85445_() - 20 - 48) + rand.nextFloat() * 32.0f, (float)(sr.m_85446_() - 12 - 24) - rand.nextFloat() * 32.0f, 100, this.used, base.toArray(new PlayerSkillBase[0])));
    }
}

