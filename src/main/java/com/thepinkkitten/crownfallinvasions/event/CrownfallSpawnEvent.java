package com.thepinkkitten.crownfallinvasions.event;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Witch;
import net.minecraft.world.entity.monster.Zombie;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraftforge.event.entity.living.MobSpawnEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import com.thepinkkitten.crownfallinvasions.CrownfallMain;

import java.util.Random;
import java.util.UUID;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.MeleeAttackGoal;
import net.minecraft.world.entity.monster.Skeleton;

@Mod.EventBusSubscriber(modid = CrownfallMain.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class CrownfallSpawnEvent {

    private static final Random RANDOM = new Random();
    // 5% spawn chance
    private static final double HORDE_SPAWN_CHANCE = 0.05;

    // ==================== HP SCALING CONSTANTS ====================
    private static final double KING_BASE_HP = 200.0;
    private static final double KING_SCALE_MULT = 15.0;

    private static final double ELITE_BASE_HP = 100.0;
    private static final double ELITE_SCALE_MULT = 10.0;

    private static final double ZOMBIE_VANGUARD_BASE_HP = 40.0;
    private static final double ZOMBIE_VANGUARD_SCALE_MULT = 6.0;

    private static final double SKELETON_ARCHER_BASE_HP = 22.5;
    private static final double SKELETON_ARCHER_SCALE_MULT = 4.0;

    @SubscribeEvent
    public static void onMobSpawn(MobSpawnEvent.FinalizeSpawn event) {
        if (event.getLevel() instanceof ServerLevel level) {
            
            if (event.getEntity() != null && event.getEntity().getType() == EntityType.ZOMBIE) {
                if (event.getSpawnType() == MobSpawnType.NATURAL) {
                    
                    String role = event.getEntity().getPersistentData().getString("crownfall_role");
                    if (!role.isEmpty()) return;

                    if (RANDOM.nextDouble() < HORDE_SPAWN_CHANCE) {
                        BlockPos pos = event.getEntity().blockPosition();
                        spawnHorde(level, pos);
                        event.setSpawnCancelled(true);
                    }
                }
            }
        }
    }

    // ==================== HP SCALING HELPER ====================
    /**
     * Calculates scaled HP based on Local Difficulty.
     * Uses getEffectiveDifficulty() which ranges from 0.75 to 6.75.
     * Normalized to 0.0 - 6.0 by subtracting 0.75.
     *
     * @param level          The server level
     * @param pos            The position to check difficulty at
     * @param baseHp         Base HP for the entity type
     * @param scaleMultiplier How aggressively HP scales with difficulty
     * @return Scaled HP value
     */
    private static double getScaledHealth(float normalizedDifficulty, double baseHp, double scaleMultiplier) {
        return Math.max(1.0, baseHp + (baseHp * normalizedDifficulty * scaleMultiplier));
    }

    /**
     * Returns the normalized difficulty value (0.0 - 6.0) for storing in NBT.
     */
    private static float getNormalizedDifficulty(ServerLevel level, BlockPos pos, int globalKills) {
        float effective = level.getCurrentDifficultyAt(pos).getEffectiveDifficulty();
        float vanillaNormalized = Math.max(0, effective - 0.75f);
        return vanillaNormalized + (globalKills * 0.5f);
    }

    /**
     * Searches for a safe block to spawn on near the target Y coordinate.
     * Prevents minions from spawning on the roof if the King is summoned underground.
     */
    private static BlockPos getSafeSpawnPos(ServerLevel level, int x, int baseY, int z) {
        for (int y = baseY + 4; y >= baseY - 4; y--) {
            BlockPos checkPos = new BlockPos(x, y, z);
            if (level.getBlockState(checkPos.below()).blocksMotion() && 
                !level.getBlockState(checkPos).blocksMotion() && 
                !level.getBlockState(checkPos.above()).blocksMotion()) {
                return checkPos;
            }
        }
        return new BlockPos(x, baseY, z); // Fallback to base Y if no safe spot found
    }

    /**
     * Applies calculated HP to an entity. 
     * Dynamically adapts: If a mod like AttributeFix is installed, the HP is fully applied.
     * If Vanilla Minecraft silently clamps the HP (e.g. to 1024.0), it detects the clamp
     * and uses the HP ratio damage reduction to preserve Endless Scaling mathematically.
     */
    private static void applyScaledHealth(Mob entity, double calculatedHp) {
        // Attempt to set the raw calculated HP
        entity.getAttribute(Attributes.MAX_HEALTH).setBaseValue(calculatedHp);
        
        // Read back what the game actually accepted (detects vanilla silent clamping)
        double actualHp = entity.getAttribute(Attributes.MAX_HEALTH).getBaseValue();
        double hpRatio = actualHp / calculatedHp;
        
        entity.setHealth((float) actualHp);
        
        // Only apply the bypass if the game refused to give us the full HP
        if (hpRatio < 0.99) {
            entity.getPersistentData().putDouble("crownfall_hp_ratio", hpRatio);
        } else {
            // Ensure no leftover ratio if scaling is supported naturally
            entity.getPersistentData().remove("crownfall_hp_ratio");
        }
    }

    public static void spawnHorde(ServerLevel level, BlockPos pos) {
        int globalKills = com.thepinkkitten.crownfallinvasions.CrownfallWorldData.get(level).getGlobalKillCount();
        String hordeId = UUID.randomUUID().toString();
        float normalizedDifficulty = getNormalizedDifficulty(level, pos, globalKills);

        // 1. Spawn King
        Zombie king = EntityType.ZOMBIE.create(level);
        if (king != null) {
            king.moveTo(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
            king.setCustomName(net.minecraft.network.chat.Component.literal("Crownfall King [Lv. " + globalKills + "]").withStyle(net.minecraft.ChatFormatting.DARK_RED, net.minecraft.ChatFormatting.BOLD));
            king.setCustomNameVisible(true);
            
            double kingHp = getScaledHealth(normalizedDifficulty, KING_BASE_HP, KING_SCALE_MULT);
            applyScaledHealth(king, kingHp);
            
            // King Gear: Full Gold + Protection IV + Unbreaking III + Thorns III
            king.setItemSlot(EquipmentSlot.HEAD, enchantKingArmor(new ItemStack(Items.GOLDEN_HELMET)));
            king.setItemSlot(EquipmentSlot.CHEST, enchantKingArmor(new ItemStack(Items.GOLDEN_CHESTPLATE)));
            king.setItemSlot(EquipmentSlot.LEGS, enchantKingArmor(new ItemStack(Items.GOLDEN_LEGGINGS)));
            king.setItemSlot(EquipmentSlot.FEET, enchantKingArmor(new ItemStack(Items.GOLDEN_BOOTS)));
            
            // Add Netherite-tier base armor stats while wearing gold
            king.getAttribute(Attributes.ARMOR).setBaseValue(20.0);
            king.getAttribute(Attributes.ARMOR_TOUGHNESS).setBaseValue(8.0);
            
            ItemStack kingWeap = new ItemStack(Items.GOLDEN_AXE);
            kingWeap.enchant(Enchantments.SHARPNESS, 5);
            kingWeap.enchant(Enchantments.FIRE_ASPECT, 2);
            king.setItemSlot(EquipmentSlot.MAINHAND, kingWeap);
            
            // Phase 1 buffs (half power) - will be upgraded to Phase 2 at <50% HP in CrownfallEvents
            king.addEffect(new MobEffectInstance(MobEffects.DAMAGE_RESISTANCE, MobEffectInstance.INFINITE_DURATION, 1, false, false)); // Resistance II
            king.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, MobEffectInstance.INFINITE_DURATION, 0, false, false));     // Strength I
            king.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, MobEffectInstance.INFINITE_DURATION, 0, false, false));   // Speed I
            king.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, MobEffectInstance.INFINITE_DURATION, 0, false, false));  // Fire Resistance
            king.addEffect(new MobEffectInstance(MobEffects.GLOWING, 600, 0, false, false));  // Glowing for 30 seconds so players can locate King

            king.getPersistentData().putString("crownfall_horde_id", hordeId);
            king.getPersistentData().putString("crownfall_role", "king");
            king.getPersistentData().putInt("crownfall_phase", 1);
            king.getPersistentData().putInt("crownfall_skill_cd", 0);
            king.getPersistentData().putInt("crownfall_warcry_cd", 0);
            // Freeze difficulty at spawn time for reward calculation
            king.getPersistentData().putFloat("crownfall_difficulty", normalizedDifficulty);
            
            level.addFreshEntity(king);
        }

        // 2. Spawn Elites
        for (int i = 0; i < 2; i++) {
            Witch elite = EntityType.WITCH.create(level);
            if (elite != null && king != null) {
                int ex = pos.getX() + (RANDOM.nextInt(8) - 4);
                int ez = pos.getZ() + (RANDOM.nextInt(8) - 4);
                BlockPos ePos = getSafeSpawnPos(level, ex, pos.getY(), ez);
                elite.moveTo(ePos.getX(), ePos.getY(), ePos.getZ(), 0, 0);
                elite.setCustomName(net.minecraft.network.chat.Component.literal("Crownfall Elite [Lv. " + globalKills + "]").withStyle(net.minecraft.ChatFormatting.DARK_PURPLE));
                elite.setCustomNameVisible(true);
                
                double eliteHp = getScaledHealth(normalizedDifficulty, ELITE_BASE_HP, ELITE_SCALE_MULT);
                applyScaledHealth(elite, eliteHp);
                elite.getAttribute(Attributes.MOVEMENT_SPEED).setBaseValue(0.35);

                elite.getPersistentData().putString("crownfall_horde_id", hordeId);
                elite.getPersistentData().putString("crownfall_role", "elite");
                elite.getPersistentData().putInt("crownfall_aura_cd", 0);
                elite.getPersistentData().putFloat("crownfall_difficulty", normalizedDifficulty);
                elite.getPersistentData().putString("crownfall_king_uuid", king.getUUID().toString());
                
                level.addFreshEntity(elite);
            }
        }

        // 3. Spawn Minions — 7:3 ratio (Skeleton:Zombie)
        // Tăng số lượng minion lên 20-35
        int minionCount = 20 + RANDOM.nextInt(16);
        for (int i = 0; i < minionCount; i++) {
            spawnMinion(level, pos, hordeId, normalizedDifficulty, king, globalKills);
        }
    }

    public static void spawnMinion(ServerLevel level, BlockPos pos, String hordeId, float normalizedDifficulty, LivingEntity king, int globalKills) {
        spawnMinion(level, pos, hordeId, normalizedDifficulty, king, globalKills, false);
    }

    public static void spawnMinion(ServerLevel level, BlockPos pos, String hordeId, float normalizedDifficulty, LivingEntity king, int globalKills, boolean isRoyalGuard) {
        boolean isZombie = isRoyalGuard || RANDOM.nextInt(10) < 3; // 30% Zombie, 70% Skeleton (Royal Guards are always Zombies)
        net.minecraft.world.entity.Mob minion = isZombie ? EntityType.ZOMBIE.create(level) : EntityType.SKELETON.create(level);
        
        if (minion != null) {
            int mx = pos.getX() + (RANDOM.nextInt(12) - 6);
            int mz = pos.getZ() + (RANDOM.nextInt(12) - 6);
            BlockPos mPos = getSafeSpawnPos(level, mx, pos.getY(), mz);
            minion.moveTo(mPos.getX(), mPos.getY(), mPos.getZ(), 0, 0);
            minion.setCustomName(net.minecraft.network.chat.Component.literal((isZombie ? "Crownfall Vanguard" : "Crownfall Archer") + " [Lv. " + globalKills + "]").withStyle(net.minecraft.ChatFormatting.GRAY));
            minion.setCustomNameVisible(true);
            
            // Zombie Vanguard: 40 base HP, Skeleton Archer: 22.5 base HP (ratio 1.77x)
            double minionHp;
            if (isRoyalGuard) {
                minionHp = getScaledHealth(normalizedDifficulty, ZOMBIE_VANGUARD_BASE_HP * 2.0, ZOMBIE_VANGUARD_SCALE_MULT);
            } else if (isZombie) {
                minionHp = getScaledHealth(normalizedDifficulty, ZOMBIE_VANGUARD_BASE_HP, ZOMBIE_VANGUARD_SCALE_MULT);
            } else {
                minionHp = getScaledHealth(normalizedDifficulty, SKELETON_ARCHER_BASE_HP, SKELETON_ARCHER_SCALE_MULT);
            }
            applyScaledHealth(minion, minionHp);

            boolean isNetherite = isRoyalGuard || RANDOM.nextBoolean(); // Royal Guards always wear Netherite
            minion.setItemSlot(EquipmentSlot.HEAD, enchantArmor(new ItemStack(isNetherite ? Items.NETHERITE_HELMET : Items.DIAMOND_HELMET), globalKills));
            minion.setItemSlot(EquipmentSlot.CHEST, enchantArmor(new ItemStack(isNetherite ? Items.NETHERITE_CHESTPLATE : Items.DIAMOND_CHESTPLATE), globalKills));
            minion.setItemSlot(EquipmentSlot.LEGS, enchantArmor(new ItemStack(isNetherite ? Items.NETHERITE_LEGGINGS : Items.DIAMOND_LEGGINGS), globalKills));
            minion.setItemSlot(EquipmentSlot.FEET, enchantArmor(new ItemStack(isNetherite ? Items.NETHERITE_BOOTS : Items.DIAMOND_BOOTS), globalKills));
            minion.setItemSlot(EquipmentSlot.MAINHAND, getWeapon(isZombie, isNetherite, globalKills));
            
            // Archers get Instant Damage II tipped arrows in offhand
            if (!isZombie) {
                ItemStack tippedArrow = new ItemStack(Items.TIPPED_ARROW, 64);
                PotionUtils.setPotion(tippedArrow, Potions.STRONG_HARMING);
                minion.setItemSlot(EquipmentSlot.OFFHAND, tippedArrow);
            }
            
            minion.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, MobEffectInstance.INFINITE_DURATION, 0, false, false));
            if (isZombie) {
                minion.addEffect(new MobEffectInstance(MobEffects.DAMAGE_BOOST, MobEffectInstance.INFINITE_DURATION, 0, false, false));
            }

            // Drop rates nerfed to 5%
            minion.setDropChance(EquipmentSlot.HEAD, 0.05f);
            minion.setDropChance(EquipmentSlot.CHEST, 0.05f);
            minion.setDropChance(EquipmentSlot.LEGS, 0.05f);
            minion.setDropChance(EquipmentSlot.FEET, 0.05f);
            minion.setDropChance(EquipmentSlot.MAINHAND, 0.05f);
            minion.setDropChance(EquipmentSlot.OFFHAND, 0.0f); // Don't drop tipped arrows

            minion.getPersistentData().putString("crownfall_horde_id", hordeId);
            minion.getPersistentData().putString("crownfall_role", "minion");
            if (isRoyalGuard) {
                minion.getPersistentData().putBoolean("crownfall_royal_guard", true);
                minion.setCustomName(net.minecraft.network.chat.Component.literal("Royal Guard").withStyle(net.minecraft.ChatFormatting.GOLD, net.minecraft.ChatFormatting.BOLD));
                minion.setCustomNameVisible(true);
            }
            if (king != null) {
                minion.getPersistentData().putString("crownfall_king_uuid", king.getUUID().toString());
            }
            
            level.addFreshEntity(minion);
        }
    }

    private static ItemStack enchantKingArmor(ItemStack stack) {
        stack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, 4);
        stack.enchant(Enchantments.UNBREAKING, 3);
        stack.enchant(Enchantments.THORNS, 3);
        stack.getOrCreateTag().putBoolean("Unbreakable", true);
        return stack;
    }

    private static ItemStack enchantArmor(ItemStack stack, int globalKills) {
        int protLevel = Math.min(10, 3 + RANDOM.nextInt(2) + (globalKills / 2));
        stack.enchant(Enchantments.ALL_DAMAGE_PROTECTION, protLevel);
        stack.enchant(Enchantments.UNBREAKING, 3);
        return stack;
    }

    private static ItemStack getWeapon(boolean isZombie, boolean isNetherite, int globalKills) {
        if (isZombie) {
            ItemStack sword = new ItemStack(isNetherite ? Items.NETHERITE_SWORD : Items.DIAMOND_SWORD);
            int sharpLevel = Math.min(10, 3 + RANDOM.nextInt(3) + (globalKills / 2));
            sword.enchant(Enchantments.SHARPNESS, sharpLevel);
            return sword;
        } else {
            ItemStack bow = new ItemStack(Items.BOW);
            int powerLevel = Math.min(10, 4 + RANDOM.nextInt(2) + (globalKills / 2));
            bow.enchant(Enchantments.POWER_ARROWS, powerLevel);
            bow.enchant(Enchantments.PUNCH_ARROWS, 1 + RANDOM.nextInt(2));
            return bow;
        }
    }
}
