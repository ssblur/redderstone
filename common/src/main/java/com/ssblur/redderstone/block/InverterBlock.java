package com.ssblur.redderstone.block;

import com.ssblur.redderstone.block.base.FacingBlock;
import com.ssblur.redderstone.tile.AlternatorTile;
import com.ssblur.redderstone.tile.InverterTile;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
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
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class InverterBlock extends FacingBlock implements EntityBlock {
  public static final BooleanProperty ACTIVE = BooleanProperty.create("active");
  public static final VoxelShape SHAPE = Shapes.box(0f, 0f, 0f, 1f, 0.125f, 1f);

  public InverterBlock() {
    super(
      Properties
        .of(Material.METAL)
        .noOcclusion()
        .lightLevel(state -> 0)
        .color(MaterialColor.COLOR_GRAY)
    );

    registerDefaultState(defaultBlockState().setValue(ACTIVE, false));
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
    return SHAPE;
  }

  public boolean connectsOnSide(BlockState blockState, Level level, BlockPos blockPos, Direction direction) {
    return getDirection(blockState) == direction || getDirection(blockState) == direction.getOpposite();
  }

  @Nullable
  @Override
  public BlockEntity newBlockEntity(@NotNull BlockPos pos, @NotNull BlockState state) {
    return new InverterTile(pos, state);
  }

  @Override
  public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level world, @NotNull BlockState state, @NotNull BlockEntityType<T> type) {
    return InverterTile::tick;
  }

  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(ACTIVE, HORIZONTAL_FACING);
  }
}
