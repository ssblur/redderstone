package com.ssblur.redderstone.tile;

import com.ssblur.redderstone.RedderstoneMod;
import com.ssblur.redderstone.RedderstoneUtility;
import com.ssblur.redderstone.block.FurnaceHeaterBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.AbstractFurnaceBlock;
import net.minecraft.world.level.block.FurnaceBlock;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.math.NumberUtils;

public class FurnaceHeaterTile extends RedderstoneTile {
  private static final int MAX_TIME = 200;
  int poweredTicks = 0;
  public FurnaceHeaterTile(BlockPos blockPos, BlockState blockState) {
    super(RedderstoneMod.FURNACE_HEATER_TYPE.get(), blockPos, blockState);
  }

  public void addCookTime(AbstractFurnaceBlockEntity entity)
  {
    var data = entity.dataAccess;
    int burnTime = data.get(AbstractFurnaceBlockEntity.DATA_LIT_TIME);
    if(burnTime < MAX_TIME)
      data.set(AbstractFurnaceBlockEntity.DATA_LIT_TIME, burnTime + (poweredTicks >= 20 ? 4 : 1));

    activate(entity);
  }

  public void expedite(AbstractFurnaceBlockEntity entity)
  {
    var data = entity.dataAccess;
    var time = data.get(AbstractFurnaceBlockEntity.DATA_COOKING_PROGRESS);
    var totalTime = data.get(AbstractFurnaceBlockEntity.DATA_COOKING_TOTAL_TIME);
    if(time > 0 && time < totalTime - 1)
      data.set(AbstractFurnaceBlockEntity.DATA_COOKING_PROGRESS, time);
  }

  public static void activate(AbstractFurnaceBlockEntity entity)
  {
    var state = entity.getBlockState();
    var level = entity.getLevel();
    if(!state.getValue(AbstractFurnaceBlock.LIT) && level != null)
      level.setBlockAndUpdate(
        entity.getBlockPos(), state.setValue(AbstractFurnaceBlock.LIT, true)
      );
  }

  public int getRedstone() {
    return NumberUtils.max(
      RedderstoneUtility.getRedstoneLevel(level, worldPosition.east(), Direction.WEST),
      RedderstoneUtility.getRedstoneLevel(level, worldPosition.west(), Direction.EAST),
      RedderstoneUtility.getRedstoneLevel(level, worldPosition.north(), Direction.SOUTH),
      RedderstoneUtility.getRedstoneLevel(level, worldPosition.south(), Direction.NORTH)
    );
  }

  public static void setActive(Level level, BlockPos pos, boolean active) {
    if(level.getBlockState(pos).getValue(FurnaceHeaterBlock.ACTIVE) != active)
      level.setBlockAndUpdate(pos, level.getBlockState(pos).setValue(FurnaceHeaterBlock.ACTIVE, active));
  }

  public void tick() {
    if(level == null) return;
    var pos = getBlockPos();
    if(!this.isRemoved() && level != null)
      if(this.getRedstone() >= 24) {
        poweredTicks++;
        setActive(level, pos, true);
        if(level.getBlockEntity(pos.relative(getBlockState().getValue(FurnaceHeaterBlock.FACING).getOpposite())) instanceof AbstractFurnaceBlockEntity furnace) {
          addCookTime(furnace);
          if (this.getRedstone() >= 32)
            expedite(furnace);
        }
      } else {
        poweredTicks = 0;
        setActive(level, pos, false);
      }
  }

  public static <T extends BlockEntity> void tick(Level level, BlockPos pos, BlockState state, T entity) {
    if(level != null && level.isClientSide) return;
    if(entity instanceof FurnaceHeaterTile tile) tile.tick();
  }

}
