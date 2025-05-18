package com.sweadex.thechestwhisperer;

import com.sweadex.thechestwhisperer.network.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
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
                processChest(player, chest);
            } else if (inv.getClass().getSimpleName().equals("CompoundContainer")) {
                try {
                    var leftField = inv.getClass().getDeclaredField("container1");
                    var rightField = inv.getClass().getDeclaredField("container2");
                    leftField.setAccessible(true);
                    rightField.setAccessible(true);

                    Object left = leftField.get(inv);
                    Object right = rightField.get(inv);

                    if (left instanceof ChestBlockEntity leftChest) {
                        processChest(player, leftChest);
                    }
                    if (right instanceof ChestBlockEntity rightChest) {
                        processChest(player, rightChest);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private void processChest(Player player, ChestBlockEntity chest) {
        BlockPos pos = chest.getBlockPos();
        CompoundTag data = chest.getPersistentData();

        if (!data.contains("owner")) {
            data.putString("owner", player.getGameProfile().getName());
            chest.setChanged();
            System.out.println("Owner defini automatiquement : " + player.getGameProfile().getName());
        }

        String ownerUUID = data.getString("owner");

        int itemCount = 0;
        for (int i = 0; i < chest.getContainerSize(); i++) {
            if (!chest.getItem(i).isEmpty()) {
                itemCount += chest.getItem(i).getCount();
            }
        }

        int lastCount = lastItemCounts.getOrDefault(pos, itemCount);
        if (itemCount < lastCount) {
            if (!player.getGameProfile().getName().equals(ownerUUID)) {
                if (data.contains("lastThief") && player.getName().getString().equals(data.getString("lastThief"))){
                    return;
                }
                data.putString("lastThief", player.getName().getString());
                chest.setChanged();
                ChestStolenDataServer.setChestStolen((ServerLevel) player.level(), pos, true);
                NetworkHandler.sendChestStolenUpdate(pos, true);
                System.out.println("Vol detecte du coffre de " + ownerUUID + " par " + player.getName().getString() + " (" + pos + ")");
            }
        }

        lastItemCounts.put(pos, itemCount);

        if (player.getGameProfile().getName().equals(ownerUUID) && data.contains("lastThief")) {
            data.remove("lastThief");
            chest.setChanged();
            ChestStolenDataServer.setChestStolen((ServerLevel) player.level(), pos, false);
            NetworkHandler.sendChestStolenUpdate(pos, false);
            System.out.println("Proprietaire a ouvert le coffre et a reset l'etat du coffre.");
        }
    }
}
