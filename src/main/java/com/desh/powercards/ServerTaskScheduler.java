package com.desh.powercards;

import net.minecraft.server.MinecraftServer;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.ServerTickEvent;

import java.util.ArrayList;
import java.util.List;

@EventBusSubscriber(modid = PowerCards.MODID)
public final class ServerTaskScheduler {
    private record ScheduledTask(long targetTick, Runnable task) {}

    private static final List<ScheduledTask> TASKS = new ArrayList<>();

    public static void scheduleIn(MinecraftServer server, int delayTicks, Runnable task) {
        TASKS.add(new ScheduledTask(server.getTickCount() + delayTicks, task));
    }

    @SubscribeEvent
    public static void onServerTick(ServerTickEvent.Post event) {
        long now = event.getServer().getTickCount();
        TASKS.removeIf(scheduled -> {
            if (scheduled.targetTick() <= now) {
                scheduled.task().run();
                return true;
            }
            return false;
        });
    }
}