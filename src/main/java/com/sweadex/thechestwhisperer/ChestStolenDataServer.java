package com.sweadex.thechestwhisperer;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;

import java.util.Set;

public class ChestStolenDataServer {
    private static final String ID = "stolen_chests";

    public static StolenChestSavedData getData(ServerLevel level) {
        return level.getDataStorage().computeIfAbsent(
                StolenChestSavedData::load,
                StolenChestSavedData::new,
                ID
        );
    }

    public static void setChestStolen(ServerLevel level, BlockPos pos, boolean stolen) {
        getData(level).setStolen(pos, stolen);
    }

    public static boolean isStolen(ServerLevel level, BlockPos pos) {
        return getData(level).isStolen(pos);
    }

    public static Set<BlockPos> getAllStolen(ServerLevel level) {
        return getData(level).getAll();
    }
}
