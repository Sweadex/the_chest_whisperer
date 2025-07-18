package com.sweadex.thechestwhisperer;

import com.sweadex.thechestwhisperer.network.NetworkHandler;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.BlockEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod(TheChestWhisperer.MODID)
public class TheChestWhisperer
{
    public static final String MODID = "thechestwhisperer";

    public TheChestWhisperer() {
        IEventBus modEventBus = MinecraftForge.EVENT_BUS;
        modEventBus.register(this);
        MinecraftForge.EVENT_BUS.register(new ChestMonitor());
        MinecraftForge.EVENT_BUS.register(new PlayerConnectionEvents());
        NetworkHandler.register();
    }

    @SubscribeEvent
    public void onPlaceBlock(BlockEvent.EntityPlaceEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player)) return;

        Level level = player.level();
        BlockState state = event.getPlacedBlock();

        if (state.getBlock() == Blocks.CHEST) {
            BlockPos pos = event.getPos();
            BlockEntity be = level.getBlockEntity(pos);
            if (be instanceof ChestBlockEntity chest) {
                CompoundTag persistentData = chest.getPersistentData();
                persistentData.putString("owner", player.getGameProfile().getName());
                persistentData.remove("lastThief");

                chest.setChanged();

                level.sendBlockUpdated(pos, state, state, 3);
                chest.getLevel().blockEvent(pos, chest.getBlockState().getBlock(), 1, 0);
            }
        }
    }

    @SubscribeEvent
    public void onBreakBlock(BlockEvent.BreakEvent event) {
        if (!(event.getPlayer() instanceof ServerPlayer player)) return;

        Level level = player.level();
        BlockPos pos = event.getPos();

        if (!level.isClientSide && level.getBlockState(pos).getBlock() == Blocks.CHEST) {
            if (level.getBlockEntity(pos) instanceof ChestBlockEntity) {
                ChestStolenDataServer.setChestStolen((ServerLevel) level, pos, false);
                NetworkHandler.sendChestStolenUpdate(pos, false);
            }
        }
    }

}
