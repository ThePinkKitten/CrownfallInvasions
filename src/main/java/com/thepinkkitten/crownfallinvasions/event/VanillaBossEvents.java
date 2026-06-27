package com.thepinkkitten.crownfallinvasions.event;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.thepinkkitten.crownfallinvasions.CrownfallMain;
import com.thepinkkitten.crownfallinvasions.CrownfallWorldData;
import net.minecraft.server.level.ServerLevel;

import net.minecraftforge.event.entity.living.LivingEvent.LivingTickEvent;

@Mod.EventBusSubscriber(modid = CrownfallMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VanillaBossEvents {

    @SubscribeEvent
    public static void onBossTick(LivingTickEvent event) {
        if (event.getEntity().level().isClientSide()) return;

        if (event.getEntity() instanceof EnderDragon || event.getEntity() instanceof WitherBoss) {
            LivingEntity boss = (LivingEntity) event.getEntity();
            
            // Only buff the boss once
            if (!boss.getPersistentData().getBoolean("crownfall_buffed")) {
                ServerLevel level = (ServerLevel) boss.level();
                int globalKills = CrownfallWorldData.get(level).getGlobalKillCount();
                
                // HP Formula: 5000 + (Kills * 2000). Dragon and Wither must be extremely tanky.
                double calculatedHp = (boss instanceof WitherBoss) ? (3000.0 + globalKills * 1000.0) : (5000.0 + globalKills * 2000.0);
                
                boss.getAttribute(Attributes.MAX_HEALTH).setBaseValue(calculatedHp);
                
                double actualHp = boss.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
                double hpRatio = actualHp / calculatedHp;
                
                boss.setHealth((float) actualHp);
                
                if (hpRatio < 0.99) {
                    boss.getPersistentData().putDouble("crownfall_hp_ratio", hpRatio);
                }
                
                // Note: EnderDragon is IMMUNE to potion effects, but Wither isn't. 
                // We add them here so the Wither gets particles, but we STILL simulate Resistance/Strength manually in LivingHurtEvent.
                boss.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.DAMAGE_RESISTANCE, net.minecraft.world.effect.MobEffectInstance.INFINITE_DURATION, 3, false, false)); // Resistance IV
                boss.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.DAMAGE_BOOST, net.minecraft.world.effect.MobEffectInstance.INFINITE_DURATION, 2, false, false)); // Strength III
                boss.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.MOVEMENT_SPEED, net.minecraft.world.effect.MobEffectInstance.INFINITE_DURATION, 2, false, false)); // Speed III
                boss.addEffect(new net.minecraft.world.effect.MobEffectInstance(net.minecraft.world.effect.MobEffects.FIRE_RESISTANCE, net.minecraft.world.effect.MobEffectInstance.INFINITE_DURATION, 0, false, false));
                
                boss.getPersistentData().putBoolean("crownfall_buffed", true);
            }
        }
    }

    @SubscribeEvent
    public static void onBossHurt(LivingHurtEvent event) {
        if (event.getEntity().level().isClientSide) return;

        LivingEntity victim = event.getEntity();
        if (victim instanceof EnderDragon || victim instanceof WitherBoss) {
            
            // 1. Endless HP Scaling Bypass (Damage Reduction)
            if (victim.getPersistentData().contains("crownfall_hp_ratio")) {
                double ratio = victim.getPersistentData().getDouble("crownfall_hp_ratio");
                if (ratio < 1.0) {
                    event.setAmount((float) (event.getAmount() * ratio));
                }
            }
            
            // 2. Simulate Resistance IV (80% Damage Reduction) because Dragon is immune to potion effects
            if (victim.getPersistentData().getBoolean("crownfall_buffed")) {
                event.setAmount(event.getAmount() * 0.2f);
            }
        }
        
        // 3. Simulate Strength III (2.3x Damage) when Boss attacks
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if ((attacker instanceof EnderDragon || attacker instanceof WitherBoss) && attacker.getPersistentData().getBoolean("crownfall_buffed")) {
                event.setAmount(event.getAmount() * 2.3f);
            }
        }
    }
}
