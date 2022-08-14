package com.ssblur.redderstone.block;

import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.piston.PistonBaseBlock;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.material.Material;
import net.minecraft.world.level.material.MaterialColor;

public class RedderstoneWireBlock extends Block {
  public static final IntegerProperty LEVEL = IntegerProperty.create("level", 0, 15);

  protected int levelOffset;

  public RedderstoneWireBlock(int offset) {
    super(Properties.of(Material.STONE).dynamicShape().color(MaterialColor.COLOR_RED));
    levelOffset = offset;

  }


}
