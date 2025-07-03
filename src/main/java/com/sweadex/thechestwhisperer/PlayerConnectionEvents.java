package com.sweadex.thechestwhisperer;

import com.sweadex.thechestwhisperer.network.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import com.sweadex.thechestwhisperer.network.ChestStolenSyncPacket;

import java.util.HashMap;
import java.util.Map;


public class PlayerConnectionEvents {

    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        ServerLevel level = player.serverLevel();
        Map<BlockPos, Boolean> stolenMap = new HashMap<>();

        for (BlockPos pos : ChestStolenDataServer.getAllStolen(level)) {
            stolenMap.put(pos, true);
            NetworkHandler.sendChestStolenUpdate(pos, true);
        }

        NetworkHandler.INSTANCE.send(
                PacketDistributor.PLAYER.with(() -> player),
                new ChestStolenSyncPacket(stolenMap)
        );
    }
}
