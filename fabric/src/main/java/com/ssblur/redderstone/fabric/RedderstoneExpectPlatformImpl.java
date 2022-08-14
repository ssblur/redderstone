package com.ssblur.redderstone.fabric;

import com.ssblur.redderstone.RedderstoneExpectPlatform;
import net.fabricmc.loader.api.FabricLoader;

import java.nio.file.Path;

public class RedderstoneExpectPlatformImpl {
    /**
     * This is our actual method to {@link RedderstoneExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FabricLoader.getInstance().getConfigDir();
    }
}
