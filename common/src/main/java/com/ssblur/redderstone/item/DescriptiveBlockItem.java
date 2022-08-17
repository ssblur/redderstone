package com.ssblur.redderstone.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class DescriptiveBlockItem extends BlockItem {
  String[] tooltips;
  String firstLine;
  public DescriptiveBlockItem(Block block, Properties properties, String firstLine, String... tooltips) {
    super(block, properties);
    this.firstLine = firstLine;
    this.tooltips = tooltips;
  }

  @Environment(EnvType.CLIENT)
  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
    list.add(Component.literal("§7" + I18n.get(firstLine)));
    if(Screen.hasShiftDown())
      for(var s: tooltips)
        list.add(Component.literal("§7" + I18n.get(s)));
    else
      list.add(Component.literal(I18n.get("tooltip.redderstone.hold_for_info")));

    super.appendHoverText(itemStack, level, list, tooltipFlag);
  }
}
