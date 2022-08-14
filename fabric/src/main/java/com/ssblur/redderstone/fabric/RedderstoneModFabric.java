package com.ssblur.redderstone.fabric;

import com.ssblur.redderstone.RedderstoneMod;
import net.fabricmc.api.ModInitializer;

public class RedderstoneModFabric implements ModInitializer {
    @Override
    public void onInitialize() {
        RedderstoneMod.init();
    }
}
