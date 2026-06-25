/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.inventory.AbstractContainerMenu
 *  net.minecraft.world.inventory.EnchantmentMenu
 *  net.minecraft.world.level.storage.loot.BuiltInLootTables
 *  net.minecraft.world.phys.AABB
 *  net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent
 */
package org.zeith.improvableskills.custom.skills;

import java.util.List;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.EnchantmentMenu;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.event.enchanting.EnchantmentLevelSetEvent;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.data.PlayerDataManager;

public class SkillEnchanter
extends PlayerSkillBase {
    public SkillEnchanter() {
        super(20);
        this.setupScroll();
        this.getLoot().chance.n = 4;
        this.getLoot().setLootTable(BuiltInLootTables.f_78761_);
        this.setColor(16717722);
        this.xpCalculator.xpValue = 2;
        this.addListener(this::hook);
    }

    private void hook(EnchantmentLevelSetEvent e) {
        List players = e.getLevel().m_45976_(ServerPlayer.class, new AABB(e.getPos()).m_82400_(9.0));
        for (ServerPlayer p : players) {
            AbstractContainerMenu abstractContainerMenu = p.f_36096_;
            if (!(abstractContainerMenu instanceof EnchantmentMenu)) continue;
            EnchantmentMenu ench = (EnchantmentMenu)abstractContainerMenu;
            if (e.getItem() != ench.f_39449_.m_8020_(0)) continue;
            int enchanter = ((Number)PlayerDataManager.handleDataSafely((Player)p, data -> data.isSkillActive(this) ? data.getSkillLevel(this) : (short)0, 0)).intValue();
            if (enchanter > 0 && e.getEnchantLevel() != 0) {
                e.setEnchantLevel(Math.max(1, e.getEnchantLevel() - enchanter / 4));
            }
            return;
        }
    }
}

