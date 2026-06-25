/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.item.trading.MerchantOffer
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.zeith.improvableskills.api.evt.ApplySpecialPricesEvent;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillHuckster
extends PlayerSkillBase {
    public SkillHuckster() {
        super(10);
        this.setupScroll();
        this.getLoot().chance.n = 3;
        this.getLoot().setLootTable(BuiltInLootTables.f_78733_);
        this.getLoot().exclusive = true;
        this.setColor(65280);
        this.xpCalculator.setBaseFormula("150*(%lvl%+1)+(%lvl%+1)^3");
        this.addListener(this::hook);
    }

    private void hook(ApplySpecialPricesEvent e) {
        float modifier = PlayerDataManager.handleDataSafely(e.getPlayer(), data -> Float.valueOf(this.getLevelProgress(data.getSkillLevel(this))), Float.valueOf(0.0f)).floatValue() * 0.25f;
        if (modifier > 0.0f) {
            for (MerchantOffer offer : e.getEntity().m_6616_()) {
                int j = (int)Math.floor((double)modifier * (double)offer.m_45352_().m_41613_());
                offer.m_45353_(-j);
            }
        }
    }
}

