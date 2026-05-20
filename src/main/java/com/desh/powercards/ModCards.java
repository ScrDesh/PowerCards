package com.desh.powercards;

import com.desh.powercards.effects.ModEffects;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.w3c.dom.Attr;

import java.util.ArrayList;
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

    public static final DeferredHolder<CardDefinition, CardDefinition> FORTIFYPLUS =
            registerCard("fortifyplus", () -> CardDefinition
                    .builder("Fortify+", 0x55FFFF, 8)
                    .rarity(CardDefinition.CardRarity.LEGENDARY)
                    .attribute(ModAttributes.DAMAGE_TAKEN, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE).maxStack(3)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> HEALTHYHEART =
            registerCard("healthyheart", () -> CardDefinition
                    .builder("Healthy Heart", 0xFF5555, 3)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .effect(ModEffects.IDLE_HEAL)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> HEARTOFGOLD =
            registerCard("heartofgold", () -> CardDefinition
                    .builder("Heart of Gold", 0xFDF55F, 6)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .effect(ModEffects.IDLE_ABSORPTION)
                    .attribute(Attributes.MAX_ABSORPTION, 10, AttributeModifier.Operation.ADD_VALUE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> RECKLESS =
            registerCard("reckless", () -> CardDefinition
                    .builder("Reckless", 0xFF5555, 4)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(ModAttributes.DAMAGE_DEALT, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(ModAttributes.DAMAGE_TAKEN, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> FLAKJACKET =
            registerCard("flakjacket", () -> CardDefinition
                    .builder("Flak Jacket", 0xB700FF, 3)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(ModAttributes.EXPLOSION_DAMAGE_TAKEN, -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 0.4, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .maxStack(3)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> NOWYOUSEEME =
            registerCard("nowyouseeme", () -> CardDefinition
                    .builder("Now You See Me", 0xFFE100, 0)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .effect(MobEffects.GLOWING)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> REACHPLUS =
            registerCard("reachplus", () -> CardDefinition
                    .builder("Reach+", 0xDD00FF, 3)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(Attributes.BLOCK_INTERACTION_RANGE, 1, AttributeModifier.Operation.ADD_VALUE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> LONGARMOFTHELAW =
            registerCard("longarmofthelaw", () -> CardDefinition
                    .builder("Long Arm of The Law", 0xFF8800, 5)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, 1, AttributeModifier.Operation.ADD_VALUE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> SPELUNKER =
            registerCard("spelunker", () -> CardDefinition
                    .builder("Spelunker", 0x3d95ae, 6)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(Attributes.BLOCK_BREAK_SPEED, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .effect(MobEffects.NIGHT_VISION)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> FREE_AIR =
            registerCard("freewater", () -> CardDefinition
                    .builder("Free Air", 0x00c8ff, 2)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(Attributes.OXYGEN_BONUS, 1, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(NeoForgeMod.SWIM_SPEED, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> WARRIOR =
            registerCard("warrior", () -> CardDefinition
                    .builder("Warrior", 0xff0800, 5)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(ModAttributes.MELEE_DAMAGE_DEALT, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(ModAttributes.RANGED_DAMAGE_DEALT, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> RANGER =
            registerCard("ranger", () -> CardDefinition
                    .builder("Ranger", 0xff6a00, 5)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(ModAttributes.RANGED_DAMAGE_DEALT, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(ModAttributes.MELEE_DAMAGE_DEALT, -0.75, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> RECOVERY =
            registerCard("recovery", () -> CardDefinition
                    .builder("Recovery", 0xffe100, 1)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(ModAttributes.HEALING_TAKEN, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> THE_DEEP_END =
            registerCard("thedeepend", () -> CardDefinition
                    .builder("The Deep End", 0xff0004, 0)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(ModAttributes.ADDITIONAL_BP, 3, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(ModAttributes.DAMAGE_DEALT, -0.35, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(ModAttributes.DAMAGE_TAKEN, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> FLIMSY =
            registerCard("flimsy", () -> CardDefinition
                    .builder("Flimsy", 0xff0004, 0)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(ModAttributes.ADDITIONAL_BP, 1, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(ModAttributes.DAMAGE_DEALT, -0.20, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> UHC =
            registerCard("uhc", () -> CardDefinition
                    .builder("Ultra Hardcore", 0xff0004, 0)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(ModAttributes.ADDITIONAL_BP, 2, AttributeModifier.Operation.ADD_VALUE)
                    .effect(ModEffects.IDLE_HEAL)
                    .attribute(ModAttributes.HEALING_TAKEN, -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> FRAGILE =
            registerCard("fragile", () -> CardDefinition
                    .builder("Fragile", 0xff0004, 0)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(ModAttributes.ADDITIONAL_BP, 5, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(Attributes.MAX_HEALTH, -0.8, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> FEEL_FINE =
            registerCard("feelfine", () -> CardDefinition
                    .builder("Feelin' Fine", 0xfbc712, 2)
                    .attribute(ModAttributes.NEGATIVE_EFFECT_DURATION, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> TOXIC =
            registerCard("toxic", () -> CardDefinition
                    .builder("Toxic", 0x00b818, 3)
                    .passive("passive.powercards.attacks_poison", true)
                    .maxStack(3)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> WEIGHTLESS =
            registerCard("weightless", () -> CardDefinition
                    .builder("Weightless", 0xd58fdc, 7)
                    .passive("passive.powercards.attacks_levitation", true)
                    .maxStack(3)
                    .rarity(CardDefinition.CardRarity.LEGENDARY)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> WEBBING =
            registerCard("webbing", () -> CardDefinition
                    .builder("Webbing", 0x7c5b5a, 3)
                    .passive("passive.powercards.attacks_slowness", true)
                    .maxStack(3)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> TUNNEL_VISION =
            registerCard("tunnelvision", () -> CardDefinition
                    .builder("Tunnel Vision", 0x6b8794, 1)
                    .passive("passive.powercards.attacks_darkness", true)
                    .maxStack(1)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .build());
}
