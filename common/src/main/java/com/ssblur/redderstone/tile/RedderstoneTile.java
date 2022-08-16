package com.ssblur.redderstone.tile;

import com.ssblur.redderstone.RedderstoneUtility;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public class RedderstoneTile extends BlockEntity {
  public RedderstoneTile(BlockEntityType<?> blockEntityType, BlockPos blockPos, BlockState blockState) {
    super(blockEntityType, blockPos, blockState);
  }

  /**
   * A convenience function for reading the signal of this BlockEntity.
   * If the block at this position faces a direction, this will accumulate the Redstone signal from the back.
   * Otherwise it will read the signal from above.
   * Generally, this won't matter as most Redderstone mechanisms use levels >15, which are not directional.
   * @param dir
   * @return
   */
  public int getSignal(Direction dir) {
    return RedderstoneUtility.getRedstoneLevel(level, worldPosition, dir);
  }
}
