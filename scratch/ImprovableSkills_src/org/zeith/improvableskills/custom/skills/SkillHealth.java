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

public class SkillHealth
extends PlayerSkillBase {
    public static final UUID HP_ID = UUID.fromString("a6c5d900-a39b-4e1f-9572-f48e174335f2");

    public SkillHealth() {
        super(20);
        this.setupScroll();
        this.xpCalculator.xpValue = 3;
        this.setColor(0xFF3535);
        this.getLoot().chance.n = 9;
        this.getLoot().setLootTable(BuiltInLootTables.f_78741_);
    }

    @Override
    public void tick(PlayerSkillData data, boolean isActive) {
        if (data.atTickRate(10)) {
            double val;
            AttributeInstance hp = data.player.m_21051_(Attributes.f_22276_);
            AttributeModifier mod = hp.m_22111_(HP_ID);
            double d = val = isActive ? (double)data.getSkillLevel(this) : 0.0;
            if (mod == null || mod.m_22218_() != val) {
                if (mod != null) {
                    hp.m_22120_(HP_ID);
                }
                if (val > 0.0) {
                    hp.m_22125_(new AttributeModifier(HP_ID, "IS3 Health", val, AttributeModifier.Operation.ADDITION));
                }
            }
            if ((double)data.player.m_21223_() > hp.m_22135_()) {
                data.player.m_21153_(data.player.m_21223_());
            }
        }
    }
}

