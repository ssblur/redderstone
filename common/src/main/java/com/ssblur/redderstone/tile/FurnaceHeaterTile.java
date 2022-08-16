package com.ssblur.redderstone.tile;

import com.ssblur.redderstone.RedderstoneMod;
import com.ssblur.redderstone.RedderstoneUtility;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class FurnaceHeaterTile extends RedderstoneTile {
  public FurnaceHeaterTile(BlockPos blockPos, BlockState blockState) {
    super(RedderstoneMod.FURNACE_HEATER_TYPE.get(), blockPos, blockState);
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(entity instanceof FurnaceHeaterTile && !entity.isRemoved()) {

    }
  }
}
