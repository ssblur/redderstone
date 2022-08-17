package com.ssblur.redderstone;

import com.ssblur.redderstone.block.base.RedderstoneConductor;
import com.ssblur.redderstone.layer.RedderstoneLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.ArrayList;
import java.util.HashMap;

public class RedderstoneUtility {
  public record PosDirection(BlockPos pos, Direction direction){}
  public static PosDirection[] surrounding(BlockPos pos) {
    return new PosDirection[] {
      new PosDirection(pos.above(), Direction.DOWN),
      new PosDirection(pos.above().east(), Direction.DOWN),
      new PosDirection(pos.above().west(), Direction.DOWN),
      new PosDirection(pos.above().north(), Direction.DOWN),
      new PosDirection(pos.above().south(), Direction.DOWN),
      new PosDirection(pos.below(), Direction.UP),
      new PosDirection(pos.below().east(), Direction.UP),
      new PosDirection(pos.below().west(), Direction.UP),
      new PosDirection(pos.below().north(), Direction.UP),
      new PosDirection(pos.below().south(), Direction.UP),
      new PosDirection(pos.east(), Direction.WEST),
      new PosDirection(pos.west(), Direction.EAST),
      new PosDirection(pos.north(), Direction.SOUTH),
      new PosDirection(pos.south(), Direction.NORTH)
    };
  }

  public static void setRedstoneLevel(Level level, BlockPos pos, int signal) {
    setRedstoneLevel(level, pos, signal, new HashMap<>());
  }

  public static void setRedstoneLevel(Level level, BlockPos pos, int signal, HashMap<BlockPos, Integer> traversal) {
    RedderstoneLayer.getMap(level).setSignal(pos, signal);

    if(signal <= 0) return;

    for(var p: surrounding(pos)) {
      if(traversal.getOrDefault(p.pos, 0) > signal - 1) continue;
      traversal.put(p.pos, signal - 1);

      var state = level.getBlockState(p.pos);
      var block = state.getBlock();

      if(block instanceof RedderstoneConductor || block == Blocks.REDSTONE_WIRE)
        setRedstoneLevel(level, p.pos, signal - 1, traversal);
    }
  }

  public static void clearRedstoneLevel(Level level, BlockPos pos) {
    clearRedstoneLevel(level, pos, new ArrayList<>());
  }

  public static void clearRedstoneLevel(Level level, BlockPos pos, ArrayList<BlockPos> traversal) {
    RedderstoneLayer.getMap(level).scheduleClear(pos);

    for(var p: surrounding(pos)) {
      if(traversal.contains(p.pos)) continue;
      traversal.add(p.pos);

      if(RedderstoneLayer.getMap(level).getSignal(p.pos) > 0)
        clearRedstoneLevel(level, p.pos, traversal);
    }
  }

  public static int getRedstoneLevel(Level level, BlockPos pos, Direction dir) {
    return Math.max(
      RedderstoneLayer.getMap(level).getSignal(pos),
      level.getBlockState(pos).getSignal(level, pos, dir)
    );
  }


}
