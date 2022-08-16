package com.ssblur.redderstone.block;

import com.google.common.collect.ImmutableMap;
import com.ssblur.redderstone.layer.RedderstoneLayer;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import net.minecraft.world.phys.shapes.VoxelShape;

import javax.swing.text.html.BlockView;
import java.util.function.Function;

public class RedderstoneWireBlock extends RedderstoneEmitter implements RedderstoneConductor {
  public static final BooleanProperty EAST = BooleanProperty.create("east");
  public static final BooleanProperty NORTH = BooleanProperty.create("north");
  public static final BooleanProperty SOUTH = BooleanProperty.create("south");
  public static final BooleanProperty WEST = BooleanProperty.create("west");

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

  public boolean connectsTo(Level level, BlockPos pos) {
    var state = level.getBlockState(pos);
    var block = state.getBlock();
    return
      block instanceof RedderstoneWireBlock
      ||
      state.isRedstoneConductor(level, pos)
      ||
      block == Blocks.REDSTONE_WIRE;
  }

  public boolean connectsToDisplaced(Level level, BlockPos pos) {
    var state = level.getBlockState(pos);
    var block = state.getBlock();
    return block instanceof RedderstoneWireBlock || block == Blocks.REDSTONE_WIRE;
  }

  public boolean connectsToFuzzy(Level level, BlockPos pos) {
    return connectsTo(level, pos) || connectsToDisplaced(level, pos.above()) || connectsToDisplaced(level, pos.below());
  }
  @Override
  public void neighborChanged(BlockState blockState, Level level, BlockPos pos, Block block, BlockPos blockPos2, boolean bl) {
    var state = getConnectedState(level, pos);
    if(!state.equals(blockState))
      level.setBlock(pos, state, 0);
  }

  public BlockState getConnectedState(Level level, BlockPos pos) {
    return defaultBlockState()
      .setValue(
        NORTH,
        connectsToFuzzy(level, pos.north())
      )
      .setValue(
        SOUTH,
        connectsToFuzzy(level, pos.south())
      )
      .setValue(
        EAST,
        connectsToFuzzy(level, pos.east())
      )
      .setValue(
        WEST,
        connectsToFuzzy(level, pos.west())
      );
  }

  public BlockState getStateForPlacement(BlockPlaceContext blockPlaceContext) {
    var level = blockPlaceContext.getLevel();
    var pos = blockPlaceContext.getClickedPos();

    return getConnectedState(level, pos);
  }

  protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
    builder.add(new Property[]{NORTH, EAST, WEST, SOUTH});
  }

  public int getSignal(BlockState blockState, BlockGetter blockGetter, BlockPos pos, Direction direction) {
    return 15;
  }
}
