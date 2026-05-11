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

    @SubscribeEvent
    public static void modifyPlayerAttributes(EntityAttributeModificationEvent event) {
        event.getTypes().forEach(entityType -> event.add(entityType, ModAttributes.DAMAGE_TAKEN));
        event.getTypes().forEach(entityType -> event.add(entityType, ModAttributes.DAMAGE_DEALT));
    }
}