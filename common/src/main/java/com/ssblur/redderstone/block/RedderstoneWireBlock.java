package com.ssblur.redderstone.block;

import com.mojang.math.Vector3f;
import com.ssblur.redderstone.block.base.RedderstoneConductor;
import com.ssblur.redderstone.block.base.RedderstoneEmitter;
import com.ssblur.redderstone.block.base.WireConnectable;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;

public class RedderstoneWireBlock extends RedderstoneEmitter implements RedderstoneConductor, WireConnectable {
  public static final int COLOR_HEX = 0xFF8800;
  public static final Vector3f COLOR = new Vector3f(Vec3.fromRGB24(COLOR_HEX));
  public static final VoxelShape SHAPE = Shapes.box(0f, 0f, 0f, 1f, 0.31f, 1f);

  public RedderstoneWireBlock() {
    super(
      Properties
        .of(Material.DECORATION)
        .noOcclusion()
    );
  }

  @SuppressWarnings("deprecation")
  @Override
  public VoxelShape getShape(@NotNull BlockState blockState, @NotNull BlockGetter blockGetter, @NotNull BlockPos blockPos, @NotNull CollisionContext collisionContext) {
    return SHAPE;
  }

  @Override
  public void animateTick(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos blockPos, RandomSource random) {
    if (random.nextFloat() >= 0.2f) {
      return;
    }
    float r = 0.3f;
    double x = 0.4 + random.nextFloat() * r;
    double y = 0.0 + random.nextFloat() * r;
    double z = 0.4 + random.nextFloat() * r;
    level.addParticle(new DustParticleOptions(COLOR, 1.0f), (double)blockPos.getX() + x, (double)blockPos.getY() + y, (double)blockPos.getZ() + z, 0.0, 0.0, 0.0);
  }

  @SuppressWarnings("deprecation")
  @Override
  public void neighborChanged(@NotNull BlockState blockState, @NotNull Level level, @NotNull BlockPos pos, @NotNull Block block, @NotNull BlockPos blockPos2, boolean bl) {
    var state = getConnectedState(level, pos);
    if(!state.equals(blockState))
      level.setBlock(pos, state, 0);
    level.sendBlockUpdated(pos, blockState, blockState, 0);
  }

  public BlockState getConnectedState(Level level, BlockPos pos) {
    return withConnectedState(defaultBlockState(), level, pos);
  }

  public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
    var level = blockPlaceContext.getLevel();
    var pos = blockPlaceContext.getClickedPos();

    return getConnectedState(level, pos);
  }

  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(NORTH, EAST, WEST, SOUTH, NORTH_UP, EAST_UP, WEST_UP, SOUTH_UP);
  }

  public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos pos, Direction direction) {
    return 15;
  }
}
