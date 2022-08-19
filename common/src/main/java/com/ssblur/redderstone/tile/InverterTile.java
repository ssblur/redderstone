package com.ssblur.redderstone.tile;

import com.ssblur.redderstone.RedderstoneMod;
import com.ssblur.redderstone.RedderstoneUtility;
import com.ssblur.redderstone.block.AlternatorBlock;
import com.ssblur.redderstone.block.FurnaceHeaterBlock;
import com.ssblur.redderstone.block.InverterBlock;
import com.ssblur.redderstone.block.base.RedderstoneConductor;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

public class InverterTile extends RedderstoneTile {
  private static final int PERIOD = 20;
  private static final int MAX_INPUT = 60;
  protected int lastSignal;
  public InverterTile(BlockPos blockPos, BlockState blockState) {
    super(RedderstoneMod.INVERTER_TYPE.get(), blockPos, blockState);

    lastSignal = 0;
  }

  public void setActive(boolean active) {
    if(level == null || worldPosition == null) return;
    if(level.getBlockState(worldPosition).getValue(InverterBlock.ACTIVE) != active)
      level.setBlockAndUpdate(worldPosition, level.getBlockState(worldPosition).setValue(InverterBlock.ACTIVE, active));
  }

  public void tick() {
    if(level == null || worldPosition == null) return;
    var offset = (level.getGameTime() + PERIOD) % (PERIOD * 2);
    var direction = getBlockState().getValue(InverterBlock.HORIZONTAL_FACING);
    var signal = RedderstoneUtility.getRedstoneLevel(level, worldPosition.relative(direction.getOpposite()), direction.getOpposite());

    if(offset == 0)
      lastSignal = signal;

    if(offset < PERIOD)
      lastSignal = Math.min(lastSignal, signal);

    if((lastSignal > 0 && offset >= PERIOD) && !getBlockState().getValue(InverterBlock.ACTIVE)) {
      level.setBlockAndUpdate(worldPosition, getBlockState().setValue(InverterBlock.ACTIVE, true));
    } else if((lastSignal <= 0 || offset < PERIOD) && getBlockState().getValue(InverterBlock.ACTIVE)){
      level.setBlockAndUpdate(worldPosition, getBlockState().setValue(InverterBlock.ACTIVE, false));
      RedderstoneUtility.clearRedstoneLevel(level, worldPosition.relative(direction));
    }

    if(offset >= PERIOD) {
      var block = level.getBlockState(worldPosition.relative(direction)).getBlock();
      if (block instanceof RedderstoneConductor || block == Blocks.REDSTONE_WIRE)
        RedderstoneUtility.setRedstoneLevel(level, worldPosition.relative(direction), Math.min(lastSignal, MAX_INPUT) - 2);
    } else if(offset == 0) {
      RedderstoneUtility.clearRedstoneLevel(level, worldPosition.relative(direction));
    }
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(entity instanceof InverterTile tile) tile.tick();
  }

}
