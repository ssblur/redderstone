package com.ssblur.redderstone;

import com.google.common.base.Suppliers;
import com.ssblur.redderstone.block.RedderstoneWireBlock;
import com.ssblur.redderstone.item.CraftingComponentItem;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;

import java.util.function.Supplier;

public class RedderstoneMod {
  public static final String MOD_ID = "redderstone";
  public static final int COLOR = 0xC80815;
  // We can use this if we don't want to use DeferredRegister
  public static final Supplier<Registries> REGISTRIES = Suppliers.memoize(() -> Registries.get(MOD_ID));
  // Registering a new creative tab
  public static final CreativeModeTab TAB = CreativeTabRegistry.create(new ResourceLocation(MOD_ID, "redderstone_tab"), () ->
          new ItemStack(RedderstoneMod.VERMILION_DUST.get()));

  public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(MOD_ID, Registry.BLOCK_REGISTRY);

  // The following blocks are virtual, and are not normally directly obtainable.
  public static final RegistrySupplier<Block> REDDERSTONE_WIRE = BLOCKS.register("redderstone_wire", () ->
    new RedderstoneWireBlock(16));


  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);
  public static final RegistrySupplier<Item> VERMILION_DUST = ITEMS.register("vermilion_dust", () ->
    new CraftingComponentItem(new Item.Properties().tab(RedderstoneMod.TAB)));

  // The following items are included for debugging, but are generally unused.
  public static final RegistrySupplier<Item> REDDERSTONE_WIRE_ITEM = ITEMS.register("redderstone_wire", () ->
    new BlockItem(REDDERSTONE_WIRE.get(), new Item.Properties()));
    
  public static void init() {

    BLOCKS.register();
    ITEMS.register();

  }
}
