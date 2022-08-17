package com.ssblur.redderstone.block;

import com.ssblur.redderstone.block.base.RedderstoneConductor;
import com.ssblur.redderstone.block.base.RedderstoneConnector;
import com.ssblur.redderstone.block.base.RedderstoneEmitter;
import com.ssblur.redderstone.tile.RedderstoneBlockTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.Nullable;

public class RedderstoneBlock extends RedderstoneEmitter implements EntityBlock, RedderstoneConductor, RedderstoneConnector {

  public RedderstoneBlock() {
    super(
      Properties
        .of(Material.STONE)
        .lightLevel(state -> 15)
        .color(MaterialColor.COLOR_RED)
    );
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new RedderstoneBlockTile(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return RedderstoneBlockTile::tick;
  }

  @Override
  public boolean connectsOnSide(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
    return true;
  }
}
