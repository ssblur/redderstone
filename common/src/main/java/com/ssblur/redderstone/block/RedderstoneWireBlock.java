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
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import java.util.function.Function;

public class RedderstoneWireBlock extends RedderstoneEmitter implements RedderstoneConductor {
  public static final BooleanProperty EAST = BooleanProperty.create("east");
  public static final BooleanProperty NORTH = BooleanProperty.create("north");
  public static final BooleanProperty SOUTH = BooleanProperty.create("south");
  public static final BooleanProperty WEST = BooleanProperty.create("west");
  public static final BooleanProperty EAST_UP = BooleanProperty.create("east_up");
  public static final BooleanProperty WEST_UP = BooleanProperty.create("west_up");
  public static final BooleanProperty NORTH_UP = BooleanProperty.create("north_up");
  public static final BooleanProperty SOUTH_UP = BooleanProperty.create("south_up");
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
  public void animateTick(BlockState blockState, Level level, BlockPos blockPos, RandomSource random) {
    if (random.nextFloat() >= 0.1f) {
      return;
    }
    float r = 0.3f;
    double x = 0.5 + random.nextFloat() * r;
    double y = 0.2 + random.nextFloat() * r;
    double z = 0.5 + random.nextFloat() * r;
    level.addParticle(new DustParticleOptions(COLOR, 1.0f), (double)blockPos.getX() + x, (double)blockPos.getY() + y, (double)blockPos.getZ() + z, 0.0, 0.0, 0.0);
  }

  @Override
  protected ImmutableMap<BlockState, VoxelShape> getShapeForEachState(Function<BlockState, VoxelShape> function) {
    return this.stateDefinition.getPossibleStates().stream().collect(ImmutableMap.toImmutableMap(Function.identity(), state -> SHAPE));
  }

  public boolean connectsTo(Level level, BlockPos pos) {
    var state = level.getBlockState(pos);
    var block = state.getBlock();
    return
      block instanceof RedderstoneWireBlock
      ||
      block == Blocks.REDSTONE_WIRE;
  }

  public boolean connectsOnSide(Level level, BlockPos pos, Direction dir) {
    var state = level.getBlockState(pos);
    var block = state.getBlock();
    return
      block instanceof RedderstoneWireBlock
        ||
        block == Blocks.REDSTONE_WIRE
        ||
        (
          block instanceof RedderstoneConnector connector
            &&
            connector.connectsOnSide(state, level, pos, dir)
        );
  }

  public boolean connectsToDisplaced(Level level, BlockPos pos) {
    var state = level.getBlockState(pos);
    var block = state.getBlock();
    return block instanceof RedderstoneWireBlock || block == Blocks.REDSTONE_WIRE;
  }

  public boolean connectsToFuzzy(Level level, BlockPos pos, Direction dir) {
    return connectsOnSide(level, pos, dir) || connectsToDisplaced(level, pos.above()) || connectsToDisplaced(level, pos.below());
  }

  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos blockPos2, boolean bl) {
    var state = getConnectedState(level, pos);
    if(!state.equals(blockState))
      level.setBlock(pos, state, 0);
    level.sendBlockUpdated(pos, blockState, blockState, 0);
  }

  public BlockState getConnectedState(Level level, BlockPos pos) {
    return defaultBlockState()
      .setValue(
        NORTH,
        connectsToFuzzy(level, pos.north(), Direction.SOUTH)
      )
      .setValue(
        SOUTH,
        connectsToFuzzy(level, pos.south(), Direction.NORTH)
      )
      .setValue(
        EAST,
        connectsToFuzzy(level, pos.east(), Direction.WEST)
      )
      .setValue(
        WEST,
        connectsToFuzzy(level, pos.west(), Direction.EAST)
      )
      .setValue(
        NORTH_UP,
        connectsTo(level, pos.north().above())
      )
      .setValue(
        SOUTH_UP,
        connectsTo(level, pos.south().above())
      )
      .setValue(
        EAST_UP,
        connectsTo(level, pos.east().above())
      )
      .setValue(
        WEST_UP,
        connectsTo(level, pos.west().above())
      );
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
