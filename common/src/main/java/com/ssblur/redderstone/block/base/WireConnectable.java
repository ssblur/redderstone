package com.ssblur.redderstone.block.base;

import com.ssblur.redderstone.block.RedderstoneWireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BooleanProperty;

public interface WireConnectable {

  BooleanProperty EAST = BooleanProperty.create("wire_east");
  BooleanProperty NORTH = BooleanProperty.create("wire_north");
  BooleanProperty SOUTH = BooleanProperty.create("wire_south");
  BooleanProperty WEST = BooleanProperty.create("wire_west");
  BooleanProperty EAST_UP = BooleanProperty.create("wire_east_up");
  BooleanProperty WEST_UP = BooleanProperty.create("wire_west_up");
  BooleanProperty NORTH_UP = BooleanProperty.create("wire_north_up");
  BooleanProperty SOUTH_UP = BooleanProperty.create("wire_south_up");
  default boolean connectsTo(Level level, BlockPos pos) {
    var state = level.getBlockState(pos);
    var block = state.getBlock();
    return
      block instanceof RedderstoneWireBlock
        ||
        block == Blocks.REDSTONE_WIRE;
  }

  default boolean connectsOnSide(Level level, BlockPos pos, Direction dir) {
    var state = level.getBlockState(pos);
    var block = state.getBlock();
    return
      block instanceof RedderstoneWireBlock
        ||
        block == Blocks.REDSTONE_WIRE
        ||
        (
          block instanceof RedderstoneConnector connector
            &&
            connector.connectsOnSide(state, level, pos, dir)
        );
  }

  default boolean connectsToDisplaced(Level level, BlockPos pos) {
    var state = level.getBlockState(pos);
    var block = state.getBlock();
    return block instanceof WireConnectable || block == Blocks.REDSTONE_WIRE;
  }

  default boolean connectsToFuzzy(Level level, BlockPos pos, Direction dir) {
    return connectsOnSide(level, pos, dir) || connectsToDisplaced(level, pos.above()) || connectsToDisplaced(level, pos.below());
  }

  default BlockState withConnectedState(BlockState state, Level level, BlockPos pos) {
    if(state.hasProperty(NORTH))
      state = state.setValue(NORTH, connectsToFuzzy(level, pos.north(), Direction.SOUTH));
    if(state.hasProperty(SOUTH))
      state = state.setValue(SOUTH, connectsToFuzzy(level, pos.south(), Direction.NORTH));
    if(state.hasProperty(EAST))
      state = state.setValue(EAST, connectsToFuzzy(level, pos.east(), Direction.WEST));
    if(state.hasProperty(WEST))
      state = state.setValue(WEST, connectsToFuzzy(level, pos.west(), Direction.EAST));
    if(state.hasProperty(NORTH_UP))
      state = state.setValue(NORTH_UP, connectsTo(level, pos.north().above()));
    if(state.hasProperty(SOUTH_UP))
      state = state.setValue(SOUTH_UP, connectsTo(level, pos.south().above()));
    if(state.hasProperty(EAST_UP))
      state = state.setValue(EAST_UP, connectsTo(level, pos.east().above()));
    if(state.hasProperty(WEST_UP))
      state = state.setValue(WEST_UP, connectsTo(level, pos.west().above()));
    return state;
  }
}
