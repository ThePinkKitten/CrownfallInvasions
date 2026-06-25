/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.blaze3d.platform.Window
 *  net.minecraft.client.Minecraft
 *  net.minecraft.world.InteractionHand
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.phys.Vec2
 */
package org.zeith.improvableskills.client.rendering;

import com.mojang.blaze3d.platform.Window;
import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.phys.Vec2;
import org.zeith.improvableskills.client.rendering.OnTopEffects;
import org.zeith.improvableskills.client.rendering.ote.OTEBook;
import org.zeith.improvableskills.client.rendering.ote.OTEItemStack;

public class ItemToBookHandler {
    public static void toBook(InteractionHand hand, int time) {
        Minecraft mc = Minecraft.m_91087_();
        Vec2 v = ItemToBookHandler.getPosOfHandSlot(hand);
        ItemToBookHandler.toBook(Minecraft.m_91087_().f_91074_.m_21120_(hand), v.f_82470_, v.f_82471_, time);
    }

    public static void toBook(InteractionHand hand, ItemStack stack, int time) {
        Minecraft mc = Minecraft.m_91087_();
        Vec2 v = ItemToBookHandler.getPosOfHandSlot(hand);
        ItemToBookHandler.toBook(stack, v.f_82470_, v.f_82471_, time);
    }

    public static void toBook(ItemStack stack, double x, double y, int time) {
        Minecraft mc = Minecraft.m_91087_();
        Window window = mc.m_91268_();
        OTEBook.show(time + 10);
        OnTopEffects.effects.add(new OTEItemStack(x, y, window.m_85445_() - 20, window.m_85446_() - 12, time, stack));
    }

    public static Vec2 getPosOfHandSlot(InteractionHand hand) {
        Minecraft mc = Minecraft.m_91087_();
        return ItemToBookHandler.getPosOfSlot(hand == InteractionHand.OFF_HAND ? -2 : mc.f_91074_.m_150109_().f_35977_);
    }

    public static Vec2 getPosOfSlot(int sl) {
        Minecraft mc = Minecraft.m_91087_();
        Window window = mc.m_91268_();
        int w = window.m_85445_();
        int h = window.m_85446_();
        float slots = 4.5f;
        float slot = 18.0f;
        return new Vec2((float)(w / 2) - slots * slot + (float)sl * slot + (float)(sl == -2 ? 4 : 0), (float)(h - 10));
    }
}

