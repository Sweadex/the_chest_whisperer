package com.sweadex.thechestwhisperer;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.world.level.saveddata.SavedData;

import java.util.HashSet;
import java.util.Set;

public class StolenChestSavedData extends SavedData {

    private final Set<BlockPos> stolenChests = new HashSet<>();

    public void setStolen(BlockPos pos, boolean stolen) {
        if (stolen) {
            stolenChests.add(pos);
        } else {
            stolenChests.remove(pos);
        }
        setDirty();
    }

    public boolean isStolen(BlockPos pos) {
        return stolenChests.contains(pos);
    }

    public Set<BlockPos> getAll() {
        return stolenChests;
    }

    @Override
    public CompoundTag save(CompoundTag tag) {
        ListTag list = new ListTag();
        for (BlockPos pos : stolenChests) {
            CompoundTag posTag = new CompoundTag();
            posTag.putInt("x", pos.getX());
            posTag.putInt("y", pos.getY());
            posTag.putInt("z", pos.getZ());
            list.add(posTag);
        }
        tag.put("StolenChests", list);
        return tag;
    }

    public static StolenChestSavedData load(CompoundTag tag) {
        StolenChestSavedData data = new StolenChestSavedData();
        ListTag list = tag.getList("StolenChests", Tag.TAG_COMPOUND);
        for (Tag t : list) {
            CompoundTag posTag = (CompoundTag) t;
            BlockPos pos = new BlockPos(
                    posTag.getInt("x"),
                    posTag.getInt("y"),
                    posTag.getInt("z")
            );
            data.stolenChests.add(pos);
        }
        return data;
    }
}
