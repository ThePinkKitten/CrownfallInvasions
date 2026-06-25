/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.entity.projectile.FishingHook
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
package org.zeith.improvableskills.custom.skills;

import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.FishingHook;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;

public class SkillLuckOfTheSea
extends PlayerSkillBase {
    public static final UUID LOTS_LUCK = UUID.fromString("d489061e-0b53-4aa3-a7f4-f1a9a726ef49");

    public SkillLuckOfTheSea() {
        super(15);
        this.setupScroll();
        this.getLoot().chance.n = 10;
        this.getLoot().setLootTable(BuiltInLootTables.f_78720_);
        this.getLoot().exclusive = true;
        this.setColor(5032168);
        this.xpCalculator.xpValue = 2;
    }

    @Override
    public void tick(PlayerSkillData data, boolean isActive) {
        Player player = data.player;
        FishingHook hook = player.f_36083_;
        short level = data.getSkillLevel(this);
        AttributeInstance luck = player.m_21204_().m_22146_(Attributes.f_22286_);
        if (luck != null) {
            luck.m_22120_(LOTS_LUCK);
            if (isActive && hook != null && !hook.m_213877_()) {
                luck.m_22125_(new AttributeModifier(LOTS_LUCK, "IS3 Fishing Luck", (double)level * 2.0, AttributeModifier.Operation.ADDITION));
            }
        }
    }
}

