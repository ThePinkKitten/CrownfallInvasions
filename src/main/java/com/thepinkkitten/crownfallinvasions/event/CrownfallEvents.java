package com.thepinkkitten.crownfallinvasions.event;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerBossEvent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.BossEvent;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.EnchantmentInstance;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.entity.EntityLeaveLevelEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.thepinkkitten.crownfallinvasions.CrownfallMain;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.UUID;
import java.util.Random;

@Mod.EventBusSubscriber(modid = CrownfallMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CrownfallEvents {

    private static final Random RANDOM = new Random();
    private static final Map<UUID, ServerBossEvent> BOSS_BARS = new HashMap<>();

    // Difficulty tier thresholds (normalized 0.0 - 6.0+)
    private static final float TIER_MID = 1.5f;
    private static final float TIER_LATE = 4.0f;
    private static final float TIER_ENDLESS = 10.0f;

    // Skill cooldowns (ticks)
    private static final int CHAIN_LIGHTNING_CD = 100;
    private static final int GRAVITY_PULL_CD = 160;
    private static final int WAR_CRY_CD = 300;
    private static final int ELITE_AURA_CD = 120;

    // ==================== FRIENDLY FIRE + VANGUARD ON-HIT + ENDLESS SCALING ====================
    @SubscribeEvent
    public static void onLivingHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide) return;

        LivingEntity victim = event.getEntity();
        String victimRole = victim.getPersistentData().getString("crownfall_role");

        // --- Horde Specific Logic ---
        if (!victimRole.isEmpty()) {
            // 1. Lightning Immunity (prevents King's Chain Lightning from killing his own horde)
            if (event.getSource().getMsgId().equals("lightningBolt")) {
                event.setCanceled(true);
                return;
            }

            // 1b. Projectile Meat Shield (King Phase 2)
            if ("king".equals(victimRole) && victim.getPersistentData().getInt("crownfall_phase") == 2) {
                boolean isProjectile = event.getSource().isIndirect() || 
                                       event.getSource().getMsgId().toLowerCase().contains("projectile") || 
                                       event.getSource().getMsgId().toLowerCase().contains("bullet") || 
                                       event.getSource().getMsgId().toLowerCase().contains("arrow");
                if (isProjectile) {
                    String hordeId = victim.getPersistentData().getString("crownfall_horde_id");
                    long guardCount = victim.level().getEntitiesOfClass(LivingEntity.class, victim.getBoundingBox().inflate(30.0D))
                        .stream().filter(e -> hordeId.equals(e.getPersistentData().getString("crownfall_horde_id")) && e.getPersistentData().getBoolean("crownfall_royal_guard") && e.isAlive()).count();
                    
                    if (guardCount > 0) {
                        event.setAmount(event.getAmount() * 0.2f); // 80% reduction
                        if (victim.level() instanceof ServerLevel sl) {
                            sl.sendParticles(ParticleTypes.CRIT, victim.getX(), victim.getY() + 1, victim.getZ(), 10, 0.5, 1, 0.5, 0.1);
                        }
                    }
                }
            }

            // 2. Endless HP Scaling Bypass (Damage Reduction)
            // Minecraft hardcaps max HP at 1024. We bypass this by storing the ratio of (1024 / intended_hp)
            // and proportionally reducing incoming damage to simulate massive HP pools!
            if (victim.getPersistentData().contains("crownfall_hp_ratio")) {
                double ratio = victim.getPersistentData().getDouble("crownfall_hp_ratio");
                if (ratio < 1.0) {
                    event.setAmount((float) (event.getAmount() * ratio));
                }
            }
        }

        // --- Friendly Fire Immunity: same horde members never damage each other ---
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            String attackerHordeId = attacker.getPersistentData().getString("crownfall_horde_id");
            String victimHordeId = victim.getPersistentData().getString("crownfall_horde_id");
            if (!attackerHordeId.isEmpty() && attackerHordeId.equals(victimHordeId)) {
                event.setCanceled(true);
                return;
            }
        }

        // --- Vanguard (Zombie minion) on-hit: Wither + Poison ---
        if (event.getSource().getEntity() instanceof Zombie attacker) {
            String role = attacker.getPersistentData().getString("crownfall_role");
            String hordeId = attacker.getPersistentData().getString("crownfall_horde_id");
            if ("minion".equals(role) && !hordeId.isEmpty() && victim instanceof Player target) {
                if (RANDOM.nextDouble() < 0.30) {
                    target.addEffect(new MobEffectInstance(MobEffects.WITHER, 80, 1, false, true));
                    target.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 1, false, true));
                }
            }
        }
    }

    // ==================== LIVING TICK ====================
    @SubscribeEvent
    public static void onLivingTick(LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        String role = entity.getPersistentData().getString("crownfall_role");
        if (role.isEmpty()) return;
        if ("king".equals(role)) handleKingTick(entity);
        else if ("elite".equals(role)) handleEliteTick(entity);
        else if ("minion".equals(role)) handleMinionTick(entity);
    }

    // ==================== KING TICK ====================
    private static void handleKingTick(LivingEntity king) {
        UUID uuid = king.getUUID();
        ServerLevel level = (ServerLevel) king.level();
        int globalKills = com.thepinkkitten.crownfallinvasions.CrownfallWorldData.get(level).getGlobalKillCount();

        // Boss Bar
        ServerBossEvent bossBar = BOSS_BARS.computeIfAbsent(uuid, k -> {
            ServerBossEvent bar = new ServerBossEvent(
                    net.minecraft.network.chat.Component.literal("Crownfall King").withStyle(net.minecraft.ChatFormatting.DARK_RED, net.minecraft.ChatFormatting.BOLD),
                    BossEvent.BossBarColor.RED, BossEvent.BossBarOverlay.NOTCHED_10);
            bar.setDarkenScreen(true);
            return bar;
        });
        bossBar.setProgress(king.getHealth() / king.getMaxHealth());

        List<ServerPlayer> nearbyPlayers = level.getPlayers(p -> p.distanceTo(king) < 64.0D);
        List<ServerPlayer> currentPlayers = List.copyOf(bossBar.getPlayers());
        for (ServerPlayer p : currentPlayers) { if (!nearbyPlayers.contains(p)) bossBar.removePlayer(p); }
        for (ServerPlayer p : nearbyPlayers) { if (!currentPlayers.contains(p)) bossBar.addPlayer(p); }

        // Phase System
        int phase = king.getPersistentData().getInt("crownfall_phase");
        if (phase == 0) phase = 1;
        if (phase == 1 && king.getHealth() < king.getMaxHealth() * 0.5f) {
            king.getPersistentData().putInt("crownfall_phase", 2);
            phase = 2;
            king.removeAllEffects();
            king.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, MobEffectInstance.INFINITE_DURATION, 3, false, false));
            king.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, MobEffectInstance.INFINITE_DURATION, 2, false, false));
            king.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, MobEffectInstance.INFINITE_DURATION, 2, false, false));
            king.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, MobEffectInstance.INFINITE_DURATION, 0, false, false));
            level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, king.getX(), king.getY() + 1, king.getZ(), 3, 1, 1, 1, 0);
            level.sendParticles(ParticleTypes.FLAME, king.getX(), king.getY() + 1, king.getZ(), 50, 2, 2, 2, 0.1);
            level.playSound(null, king.getX(), king.getY(), king.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 4.0F, 1.0F);
            for (ServerPlayer p : nearbyPlayers) {
                p.sendSystemMessage(net.minecraft.network.chat.Component.literal("⚠ Crownfall King has entered Phase 2!").withStyle(net.minecraft.ChatFormatting.DARK_RED, net.minecraft.ChatFormatting.BOLD));
            }

            // --- Phase 2 Reinforcements (Meat Shield) ---
            int reinforcementCount = 10 + RANDOM.nextInt(6); // 10 to 15 minions
            String hordeId = king.getPersistentData().getString("crownfall_horde_id");
            float difficulty = king.getPersistentData().getFloat("crownfall_difficulty");
            
            // Spawn 3 Royal Guards (Meat Shields)
            for (int i = 0; i < 3; i++) {
                CrownfallSpawnEvent.spawnMinion(level, king.blockPosition(), hordeId, difficulty, king, globalKills, true);
            }
            // Spawn regular reinforcements
            for (int i = 0; i < reinforcementCount; i++) {
                CrownfallSpawnEvent.spawnMinion(level, king.blockPosition(), hordeId, difficulty, king, globalKills, false);
            }
            level.sendParticles(ParticleTypes.CAMPFIRE_COSY_SMOKE, king.getX(), king.getY(), king.getZ(), 100, 5, 1, 5, 0.05);
        }

        // Skill Cooldowns
        int skillCd = king.getPersistentData().getInt("crownfall_skill_cd");
        int warcryCd = king.getPersistentData().getInt("crownfall_warcry_cd");
        float cdMultiplier = (phase == 2) ? 0.5f : 1.0f;
        if (skillCd > 0) { king.getPersistentData().putInt("crownfall_skill_cd", --skillCd); }
        if (warcryCd > 0) { king.getPersistentData().putInt("crownfall_warcry_cd", --warcryCd); }

        // Skill Execution — radius scales endlessly with globalKills (no cap, by design)
        double maxSkillRange = 30.0D + globalKills;
        List<ServerPlayer> playersInRange = level.getPlayers(p -> p.distanceTo(king) < maxSkillRange && !p.isSpectator() && p.isAlive() && king.hasLineOfSight(p));
        if (playersInRange.isEmpty()) return;

        if (warcryCd <= 0) { executeWarCry(king, level, playersInRange, phase, cdMultiplier, globalKills); return; }
        if (skillCd <= 0) {
            ServerPlayer farthest = playersInRange.stream().max(Comparator.comparingDouble(p -> p.distanceTo(king))).orElse(null);
            ServerPlayer nearestFar = playersInRange.stream().filter(p -> p.distanceTo(king) > 8.0D).min(Comparator.comparingDouble(p -> p.distanceTo(king))).orElse(null);
            if (farthest != null && farthest.distanceTo(king) > 8.0D) {
                if (RANDOM.nextBoolean()) {
                    if (phase == 2) executeBlackHole(king, level, farthest, cdMultiplier, globalKills);
                    else executeGravityPull(king, level, farthest, cdMultiplier, globalKills);
                }
                else if (nearestFar != null) executeChainLightning(king, level, nearestFar, cdMultiplier, globalKills);
                else {
                    if (phase == 2) executeBlackHole(king, level, farthest, cdMultiplier, globalKills);
                    else executeGravityPull(king, level, farthest, cdMultiplier, globalKills);
                }
            } else if (nearestFar != null) {
                executeChainLightning(king, level, nearestFar, cdMultiplier, globalKills);
            }
        }
    }

    // ==================== KING SKILLS ====================
    private static void executeChainLightning(LivingEntity king, ServerLevel level, ServerPlayer target, float cdMultiplier, int globalKills) {
        king.getPersistentData().putInt("crownfall_skill_cd", (int)(CHAIN_LIGHTNING_CD * cdMultiplier));
        int strikeCount = 3 + (globalKills / 3);
        for (int i = 0; i < strikeCount; i++) {
            final int delay = i * 5;
            final double offsetX = (i == 0) ? 0 : (RANDOM.nextDouble() * 4 - 2);
            final double offsetZ = (i == 0) ? 0 : (RANDOM.nextDouble() * 4 - 2);
            level.getServer().tell(new net.minecraft.server.TickTask(level.getServer().getTickCount() + delay, () -> {
                if (target.isAlive() && !target.isRemoved() && king.isAlive() && !king.isRemoved()) {
                    LightningBolt bolt = EntityType.LIGHTNING_BOLT.create(level);
                    if (bolt != null) { 
                        bolt.moveTo(target.getX() + offsetX, target.getY(), target.getZ() + offsetZ); 
                        bolt.setVisualOnly(false); 
                        bolt.getPersistentData().putString("crownfall_horde_id", king.getPersistentData().getString("crownfall_horde_id"));
                        level.addFreshEntity(bolt); 
                    }
                }
            }));
        }
    }

    private static void executeGravityPull(LivingEntity king, ServerLevel level, ServerPlayer target, float cdMultiplier, int globalKills) {
        king.getPersistentData().putInt("crownfall_skill_cd", (int)(GRAVITY_PULL_CD * cdMultiplier));
        level.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY() + 1, target.getZ(), 40, 1, 1, 1, 0.5);
        level.playSound(null, target.getX(), target.getY(), target.getZ(), SoundEvents.ENDERMAN_TELEPORT, SoundSource.HOSTILE, 2.0F, 0.5F);
        level.getServer().tell(new net.minecraft.server.TickTask(level.getServer().getTickCount() + 10, () -> {
            if (target.isAlive() && !target.isRemoved() && king.isAlive() && !king.isRemoved()) {
                Vec3 direction = king.position().subtract(target.position()).normalize().scale(2.0);
                target.setDeltaMovement(direction.x, Math.max(direction.y, 0.4), direction.z);
                target.hurtMarked = true;
                target.hurt(level.damageSources().magic(), 8.0f);
                target.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 60, 1, false, true));
                if (globalKills >= 5) {
                    target.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 60, 0, false, true));
                }
                level.playSound(null, king.getX(), king.getY(), king.getZ(), SoundEvents.ANVIL_LAND, SoundSource.HOSTILE, 1.5F, 0.8F);
                level.sendParticles(ParticleTypes.PORTAL, target.getX(), target.getY(), target.getZ(), 30, 0.5, 0.5, 0.5, 0.2);
            }
        }));
    }

    private static void executeBlackHole(LivingEntity king, ServerLevel level, ServerPlayer target, float cdMultiplier, int globalKills) {
        king.getPersistentData().putInt("crownfall_skill_cd", (int)(GRAVITY_PULL_CD * cdMultiplier * 1.2f)); // Slightly longer CD
        Vec3 epicenter = target.position();
        level.playSound(null, epicenter.x, epicenter.y, epicenter.z, SoundEvents.PORTAL_TRIGGER, SoundSource.HOSTILE, 3.0F, 0.5F);
        
        // TickTask every 10 ticks for 5 seconds (10 iterations)
        for (int i = 0; i < 10; i++) {
            final int delay = i * 10;
            level.getServer().tell(new net.minecraft.server.TickTask(level.getServer().getTickCount() + delay, () -> {
                if (king.isAlive() && !king.isRemoved()) {
                    level.sendParticles(ParticleTypes.REVERSE_PORTAL, epicenter.x, epicenter.y + 1, epicenter.z, 200, 5, 2, 5, 0.1);
                    level.sendParticles(ParticleTypes.SMOKE, epicenter.x, epicenter.y + 1, epicenter.z, 50, 1, 1, 1, 0.0);
                    
                    List<ServerPlayer> suckedPlayers = level.getPlayers(p -> p.distanceToSqr(epicenter) < 900.0D && p.isAlive() && !p.isSpectator()); // 30 block radius
                    for (ServerPlayer p : suckedPlayers) {
                        double dist = p.distanceToSqr(epicenter);
                        Vec3 direction = epicenter.subtract(p.position()).normalize().scale(0.8); // Strong pull
                        p.setDeltaMovement(p.getDeltaMovement().add(direction.x, Math.max(direction.y, 0.1), direction.z));
                        p.hurtMarked = true;
                        
                        if (dist < 9.0D) { // < 3 blocks from center
                            p.hurt(level.damageSources().wither(), 5.0f);
                            p.removeAllEffects(); // PURGE BUFFS
                        }
                    }
                }
            }));
        }
    }

    private static void executeWarCry(LivingEntity king, ServerLevel level, List<ServerPlayer> players, int phase, float cdMultiplier, int globalKills) {
        king.getPersistentData().putInt("crownfall_warcry_cd", (int)(WAR_CRY_CD * cdMultiplier));
        level.playSound(null, king.getX(), king.getY(), king.getZ(), SoundEvents.ENDER_DRAGON_GROWL, SoundSource.HOSTILE, 4.0F, 0.7F);
        level.sendParticles(ParticleTypes.EXPLOSION, king.getX(), king.getY() + 1, king.getZ(), 8, 3, 1, 3, 0);
        level.sendParticles(ParticleTypes.SONIC_BOOM, king.getX(), king.getY() + 1, king.getZ(), 3, 2, 0.5, 2, 0);
        double debuffRadius = 20.0D + globalKills;
        double buffRadius = 30.0D + globalKills;
        for (ServerPlayer player : players) {
            if (player.distanceTo(king) <= debuffRadius) {
                player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 3, false, true));
                player.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 100, 2, false, true));
                
                // Armor Corrosion: Damage all armor pieces by 5% of their max durability
                for (ItemStack armor : player.getArmorSlots()) {
                    if (!armor.isEmpty() && armor.isDamageableItem()) {
                        int damageAmount = Math.max(1, (int)(armor.getMaxDamage() * 0.05f));
                        armor.hurtAndBreak(damageAmount, player, (p) -> p.broadcastBreakEvent(player.getEquipmentSlotForItem(armor)));
                    }
                }
                // Corrode main hand weapon too
                ItemStack mainHand = player.getMainHandItem();
                if (!mainHand.isEmpty() && mainHand.isDamageableItem()) {
                    int damageAmount = Math.max(1, (int)(mainHand.getMaxDamage() * 0.05f));
                    mainHand.hurtAndBreak(damageAmount, player, (p) -> p.broadcastBreakEvent(net.minecraft.world.entity.EquipmentSlot.MAINHAND));
                }
            }
        }
        String hordeId = king.getPersistentData().getString("crownfall_horde_id");
        List<LivingEntity> hordeMembers = level.getEntitiesOfClass(LivingEntity.class, king.getBoundingBox().inflate(buffRadius));
        for (LivingEntity member : hordeMembers) {
            if (hordeId.equals(member.getPersistentData().getString("crownfall_horde_id")) && member != king) {
                member.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, 160, 1, false, false));
                member.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 160, 1, false, false));
            }
        }
    }

    // ==================== ELITE TICK — Aura with scaled damage ====================
    private static void handleEliteTick(LivingEntity elite) {
        // Prevent infighting — clear target if targeting a horde member
        clearFriendlyTarget(elite);
        // Follow King when idle
        followKingIfIdle(elite);

        ServerLevel level = (ServerLevel) elite.level();
        int auraCd = elite.getPersistentData().getInt("crownfall_aura_cd");
        if (auraCd > 0) { elite.getPersistentData().putInt("crownfall_aura_cd", auraCd - 1); return; }
        elite.getPersistentData().putInt("crownfall_aura_cd", ELITE_AURA_CD);

        String hordeId = elite.getPersistentData().getString("crownfall_horde_id");
        if (hordeId.isEmpty()) return;

        // Scaled aura damage: 4 at Day 1, ~22 at endgame
        float normalizedDiff = elite.getPersistentData().getFloat("crownfall_difficulty");
        float auraDamage = 4.0f + (normalizedDiff * 3.0f);

        level.sendParticles(ParticleTypes.WITCH, elite.getX(), elite.getY() + 2, elite.getZ(), 15, 0.5, 0.5, 0.5, 0.1);
        level.sendParticles(ParticleTypes.CAMPFIRE_SIGNAL_SMOKE, elite.getX(), elite.getY() + 1, elite.getZ(), 8, 0.3, 0.3, 0.3, 0.02);
        level.playSound(null, elite.getX(), elite.getY(), elite.getZ(), SoundEvents.WITCH_CELEBRATE, SoundSource.HOSTILE, 2.0F, 1.0F);

        // Buff horde members
        List<LivingEntity> hordeMembers = level.getEntitiesOfClass(LivingEntity.class, elite.getBoundingBox().inflate(20.0D));
        for (LivingEntity member : hordeMembers) {
            if (!hordeId.equals(member.getPersistentData().getString("crownfall_horde_id")) || member == elite) continue;
            String memberRole = member.getPersistentData().getString("crownfall_role");
            if ("king".equals(memberRole)) {
                member.heal(5.0f); // Regeneration effect does not work on Undead (King is Zombie)
                level.sendParticles(ParticleTypes.HEART, member.getX(), member.getY() + 2, member.getZ(), 3, 0.5, 0.5, 0.5, 0);
            } else if ("minion".equals(memberRole)) {
                member.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, 120, 3, false, false));
            }
        }

        // Debuff players with scaled damage and spawn Lingering Toxic Cloud
        List<ServerPlayer> nearbyPlayers = level.getPlayers(p -> p.distanceTo(elite) < 15.0D && !p.isSpectator() && p.isAlive());
        if (!nearbyPlayers.isEmpty()) {
            net.minecraft.world.entity.AreaEffectCloud cloud = new net.minecraft.world.entity.AreaEffectCloud(level, elite.getX(), elite.getY(), elite.getZ());
            cloud.setOwner(elite);
            cloud.setRadius(6.0F);
            cloud.setWaitTime(10);
            cloud.setDuration(100); // 5 seconds
            cloud.setRadiusPerTick(-cloud.getRadius() / (float)cloud.getDuration());
            cloud.addEffect(new MobEffectInstance(MobEffects.HARM, 1, 1)); // Instant Damage II
            cloud.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 2)); // Slowness III
            level.addFreshEntity(cloud);
        }
        for (ServerPlayer player : nearbyPlayers) {
            player.hurt(level.damageSources().magic(), auraDamage);
            player.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 1, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.POISON, 80, 1, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.HUNGER, 100, 2, false, true));
            player.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 80, 0, false, true));
        }
    }

    // ==================== MINION/ELITE FOLLOW-KING AI ====================
    private static void handleMinionTick(LivingEntity minion) {
        // Prevent infighting — clear target if targeting a horde member
        clearFriendlyTarget(minion);
        followKingIfIdle(minion);
    }

    /**
     * If a horde member is targeting another horde member (same horde ID),
     * forcibly clear the target. Prevents infighting from Chain Lightning,
     * Thorns, or other indirect damage sources.
     */
    private static void clearFriendlyTarget(LivingEntity entity) {
        if (!(entity instanceof Mob mob)) return;
        if (mob.getTarget() == null) return;
        String hordeId = entity.getPersistentData().getString("crownfall_horde_id");
        if (hordeId.isEmpty()) return;
        String targetHordeId = mob.getTarget().getPersistentData().getString("crownfall_horde_id");
        if (hordeId.equals(targetHordeId)) {
            mob.setTarget(null);
        }
    }

    /**
     * If a horde member (Mob) has no current attack target, pathfind towards the King
     * every ~2 seconds to keep the horde grouped together.
     */
    private static void followKingIfIdle(LivingEntity entity) {
        if (!(entity instanceof Mob mob)) return;
        ServerLevel level = (ServerLevel) entity.level();

        // Only redirect if no current target
        if (mob.getTarget() != null && mob.getTarget().isAlive()) return;

        // Throttle — only check every 40 ticks (2 seconds)
        int followTick = entity.getPersistentData().getInt("crownfall_follow_tick");
        if (followTick > 0) {
            entity.getPersistentData().putInt("crownfall_follow_tick", followTick - 1);
            return;
        }
        entity.getPersistentData().putInt("crownfall_follow_tick", 40);

        // Find King by UUID
        String kingUuidStr = entity.getPersistentData().getString("crownfall_king_uuid");
        if (kingUuidStr.isEmpty()) return;

        try {
            UUID kingUuid = UUID.fromString(kingUuidStr);
            // Search for King within 50 blocks
            List<LivingEntity> nearby = level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(50.0D));
            for (LivingEntity candidate : nearby) {
                if (candidate.getUUID().equals(kingUuid) && candidate.isAlive()) {
                    // Only pathfind if more than 10 blocks away from King
                    if (entity.distanceTo(candidate) > 10.0D) {
                        mob.getNavigation().moveTo(candidate.getX(), candidate.getY(), candidate.getZ(), 1.0D);
                    }
                    return;
                }
            }
        } catch (IllegalArgumentException ignored) {}
    }

    // ==================== BOSS BAR CLEANUP ====================
    @SubscribeEvent
    public static void onEntityLeaveLevel(EntityLeaveLevelEvent event) {
        if (event.getEntity() instanceof LivingEntity entity && !entity.level().isClientSide) {
            if ("king".equals(entity.getPersistentData().getString("crownfall_role"))) {
                ServerBossEvent bossBar = BOSS_BARS.remove(entity.getUUID());
                if (bossBar != null) bossBar.removeAllPlayers();
            }
        }
    }

    // ==================== KING DEATH / VAMPIRISM ====================
    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        
        // --- 1. Vampirism: Heal King if player dies nearby ---
        if (entity instanceof Player player) {
            ServerLevel level = (ServerLevel) player.level();
            List<LivingEntity> kings = level.getEntitiesOfClass(LivingEntity.class, player.getBoundingBox().inflate(100.0D), 
                e -> "king".equals(e.getPersistentData().getString("crownfall_role")));
            for (LivingEntity king : kings) {
                if (king.isAlive()) {
                    float healAmount = king.getMaxHealth() * 0.15f;
                    king.heal(healAmount);
                    level.sendParticles(ParticleTypes.HEART, king.getX(), king.getY() + 2, king.getZ(), 15, 1.0, 1.0, 1.0, 0);
                    level.playSound(null, king.getX(), king.getY(), king.getZ(), SoundEvents.WITHER_SPAWN, SoundSource.HOSTILE, 2.0F, 1.5F);
                    
                    List<ServerPlayer> nearbyPlayers = level.getPlayers(p -> p.distanceTo(king) < 64.0D);
                    for (ServerPlayer p : nearbyPlayers) {
                        p.sendSystemMessage(net.minecraft.network.chat.Component.literal("🩸 The King harvests the fallen soul and heals!").withStyle(net.minecraft.ChatFormatting.DARK_RED, net.minecraft.ChatFormatting.ITALIC));
                    }
                }
            }
        }
        
        String hordeId = entity.getPersistentData().getString("crownfall_horde_id");
        if (hordeId.isEmpty()) return;
        String role = entity.getPersistentData().getString("crownfall_role");

        if ("king".equals(role)) {
            ServerBossEvent bossBar = BOSS_BARS.remove(entity.getUUID());
            if (bossBar != null) bossBar.removeAllPlayers();
            ServerLevel level = (ServerLevel) entity.level();
            level.sendParticles(ParticleTypes.EXPLOSION_EMITTER, entity.getX(), entity.getY() + 1, entity.getZ(), 5, 2, 2, 2, 0);
            level.sendParticles(ParticleTypes.FLASH, entity.getX(), entity.getY() + 1, entity.getZ(), 3, 0, 0, 0, 0);
            level.playSound(null, entity.getX(), entity.getY(), entity.getZ(), SoundEvents.GENERIC_EXPLODE, SoundSource.HOSTILE, 6.0F, 0.8F);
            com.thepinkkitten.crownfallinvasions.CrownfallWorldData data = com.thepinkkitten.crownfallinvasions.CrownfallWorldData.get(level);
            data.addKill();
            int newKills = data.getGlobalKillCount();

            List<ServerPlayer> nearbyPlayers = level.getPlayers(p -> p.distanceTo(entity) < 64.0D);
            for (ServerPlayer p : nearbyPlayers) {
                p.sendSystemMessage(net.minecraft.network.chat.Component.literal("☠ Crownfall King has fallen! The horde crumbles!").withStyle(net.minecraft.ChatFormatting.GOLD, net.minecraft.ChatFormatting.BOLD));
                p.sendSystemMessage(net.minecraft.network.chat.Component.literal("⚔ Global Horde Difficulty increased! (Kills: " + newKills + ")").withStyle(net.minecraft.ChatFormatting.RED, net.minecraft.ChatFormatting.BOLD));
            }
            List<LivingEntity> nearbyEntities = level.getEntitiesOfClass(LivingEntity.class, entity.getBoundingBox().inflate(100.0D));
            for (LivingEntity nearby : nearbyEntities) {
                if (hordeId.equals(nearby.getPersistentData().getString("crownfall_horde_id")) && !"king".equals(nearby.getPersistentData().getString("crownfall_role"))) {
                    nearby.kill();
                }
            }
        }
    }

    // ==================== DROP LOGIC — Scaled by frozen difficulty ====================
    @SubscribeEvent
    public static void onLivingDrops(LivingDropsEvent event) {
        LivingEntity entity = event.getEntity();
        if (entity.level().isClientSide) return;
        String role = entity.getPersistentData().getString("crownfall_role");
        if (role.isEmpty()) return;

        float diff = entity.getPersistentData().getFloat("crownfall_difficulty");

        if ("elite".equals(role)) {
            addDrop(event, new ItemStack(Items.TOTEM_OF_UNDYING, 1));
            // Thay sách enchant bằng Bình XP (phục vụ Puffish Skills) và Táo vàng
            addDrop(event, new ItemStack(Items.EXPERIENCE_BOTTLE, 16 + RANDOM.nextInt(16)));
            if (RANDOM.nextBoolean()) {
                addDrop(event, new ItemStack(Items.GOLDEN_APPLE, 1 + RANDOM.nextInt(2)));
            }
        } else if ("minion".equals(role)) {
            // Cap enchantments on dropped gear to vanilla max levels
            for (ItemEntity drop : event.getDrops()) {
                capDropEnchantments(drop.getItem());
            }
        } else if ("king".equals(role)) {
            int rewardCount = getRewardCount(diff);
            for (int i = 0; i < rewardCount; i++) {
                addDrop(event, getRandomReward(diff));
            }
        }
    }

    private static void addDrop(LivingDropsEvent event, ItemStack stack) {
        LivingEntity entity = event.getEntity();
        ItemEntity itemEntity = new ItemEntity(entity.level(), entity.getX(), entity.getY(), entity.getZ(), stack);
        itemEntity.setDefaultPickUpDelay();
        event.getDrops().add(itemEntity);
    }

    /**
     * Caps all enchantments on an ItemStack to their vanilla max levels.
     * Mobs wear OP gear (Prot 10, Sharp 10) for combat, but if gear drops,
     * players only receive vanilla-legal enchantments (Prot 4, Sharp 5, etc.).
     */
    private static void capDropEnchantments(ItemStack stack) {
        Map<net.minecraft.world.item.enchantment.Enchantment, Integer> enchants = stack.getAllEnchantments();
        if (enchants.isEmpty()) return;
        // Clear existing enchantments tag
        if (stack.getTag() != null) stack.getTag().remove("Enchantments");
        // Re-add with capped levels
        for (Map.Entry<net.minecraft.world.item.enchantment.Enchantment, Integer> entry : enchants.entrySet()) {
            int cappedLevel = Math.min(entry.getValue(), entry.getKey().getMaxLevel());
            stack.enchant(entry.getKey(), cappedLevel);
        }
    }

    // ==================== REWARD TIER SYSTEM ====================
    private static int getRewardCount(float diff) {
        if (diff < TIER_MID) return 2 + RANDOM.nextInt(2);       // Early: 2-3
        if (diff < TIER_LATE) return 4 + RANDOM.nextInt(2);      // Mid: 4-5
        return 5 + RANDOM.nextInt(3);                             // Late/Endless: 5-7
    }

    private static ItemStack getRandomReward(float diff) {
        int roll = RANDOM.nextInt(100);

        if (diff < TIER_MID) {
            // Early: 0% jackpot, 15% rare, 85% common
            if (roll < 15) return getRareReward(diff);
            return getCommonReward(diff);
        } else if (diff < TIER_LATE) {
            // Mid: 5% jackpot, 30% rare, 65% common
            if (roll < 5) return getJackpotReward();
            if (roll < 35) return getRareReward(diff);
            return getCommonReward(diff);
        } else {
            // Late/Endless: 12% jackpot, 40% rare, 48% common
            if (roll < 12) return getJackpotReward();
            if (roll < 52) return getRareReward(diff);
            return getCommonReward(diff);
        }
    }

    private static ItemStack getCommonReward(float diff) {
        int type = RANDOM.nextInt(4);
        if (diff < TIER_MID) {
            // Early: small quantities
            return switch (type) {
                case 0 -> new ItemStack(Items.DIAMOND, 4 + RANDOM.nextInt(5));
                case 1 -> new ItemStack(Items.EMERALD, 4 + RANDOM.nextInt(5));
                case 2 -> new ItemStack(Items.EXPERIENCE_BOTTLE, 8 + RANDOM.nextInt(9));
                default -> new ItemStack(Items.GOLD_INGOT, 4 + RANDOM.nextInt(5));
            };
        } else if (diff < TIER_LATE) {
            // Mid: medium quantities
            return switch (type) {
                case 0 -> new ItemStack(Items.DIAMOND, 8 + RANDOM.nextInt(9));
                case 1 -> new ItemStack(Items.EMERALD, 8 + RANDOM.nextInt(9));
                case 2 -> new ItemStack(Items.EXPERIENCE_BOTTLE, 16 + RANDOM.nextInt(17));
                default -> new ItemStack(Items.GOLD_BLOCK, 4 + RANDOM.nextInt(5));
            };
        } else {
            // Late: large quantities
            return switch (type) {
                case 0 -> new ItemStack(Items.DIAMOND, 16 + RANDOM.nextInt(17));
                case 1 -> new ItemStack(Items.EMERALD, 16 + RANDOM.nextInt(17));
                case 2 -> new ItemStack(Items.EXPERIENCE_BOTTLE, 16 + RANDOM.nextInt(17));
                default -> new ItemStack(Items.GOLD_BLOCK, 8 + RANDOM.nextInt(9));
            };
        }
    }

    private static ItemStack getRareReward(float diff) {
        if (diff < TIER_MID) {
            // Early: no endgame items
            int type = RANDOM.nextInt(3);
            return switch (type) {
                case 0 -> new ItemStack(Items.IRON_BLOCK, 4 + RANDOM.nextInt(5));
                case 1 -> new ItemStack(Items.DIAMOND, 8 + RANDOM.nextInt(5));
                default -> new ItemStack(Items.EXPERIENCE_BOTTLE, 16 + RANDOM.nextInt(16)); // Thay vì sách Unbreaking
            };
        } else {
            // Mid & Late: full rare pool
            int type = RANDOM.nextInt(6);
            return switch (type) {
                case 0 -> new ItemStack(Items.NETHERITE_INGOT, 1 + RANDOM.nextInt(2));
                case 1 -> new ItemStack(Items.NETHERITE_UPGRADE_SMITHING_TEMPLATE, 1);
                case 2 -> new ItemStack(Items.TOTEM_OF_UNDYING, 1);
                case 3 -> new ItemStack(Items.TRIDENT, 1);
                case 4 -> new ItemStack(Items.ENCHANTED_GOLDEN_APPLE, 1);
                default -> new ItemStack(Items.GOLDEN_APPLE, 2 + RANDOM.nextInt(3)); // Thay vì sách Mending
            };
        }
    }

    private static ItemStack getJackpotReward() {
        int type = RANDOM.nextInt(5);
        return switch (type) {
            case 0 -> new ItemStack(Items.ELYTRA, 1);
            case 1 -> new ItemStack(Items.NETHER_STAR, 1);
            case 2 -> new ItemStack(Items.DRAGON_HEAD, 1);
            case 3 -> new ItemStack(Items.BEACON, 1);
            default -> new ItemStack(Items.NETHERITE_INGOT, 4 + RANDOM.nextInt(5));
        };
    }


}
