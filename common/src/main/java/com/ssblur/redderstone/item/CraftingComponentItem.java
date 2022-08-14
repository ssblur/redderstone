package com.ssblur.redderstone.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.Nullable;

import java.nio.file.StandardOpenOption;
import java.util.List;

public class CraftingComponentItem extends Item {
  public CraftingComponentItem(Properties properties) {
    super(properties);
  }

  @Override
  public void appendHoverText(ItemStack itemStack, @Nullable Level level, List<Component> list, TooltipFlag tooltipFlag) {
    list.add(Component.literal("§7A crafting component used to make Redstone contraptions"));

    super.appendHoverText(itemStack, level, list, tooltipFlag);
  }
}
