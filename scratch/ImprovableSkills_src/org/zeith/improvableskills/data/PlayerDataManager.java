/*
 * Decompiled with CFR 0.152.
 * 
 * Could not load the following classes:
 *  net.minecraft.nbt.CompoundTag
 *  net.minecraft.nbt.NbtIo
 *  net.minecraft.server.MinecraftServer
 *  net.minecraft.server.level.ServerPlayer
 *  net.minecraft.world.entity.player.Player
 *  net.minecraftforge.common.util.FakePlayer
 *  net.minecraftforge.event.TickEvent$Phase
 *  net.minecraftforge.event.TickEvent$PlayerTickEvent
 *  net.minecraftforge.event.TickEvent$ServerTickEvent
 *  net.minecraftforge.event.entity.player.PlayerEvent$LoadFromFile
 *  net.minecraftforge.event.entity.player.PlayerEvent$PlayerRespawnEvent
 *  net.minecraftforge.event.entity.player.PlayerEvent$SaveToFile
 *  net.minecraftforge.eventbus.api.SubscribeEvent
 *  net.minecraftforge.fml.common.Mod$EventBusSubscriber
 *  org.zeith.hammerlib.event.player.PlayerLoadedInEvent
 */
package org.zeith.improvableskills.data;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Function;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtIo;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.zeith.hammerlib.event.player.PlayerLoadedInEvent;
import org.zeith.improvableskills.ImprovableSkills;
import org.zeith.improvableskills.SyncSkills;
import org.zeith.improvableskills.api.PlayerSkillData;
import org.zeith.improvableskills.net.NetSkillCalculator;
import org.zeith.improvableskills.net.PacketSyncSkillData;

@Mod.EventBusSubscriber
public class PlayerDataManager {
    private static final Map<UUID, PlayerSkillData> DATAS = new HashMap<UUID, PlayerSkillData>();
    private static final ThreadLocal<Player> LPLAYER = ThreadLocal.withInitial(() -> null);

    public static void handleDataSafely(Player player, Consumer<PlayerSkillData> acceptor) {
        PlayerSkillData psd = PlayerDataManager.getDataFor(player);
        if (psd != null) {
            acceptor.accept(psd);
        }
    }

    public static <T> T handleDataSafely(Player player, Function<PlayerSkillData, T> acceptor, T defaultValue) {
        PlayerSkillData psd = PlayerDataManager.getDataFor(player);
        if (psd != null) {
            return acceptor.apply(psd);
        }
        return defaultValue;
    }

    public static PlayerSkillData getDataFor(Player player) {
        if (player == null || player instanceof FakePlayer) {
            return null;
        }
        if (player.m_9236_().f_46443_) {
            if (player.m_7578_()) {
                return SyncSkills.getData();
            }
            return PlayerSkillData.deserialize(player, player.getPersistentData().m_128469_("ImprovableSkillsData"));
        }
        LPLAYER.set(player);
        PlayerSkillData data = PlayerDataManager.getDataFor(player.m_36316_().getId());
        if (data != null && data.getPlayer() != player) {
            data = PlayerSkillData.deserialize(player, data.serializeNBT());
            DATAS.put(player.m_36316_().getId(), data);
        }
        return data != null ? data.toCurrent(player) : null;
    }

    private static PlayerSkillData getDataFor(UUID id) {
        CompoundTag data;
        ServerPlayer mp;
        if (id == null) {
            return null;
        }
        if (DATAS.containsKey(id)) {
            return DATAS.get(id);
        }
        Player epl = LPLAYER.get();
        if (epl instanceof ServerPlayer && (mp = (ServerPlayer)epl).getPersistentData().m_128441_("ImprovableSkillsData") && !(data = mp.getPersistentData().m_128469_("ImprovableSkillsData")).m_128456_()) {
            PlayerSkillData dat = PlayerSkillData.deserialize((Player)mp, data);
            DATAS.put(mp.m_20148_(), dat);
        }
        return null;
    }

