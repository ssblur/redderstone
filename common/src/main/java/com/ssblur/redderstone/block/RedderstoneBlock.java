package com.ssblur.redderstone.block;

import com.ssblur.redderstone.RedderstoneUtility;
import com.ssblur.redderstone.tile.RedderstoneBlockTile;
import com.ssblur.redderstone.tile.RedderstoneTile;
import dev.architectury.platform.Platform;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BaseEntityBlock;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;
import org.jetbrains.annotations.Nullable;

public class RedderstoneBlock extends RedderstoneEmitter implements EntityBlock, RedderstoneConductor {

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

  public void onRemove(BlockState state, Level level, BlockPos pos, BlockState toState, boolean bl) {
    super.onRemove(state, level, pos, toState, bl);
    RedderstoneUtility.clearRedstoneLevel(level, pos);
  }
}
