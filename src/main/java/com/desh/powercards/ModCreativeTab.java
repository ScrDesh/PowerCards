package com.desh.powercards;

import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.checkerframework.checker.units.qual.C;

import java.util.Comparator;
import java.util.List;
import java.util.function.Supplier;

public class ModCreativeTab {

    public static final DeferredRegister<CreativeModeTab> CREATIVE_TAB =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, PowerCards.MODID);

    public static final Supplier<CreativeModeTab> CARDS_TAB = CREATIVE_TAB.register("powercards_tab",
            () -> CreativeModeTab.builder()
                    .icon(() -> new ItemStack(BuiltInRegistries.ITEM.get(ResourceLocation.fromNamespaceAndPath("powercards", "hpplus"))))
                    .title(Component.translatable("creativetab.powercards.cards"))
                    .displayItems(((itemDisplayParameters, output) -> {
                        List<ItemStack> cards = BuiltInRegistries.ITEM.entrySet().stream()
                                .filter(entry -> entry.getKey().location().getNamespace().equals("powercards"))
                                .map(entry -> new ItemStack(entry.getValue()))
                                .sorted(Comparator.comparingInt(stack -> {
                                    if (stack.getItem() instanceof CardItem card) {
                                        return card.getDefinition().getBpCost();
                                    }
                                    return 0;
                                })).toList();
                        output.acceptAll(cards);
                    }))
                    .build());

    public static void register(IEventBus eventBus) {
        CREATIVE_TAB.register(eventBus);
    }
}
