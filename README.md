<div align="center">
  <img src="https://i.imgur.com/placeholder-Crownfall-Invasions-banner.png" alt="Crownfall Invasions Banner" width="100%" />
  <h1>🗡️ Crownfall Invasions: The Ultimate Vanilla+ Raid Experience 🛡️</h1>
  
  <p>
    <b>Prepare for war. The longer you survive, the harder they strike.</b>
  </p>

  <p>
    <a href="https://files.minecraftforge.net/"><img src="https://img.shields.io/badge/Forge-1.20.1-orange.svg?style=for-the-badge&logo=minecraft" alt="Forge version"></a>
    <img src="https://img.shields.io/badge/License-MIT-blue.svg?style=for-the-badge" alt="License">
    <a href="https://modrinth.com/"><img src="https://img.shields.io/badge/Modrinth-Available-green.svg?style=for-the-badge&logo=modrinth" alt="Modrinth"></a>
    <a href="https://curseforge.com/"><img src="https://img.shields.io/badge/CurseForge-Available-red.svg?style=for-the-badge&logo=curseforge" alt="CurseForge"></a>
  </p>
</div>

---

## 📖 What is Crownfall Invasions?

Tired of aimless, brainless zombies wandering around your base? **Crownfall Invasions** completely revolutionizes PvE in Minecraft. It replaces standard, boring mob spawns with **organized, highly lethal military raids** executed by vanilla-like hostile mobs. 

Instead of fighting 1 or 2 zombies, you will face a structured army led by a Boss, supported by Elites, and swarmed by Minions. They use advanced AI, phase mechanics, and terrifying coordinated attacks to breach your defenses. 

If you want a true survival challenge where your gear actually matters, this is the mod for you.

---

## 🔥 Key Features

### 📖 Bestiary & Skills Guide

The Horde is a structured army with complex AI and abilities. Here is a detailed breakdown of every unit, their skills, and how to identify them:

#### 👑 The Crownfall King (Boss)
The leader of the invasion. A massive Zombie or Husk wearing full Gold Armor and a Glowing effect. He possesses hidden Netherite-tier base stats and unbreakable gear.

**Phase 1 Abilities (100% - 50% HP):**
*   **Chain Lightning:**
    *   *Trigger:* Used frequently if players are in melee range (< 8 blocks), or randomly at range.
    *   *Effect:* Strikes the target with multiple lightning bolts (number of strikes scales with global level). Friendly fire is disabled, so it won't hurt his minions.
    *   *Indicator:* Actual lightning strikes hitting you.
*   **Gravity Pull:**
    *   *Trigger:* Used against ranged players (> 8 blocks away).
    *   *Effect:* Forcibly yanks the player towards the King. At higher levels (Lv. 10+), it also inflicts Blindness for 3 seconds.
    *   *Indicator:* End Rod particles forming a vortex towards the player + Enderman teleport sound.
*   **Commanding War Cry:**
    *   *Trigger:* Used periodically.
    *   *Effect:* Heals all nearby minions for 10% of their max HP and grants them Strength and Speed buffs.
    *   *Indicator:* Ender Dragon growl sound + Angry Villager particles around the King + Heart particles on minions.

**Phase 2: Enraged (Under 50% HP):**
*   *Transition Indicator:* A massive fiery explosion, Wither spawn sound, and a chat warning: `"⚠ Crownfall King has entered Phase 2!"`.
*   *Stat Boost:* The King gains **Resistance IV** (80% damage reduction), **Strength III**, and **Speed III**.
*   *Frenzy:* All skill cooldowns are cut in half. He attacks twice as fast.
*   *Black Hole Upgrade:* Gravity Pull is upgraded to **Black Hole**, which deals heavy magic damage upon pulling you and applies longer Blindness.
*   *Reinforcements:* Immediately summons a wave of 10-15 Minions.
*   *Meat Shield (CRITICAL):* Summons 3 elite "Royal Guards". **As long as ANY Royal Guard is alive, the King takes 80% LESS damage from projectiles (Bullets/Arrows).** You must kill the guards first or use melee/magic!
    *   *Indicator:* Projectiles hitting the King will spawn Crit particles, indicating the damage was heavily blocked.

#### 🔮 The Elites (Witches)
The support casters of the Horde. They don't throw standard potions; they emit deadly AoE auras.
*   **Aura of Decay (Offensive):**
    *   *Trigger:* Player gets within 15 blocks of an Elite.
    *   *Effect:* Deals continuous magic damage and inflicts a devastating combo of debuffs: **Nausea (15s), Weakness II (10s), Hunger IV (10s), and Poison II (8s)**.
    *   *Indicator:* Witch cackling sound, an Instant Damage II Lingering Cloud at the Witch's feet, and your screen distorting wildly.
*   **Aura of Vitality (Support):**
    *   *Trigger:* Passive.
    *   *Effect:* Constantly heals the King and nearby Vanguard minions.
    *   *Indicator:* Happy Villager (green star) particles floating around healed mobs.

#### ⚔️ The Vanguard (Minions)
The grunts. Zombies and Skeletons wearing Diamond or Netherite armor. Their gear enchantments scale infinitely with the global level (up to Sharpness X, Protection X, Power X).
*   **Zombie Vanguard (Melee):**
    *   *Skill (Infectious Strike):* Every successful melee hit inflicts **Wither II and Poison II** for 5 seconds.
    *   *Indicator:* Black and green potion particles swirling around you after being hit.
*   **Skeleton Archer (Ranged):**
    *   *Skill (Toxic Volley):* They do not shoot normal arrows. Every arrow shot is an **Instant Damage II** tipped arrow.
    *   *Indicator:* Purple potion trails following their arrows.
