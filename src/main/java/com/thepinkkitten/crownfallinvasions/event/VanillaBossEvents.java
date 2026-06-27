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
import net.minecraft.world.entity.projectile.Projectile;

@Mod.EventBusSubscriber(modid = CrownfallMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class VanillaBossEvents {


    @SubscribeEvent
    public static void onBossJoinLevel(EntityJoinLevelEvent event) {
        if (event.getLevel().isClientSide) return;

        if (event.getEntity() instanceof EnderDragon || event.getEntity() instanceof WitherBoss) {
            LivingEntity boss = (LivingEntity) event.getEntity();
            
            // Only buff the boss once
            if (!boss.getPersistentData().getBoolean("crownfall_buffed")) {
                ServerLevel level = (ServerLevel) event.getLevel();
                int globalKills = CrownfallWorldData.get(level).getGlobalKillCount();
                
                // HP Formula: 1000 + (Kills * 1000). Dragon and Wither will scale significantly in the late game.
                double calculatedHp = 1000.0 + (globalKills * 1000.0);
                
                boss.getAttribute(Attributes.MAX_HEALTH).setBaseValue(calculatedHp);
                
                double actualHp = boss.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
                double hpRatio = actualHp / calculatedHp;
                
                boss.setHealth((float) actualHp);
                
                if (hpRatio < 0.99) {
                    boss.getPersistentData().putDouble("crownfall_hp_ratio", hpRatio);
                }
                
                // Add innate strength buffs (Except Regeneration)
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
        }
    }
}
