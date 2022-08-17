package com.ssblur.redderstone.block;

import com.ssblur.redderstone.block.base.FacingBlock;
import com.ssblur.redderstone.block.base.RedderstoneConductor;
import com.ssblur.redderstone.block.base.WireConnectable;
import com.ssblur.redderstone.tile.FurnaceHeaterTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.Nullable;

public class FurnaceHeaterBlock extends FacingBlock implements EntityBlock, WireConnectable, RedderstoneConductor {
  public static final BooleanProperty ACTIVE = BooleanProperty.create("active");

  public FurnaceHeaterBlock() {
    super(
      Properties
        .of(Material.METAL)
        .noOcclusion()
        .lightLevel(state -> 0)
        .color(MaterialColor.COLOR_GRAY)
    );
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
    return new FurnaceHeaterTile(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level world, BlockState state, BlockEntityType<T> type) {
    return FurnaceHeaterTile::tick;
  }

  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(ACTIVE, FACING, RedderstoneWireBlock.NORTH, RedderstoneWireBlock.SOUTH, RedderstoneWireBlock.EAST, RedderstoneWireBlock.WEST);
  }
}
