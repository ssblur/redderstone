package com.ssblur.redderstone.block;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.DirectionProperty;

public class FacingBlock extends Block implements RedderstoneConnector {
  public static final DirectionProperty FACING = DirectionProperty.create("facing");

  public FacingBlock(Properties properties) {
    super(properties);
  }

  @Override
  public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
    return defaultBlockState().setValue(FACING, blockPlaceContext.getNearestLookingDirection().getOpposite());
  }

  @Override
  public boolean connectsOnSide(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
    return blockState.getValue(FACING) == direction;
  }
}
