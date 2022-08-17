package com.ssblur.redderstone.block.base;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

public interface RedderstoneConnector {
  boolean connectsOnSide(BlockState blockState, Level level, BlockPos blockPos, Direction direction);
}
