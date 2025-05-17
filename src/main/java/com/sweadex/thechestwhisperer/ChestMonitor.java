package com.sweadex.thechestwhisperer;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ChestMenu;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.HashMap;
import java.util.Map;

public class ChestMonitor {
    private final Map<BlockPos, Integer> lastItemCounts = new HashMap<>();

    @SubscribeEvent
    public void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (event.phase != TickEvent.Phase.END || event.player.level().isClientSide) return;
        Player player = event.player;

        AbstractContainerMenu container = player.containerMenu;
        if (container instanceof ChestMenu chestMenu) {
            Container inv = chestMenu.getContainer();
            if (inv instanceof ChestBlockEntity chest) {
                BlockPos pos = chest.getBlockPos();
                CompoundTag data = chest.getPersistentData();
                String ownerUUID = data.getString("owner");

                int itemCount = 0;
                for (int i = 0; i < inv.getContainerSize(); i++) {
                    if (!inv.getItem(i).isEmpty()) {
                        itemCount += inv.getItem(i).getCount();
                    }
                }

                int lastCount = lastItemCounts.getOrDefault(pos, itemCount);
                if (itemCount < lastCount) {
                    if (!player.getGameProfile().getName().equals(ownerUUID)) {
                        data.putString("lastThief", player.getName().getString());
                        chest.setChanged();
                        System.out.println("Vol détecté par " + player.getName().getString());
                    }
                }

                lastItemCounts.put(pos, itemCount);

                if (player.getGameProfile().getName().equals(ownerUUID)) {
                    data.remove("lastThief");
                    chest.setChanged();
                    System.out.println("Propriétaire a ouvert le coffre, reset.");
                }
            }
        }
    }
}
