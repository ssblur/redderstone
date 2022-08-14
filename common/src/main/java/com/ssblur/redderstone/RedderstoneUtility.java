package com.ssblur.redderstone;

import com.ssblur.redderstone.block.RedderstoneWireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.RedstoneTorchBlock;
import net.minecraft.world.level.block.state.BlockState;

public class RedderstoneUtility {
  public static void setRedstoneLevel(Level level, BlockPos pos, int redstoneLevel) {
    Block block = level.getBlockState(pos).getBlock();
    if(block == Blocks.REDSTONE_WIRE || block instanceof RedderstoneWireBlock) {
      if(redstoneLevel <= 15)
        level.setBlock(pos, Blocks.REDSTONE_WIRE.defaultBlockState(), 0);
      else {
        level.setBlock(
          pos,
          RedderstoneMod
            .REDDERSTONE_WIRE
            .get()
            .defaultBlockState()
            .setValue(
              RedderstoneWireBlock.LEVEL,
              redstoneLevel % 16
            ),
          0
        );
      }
    }
  }
}
