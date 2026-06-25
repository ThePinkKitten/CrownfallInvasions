# Crownfall Invasions — Complete Mod Specification

> Forge 1.20.1 mod: Vanilla hostile mob "Crownfall Invasions" raid system. NO custom mobs, dimensions, blocks, gear, or items. Uses only vanilla entities (Zombie, Skeleton, Witch) with modified stats, NBT data, and event-driven combat AI.

---

## Project Identity

| Field | Value |
|---|---|
| **Mod ID** | `crownfallinvasions` |
| **Display Name** | Crownfall Invasions |
| **Package** | `com.thepinkkitten.crownfallinvasions` |
| **Author** | ThePinkKitten |
| **License** | MIT |
| **Forge Version** | 47.1.106 (1.20.1) |
| **Build Output** | `build/libs/crownfallinvasions-1.20.1-1.0.0.jar` |
| **Mods Folder** | `D:\Games\ModrinthApp\launcher_logs\profiles\Gun\mods\` |

---

## Project Structure

```
src/main/java/com/thepinkkitten/crownfallinvasions/
├── CrownfallMain.java                    # @Mod entry point (event-driven only, no registries)
├── command/
│   └── SummonCrownfallCommand.java       # /summon_crownfall_horde command (OP level 2)
└── event/
    ├── CrownfallSpawnEvent.java           # Horde spawning, gear, HP scaling, minion factory
    ├── CrownfallEvents.java              # Tick logic, skills, phase system, drops, death
    └── CrownfallForgeEvents.java         # Command registration via RegisterCommandsEvent
