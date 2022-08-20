package com.ssblur.redderstone.tile;

import com.ssblur.redderstone.RedderstoneMod;
import com.ssblur.redderstone.RedderstoneUtility;
import com.ssblur.redderstone.block.AlternatorBlock;
import com.ssblur.redderstone.block.FurnaceHeaterBlock;
import com.ssblur.redderstone.block.base.RedderstoneConductor;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.math.NumberUtils;

public class AlternatorTile extends RedderstoneTile {
  private static final int PERIOD = 20;
  private static final int MAX_INPUT = 30;
  protected int lastSignal;
  public AlternatorTile(BlockPos blockPos, BlockState blockState) {
    super(RedderstoneMod.ALTERNATOR_TYPE.get(), blockPos, blockState);

    lastSignal = 0;
  }

  public void tick() {
    if(level == null) return;
    var offset = level.getGameTime() % (PERIOD * 2);
    var direction = getBlockState().getValue(AlternatorBlock.HORIZONTAL_FACING);
    var signal = RedderstoneUtility.getRedstoneLevel(level, worldPosition.relative(direction.getOpposite()), direction.getOpposite());

    if(offset == 0)
      lastSignal = signal;

    lastSignal = Math.min(lastSignal, signal);

    if(lastSignal > 0 && !getBlockState().getValue(AlternatorBlock.ACTIVE)) {
      level.setBlockAndUpdate(worldPosition, getBlockState().setValue(AlternatorBlock.ACTIVE, true));
    } else if(lastSignal <= 0 && getBlockState().getValue(AlternatorBlock.ACTIVE)){
      level.setBlockAndUpdate(worldPosition, getBlockState().setValue(AlternatorBlock.ACTIVE, false));
      RedderstoneUtility.clearRedstoneLevel(level, worldPosition.relative(direction));
    }

    if(offset >= PERIOD) {
      var block = level.getBlockState(worldPosition.relative(direction)).getBlock();
      if (block instanceof RedderstoneConductor || block == Blocks.REDSTONE_WIRE)
        RedderstoneUtility.setRedstoneLevel(level, worldPosition.relative(direction), Math.min(lastSignal, MAX_INPUT) * 2);
    } else if(offset == 0) {
      RedderstoneUtility.clearRedstoneLevel(level, worldPosition.relative(direction));
    }
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(level != null && level.isClientSide) return;
    if(entity instanceof AlternatorTile tile) tile.tick();
  }

}
