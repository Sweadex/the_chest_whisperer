package com.sweadex.thechestwhisperer.network;

import com.sweadex.thechestwhisperer.client.ChestStolenData;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation("thechestwhisperer", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        INSTANCE.registerMessage(
                packetId++,
                ChestStolenSyncPacket.class,
                ChestStolenSyncPacket::encode,
                ChestStolenSyncPacket::new,
                NetworkHandler::handleChestStolenSyncPacket);
        INSTANCE.registerMessage(
                packetId++,
                ChestStolenUpdatePacket.class,
                ChestStolenUpdatePacket::encode,
                ChestStolenUpdatePacket::new,
                NetworkHandler::handleChestStolenUpdatePacket);
    }

    private static void handleChestStolenUpdatePacket(ChestStolenUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ChestStolenData.setChestStolen(packet.getPos(), packet.isStolen());
        });
        ctx.get().setPacketHandled(true);
    }

    private static void handleChestStolenSyncPacket(ChestStolenSyncPacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            ChestStolenData.clear();
            packet.getStolenMap().forEach(ChestStolenData::setChestStolen);
        });
        ctx.get().setPacketHandled(true);
    }

    public static void sendChestStolenUpdate(BlockPos pos, boolean stolen) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), new ChestStolenUpdatePacket(pos, stolen));
    }
}
