/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  com.mojang.datafixers.kinds.Applicative
 *  com.mojang.serialization.Codec
 *  com.mojang.serialization.codecs.RecordCodecBuilder
 *  com.mojang.serialization.codecs.RecordCodecBuilder$Instance
 *  it.unimi.dsi.fastutil.objects.ObjectArrayList
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.world.item.Item
 *  net.minecraft.world.item.ItemStack
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.predicates.LootItemCondition
 *  net.minecraftforge.common.loot.IGlobalLootModifier
 *  net.minecraftforge.common.loot.LootModifier
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber$Bus
 *  net.minecraftforge.registries.ForgeRegistries$Keys
 *  net.minecraftforge.registries.RegisterEvent
 *  org.jetbrains.annotations.NotNull
 *  org.zeith.hammerlib.util.java.Cast
 */
package org.zeith.improvableskills.api.loot;

import com.mojang.datafixers.kinds.Applicative;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.util.List;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegisterEvent;
import org.jetbrains.annotations.NotNull;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.api.loot.SkillLoot;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.custom.items.ItemSkillScroll;
import org.zeith.improvableskills.init.ItemsIS;

@Mod.EventBusSubscriber(bus=Mod.EventBusSubscriber.Bus.MOD)
public class GlobalSkillLootModifier
extends LootModifier {
    public static final ResourceLocation EXCLUSIVE_SKILL_MODIFIERS = ImprovableSkills.id("exclusive_modifier");
    public static final Codec<GlobalSkillLootModifier> CODEC = RecordCodecBuilder.create(inst -> GlobalSkillLootModifier.codecStart((RecordCodecBuilder.Instance)inst).apply((Applicative)inst, GlobalSkillLootModifier::new));

    protected GlobalSkillLootModifier(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @NotNull
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        return generatedLoot.stream().filter(stack -> stack.m_150930_((Item)ItemsIS.SKILL_SCROLL)).filter(is -> {
            PlayerSkillBase skill = ItemSkillScroll.getSkillFromScroll(is);
            if (skill == null) {
                return false;
            }
            SkillLoot loot = skill.getLoot();
            if (loot == null) {
                return false;
            }
            return loot.exclusive;
        }).findFirst().map(lst -> new ObjectArrayList(List.of(lst))).orElse(generatedLoot);
    }

    public Codec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    @SubscribeEvent
    public static void register(RegisterEvent e) {
        e.register(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, EXCLUSIVE_SKILL_MODIFIERS, Cast.constant(CODEC));
    }
}

