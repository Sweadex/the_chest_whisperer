package com.sweadex.thechestwhisperer.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

import java.util.HashMap;
import java.util.Map;

public class ChestStolenSyncPacket {
    private final Map<BlockPos, Boolean> stolenMap;

    public ChestStolenSyncPacket(Map<BlockPos, Boolean> stolenMap) {
        this.stolenMap = stolenMap;
    }

    public ChestStolenSyncPacket(FriendlyByteBuf buf) {
        int size = buf.readInt();
        this.stolenMap = new HashMap<>();
        for (int i = 0; i < size; i++) {
            BlockPos pos = buf.readBlockPos();
            boolean stolen = buf.readBoolean();
            stolenMap.put(pos, stolen);
        }
    }

    public static void encode(ChestStolenSyncPacket pkt, FriendlyByteBuf buf) {
        buf.writeInt(pkt.stolenMap.size());
        for (Map.Entry<BlockPos, Boolean> entry : pkt.stolenMap.entrySet()) {
            buf.writeBlockPos(entry.getKey());
            buf.writeBoolean(entry.getValue());
        }
    }

    public Map<BlockPos, Boolean> getStolenMap() {
        return stolenMap;
    }
}
