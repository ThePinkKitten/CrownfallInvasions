/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.ai.attributes.Attribute
 *  net.minecraft.world.entity.ai.attributes.AttributeInstance
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier
 *  net.minecraft.world.entity.ai.attributes.AttributeModifier$Operation
 *  net.minecraftforge.common.ForgeMod
 */
package org.zeith.improvableskills.custom.skills;

import java.util.UUID;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraftforge.common.ForgeMod;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;

public class SkillFastSwimmer
extends PlayerSkillBase {
    public static final UUID SWIM_ID = UUID.fromString("856d21d3-0086-4ccd-bdb0-a43d98f5104c");

    public SkillFastSwimmer() {
        super(25);
        this.xpCalculator.xpValue = 2;
    }

    @Override
    public void tick(PlayerSkillData data, boolean isActive) {
        if (data.atTickRate(10) && !data.player.m_9236_().f_46443_) {
            double val;
            AttributeInstance hp = data.player.m_21051_((Attribute)ForgeMod.SWIM_SPEED.get());
            AttributeModifier mod = hp.m_22111_(SWIM_ID);
            double d = val = isActive ? (double)(data.getSkillProgress(this) * 1.75f) : 0.0;
            if (mod == null || mod.m_22218_() != val) {
                if (mod != null) {
                    hp.m_22120_(SWIM_ID);
                }
                if (val > 0.0) {
                    hp.m_22125_(new AttributeModifier(SWIM_ID, "IS3 Swim Speed", val, AttributeModifier.Operation.ADDITION));
                }
            }
        }
    }
}

