package com.desh.powercards;

import com.desh.powercards.effects.ModEffects;
import com.desh.powercards.item.CardPack;
import com.desh.powercards.item.PowerCard;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.neoforged.neoforge.common.NeoForgeMod;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.w3c.dom.Attr;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

public class ModCards {

    public static final DeferredRegister<CardDefinition> CARDS =
            DeferredRegister.create(ModRegistries.CARD_REGISTRY_KEY, "powercards");

    public static final DeferredRegister.Items ITEMS =
            DeferredRegister.createItems(PowerCards.MODID);

    private static final Map<ResourceKey<CardDefinition>, DeferredHolder<Item, CardItem>> CARD_ITEMS = new HashMap<>();

    private static DeferredHolder<CardDefinition, CardDefinition> registerCard(String id, Supplier<CardDefinition> supplier) {
        DeferredHolder<CardDefinition, CardDefinition> defHolder = CARDS.register(id, supplier);
        DeferredHolder<Item, CardItem> itemHolder = ITEMS.register(id, () -> new CardItem(defHolder::get));
        CARD_ITEMS.put(defHolder.getKey(), itemHolder);
        return defHolder;
    }

    public static CardItem getCardItem(DeferredHolder<CardDefinition, CardDefinition> definition) {
        return CARD_ITEMS.get(definition.getKey()).value();
    }

    //GET ALL CARD ITEMS IN A
    public static ArrayList<CardItem> getAllCardItems() {
        ArrayList<CardItem> toReturn = new ArrayList<CardItem>();
        for (DeferredHolder<Item, CardItem> i : CARD_ITEMS.values()) {
            toReturn.add(i.get());
        }
        return toReturn;
    }

    //ALT VERSION OF THE FUNCTION TO GET ALL OF A SPECIIFC RARITY
    public static ArrayList<CardItem> getAllCardItems(CardDefinition.CardRarity rarity) {
        ArrayList<CardItem> toReturn = new ArrayList<CardItem>();
        for (DeferredHolder<Item, CardItem> i : CARD_ITEMS.values()) {
            if (rarity == i.value().getDefinition().getRarity()) {toReturn.add(i.get());}
        }
        return toReturn;
    }

    public static final DeferredItem<Item> CARD_PACK = ITEMS.register("card_pack", () -> new CardPack(
            new Item.Properties().stacksTo(1).durability(3).setNoRepair().fireResistant()
    ));

    public static final DeferredItem<Item> CARD_SHREDS = ITEMS.register("card_shreds", () -> new Item(new Item.Properties().stacksTo(64).fireResistant()));
    public static final DeferredItem<Item> POWER_CARD = ITEMS.register("power_card", () -> new PowerCard(new Item.Properties().stacksTo(1).fireResistant().rarity(Rarity.RARE)));

