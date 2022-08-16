package com.ssblur.redderstone;

import com.google.common.base.Suppliers;
import com.ssblur.redderstone.block.FurnaceHeaterBlock;
import com.ssblur.redderstone.block.RedderstoneBlock;
import com.ssblur.redderstone.block.RedderstoneWireBlock;
import com.ssblur.redderstone.events.RedderstoneBlockEvent;
import com.ssblur.redderstone.events.RedderstoneTickEvent;
import com.ssblur.redderstone.item.CraftingComponentItem;
import com.ssblur.redderstone.item.DescriptiveBlockItem;
import com.ssblur.redderstone.tile.RedderstoneBlockTile;
import dev.architectury.event.events.common.BlockEvent;
import dev.architectury.event.events.common.TickEvent;
import dev.architectury.registry.CreativeTabRegistry;
import dev.architectury.registry.client.rendering.RenderTypeRegistry;
import dev.architectury.registry.registries.DeferredRegister;
import dev.architectury.registry.registries.Registries;
import dev.architectury.registry.registries.RegistrySupplier;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;

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
  public static final RegistrySupplier<Block> REDDERSTONE_BLOCK = BLOCKS.register("redderstone_block", () ->
    new RedderstoneBlock());
  public static final RegistrySupplier<Block> FURNACE_HEATER = BLOCKS.register("furnace_heater", () ->
    new FurnaceHeaterBlock());
  // The following blocks are not normally directly obtainable.
  public static final RegistrySupplier<Block> REDDERSTONE_WIRE = BLOCKS.register("redderstone_wire", () ->
    new RedderstoneWireBlock());


  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(MOD_ID, Registry.ITEM_REGISTRY);
  public static final RegistrySupplier<Item> VERMILION_DUST = ITEMS.register("vermilion_dust", () ->
    new CraftingComponentItem(new Item.Properties().tab(RedderstoneMod.TAB)));
  public static final RegistrySupplier<Item> REDDERSTONE_BLOCK_ITEM = ITEMS.register("redderstone_block", () ->
    new DescriptiveBlockItem(
      REDDERSTONE_BLOCK.get(),
      new Item.Properties().tab(RedderstoneMod.TAB),
      "tooltip.redderstone.furnace_heater_1",
      "tooltip.redderstone.furnace_heater_2"
    ));
  public static final RegistrySupplier<Item> FURNACE_HEATER_ITEM = ITEMS.register("furnace_heater", () ->
    new DescriptiveBlockItem(
      FURNACE_HEATER.get(),
      new Item.Properties().tab(RedderstoneMod.TAB),
      "tooltip.redderstone.redderstone_block_1",
      "tooltip.redderstone.redderstone_block_2",
      "tooltip.redderstone.redderstone_block_3"
    ));

  // The following items are included for debugging, but are generally unused.
  public static final RegistrySupplier<Item> REDDERSTONE_WIRE_ITEM = ITEMS.register("redderstone_wire", () ->
    new BlockItem(REDDERSTONE_WIRE.get(), new Item.Properties()));

  public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITY_TYPES =
    DeferredRegister.create(MOD_ID, Registry.BLOCK_ENTITY_TYPE_REGISTRY);
  public static final RegistrySupplier<BlockEntityType<RedderstoneBlockTile>> REDDERSTONE_BLOCK_TYPE = BLOCK_ENTITY_TYPES.register(
    "redderstone_block",
    () -> BlockEntityType.Builder.of(RedderstoneBlockTile::new, REDDERSTONE_BLOCK.get()).build(null)
  );
  public static final RegistrySupplier<BlockEntityType<RedderstoneBlockTile>> FURNACE_HEATER_TYPE = BLOCK_ENTITY_TYPES.register(
    "furnace_heater",
    () -> BlockEntityType.Builder.of(RedderstoneBlockTile::new, FURNACE_HEATER.get()).build(null)
  );

  public static void init() {
    BLOCKS.register();
    ITEMS.register();
    BLOCK_ENTITY_TYPES.register();

    TickEvent.ServerLevelTick.SERVER_LEVEL_POST.register(new RedderstoneTickEvent());

    var blockEvents = new RedderstoneBlockEvent();
    BlockEvent.BREAK.register(blockEvents);
    BlockEvent.PLACE.register(blockEvents);

    try {
      registerRenderTypes();
    } catch (NoSuchMethodError e) {
      System.out.println("Skipping client initialization...");
    }
  }

  @Environment(EnvType.CLIENT)
  public static void registerRenderTypes() {
    RenderTypeRegistry.register(RenderType.cutout(), RedderstoneMod.REDDERSTONE_WIRE.get());
    RenderTypeRegistry.register(RenderType.cutout(), RedderstoneMod.FURNACE_HEATER.get());
  }
}