```

---

## 1. System Overview

**Composition:** 1 King + 2 Elites + 20-35 Minions (initial) + 10-15 reinforcement Minions (Phase 2).

All members are linked by a shared `crownfall_horde_id` (UUID string) stored in each entity's PersistentData (NBT). Friendly fire between same-horde members is completely disabled via `LivingHurtEvent`.

**Spawning:** 5% chance to replace any naturally spawning Zombie. Can also be manually triggered via `/summon_crownfall_horde`.

---

## 2. HP Scaling System

All entity HP scales dynamically with Minecraft's **Local Difficulty** (`getEffectiveDifficulty()`), which ranges from 0.75 to 6.75 based on time played, moon phase, and regional difficulty.

**Formula:** `finalHP = baseHP + (baseHP × normalizedDifficulty × scaleMultiplier)`
Where `normalizedDifficulty = max(0, effectiveDifficulty - 0.75)` → range 0.0 to 6.0

| Entity | Base HP | Scale Multiplier | HP Range (Day 1 → Endgame) |
|---|---|---|---|
| **King** | 200 | 15.0 | 200 → 18,200 |
| **Elite** | 100 | 10.0 | 100 → 6,100 |
| **Vanguard (Zombie)** | 40 | 6.0 | 40 → 1,480 |
| **Archer (Skeleton)** | 22.5 | 4.0 | 22.5 → 562.5 |

The `normalizedDifficulty` value is frozen at spawn time and stored in each entity's NBT (`crownfall_difficulty`) for consistent reward scaling.

---

## 3. Stats & Roles

### King (Zombie — "Crownfall King")

- **Gear:** Full Gold Armor (Protection IV, Unbreaking III, Thorns III) + Golden Axe (Sharpness V, Fire Aspect II)
- **Phase 1 Buffs (>50% HP):** Resistance II, Strength I, Speed I, Fire Resistance, Glowing (30s)
- **Phase 2 Buffs (<50% HP):** Resistance IV, Strength III, Speed III, Fire Resistance. All skill cooldowns reduced by 50%.
- **Phase 2 Transition Effects:** Explosion particles, flame burst, Wither Spawn sound, chat message "⚠ Legion King has entered Phase 2!", summons 10-15 reinforcement Minions, campfire smoke particles.
- **Boss Bar:** Custom RED boss bar with NOTCHED_10 overlay, darkened screen, visible within 64 blocks.
- **NBT Tags:** `crownfall_horde_id`, `crownfall_role` ("king"), `crownfall_phase` (1 or 2), `crownfall_skill_cd`, `crownfall_warcry_cd`, `crownfall_difficulty`

### Elites (Witch — "Crownfall Elite")

- **HP:** Scaled (100 base, 10.0 multiplier)
- **Movement Speed:** 0.35 (faster than default)
- **Commander Aura (every 6 seconds / 120 ticks):**
  - **Buffs Horde (20 blocks):** King gets Regen II (6s), Minions get Resistance IV (6s, 80% damage reduction)
  - **Debuffs Players (15 blocks):** Scaled magic damage (4 + difficulty×3), Slowness IV (5s), Weakness II (5s), Poison II (4s), Hunger III (5s), Nausea I (4s)
  - **Visual/Sound:** Witch particles, campfire signal smoke, Witch celebrate sound
- **Follow-King AI:** Pathfinds to King every 2 seconds when idle (no attack target), if >10 blocks away
- **NBT Tags:** `crownfall_horde_id`, `crownfall_role` ("elite"), `crownfall_aura_cd`, `crownfall_difficulty`, `crownfall_king_uuid`

### Minions — Vanguard (Zombie — "Crownfall Vanguard", 30% spawn rate)

- **Gear:** Full Diamond/Netherite Armor (Protection III-IV, Unbreaking III) + Diamond/Netherite Sword (Sharpness III-V)
- **Buffs:** Permanent Speed I + Strength I
- **On-Hit (30% chance):** Applies Wither II + Poison II for 4 seconds (bypasses armor)
- **Follow-King AI:** Same as Elites
- **Drop Rates:** All equipment slots nerfed to 5%
- **NBT Tags:** `crownfall_horde_id`, `crownfall_role` ("minion"), `crownfall_king_uuid`

### Minions — Archer (Skeleton — "Crownfall Archer", 70% spawn rate)

- **Gear:** Full Diamond/Netherite Armor (Protection III-IV, Unbreaking III) + Bow (Power IV-V, Punch I-II)
- **Offhand:** 64× Instant Damage II Tipped Arrows (drop rate 0%)
- **Buffs:** Permanent Speed I
- **Follow-King AI:** Same as Elites
- **Drop Rates:** All equipment slots nerfed to 5%, offhand arrows = 0%
- **NBT Tags:** Same as Vanguard

---

## 4. King Skills (Line of Sight required, 30-block range)

All skills require the King to have **line of sight** to the target player (`king.hasLineOfSight(player)`). No casting through walls.

### Skill 1: Chain Lightning ⚡
- **Cooldown:** 100 ticks (5s) / Phase 2: 50 ticks (2.5s)
- **Target:** Nearest player >8 blocks away
- **Effect:** 3 real lightning bolts, staggered 5 ticks apart, with slight random offset
- **Damage:** Vanilla lightning damage

### Skill 2: Gravity Pull 🌀
- **Cooldown:** 160 ticks (8s) / Phase 2: 80 ticks (4s)
- **Target:** Farthest player (priority = maximum distance)
- **Effect:** 0.5s warning (portal particles + Enderman teleport sound), then pulls target towards King with velocity, deals 4♥ magic damage (bypasses armor), applies Slowness II (3s)
- **Sound:** Anvil land on impact

### Skill 3: War Cry 📢
- **Cooldown:** 300 ticks (15s) / Phase 2: 150 ticks (7.5s)
- **Priority:** Highest — always cast first if off cooldown
- **Player Debuff (20 blocks):** Slowness IV (5s), Mining Fatigue III (5s)
- **Horde Buff (30 blocks):** Strength II (8s), Speed II (8s) for all horde members
- **Visual/Sound:** Explosion + Sonic Boom particles, Ender Dragon growl

### Skill Priority Order:
1. War Cry (if off cooldown)
2. Gravity Pull or Chain Lightning (random 50/50, only if target >8 blocks away)

---

## 5. Mechanics

### King Death → Horde Wipe
- Removes Boss Bar
- Explosion + Flash particles, generic explosion sound
- Chat message: "☠ Legion King has fallen! The horde crumbles!"
- Instantly kills ALL linked horde members within 100 blocks (no chain reactions)

### Phase 2 Reinforcements
- Triggers when King HP drops below 50%
- Spawns 10-15 additional Minions (Vanguards + Archers) at King's position
- Accompanied by campfire smoke particle burst

### Follow-King AI
- All Elites and Minions pathfind towards the King every 2 seconds (40 ticks) when they have no attack target
- Only triggers if >10 blocks from King
- Uses King UUID stored in each member's NBT (`crownfall_king_uuid`)

### Friendly Fire Immunity
- All horde members sharing the same `crownfall_horde_id` cannot damage each other
- Prevents King's lightning from hitting Minions, Archer arrows from hitting Vanguards, etc.

---

## 6. Drops (Scaled by Frozen Difficulty)

Difficulty tiers: **Early** (<1.5), **Mid** (1.5-4.0), **Late** (>4.0)

### Minions
- Vanilla drops + 5% chance for each equipped enchanted gear piece
- Tipped arrows never drop (0%)

### Elites
- 1× Totem of Undying (guaranteed)
- 1× Enchanted Book (random from: Mending, Unbreaking, Efficiency, Fortune, Silk Touch, Looting, Sharpness, Protection — level scales with difficulty tier)

### King — Reward Count
| Tier | Reward Drops |
|---|---|
| Early | 2-3 |
| Mid | 4-5 |
| Late | 5-7 |

### King — Loot Pool Rates

| Tier | Common | Rare | Jackpot |
|---|---|---|---|
| Early | 85% | 15% | 0% |
| Mid | 65% | 30% | 5% |
| Late | 48% | 40% | 12% |

### King — Common Rewards (scaled by tier)
- Diamonds (4-8 → 8-16 → 16-32)
- Emeralds (4-8 → 8-16 → 16-32)
- XP Bottles (8-16 → 16-32 → 16-32)
- Gold Ingots/Blocks (4-8 Ingots → 4-8 Blocks → 8-16 Blocks)

### King — Rare Rewards
- **Early:** Iron Blocks, Diamonds, Unbreaking Book
- **Mid/Late:** 1-2 Netherite Ingots, Netherite Upgrade Template, Totem, Trident, Enchanted Golden Apple, Mending Book

### King — Jackpot Rewards (Mid/Late only)
- Elytra, Nether Star, Dragon Head, Beacon, 4-8 Netherite Ingots

---

## 7. Commands

| Command | Permission | Description |
|---|---|---|
| `/summon_crownfall_horde` | OP Level 2 | Spawns a full horde at executor's position |

---

## 8. Player Context (Design Decisions)

This mod is designed for a **2-player co-op mid-game server** with extensive modding:
- Players have **50+ hearts HP**, strong armor sets, and access to the **TACZ gun mod** for high ranged DPS
- **Puffish Skills** mod provides a skill tree that scales base attributes (ATK, HP, Speed, Jump)
- The horde is designed as a **raid-boss encounter** that forces players to:
  1. **Prioritize Elites first** — their aura provides massive damage reduction to Minions and devastating debuffs to players
  2. **Stay mobile** — King's Gravity Pull punishes stationary sniping
  3. **Manage positioning** — Line of Sight blocking negates King's skills entirely

---

## 9. NBT Data Reference

All data stored in entity `PersistentData` (survives chunk save/load):

| Key | Type | Used By | Description |
|---|---|---|---|
| `crownfall_horde_id` | String (UUID) | All | Links all members of same horde |
| `crownfall_role` | String | All | "king", "elite", or "minion" |
| `crownfall_phase` | Int | King | 1 or 2 |
| `crownfall_skill_cd` | Int | King | Lightning/Pull cooldown (ticks) |
| `crownfall_warcry_cd` | Int | King | War Cry cooldown (ticks) |
| `crownfall_aura_cd` | Int | Elite | Aura cooldown (ticks) |
| `crownfall_difficulty` | Float | King, Elite | Frozen normalized difficulty (0.0-6.0) |
| `crownfall_king_uuid` | String (UUID) | Elite, Minion | King's entity UUID for follow AI |
| `crownfall_follow_tick` | Int | Elite, Minion | Follow-King throttle counter |

---

## 10. Build & Install

```bash
# Build
cd c:\Users\asdsa\Desktop\CV\Minecraft\TemplateMod
.\gradlew.bat build

