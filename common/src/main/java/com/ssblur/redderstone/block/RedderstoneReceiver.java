package com.ssblur.redderstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.state.BlockState;

public interface RedderstoneReceiver {
  boolean canReceive(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, Direction direction);
}
