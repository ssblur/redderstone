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
    var state = defaultBlockState().setValue(FACING, blockPlaceContext.getNearestLookingDirection().getOpposite());
    if(this instanceof WireConnectable wire)
      state = wire.withConnectedState(state, blockPlaceContext.getLevel(), blockPlaceContext.getClickedPos());
    return state;
  }

  @Override
  public boolean connectsOnSide(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
    if(this instanceof WireConnectable)
      return direction.getAxis() != Direction.Axis.Y;
    return blockState.getValue(FACING) == direction;
  }

  public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos blockPos2, boolean bl) {
    if(this instanceof WireConnectable wire) {
      var state = wire.withConnectedState(blockState, level, pos);
      if(!state.equals(blockState))
        level.setBlock(pos, state, 0);
      level.sendBlockUpdated(pos, blockState, blockState, 0);
    }
  }
}