*   **Royal Guards (Phase 2 Only):**
    *   *Visual:* Zombies in full Netherite with a gold `Royal Guard` nametag.
    *   *Effect:* As mentioned above, their mere presence gives the King an 80% Projectile Shield. Target them immediately!

### 📈 Hybrid Endless Scaling (The Core Mechanic)
Crownfall Invasions uses an **Endless Hybrid Scaling System** that combines Minecraft's vanilla *Local Difficulty* with a global server-wide kill counter.

* **What does this mean?** The base strength of the Horde depends on how long you've stayed in the chunk (Local Difficulty). On top of that, every time a Crownfall King is defeated, the global kill count increments. This counter adds an *endless* multiplier to all future invasions across the entire server.
* **Level 1 Survival:** A King spawning in a fresh chunk (0 kills) might start with ~280 HP. It's tough, but manageable.
* **Level 20+ Endgame:** If you stay in your megabase for weeks (Max Local Difficulty) AND the server has defeated 20 Kings, the exact same King will spawn with **over 1,000+ HP**, massive AoE debuffs, and Sharpness X / Protection X gear.

**📊 Example Scaling Progression (Assuming Hard Difficulty):**

| Boss Level | Global Kills | King's Health | King's Skills | Minion Enchantments | Loot Quality |
| :---: | :---: | :--- | :--- | :--- | :--- |
| **Lv. 0** | 0 | ~280 HP | 3 Lightning Strikes | Prot 3-4, Sharpness 3-5 | Basic (Emeralds, XP) |
| **Lv. 10** | 10 | ~680 HP | 6 Strikes, Blindness Pull | Prot 8, Sharpness 8 | High (Totems, G-Apples) |
| **Lv. 20** | 20 | ~1,080 HP | 9 Strikes, Huge Warcry | Prot 10 (MAX), Sharp X (MAX)| Jackpot (Nether Stars, Elytras) |
| **Lv. 50** | 50 | ~2,280 HP | 19 Strikes, Map-wide Warcry | Prot 10 (MAX), Sharp X (MAX)| Jackpot (Guaranteed Rares) |

* **The Risk vs. Reward:** You cannot out-gear the mod. As you get stronger, the Horde adapts and grows exponentially stronger without any vanilla hard caps.

### 💎 Risk vs. Reward Loot System
Fighting the Horde is highly rewarding. The loot table dynamically scales with the horde's level. Designed with modpacks in mind (like TACZ and Puffish Skills), enchanted books have been removed in favor of pure resources and consumables:
* **Common Drops:** Earn Emeralds, Diamonds, and massive amounts of **Experience Bottles** (perfect for leveling skills or repairing gear).
* **Elite Drops:** Guaranteed Totems of Undying and a shower of XP Bottles and Golden Apples.
* **Late Game (Max Tier):** Defeating a Late-Game King grants a high chance of a **Jackpot Drop**, which includes extremely rare vanilla items like **Netherite Ingots, Elytras, Nether Stars, and Dragon Heads**.

### 🐉 Vanilla Boss Enhancements
The Crownfall curse extends beyond the Horde! Vanilla Bosses (The **Ender Dragon** and **Wither**) are now affected by the global kill count:
* **Endless HP Scaling:** Like the King, their health scales infinitely based on the number of Crownfall Kings defeated across the server.
* **Innate Power:** Upon spawning, Vanilla Bosses are permanently buffed with **Strength III, Speed II, Resistance II, and Fire Resistance**. Be prepared for a grueling, high-stakes battle!

### 🌌 Dimension Invasions (Upcoming Update!)
*(Currently in active development)* 
Soon, the Horde will follow you into the abyss:
* **The Infernal Legion (Nether):** Wither Skeleton Kings summoning Meteor Strikes, and Blazes granting Fire Resistance to Piglin Brutes.
* **The Void Swarm (The End):** Enderman Kings casting Gravity Wells, and Shulkers riding Endermen like mobile anti-air tanks.

---

## 🎮 How to Play

### 1. Triggering a Raid
Hordes can spawn naturally! Every time a Zombie or Skeleton spawns in the world, there is a **5% chance** it triggers a full-scale Crownfall invasion instead. Be always on your guard.

Alternatively, Server Admins or players with cheats enabled can trigger a raid or manage the difficulty via commands:
```mcfunction
/summon_crownfall_horde
/crownfall_config get_kills
/crownfall_config set_kills <amount>
```

### 2. Survival Tactics
* **Assassinate the Elites:** Elites constantly heal the King and debuff you. Take them out first.
* **Watch the King's Phase:** When the King drops below 50% HP, an explosion occurs. He gains immense buffs and summons massive reinforcements. Fall back and use ranged weapons!
* **Beware Friendly Fire Immunity:** The Horde cannot hurt each other. You cannot trick Skeletons into shooting Zombies. They are a united army.

---

## 🛠️ Configuration & Setup

Upon launching the game, a configuration file will be generated in your `config` folder. Modpack creators have ultimate control:
* Tweak the `Base HP` and `Scale Multipliers` for every mob tier.
* Adjust the natural spawn probability.
* Modify the cooldowns of the King's devastating abilities.

---

## 📥 Installation

1. Install **Minecraft Forge** for version `1.20.1`.
2. Download the `Crownfall Invasions` `.jar` file.
3. Drop the file into your `.minecraft/mods` directory.
4. Launch the game and prepare your defenses.

---

## 🤝 Community & Support

* **Issues & Bugs:** Found a glitch? Report it on our [GitHub Issues page](#).
* **Discord:** Join our community to share your base-defense strategies and suggest new features! [*(Discord Link Placeholder)*](#)

---
<div align="center">
  <i>"They don't just spawn. They invade."</i>
</div>
