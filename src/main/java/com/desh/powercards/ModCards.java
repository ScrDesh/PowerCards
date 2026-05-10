package com.desh.powercards;

import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModCards {
    public static final DeferredRegister<CardDefinition> CARDS =
            DeferredRegister.create(ModRegistries.CARD_REGISTRY_KEY, "powercards");

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems("powercards");

    private static DeferredHolder<CardDefinition, CardDefinition> registerCard(String id, Supplier<CardDefinition> supplier) {
        DeferredHolder<CardDefinition, CardDefinition> holder = CARDS.register(id, supplier);
        ITEMS.register(id, () -> new CardItem(holder::get));
        return holder;
    }

    public static final DeferredHolder<CardDefinition, CardDefinition> STEP_IT_UP =
            registerCard("step_it_up", () -> CardDefinition
                    .builder("Step It Up", 0x55FF55, 2)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(Attributes.MOVEMENT_SPEED, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> ITTY_BITTY =
            registerCard("itty_bitty", () -> CardDefinition
                    .builder("Itty Bitty", 0xFF5555, 3)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(Attributes.SCALE, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(Attributes.MAX_HEALTH, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE).maxStack(4)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> BIG_UPS =
            registerCard("big_ups", () -> CardDefinition
                    .builder("Big Ups", 0x55FFFF, 3)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(Attributes.SCALE, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(Attributes.MAX_HEALTH, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE).maxStack(4)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> HPPLUS =
            registerCard("hpplus", () -> CardDefinition
                    .builder("HP+", 0xFF5555, 5)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(Attributes.MAX_HEALTH, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> SWIMPLUS =
            registerCard("swimplus", () -> CardDefinition
                    .builder("Swim+", 0x55FFFF, 3)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(NeoForgeMod.SWIM_SPEED, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> BOUNCE =
            registerCard("bounce", () -> CardDefinition
                    .builder("Bounce", 0xE1CA56, 2)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(Attributes.JUMP_STRENGTH, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(Attributes.SAFE_FALL_DISTANCE, 0.75, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());
}
