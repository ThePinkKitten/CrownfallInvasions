/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  it.unimi.dsi.fastutil.objects.Object2IntMap$Entry
 *  it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerLevel
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.crafting.AbstractCookingRecipe
 *  net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.common.MinecraftForge
 *  net.minecraftforge.eventbus.api.Event
 *  org.spongepowered.asm.mixin.Final
 *  org.spongepowered.asm.mixin.Mixin
 *  org.spongepowered.asm.mixin.Shadow
 *  org.spongepowered.asm.mixin.injection.At
 *  org.spongepowered.asm.mixin.injection.Inject
 *  org.spongepowered.asm.mixin.injection.callback.CallbackInfo
 *  org.zeith.hammerlib.util.java.Cast
 */
package org.zeith.improvableskills.mixins;

import it.unimi.dsi.fastutil.objects.Object2IntMap;
import it.unimi.dsi.fastutil.objects.Object2IntOpenHashMap;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.AbstractCookingRecipe;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.Event;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.improvableskills.api.evt.CalculateAdditionalFurnaceExperienceMultiplier;

@Mixin(value={AbstractFurnaceBlockEntity.class})
public abstract class AbstractFurnaceBlockEntityMixin {
    @Shadow
    @Final
    private Object2IntOpenHashMap<ResourceLocation> f_58320_;

    @Shadow
    private static void m_154998_(ServerLevel level, Vec3 p_155000_, int p_155001_, float p_155002_) {
    }

    @Inject(method={"awardUsedRecipesAndPopExperience"}, at={@At(value="HEAD")})
    public void awardUsedRecipesAndPopExperience_IS3(ServerPlayer player, CallbackInfo ci) {
        ServerLevel level = player.m_284548_();
        CalculateAdditionalFurnaceExperienceMultiplier evt = new CalculateAdditionalFurnaceExperienceMultiplier((Player)player, (AbstractFurnaceBlockEntity)Cast.cast((Object)this));
        MinecraftForge.EVENT_BUS.post((Event)evt);
        float mul = evt.getMultiplier();
        if (mul > 0.0f) {
            for (Object2IntMap.Entry entry : this.f_58320_.object2IntEntrySet()) {
                level.m_7465_().m_44043_((ResourceLocation)entry.getKey()).ifPresent(recipe -> AbstractFurnaceBlockEntityMixin.m_154998_(level, player.m_20182_(), entry.getIntValue(), ((AbstractCookingRecipe)recipe).m_43750_() * mul));
            }
        }
    }
}

