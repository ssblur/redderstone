package com.ssblur.redderstone.events;

import com.ssblur.redderstone.RedderstoneUtility;
import dev.architectury.event.EventResult;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.utils.value.IntValue;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;

public class RedderstoneBlockEvent implements BlockEvent.Place, BlockEvent.Break {
  @Override
  public EventResult breakBlock(Level level, BlockPos pos, BlockState state, ServerPlayer player, @Nullable IntValue xp) {
    RedderstoneUtility.clearRedstoneLevel(level, pos);
    return EventResult.pass();
  }

  @Override
  public EventResult placeBlock(Level level, BlockPos pos, BlockState state, @Nullable Entity placer) {
    RedderstoneUtility.clearRedstoneLevel(level, pos);
    return EventResult.pass();
  }
}
