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

### 👑 The Mob Hierarchy
The Horde is not just a random group of mobs; it is a structured army. All units display their current level directly in their name tags and boss bars (e.g., **[Lv. 15]**).
* **The King (Boss):** A massive, heavily armored leader boasting hundreds of HP. Despite wearing a stylish Gold Armor set, he possesses hidden Netherite-tier base attributes and unbreakable gear. When pushed below 50% HP, the King becomes enraged, unleashing level-scaling skills like **Chain Lightning**, **Gravity Pull** (causing Blindness at higher levels), and **War Cries** that buff the entire legion.
* **The Elites (Specialists):** Advanced units (like Witches) that don't just attack—they cast AoE buffs on their allies and deadly debuff auras (Poison, Weakness, Slowness) on players. 
* **The Vanguard (Minions):** Armored grunts equipped with tipped arrows and enchanted weapons. Their gear enchantments scale endlessly with the global level, capable of bypassing vanilla limits (up to Sharpness X, Protection X).

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
