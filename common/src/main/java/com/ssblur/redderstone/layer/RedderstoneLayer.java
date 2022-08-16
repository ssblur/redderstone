package com.ssblur.redderstone.layer;

import com.ssblur.redderstone.RedderstoneMod;
import com.ssblur.redderstone.block.RedderstoneWireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;

public class RedderstoneLayer {
  public static HashMap<ResourceKey<Level>, RedderstoneLayer> MAP = new HashMap<>();

  public static RedderstoneLayer getMap(Level level) {
    var dimension = level.dimension();
    if(MAP.containsKey(dimension))
      return MAP.get(dimension);
    var layer = new RedderstoneLayer();
    MAP.put(dimension, layer);
    return layer;
  }

  HashMap<BlockPos, Integer> layerSignal = new HashMap<>();
  HashMap<BlockPos, Integer> layerMemory = new HashMap<>();

  public void setSignal(BlockPos pos, int signal) {
    if(layerMemory.getOrDefault(pos, 0) < signal)
      layerMemory.put(pos, signal);
  }

  public void clearSignal(BlockPos pos) {
    layerSignal.remove(pos);
    layerMemory.put(pos, 0);
  }

  public int getSignal(BlockPos pos) {
    return layerSignal.getOrDefault(pos, 0);
  }


  public int getMemory(BlockPos pos) {
    return Math.max(layerMemory.getOrDefault(pos, 0), layerSignal.getOrDefault(pos, 0));
  }

  public void tick(Level level) {
    for(var pos: layerMemory.keySet())
      if (!layerSignal.containsKey(pos) || layerMemory.get(pos) != layerSignal.get(pos)) {
        layerSignal.put(pos, layerMemory.get(pos));

        var blockPos = new BlockPos(pos);
        var signal = layerMemory.get(pos);
        var block = level.getBlockState(blockPos).getBlock();

        if (block instanceof RedderstoneWireBlock)
          if (signal < 16) {
            level.setBlock(blockPos, Blocks.REDSTONE_WIRE.defaultBlockState(), 0);
            level.sendBlockUpdated(blockPos, level.getBlockState(blockPos), Blocks.REDSTONE_WIRE.defaultBlockState(), 0);
          }

        if (signal >= 16 && block == Blocks.REDSTONE_WIRE) {
          level.setBlock(
            blockPos,
            RedderstoneMod
              .REDDERSTONE_WIRE
              .get()
              .defaultBlockState(),
            0
          );
          level.sendBlockUpdated(
            blockPos,
            level.getBlockState(blockPos),
            ((RedderstoneWireBlock) RedderstoneMod.REDDERSTONE_WIRE.get()).getConnectedState(level, pos),
            0
          );
        }
      }
    layerMemory.clear();
  }
}
