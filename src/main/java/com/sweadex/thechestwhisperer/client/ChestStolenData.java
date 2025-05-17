package com.sweadex.thechestwhisperer.client;

import net.minecraft.core.BlockPos;

import java.util.HashMap;
import java.util.Map;

public class ChestStolenData {
    public static final Map<BlockPos, Boolean> stolenChests = new HashMap<>();

    public static boolean isChestStolen(BlockPos pos) {
        return stolenChests.getOrDefault(pos, false);
    }

    public static void setChestStolen(BlockPos pos, boolean stolen) {
        if (stolen) stolenChests.put(pos, true);
        else stolenChests.remove(pos);
    }
}

