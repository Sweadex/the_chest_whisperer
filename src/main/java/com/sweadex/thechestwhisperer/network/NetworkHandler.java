package com.sweadex.thechestwhisperer.network;

import net.minecraft.core.BlockPos;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.simple.SimpleChannel;

import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class NetworkHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new net.minecraft.resources.ResourceLocation("thechestwhisperer", "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    private static int packetId = 0;

    public static void register() {
        INSTANCE.registerMessage(
                packetId++,
                ChestStolenUpdatePacket.class,
                ChestStolenUpdatePacket::encode,
                ChestStolenUpdatePacket::new,
                NetworkHandler::handleChestStolenUpdatePacket
        );
    }

    private static void handleChestStolenUpdatePacket(ChestStolenUpdatePacket packet, Supplier<NetworkEvent.Context> ctx) {
        ctx.get().enqueueWork(() -> {
            com.sweadex.thechestwhisperer.client.ChestStolenData.setChestStolen(packet.getPos(), packet.isStolen());
        });
        ctx.get().setPacketHandled(true);
    }

    public static void sendChestStolenUpdate(BlockPos pos, boolean stolen) {
        INSTANCE.send(PacketDistributor.ALL.noArg(), new ChestStolenUpdatePacket(pos, stolen));
    }
}
