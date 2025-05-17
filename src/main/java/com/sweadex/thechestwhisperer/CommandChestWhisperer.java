package com.sweadex.thechestwhisperer;

import com.mojang.brigadier.CommandDispatcher;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.coordinates.BlockPosArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.ChestBlockEntity;
import net.minecraft.network.chat.Component;

public class CommandChestWhisperer {

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        dispatcher.register(
                Commands.literal("checkchest")
                        .then(Commands.argument("pos", BlockPosArgument.blockPos())
                                .executes(context -> {
                                    BlockPos pos = BlockPosArgument.getLoadedBlockPos(context, "pos");
                                    CommandSourceStack source = context.getSource();

                                    // Récupérer le block entity à la position donnée
                                    BlockEntity blockEntity = source.getLevel().getBlockEntity(pos);
                                    if (blockEntity instanceof ChestBlockEntity) {
                                        CompoundTag persistentData = blockEntity.getPersistentData();
                                        String owner = persistentData.contains("owner") ? persistentData.getString("owner") : "Aucun";
                                        String lastThief = persistentData.contains("lastThief") ? persistentData.getString("lastThief") : "Aucun";


                                        // Envoyer un message au joueur admin
                                        source.sendSuccess(() -> Component.literal("Owner: " + owner), false);
                                        source.sendSuccess(() -> Component.literal("Last Thief: " + lastThief), false);

                                        return 1;
                                    } else {
                                        source.sendFailure(Component.literal("Ce n'est pas un coffre valide !"));
                                        return 0;
                                    }
                                }))
        );
    }
}