    @SubscribeEvent
    public static void playerTick(TickEvent.PlayerTickEvent e) {
        if (e.phase == TickEvent.Phase.START) {
            PlayerDataManager.handleDataSafely(e.player, PlayerSkillData::handleTick);
        }
    }

    @SubscribeEvent
    public static void playerLoadedIn(PlayerLoadedInEvent e) {
        ImprovableSkills.LOG.info("Sending skill data to {} ({})", (Object)e.getEntity().m_36316_().getName(), (Object)e.getEntity().m_36316_().getId());
        PlayerDataManager.handleDataSafely((Player)e.getEntity(), PlayerSkillData::sync);
        NetSkillCalculator.pack().build().sendTo(e.getEntity());
    }

    @SubscribeEvent
    public static void respawn(PlayerEvent.PlayerRespawnEvent e) {
        Player player = e.getEntity();
        if (player instanceof ServerPlayer) {
            ServerPlayer mp = (ServerPlayer)player;
            PacketSyncSkillData.sync(mp);
        }
    }

    @SubscribeEvent
    public static void serverTick(TickEvent.ServerTickEvent e) {
        if (e.phase == TickEvent.Phase.END) {
            MinecraftServer mcs = e.getServer();
            DATAS.keySet().removeIf(uuid -> {
                ServerPlayer mp = mcs.m_6846_().m_11259_(uuid);
                if (mp == null) {
                    return true;
                }
                PlayerSkillData data = DATAS.get(uuid);
                data.player = mp;
                return false;
            });
        }
    }

    @SubscribeEvent
    public static void loadPlayerFromFile(PlayerEvent.LoadFromFile e) {
        CompoundTag nbt;
        block7: {
            nbt = null;
            try {
                File mainFile = e.getPlayerFile(".is3.dat");
                if (mainFile.isFile()) {
                    nbt = NbtIo.m_128939_((InputStream)new FileInputStream(mainFile));
                }
            }
            catch (Exception error) {
                ImprovableSkills.LOG.warn("Failed to load player data for {}", (Object)e.getEntity().m_7755_());
                error.printStackTrace();
                File oldFile = e.getPlayerFile(".is3.dat_old");
                if (!oldFile.isFile()) break block7;
                ImprovableSkills.LOG.warn("Detected old data file forp layer {}, trying to read...", (Object)e.getEntity().m_7755_());
                try {
                    nbt = NbtIo.m_128939_((InputStream)new FileInputStream(oldFile));
                }
                catch (Exception error2) {
                    ImprovableSkills.LOG.warn("Failed to load player backup data for {}", (Object)e.getEntity().m_7755_());
                    error2.printStackTrace();
                }
            }
        }
        if (nbt != null) {
            DATAS.put(UUID.fromString(e.getPlayerUUID()), PlayerSkillData.deserialize(e.getEntity(), nbt));
        } else {
            DATAS.put(UUID.fromString(e.getPlayerUUID()), new PlayerSkillData(e.getEntity()));
        }
    }

    @SubscribeEvent
    public static void savePlayerToFile(PlayerEvent.SaveToFile e) {
        PlayerSkillData data = PlayerDataManager.getDataFor(e.getEntity());
        if (data == null) {
            return;
        }
        try {
            CompoundTag nbt = data.serializeNBT();
            File tmp = e.getPlayerFile(".is3.dat.tmp");
            File main = e.getPlayerFile(".is3.dat");
            File mainOld = e.getPlayerFile(".is3.dat_old");
            NbtIo.m_128947_((CompoundTag)nbt, (OutputStream)new FileOutputStream(tmp));
            if (mainOld.isFile()) {
                mainOld.delete();
            }
            if (main.exists()) {
                main.renameTo(mainOld);
            }
            tmp.renameTo(main);
        }
        catch (Exception var5) {
            ImprovableSkills.LOG.warn("Failed to save player data for {}", (Object)e.getEntity().m_7755_());
        }
    }
}

