package com.thepinkkitten.crownfallinvasions;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.saveddata.SavedData;

public class CrownfallWorldData extends SavedData {

    private int globalKillCount = 0;

    public static CrownfallWorldData create() {
        return new CrownfallWorldData();
    }

    public static CrownfallWorldData load(CompoundTag tag) {
        CrownfallWorldData data = create();
        data.globalKillCount = tag.getInt("global_kill_count");
        return data;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        tag.putInt("global_kill_count", globalKillCount);
        return tag;
    }

    public int getGlobalKillCount() {
        return globalKillCount;
    }

    public void setGlobalKillCount(int count) {
        this.globalKillCount = count;
        this.setDirty();
    }

    public void addKill() {
        this.globalKillCount++;
        this.setDirty();
    }

    public static CrownfallWorldData get(ServerLevel level) {
        // Data is tied to the Overworld so it's truly global across dimensions
        ServerLevel overworld = level.getServer().getLevel(net.minecraft.world.level.Level.OVERWORLD);
        if (overworld == null) overworld = level;

        return overworld.getDataStorage().computeIfAbsent(
                CrownfallWorldData::load,
                CrownfallWorldData::create,
                "crownfall_invasions_data"
        );
    }
}
