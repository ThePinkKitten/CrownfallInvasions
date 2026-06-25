/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  org.zeith.hammerlib.util.configured.ConfiguredLib
 *  org.zeith.hammerlib.util.configured.types.ConfigCategory
 *  org.zeith.hammerlib.util.configured.types.ConfigString
 *  org.zeith.hammerlib.util.mcf.LogicalSidePredictor
 */
package org.zeith.improvableskills.api;

import net.minecraft.nbt.CompoundTag;
import org.zeith.hammerlib.util.configured.ConfiguredLib;
import org.zeith.hammerlib.util.configured.types.ConfigCategory;
import org.zeith.hammerlib.util.configured.types.ConfigString;
import org.zeith.hammerlib.util.mcf.LogicalSidePredictor;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.api.math.ExpressionEvaluator;
import org.zeith.improvableskills.api.math.functions.ExpressionFunction;

public class SkillCostConfig
extends ExpressionFunction {
    public static final String DEF_FORMULA = "(%lvl%+1)^%xpv%";
    public int xpValue;
    private String baseFormula = "(%lvl%+1)^%xpv%";
    private String serverFormula;
    private String clientFormula;

    public SkillCostConfig(int xpValue) {
        super("skill");
        this.xpValue = xpValue;
    }

    public void setBaseFormula(String baseFormula) {
        this.serverFormula = this.clientFormula = baseFormula;
        this.baseFormula = this.clientFormula;
    }

    public void load(ConfigCategory cfg, String skill) {
        this.serverFormula = ((ConfigString)((ConfigString)cfg.getElement(ConfiguredLib.STRING, skill)).withDefault(this.baseFormula).withComment("Cost calculator for this skill.\nAvailable variables:\n- %lvl% = the level we want to calculate XP value for.\n- %xpv% preset value (" + this.xpValue + ") for current skill.")).getValue();
    }

    public void writeServerNBT(CompoundTag nbt) {
        if (this.serverFormula != null) {
            nbt.m_128359_("Formula", this.serverFormula);
        }
    }

    public void readClientNBT(CompoundTag nbt) {
        this.resetClient();
        if (nbt.m_128425_("Formula", 8)) {
            this.clientFormula = nbt.m_128461_("Formula");
        }
    }

    public void resetClient() {
        this.clientFormula = this.baseFormula;
    }

    public int getXPToUpgrade(PlayerSkillData data, short targetLvl) {
        if (this.clientFormula != null && LogicalSidePredictor.getCurrentLogicalSide().isClient()) {
            String formula = this.clientFormula.replace("%lvl%", Short.toString(targetLvl)).replace("%xpv%", Integer.toString(this.xpValue));
            int val = (int)Math.ceil(ExpressionEvaluator.evaluateDouble(formula, this));
            return val;
        }
        if (this.serverFormula != null) {
            String formula = this.serverFormula.replace("%lvl%", Short.toString(targetLvl)).replace("%xpv%", Integer.toString(this.xpValue));
            int val = (int)Math.ceil(ExpressionEvaluator.evaluateDouble(formula, this));
            return val;
        }
        return (int)Math.pow(targetLvl + 1, this.xpValue);
    }
}

