package com.desh.powercards;

import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.RangedAttribute;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.registries.DeferredRegister;

@EventBusSubscriber(modid = PowerCards.MODID, bus = EventBusSubscriber.Bus.MOD)
public class ModAttributes {
    public static void register(IEventBus eventBus) {
        ATTRIBUTES.register(eventBus);
    }

    public static final DeferredRegister<Attribute> ATTRIBUTES =
            DeferredRegister.create(Registries.ATTRIBUTE, "powercards");

    public static final Holder<Attribute> DAMAGE_TAKEN =
            ATTRIBUTES.register("damage_taken", () ->
                    new RangedAttribute("attribute.powercards.damage_taken", 1, 0, 1000)
                            .setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE)
            );

    public static final Holder<Attribute> DAMAGE_DEALT =
            ATTRIBUTES.register("damage_dealt", () ->
                    new RangedAttribute("attribute.powercards.damage_dealt", 1, 0, 1000)
                            .setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
            );

    public static final Holder<Attribute> EXPLOSION_DAMAGE_TAKEN =
            ATTRIBUTES.register("explosion_damage_taken", () ->
                    new RangedAttribute("attribute.powercards.explosion_damage_taken", 1, 0, 1000)
                            .setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE)
            );

    public static final Holder<Attribute> HEALING_TAKEN =
            ATTRIBUTES.register("healing_taken", () ->
                    new RangedAttribute("attribute.powercards.healing_taken", 1, 0, 1000)
                            .setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
            );

    public static final Holder<Attribute> MELEE_DAMAGE_DEALT =
            ATTRIBUTES.register("melee_damage_dealt", () ->
                    new RangedAttribute("attribute.powercards.melee_damage_dealt", 1, 0, 1000)
                            .setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
            );

    public static final Holder<Attribute> RANGED_DAMAGE_DEALT =
            ATTRIBUTES.register("ranged_damage_dealt", () ->
                    new RangedAttribute("attribute.powercards.ranged_damage_dealt", 1, 0, 1000)
                            .setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
            );

    public static final Holder<Attribute> ADDITIONAL_BP =
            ATTRIBUTES.register("bp_bonus", () ->
                    new RangedAttribute("attribute.powercards.bp_bonus", 0, 0, 1000)
                            .setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
            );

    public static final Holder<Attribute> NEGATIVE_EFFECT_DURATION =
            ATTRIBUTES.register("negative_effect_duration", () ->
                    new RangedAttribute("attribute.powercards.negative_effect_duration", 1, 0, 1000)
                            .setSyncable(true).setSentiment(Attribute.Sentiment.NEGATIVE)
            );

    public static final Holder<Attribute> POSITIVE_EFFECT_DURATION =
            ATTRIBUTES.register("positive_effect_duration", () ->
                    new RangedAttribute("attribute.powercards.positive_effect_duration", 1, 0, 1000)
                            .setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
            );

    public static final Holder<Attribute> LIFESTEAL =
            ATTRIBUTES.register("lifesteal", () ->
                    new RangedAttribute("attribute.powercards.lifesteal", 1, 0, 1000)
                            .setSyncable(true).setSentiment(Attribute.Sentiment.POSITIVE)
            );

    @SubscribeEvent
    public static void modifyPlayerAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> event.add(entityType, ModAttributes.DAMAGE_TAKEN));
        event.getTypes().forEach(entityType -> event.add(entityType, ModAttributes.DAMAGE_DEALT));
        event.getTypes().forEach(entityType -> event.add(entityType, ModAttributes.EXPLOSION_DAMAGE_TAKEN));
        event.getTypes().forEach(entityType -> event.add(entityType, ModAttributes.HEALING_TAKEN));
        event.getTypes().forEach(entityType -> event.add(entityType, ModAttributes.MELEE_DAMAGE_DEALT));
        event.getTypes().forEach(entityType -> event.add(entityType, ModAttributes.RANGED_DAMAGE_DEALT));
        event.getTypes().forEach(entityType -> event.add(entityType, ModAttributes.ADDITIONAL_BP));
        event.getTypes().forEach(entityType -> event.add(entityType, ModAttributes.NEGATIVE_EFFECT_DURATION));
        event.getTypes().forEach(entityType -> event.add(entityType, ModAttributes.LIFESTEAL));
    }
}