/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.ListTag
 *  net.minecraft.nbt.StringTag
 *  net.minecraft.nbt.Tag
 *  net.minecraft.network.chat.Component
 *  net.minecraft.resources.ResourceLocation
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraft.world.level.Level
 *  net.minecraftforge.common.util.INBTSerializable
 *  net.minecraftforge.registries.IForgeRegistry
 *  org.apache.logging.log4j.LogManager
 *  org.apache.logging.log4j.Logger
 *  org.zeith.hammerlib.net.IPacket
 *  org.zeith.hammerlib.net.Network
 */
package org.zeith.improvableskills.api;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.util.INBTSerializable;
import net.minecraftforge.registries.IForgeRegistry;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.zeith.hammerlib.net.IPacket;
import org.zeith.hammerlib.net.Network;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.registry.PlayerAbilityBase;
import org.zeith.improvableskills.api.registry.PlayerSkillBase;
import org.zeith.improvableskills.cfg.ConfigsIS;
import org.zeith.improvableskills.net.PacketSyncSkillData;

public class PlayerSkillData
implements INBTSerializable<CompoundTag> {
    public static final Logger LOG = LogManager.getLogger((String)"ImprovableSkills-IO");
    public Player player;
    private final Map<ResourceLocation, Short> stats = new HashMap<ResourceLocation, Short>();
    private final List<ResourceLocation> skillScrolls = new ArrayList<ResourceLocation>();
    private final List<ResourceLocation> disabledSkills = new ArrayList<ResourceLocation>();
    private final List<ResourceLocation> abilities = new ArrayList<ResourceLocation>();
    public BigInteger storageXp = BigInteger.ZERO;
    public CompoundTag persistedData = new CompoundTag();
    public boolean hasCraftedSkillBook = false;
    public boolean enableXPBank = true;
    public boolean hasCraftedSkillBookPrev = false;
    public float enchantPower = 0.0f;
    public boolean cowboy;
    public boolean magnetism;
    public float magnetismRange;
    public boolean autoXpBank;
    public int autoXpBankThreshold;
    private boolean isInIO = false;
    public ResourceLocation prevDim;

    public PlayerSkillData(Player player) {
        this.player = player;
    }

    public void handleTick() {
        long end;
        boolean xpBankShown;
        long start = System.currentTimeMillis();
        if (this.player == null || this.player.m_5833_()) {
            return;
        }
        Level level = this.player.m_9236_();
        HashMap<ResourceLocation, Long> updates = new HashMap<ResourceLocation, Long>();
        IForgeRegistry<PlayerSkillBase> skillReg = ImprovableSkills.SKILLS();
        IForgeRegistry<PlayerAbilityBase> abilsReg = ImprovableSkills.ABILITIES();
        for (PlayerSkillBase value : skillReg.getValues()) {
            long start0 = System.currentTimeMillis();
            ResourceLocation rn = value.getRegistryName();
            value.tick(this, !this.disabledSkills.contains(rn));
            updates.put(rn, System.currentTimeMillis() - start0);
        }
        for (int i = 0; i < this.abilities.size(); ++i) {
            long start0 = System.currentTimeMillis();
            ResourceLocation key = this.abilities.get(i);
            PlayerAbilityBase value = (PlayerAbilityBase)abilsReg.getValue(key);
            if (value == null) continue;
            value.tick(this);
            updates.put(value.getRegistryName(), System.currentTimeMillis() - start0);
        }
        if (level.f_46443_ && !Objects.equals(this.prevDim, level.m_46472_().m_135782_())) {
            this.prevDim = level.m_46472_().m_135782_();
            this.requestSync();
        }
        if (!level.f_46443_ && this.hasCraftedSkillBookPrev != this.hasCraftedSkillBook && !this.hasCraftedSkillBookPrev) {
            this.player.m_213846_((Component)Component.m_237115_((String)"chat.improvableskills.guide"));
            this.hasCraftedSkillBookPrev = true;
            this.sync();
        }
        this.hasCraftedSkillBookPrev = this.hasCraftedSkillBook;
        if (!level.f_46443_ && this.enableXPBank != (xpBankShown = ConfigsIS.xpBank)) {
            this.enableXPBank = true;
            this.sync();
        }
        if ((end = System.currentTimeMillis()) - start > 50L) {
            ImprovableSkills.LOG.warn("Skill tick took too long! ({} ms, expected <50 ms!). Time map: {}", (Object)(end - start), updates.entrySet().stream().sorted(Comparator.comparingLong(Map.Entry::getValue).reversed()).toList());
        }
    }

    public void sync() {
        Player player;
        if (!this.isInIO && (player = this.player) instanceof ServerPlayer) {
            ServerPlayer sp = (ServerPlayer)player;
            PacketSyncSkillData.sync(sp);
        }
    }

    public void requestSync() {
        if (this.player != null && this.player.m_7578_()) {
            Network.sendToServer((IPacket)new PacketSyncSkillData());
        }
    }

    public short getSkillLevel(PlayerSkillBase stat) {
        return this.stats.getOrDefault(stat.getRegistryName(), (short)0);
    }

    public float getSkillProgress(PlayerSkillBase skill) {
        return skill.getLevelProgress(this.getSkillLevel(skill));
    }

    public void setSkillLevel(PlayerSkillBase stat, Number lvl) {
        this.setSkillLevelNoSync(stat, lvl);
        this.sync();
    }

    public void setSkillLevelNoSync(PlayerSkillBase stat, Number lvl) {
        this.stats.put(stat.getRegistryName(), lvl.shortValue());
    }

    public boolean hasCraftedSkillsBook() {
        return this.hasCraftedSkillBook;
    }

    public boolean atTickRate(int i) {
        return i > 0 && this.player.f_19797_ % i == 0;
    }

    public boolean hasAbility(PlayerAbilityBase ability) {
        return ability != null && this.abilities.contains(ability.getRegistryName());
    }

    public boolean hasSkillScroll(PlayerSkillBase skill) {
        return skill != null && this.skillScrolls.contains(skill.getRegistryName());
    }

    public boolean unlockAbility(PlayerAbilityBase ability, boolean sync) {
        if (ability != null && this.player != null && !this.player.m_9236_().f_46443_ && !this.abilities.contains(ability.getRegistryName())) {
            this.abilities.add(ability.getRegistryName());
            ability.onUnlocked(this);
            if (sync) {
                this.sync();
            }
            return true;
        }
        return false;
    }

    public boolean unlockSkillScroll(PlayerSkillBase skill, boolean sync) {
        if (skill != null && skill.getScrollState().hasScroll() && this.player != null && !this.player.m_9236_().f_46443_ && !this.skillScrolls.contains(skill.getRegistryName())) {
            this.skillScrolls.add(skill.getRegistryName());
            skill.onUnlocked(this);
            if (sync) {
                this.sync();
            }
            return true;
        }
        return false;
    }

    public boolean lockAbility(PlayerAbilityBase ability, boolean sync) {
        if (ability != null && this.player != null && !this.player.m_9236_().f_46443_ && this.abilities.remove(ability.getRegistryName())) {
            if (sync) {
                this.sync();
            }
            return true;
        }
        return false;
    }

    public boolean lockSkillScroll(PlayerSkillBase skill, boolean sync) {
        if (skill != null && this.player != null && !this.player.m_9236_().f_46443_ && this.skillScrolls.remove(skill.getRegistryName())) {
            if (sync) {
                this.sync();
            }
            return true;
        }
        return false;
    }

    public boolean isSkillActive(PlayerSkillBase skill) {
        return !this.disabledSkills.contains(skill.getRegistryName());
    }

    public void setSkillState(PlayerSkillBase skill, boolean active) {
        if (active) {
            if (this.disabledSkills.remove(skill.getRegistryName())) {
                this.sync();
            }
        } else if (!this.disabledSkills.contains(skill.getRegistryName())) {
            this.disabledSkills.add(skill.getRegistryName());
            this.sync();
        }
    }

    public int getAbilityCount() {
        return this.abilities.size();
    }

    public Player getPlayer() {
        if (SyncSkills.is(this)) {
            return ImprovableSkills.PROXY.getClientPlayer();
        }
        return this.player;
    }

    public PlayerSkillData toCurrent(Player playerReference) {
        if (this.player != playerReference) {
            this.player = playerReference;
        }
        return this;
    }

    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        this.persistedData.m_128359_("BankXP", this.storageXp.toString(36));
        this.persistedData.m_128379_("SkillBookCrafted", this.hasCraftedSkillBook);
        this.persistedData.m_128379_("PrevSkillBookCrafted", this.hasCraftedSkillBookPrev);
        this.persistedData.m_128379_("Cowboy", this.cowboy);
        this.persistedData.m_128379_("Magnetism", this.magnetism);
        this.persistedData.m_128350_("MagnetismRange", this.magnetismRange);
        this.persistedData.m_128379_("AutoXPBank", this.autoXpBank);
        this.persistedData.m_128405_("AutoXPBankThreshold", this.autoXpBankThreshold);
        nbt.m_128379_("EnableXPBank", this.enableXPBank);
        IForgeRegistry<PlayerSkillBase> reg = ImprovableSkills.SKILLS();
        nbt.m_128365_("Persisted", (Tag)this.persistedData);
        nbt.m_128350_("EnchantPower", this.enchantPower);
        ListTag list = new ListTag();
        for (ResourceLocation skillKey : this.stats.keySet()) {
            PlayerSkillBase stat = (PlayerSkillBase)reg.getValue(skillKey);
            if (stat == null) {
                LOG.warn("[SAVE] Skill '" + skillKey + "' wasn't found. Maybe you removed the addon? Skipping unregistered skill.");
                continue;
            }
            CompoundTag tag = new CompoundTag();
            tag.m_128359_("Id", stat.getRegistryName().toString());
            tag.m_128376_("Lvl", this.getSkillLevel(stat));
            list.add((Object)tag);
        }
        nbt.m_128365_("Levels", (Tag)list);
        list = new ListTag();
        for (ResourceLocation scroll : this.skillScrolls) {
            list.add((Object)StringTag.m_129297_((String)scroll.toString()));
        }
        nbt.m_128365_("Scrolls", (Tag)list);
        list = new ListTag();
        for (ResourceLocation scroll : this.disabledSkills) {
            list.add((Object)StringTag.m_129297_((String)scroll.toString()));
        }
        nbt.m_128365_("DisabledSkills", (Tag)list);
        list = new ListTag();
        for (ResourceLocation scroll : this.abilities) {
            list.add((Object)StringTag.m_129297_((String)scroll.toString()));
        }
        nbt.m_128365_("Abilities", (Tag)list);
        return nbt;
    }

    public void deserializeNBT(CompoundTag nbt) {
        int i;
        this.isInIO = true;
        this.enableXPBank = nbt.m_128471_("EnableXPBank");
        IForgeRegistry<PlayerSkillBase> reg = ImprovableSkills.SKILLS();
        ListTag lvls = nbt.m_128437_("Levels", 10);
        for (int i2 = 0; i2 < lvls.size(); ++i2) {
            CompoundTag tag = lvls.m_128728_(i2);
            String sstat = tag.m_128461_("Id");
            PlayerSkillBase stat = (PlayerSkillBase)reg.getValue(new ResourceLocation(sstat));
            if (stat == null) {
                LOG.warn("[LOAD] Skill '" + sstat + "' wasn't found. Maybe you removed the addon? Skipping unregistered skill.");
                continue;
            }
            this.setSkillLevel(stat, tag.m_128448_("Lvl"));
        }
        this.skillScrolls.clear();
        this.disabledSkills.clear();
        this.abilities.clear();
        ListTag list = nbt.m_128437_("Scrolls", 8);
        for (i = 0; i < list.size(); ++i) {
            this.skillScrolls.add(new ResourceLocation(list.m_128778_(i)));
        }
        list = nbt.m_128437_("DisabledSkills", 8);
        for (i = 0; i < list.size(); ++i) {
            this.disabledSkills.add(new ResourceLocation(list.m_128778_(i)));
        }
        list = nbt.m_128437_("Abilities", 8);
        for (i = 0; i < list.size(); ++i) {
            this.abilities.add(new ResourceLocation(list.m_128778_(i)));
        }
        this.enchantPower = nbt.m_128457_("EnchantPower");
        this.persistedData = nbt.m_128469_("Persisted");
        if (this.persistedData.m_128425_("BankXP", 8)) {
            try {
                this.storageXp = new BigInteger(this.persistedData.m_128461_("BankXP"), 36);
            }
            catch (Throwable err) {
                this.storageXp = BigInteger.ZERO;
            }
        }
        this.hasCraftedSkillBook = this.persistedData.m_128471_("SkillBookCrafted");
        this.hasCraftedSkillBookPrev = this.persistedData.m_128471_("PrevSkillBookCrafted");
        this.cowboy = this.persistedData.m_128471_("Cowboy");
        this.magnetism = this.persistedData.m_128471_("Magnetism");
        this.magnetismRange = this.persistedData.m_128457_("MagnetismRange");
        this.autoXpBank = this.persistedData.m_128471_("AutoXPBank");
        this.autoXpBankThreshold = this.persistedData.m_128451_("AutoXPBankThreshold");
        this.isInIO = false;
    }

    public static PlayerSkillData deserialize(Player player, CompoundTag nbt) {
        PlayerSkillData data = new PlayerSkillData(player);
        data.prevDim = player.m_9236_().m_46472_().m_135782_();
        data.deserializeNBT(nbt);
        return data;
    }
}

