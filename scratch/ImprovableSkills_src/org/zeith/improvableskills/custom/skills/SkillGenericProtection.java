/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraft.world.entity.ai.attributes.Attributes
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
package org.zeith.improvableskills.custom.skills;

import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;

public class SkillGenericProtection
extends PlayerSkillBase {
    public static final UUID PROTECTION_ID = UUID.fromString("8e56f8a6-a695-42d5-899b-89605f38cf80");

    public SkillGenericProtection() {
        super(20);
        this.setupScroll();
        this.getLoot().chance.n = 4;
        this.getLoot().setLootTable(BuiltInLootTables.f_78742_);
        this.setColor(5242846);
        this.xpCalculator.setBaseFormula("%lvl%^2.75");
    }

    @Override
    public void tick(PlayerSkillData data, boolean isActive) {
        AttributeInstance armor;
        if (data.atTickRate(10) && (armor = data.player.m_21051_(Attributes.f_22284_)) != null) {
            armor.m_22120_(PROTECTION_ID);
            if (isActive) {
                armor.m_22125_(new AttributeModifier(PROTECTION_ID, "IS3 Protection", (double)data.getSkillLevel(this), AttributeModifier.Operation.ADDITION));
            }
        }
    }
}

