/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.item.enchantment.Enchantments
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 */
package org.zeith.improvableskills.custom.skills;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import org.zeith.improvableskills.api.evt.EntityEnchantmentLevelEvent;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillSoulSpeed
extends PlayerSkillBase {
    public SkillSoulSpeed() {
        super(Enchantments.f_44976_.m_6586_());
        this.setupScroll();
        this.getLoot().chance.n = 7;
        this.getLoot().setLootTable(BuiltInLootTables.f_78738_);
        this.getLoot().exclusive = true;
        this.setColor(65535);
        this.xpCalculator.setBaseFormula("(%lvl%+1)^6+150");
        this.addListener(this::hook);
    }

    private void hook(EntityEnchantmentLevelEvent e) {
        LivingEntity livingEntity = e.getEntity();
        if (livingEntity instanceof Player) {
            Player player = (Player)livingEntity;
            if (e.getEnchantment() == Enchantments.f_44976_) {
                e.max(PlayerDataManager.handleDataSafely(player, data -> data.isSkillActive(this) ? Math.min(data.getSkillLevel(this), Enchantments.f_44976_.m_6586_()) : 0, 0));
            }
        }
    }
}

