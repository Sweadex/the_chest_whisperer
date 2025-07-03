package com.sweadex.thechestwhisperer.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;

public class ChestStolenUpdatePacket {
    private final BlockPos pos;
    private final boolean stolen;

    public ChestStolenUpdatePacket(BlockPos pos, boolean stolen) {
        this.pos = pos;
        this.stolen = stolen;
    }

    public ChestStolenUpdatePacket(FriendlyByteBuf buf) {
        this.pos = buf.readBlockPos();
        this.stolen = buf.readBoolean();
    }

    public void encode(FriendlyByteBuf buf) {
        buf.writeBlockPos(pos);
        buf.writeBoolean(stolen);
    }

    public BlockPos getPos() {
        return pos;
    }

    public boolean isStolen() {
        return stolen;
    }
}
