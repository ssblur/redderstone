package com.ssblur.redderstone.forge;

import com.ssblur.redderstone.RedderstoneExpectPlatform;
import net.minecraftforge.fml.loading.FMLPaths;

import java.nio.file.Path;

public class RedderstoneExpectPlatformImpl {
    /**
     * This is our actual method to {@link RedderstoneExpectPlatform#getConfigDirectory()}.
     */
    public static Path getConfigDirectory() {
        return FMLPaths.CONFIGDIR.get();
    }
}
