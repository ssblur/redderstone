package com.ssblur.redderstone.forge;

import dev.architectury.platform.forge.EventBuses;
import com.ssblur.redderstone.RedderstoneMod;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod(RedderstoneMod.MOD_ID)
public class RedderstoneModForge {
    public RedderstoneModForge() {
        // Submit our event bus to let architectury register our content on the right time
        EventBuses.registerModEventBus(RedderstoneMod.MOD_ID, FMLJavaModLoadingContext.get().getModEventBus());
        RedderstoneMod.init();
    }
}