    public static final DeferredHolder<CardDefinition, CardDefinition> STEP_IT_UP =
            registerCard("step_it_up", () -> CardDefinition
                    .builder("card.powercards.step_it_up", 0x55FF55, 2)
                    .rarity(CardDefinition.CardRarity.COMMON)
                    .attribute(Attributes.MOVEMENT_SPEED, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> ITTY_BITTY =
            registerCard("itty_bitty", () -> CardDefinition
                    .builder("card.powercards.itty_bitty", 0xFF5555, 3)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(Attributes.SCALE, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(Attributes.MAX_HEALTH, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE).maxStack(4)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> BIG_UPS =
            registerCard("big_ups", () -> CardDefinition
                    .builder("card.powercards.big_ups", 0x55FFFF, 3)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(Attributes.SCALE, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(Attributes.MAX_HEALTH, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE).maxStack(4)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> HPPLUS =
            registerCard("hpplus", () -> CardDefinition
                    .builder("card.powercards.hpplus", 0xFF5555, 5)
                    .rarity(CardDefinition.CardRarity.EPIC)
                    .attribute(Attributes.MAX_HEALTH, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> SWIMPLUS =
            registerCard("swimplus", () -> CardDefinition
                    .builder("card.powercards.swimplus", 0x55FFFF, 3)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(NeoForgeMod.SWIM_SPEED, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> BOUNCE =
            registerCard("bounce", () -> CardDefinition
                    .builder("card.powercards.bounce", 0xE1CA56, 2)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(Attributes.JUMP_STRENGTH, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(Attributes.SAFE_FALL_DISTANCE, 0.75, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> FORTIFYPLUS =
            registerCard("fortifyplus", () -> CardDefinition
                    .builder("card.powercards.fortifyplus", 0x55FFFF, 8)
                    .rarity(CardDefinition.CardRarity.LEGENDARY)
                    .attribute(ModAttributes.DAMAGE_TAKEN, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE).maxStack(3)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> HEALTHYHEART =
            registerCard("healthyheart", () -> CardDefinition
                    .builder("card.powercards.healthyheart", 0xFF5555, 3)
                    .rarity(CardDefinition.CardRarity.COMMON)
                    .effect(ModEffects.IDLE_HEAL)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> HEARTOFGOLD =
            registerCard("heartofgold", () -> CardDefinition
                    .builder("card.powercards.heartofgold", 0xFDF55F, 3)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .effect(ModEffects.IDLE_ABSORPTION)
                    .attribute(Attributes.MAX_ABSORPTION, 8, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(Attributes.MAX_HEALTH, -4, AttributeModifier.Operation.ADD_VALUE)
                    .maxStack(4)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> RECKLESS =
            registerCard("reckless", () -> CardDefinition
                    .builder("card.powercards.reckless", 0xFF5555, 4)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(ModAttributes.DAMAGE_DEALT, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(ModAttributes.DAMAGE_TAKEN, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> FLAKJACKET =
            registerCard("flakjacket", () -> CardDefinition
                    .builder("card.powercards.flakjacket", 0xB700FF, 3)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(ModAttributes.EXPLOSION_DAMAGE_TAKEN, -0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(Attributes.EXPLOSION_KNOCKBACK_RESISTANCE, 0.4, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .maxStack(3)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> NOWYOUSEEME =
            registerCard("nowyouseeme", () -> CardDefinition
                    .builder("card.powercards.nowyouseeme", 0xFFE100, 0)
                    .rarity(CardDefinition.CardRarity.COMMON)
                    .effect(MobEffects.GLOWING)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> REACHPLUS =
            registerCard("reachplus", () -> CardDefinition
                    .builder("card.powercards.reachplus", 0xDD00FF, 3)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(Attributes.BLOCK_INTERACTION_RANGE, 1, AttributeModifier.Operation.ADD_VALUE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> LONGARMOFTHELAW =
            registerCard("longarmofthelaw", () -> CardDefinition
                    .builder("card.powercards.longarm", 0xFF8800, 5)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(Attributes.ENTITY_INTERACTION_RANGE, 1, AttributeModifier.Operation.ADD_VALUE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> SPELUNKER =
            registerCard("spelunker", () -> CardDefinition
                    .builder("card.powercards.spelunker", 0x3d95ae, 6)
                    .rarity(CardDefinition.CardRarity.EPIC)
                    .attribute(Attributes.BLOCK_BREAK_SPEED, 0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .effect(MobEffects.NIGHT_VISION)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> FREE_AIR =
            registerCard("freewater", () -> CardDefinition
                    .builder("card.powercards.freewater", 0x00c8ff, 2)
                    .rarity(CardDefinition.CardRarity.COMMON)
                    .attribute(Attributes.OXYGEN_BONUS, 1, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(NeoForgeMod.SWIM_SPEED, -0.2, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> WARRIOR =
            registerCard("warrior", () -> CardDefinition
                    .builder("card.powercards.warrior", 0xff0800, 5)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(ModAttributes.MELEE_DAMAGE_DEALT, 0.25, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(ModAttributes.RANGED_DAMAGE_DEALT, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> RANGER =
            registerCard("ranger", () -> CardDefinition
                    .builder("card.powercards.ranger", 0xff6a00, 5)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(ModAttributes.RANGED_DAMAGE_DEALT, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(ModAttributes.MELEE_DAMAGE_DEALT, -0.75, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> RECOVERY =
            registerCard("recovery", () -> CardDefinition
                    .builder("card.powercards.recovery", 0xffe100, 1)
                    .rarity(CardDefinition.CardRarity.COMMON)
                    .attribute(ModAttributes.HEALING_TAKEN, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> THE_DEEP_END =
            registerCard("thedeepend", () -> CardDefinition
                    .builder("card.powercards.thedeepend", 0xff0004, 0)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .attribute(ModAttributes.ADDITIONAL_BP, 3, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(ModAttributes.DAMAGE_DEALT, -0.35, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(ModAttributes.DAMAGE_TAKEN, 0.5, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> FLIMSY =
            registerCard("flimsy", () -> CardDefinition
                    .builder("card.powercards.flimsy", 0xff0004, 0)
                    .rarity(CardDefinition.CardRarity.COMMON)
                    .attribute(ModAttributes.ADDITIONAL_BP, 1, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(ModAttributes.DAMAGE_DEALT, -0.20, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> UHC =
            registerCard("uhc", () -> CardDefinition
                    .builder("card.powercards.uhc", 0xff0004, 0)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .attribute(ModAttributes.ADDITIONAL_BP, 2, AttributeModifier.Operation.ADD_VALUE)
                    .effect(ModEffects.IDLE_HEAL)
                    .attribute(ModAttributes.HEALING_TAKEN, -1, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> FRAGILE =
            registerCard("fragile", () -> CardDefinition
                    .builder("card.powercards.fragile", 0xff0004, 0)
                    .rarity(CardDefinition.CardRarity.EPIC)
                    .attribute(ModAttributes.ADDITIONAL_BP, 5, AttributeModifier.Operation.ADD_VALUE)
                    .attribute(Attributes.MAX_HEALTH, -0.8, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .maxStack(1)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> FEEL_FINE =
            registerCard("feelfine", () -> CardDefinition
                    .builder("card.powercards.feelfine", 0xfbc712, 2)
                    .attribute(ModAttributes.NEGATIVE_EFFECT_DURATION, -0.5, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL)
                    .rarity(CardDefinition.CardRarity.COMMON)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> TOXIC =
            registerCard("toxic", () -> CardDefinition
                    .builder("card.powercards.toxic", 0x00b818, 3)
                    .passive("passive.powercards.attacks_poison", true)
                    .maxStack(3)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> WEIGHTLESS =
            registerCard("weightless", () -> CardDefinition
                    .builder("card.powercards.weightless", 0xd58fdc, 7)
                    .passive("passive.powercards.attacks_levitation", true)
                    .maxStack(3)
                    .rarity(CardDefinition.CardRarity.LEGENDARY)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> WEBBING =
            registerCard("webbing", () -> CardDefinition
                    .builder("card.powercards.webbing", 0x7c5b5a, 3)
                    .passive("passive.powercards.attacks_slowness", true)
                    .maxStack(3)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> TUNNEL_VISION =
            registerCard("tunnelvision", () -> CardDefinition
                    .builder("card.powercards.tunnelvision", 0x6b8794, 1)
                    .passive("passive.powercards.attacks_darkness", true)
                    .maxStack(1)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> VENGEFUL_GHOST =
            registerCard("spiritofspite", () -> CardDefinition
                    .builder("card.powercards.spiritofspite", 0x8a7975, 2)
                    .passive("passive.powercards.spiritofspite", true)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> HOLDING =
            registerCard("holding", () -> CardDefinition
                    .builder("card.powercards.holding", 0xb700ff, 6)
                    .passive("passive.powercards.holding", true)
                    .maxStack(1)
                    .rarity(CardDefinition.CardRarity.EPIC)
                    .build());
//00c8ff
    public static final DeferredHolder<CardDefinition, CardDefinition> BLOOM =
            registerCard("bloom", () -> CardDefinition
                    .builder("card.powercards.bloom", 0xffa5fa, 0)
                    .passive("passive.powercards.cosmetic_bloom", true)
                    .maxStack(1)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> BADDAY =
            registerCard("badday", () -> CardDefinition
                    .builder("card.powercards.badday", 0x00c8ff, 0)
                    .passive("passive.powercards.cosmetic_badday", true)
                    .maxStack(1)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> LEECH =
            registerCard("leech", () -> CardDefinition
                    .builder("card.powercards.leech", 0xf7ff00, 2)
                    .attribute(ModAttributes.LIFESTEAL, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(ModAttributes.DAMAGE_DEALT, -0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .maxStack(5)
                    .rarity(CardDefinition.CardRarity.COMMON)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> WORKETHIC =
            registerCard("workethic", () -> CardDefinition
                    .builder("card.powercards.workethic", 0xfffb00, 2)
                    .attribute(Attributes.BLOCK_BREAK_SPEED, 0.1, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .rarity(CardDefinition.CardRarity.UNCOMMON)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> EYESOPEN =
            registerCard("eyesopen", () -> CardDefinition
                    .builder("card.powercards.eyesopen", 0xcfcfcf, 2)
                    .passive("passive.powercards.immunityto.darkness", true)
                    .passive("passive.powercards.immunityto.blindness", true)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> LUNAR =
            registerCard("lunar", () -> CardDefinition
                    .builder("card.powercards.lunar", 0x7300ff, 4)
                    .attribute(Attributes.GRAVITY, -0.8, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(Attributes.FALL_DAMAGE_MULTIPLIER, -0.8, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .attribute(Attributes.SAFE_FALL_DISTANCE, 3, AttributeModifier.Operation.ADD_MULTIPLIED_BASE)
                    .passive("passive.powercards.cannotusemace", false)
                    .maxStack(1)
                    .rarity(CardDefinition.CardRarity.LEGENDARY)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> TENACITY =
            registerCard("tenacity", () -> CardDefinition
                    .builder("card.powercards.tenacity", 0xFF001A, 6)
                    .passive("passive.powercards.tenacity", true)
                    .maxStack(1)
                    .rarity(CardDefinition.CardRarity.EPIC)
                    .build());

    public static final DeferredHolder<CardDefinition, CardDefinition> GET_LOW =
            registerCard("spring_jump", () -> CardDefinition
                    .builder("card.powercards.spring_jump", 0x95FF00, 1)
                    .passive("passive.powercards.spring_jump", true)
                    .rarity(CardDefinition.CardRarity.RARE)
                    .build());
}
