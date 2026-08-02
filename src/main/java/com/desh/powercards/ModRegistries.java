package com.desh.powercards;

import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;

public class ModRegistries {
    public static final ResourceKey<Registry<CardDefinition>> CARD_REGISTRY_KEY =
            ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("powercards", "cards"));

    public static Registry<CardDefinition> CARDS;

    public static void register(IEventBus modBus) {
        modBus.addListener(ModRegistries::onNewRegistry);
    }

    private static void onNewRegistry(NewRegistryEvent event) {
        CARDS = event.create(new RegistryBuilder<>(CARD_REGISTRY_KEY));
    }
}
