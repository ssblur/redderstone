package com.ssblur.redderstone.block;

import com.google.common.collect.ImmutableMap;
import com.mojang.math.Vector3f;
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
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class RedderstoneWireBlock extends RedderstoneEmitter implements RedderstoneConductor, WireConnectable {
  public static final int COLOR_HEX = 0xFF8800;
  public static final Vector3f COLOR = new Vector3f(Vec3.fromRGB24(COLOR_HEX));
  public static final VoxelShape SHAPE = Shapes.box(0f, 0f, 0f, 1f, 0.31f, 1f);

  public RedderstoneWireBlock() {
    super(
      Properties
        .of(Material.STONE)
        .noOcclusion()
        .noCollission()
        .lightLevel(state -> 9)
        .emissiveRendering((state, level, pos) -> true)
        .isViewBlocking((state, level, pos) -> false)
        .color(MaterialColor.COLOR_RED)
    );
  }

  @Override
  public VoxelShape getShape(BlockState blockState, BlockGetter blockGetter, BlockPos blockPos, CollisionContext collisionContext) {
    return SHAPE;
  }

  @Override
  public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
    if (random.nextFloat() >= 0.2f) {
      return;
    }
    float r = 0.3f;
    double x = 0.4 + random.nextFloat() * r;
    double y = 0.0 + random.nextFloat() * r;
    double z = 0.4 + random.nextFloat() * r;
    level.addParticle(new DustParticleOptions(COLOR, 1.0f), (double)blockPos.getX() + x, (double)blockPos.getY() + y, (double)blockPos.getZ() + z, 0.0, 0.0, 0.0);
  }

  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos blockPos2, boolean bl) {
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
    builder.add(new Property[]{NORTH, EAST, WEST, SOUTH, NORTH_UP, EAST_UP, WEST_UP, SOUTH_UP});
  }

  public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos pos, Direction direction) {
    return 15;
  }
}
