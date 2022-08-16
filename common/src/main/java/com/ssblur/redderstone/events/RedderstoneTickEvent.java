package com.ssblur.redderstone.events;

import com.ssblur.redderstone.layer.RedderstoneLayer;
import dev.architectury.event.events.common.TickEvent;
import net.minecraft.server.level.ServerLevel;

public class RedderstoneTickEvent implements TickEvent.ServerLevelTick {
  @Override
  public void tick(ServerLevel instance) {
    RedderstoneLayer.getMap(instance).tick(instance);
  }
}
