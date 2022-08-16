package com.ssblur.redderstone.block;

import com.ssblur.redderstone.RedderstoneUtility;
import com.ssblur.redderstone.layer.RedderstoneLayer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

public abstract class RedderstoneEmitter extends Block {
  public RedderstoneEmitter(Properties properties) {
    super(properties);
  }

  @Override
  public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos pos, Direction direction) {
    if(blockGetter instanceof Level level)
      return RedderstoneLayer.getMap(level).getSignal(pos);

    return 0;
  }
}