# Output
build\libs\crownfallinvasions-1.20.1-1.0.0.jar

# Install — copy to mods folder
Copy-Item "build\libs\crownfallinvasions-1.20.1-1.0.0.jar" -Destination "D:\Games\ModrinthApp\launcher_logs\profiles\Gun\mods\" -Force
```

**Dependencies:** Forge only. No external library dependencies (flib removed).

## Note for new Agent:

Ý tưởng hay đó! Đây là mấy hướng mình nghĩ cho v1.1:

---

## 🔥 Nether Horde — "Crownfall Inferno"

**Trigger:** 5% khi Wither Skeleton spawn tự nhiên trong Nether.

| Role | Mob | Tên |
|---|---|---|
| **King** | Wither Skeleton | Inferno King |
| **Elite** | Blaze (×2) | Inferno Elite |
| **Vanguard** | Wither Skeleton | Inferno Vanguard |
| **Marksman** | Piglin + Crossbow | Inferno Marksman |

**King Skills (Fire theme):**
1. **Fire Rain** — Triệu hồi 5-8 quả cầu lửa Ghast rơi xuống từ trên cao vào vị trí player
2. **Magma Surge** — Spawn 4-6 Magma Cube lớn bao vây player (ép player di chuyển)
3. **Inferno Cry** — Debuff player Fire Resistance bị xóa + đốt 10s, buff horde Fire Resistance

**Elite Aura:** Fire Resistance cho horde + đốt cháy player liên tục + Slow (lava-like)

**Đặc biệt:** Wither Skeleton Vanguard **tự nhiên đã gây Wither on-hit** → không cần code thêm, nhưng có thể nâng lên Wither III

**Loot Nether-themed:** Wither Skeleton Skulls, Ancient Debris, Netherite Scrap, Nether Star

---

## 🟣 End Horde — "Crownfall Void"

**Trigger:** 5% khi Enderman spawn tự nhiên trong The End.

| Role | Mob | Tên |
|---|---|---|
| **King** | Zombie (End-themed) | Void King |
| **Elite** | Shulker (×2) | Void Elite |
| **Swarm** | Endermite (×30-50) | Void Parasite |
| **Phantom** | Phantom (×8-12) | Void Wraith |

**King Skills (Teleport/Void theme):**
1. **Void Scatter** — Teleport ngẫu nhiên tất cả player trong 20 blocks ra các hướng khác nhau (tách đội, cực kỳ nguy hiểm trên End islands)
2. **Shulker Barrage** — Spawn 5-8 Shulker Bullet entity nhắm vào player (Levitation = rơi xuống void)
3. **Void Collapse** — AoE Gravity Pull — kéo TẤT CẢ player về vị trí King cùng lúc + Blindness

**Elite Aura (Shulker):** Bắn Levitation bullet tự động + Aura cho horde Resistance + debuff player Levitation II (cực nguy hiểm ở End)

**Đặc biệt:** Endermite swarm = HP thấp nhưng số lượng cực lớn (30-50 con), tạo cảm giác bị bao vây. Phantom bay trên đầu bắn xuống.

**Loot End-themed:** Dragon's Breath, Shulker Shell, Elytra (jackpot), End Crystal, Chorus Fruit

---

## ⚖️ v1.1 Balance Changes

| Change | Lý do |
|---|---|
| King Chain Lightning → `setVisualOnly(true)` nếu trúng horde member | Fix sét đánh đồng đội |
| Reinforcement spawn offset rộng hơn (±8 thay vì ±5) | Tránh spawn chồng lên King |
| Thêm `/crownfall_config` command chỉnh spawn rate | Tiện test/điều chỉnh |
| Kill Counter (NBT trên player) | Track bao nhiêu horde đã hạ |

---

## 🎯 Gợi ý roadmap

| Version | Nội dung |
|---|---|
| **v1.0** ✅ | Overworld Horde (hiện tại) |
| **v1.1** | Nether Horde + Balance fix |
| **v1.2** | End Horde + Kill Counter |
| **v1.3** | Boss Horde (Warden-tier, cực hiếm, loot siêu khủng) |

---

