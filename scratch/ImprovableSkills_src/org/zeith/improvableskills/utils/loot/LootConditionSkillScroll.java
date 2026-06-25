/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.network.chat.Component
 *  net.minecraft.world.entity.Entity
 *  net.minecraft.world.entity.LivingEntity
 *  net.minecraft.world.entity.ai.Brain
 *  net.minecraft.world.entity.ai.memory.MemoryModuleType
 *  net.minecraft.world.entity.ai.targeting.TargetingConditions
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.storage.loot.LootContext
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParam
 *  net.minecraft.world.level.storage.loot.parameters.LootContextParams
 *  net.minecraft.world.level.storage.loot.predicates.LootItemConditionType
 *  net.minecraft.world.level.storage.loot.predicates.LootItemConditions
 *  net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition
 *  net.minecraft.world.phys.Vec3
 *  net.minecraftforge.common.util.FakePlayer
 *  org.zeith.hammerlib.util.java.Cast
 */
package org.zeith.improvableskills.utils.loot;

import java.util.Optional;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.targeting.TargetingConditions;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditions;
import net.minecraft.world.level.storage.loot.predicates.LootItemRandomChanceCondition;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.util.FakePlayer;
import org.zeith.hammerlib.util.java.Cast;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.cfg.ConfigsIS;
import org.zeith.improvableskills.data.PlayerDataManager;

public class LootConditionSkillScroll
extends LootItemRandomChanceCondition {
    private final PlayerSkillBase skill;

    public LootConditionSkillScroll(float probability, PlayerSkillBase skill) {
        super(probability);
        this.skill = skill;
    }

    public Component getContextComponent() {
        return this.skill.getLocalizedName();
    }

    public LootItemConditionType m_7940_() {
        return LootItemConditions.f_81813_;
    }

    private Player findPlayer(LootContext context, LootContextParam<Entity> par) {
        Player pl;
        LivingEntity le;
        Brain brain;
        Optional optional;
        if (!context.m_78936_(par)) {
            return null;
        }
        Entity ent = (Entity)context.m_78953_(par);
        if (!(ent instanceof Player) && ent instanceof LivingEntity && (optional = this.optMemory(brain = (le = (LivingEntity)ent).m_6274_(), MemoryModuleType.f_26368_).or(() -> this.optMemory(brain, MemoryModuleType.f_26374_).map(Cast.convertTo(Player.class)))).isPresent()) {
            return optional.orElse(null);
        }
        return ent instanceof Player && !((pl = (Player)ent) instanceof FakePlayer) ? pl : null;
    }

    protected <T> Optional<T> optMemory(Brain<?> brain, MemoryModuleType<T> type) {
        if (!brain.m_21874_(type)) {
            return Optional.empty();
        }
        return brain.m_21952_(type);
    }

    public boolean test(LootContext context) {
        Vec3 pos;
        Player p = this.findPlayer(context, (LootContextParam<Entity>)LootContextParams.f_81458_);
        if (p == null) {
            p = this.findPlayer(context, (LootContextParam<Entity>)LootContextParams.f_81455_);
        }
        if (p == null) {
            p = this.findPlayer(context, (LootContextParam<Entity>)LootContextParams.f_81459_);
        }
        if (!(p != null || context.m_78936_(LootContextParams.f_81458_) || context.m_78936_(LootContextParams.f_81455_) || context.m_78936_(LootContextParams.f_81459_) || (pos = (Vec3)context.m_78953_(LootContextParams.f_81460_)) == null)) {
            p = context.m_78952_().m_45941_(TargetingConditions.m_148353_().m_148355_().m_26883_(Double.POSITIVE_INFINITY).m_26893_(), pos.f_82479_, pos.f_82480_, pos.f_82481_);
        }
        if (p != null) {
            return PlayerDataManager.handleDataSafely(p, data -> !data.hasSkillScroll(this.skill) || ConfigsIS.dropScrollsAfterUnlock && data.getSkillProgress(this.skill) < 1.0f, true);
        }
        return false;
    }
}

