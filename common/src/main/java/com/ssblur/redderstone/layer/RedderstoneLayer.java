package com.ssblur.redderstone.layer;

import com.ssblur.redderstone.RedderstoneMod;
import com.ssblur.redderstone.block.RedderstoneWireBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;

import java.util.HashMap;

public class RedderstoneLayer {
  public static final HashMap<ResourceKey<Level>, RedderstoneLayer> MAP = new HashMap<>();

  public static RedderstoneLayer getMap(Level level) {
    var dimension = level.dimension();
    synchronized (MAP) {
      if(MAP.containsKey(dimension))
        return MAP.get(dimension);
      var layer = new RedderstoneLayer();
      MAP.put(dimension, layer);
      return layer;
    }
  }

  final HashMap<BlockPos, Integer> layerSignal = new HashMap<>();
  // layerMemory acts as a mutex for both itself and layerSignal, as they are typically modified at the same time.
  // Technically, I could synchronize with both, but order issues could cause race conditions.
  // This is the lazy option.
  final HashMap<BlockPos, Integer> layerMemory = new HashMap<>();
  final HashMap<BlockPos, Boolean> layerClear = new HashMap<>();


  public void setSignal(BlockPos pos, int signal) {
    synchronized (layerMemory) {
      if (layerMemory.getOrDefault(pos, 0) < signal)
        layerMemory.put(pos, signal);
    }
  }

  public void scheduleClear(BlockPos pos) {
    synchronized (layerClear) {
      layerClear.put(pos, true);
    }
  }

  public void clearSignal(BlockPos pos) {
    synchronized (layerMemory){
//      layerSignal.remove(pos);
      layerMemory.put(pos, 0);
    }
  }

  public int getSignal(BlockPos pos) {
    synchronized (layerMemory) {
      return layerSignal.getOrDefault(pos, 0);
    }
  }


  public int getMemory(BlockPos pos) {
    synchronized (layerMemory) {
      return layerMemory.getOrDefault(pos, 0);
    }
  }

  public void preTick(Level level) {
    synchronized (layerClear) {
      for (var pos : layerClear.keySet())
        clearSignal(pos);
      layerClear.clear();
    }
  }

  public void tick(Level level) {
    synchronized (layerMemory) {
      for (var pos : layerMemory.keySet())
        if (!layerSignal.containsKey(pos) || !layerMemory.get(pos).equals(layerSignal.get(pos))) {
          layerSignal.put(pos, layerMemory.get(pos));

          var blockPos = new BlockPos(pos);
          var signal = layerMemory.get(pos);
          var block = level.getBlockState(blockPos).getBlock();

          if (block instanceof RedderstoneWireBlock)
            if (signal < 16)
              level.setBlockAndUpdate(blockPos, Blocks.REDSTONE_WIRE.defaultBlockState());

          if (signal >= 16 && block == Blocks.REDSTONE_WIRE)
            level.setBlockAndUpdate(
              blockPos,
              RedderstoneMod
                .REDDERSTONE_WIRE
                .get()
                .defaultBlockState()
            );
        }
      layerMemory.clear();
    }
  }
}
