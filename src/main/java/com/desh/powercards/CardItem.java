package com.desh.powercards;

import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.components.ChatComponent;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

public class CardItem extends Item {

    private final Supplier<CardDefinition> definitionSupplier;

    public CardItem(Supplier<CardDefinition> definitionSupplier) {
        super(new Item.Properties().stacksTo(1));
        this.definitionSupplier = definitionSupplier;
    }

    public CardDefinition getDefinition() {
        return definitionSupplier.get();
    }

    @Override
    public Component getName(ItemStack stack) {
        return Component.literal(definitionSupplier.get().getDisplayName())
                .withColor(definitionSupplier.get().getColour());
    }

    public static Attribute.Sentiment getSentiment(Attribute attribute) {
        try {
            Field field = Attribute.class.getDeclaredField("sentiment");
            field.setAccessible(true);
            return (Attribute.Sentiment) field.get(attribute);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            return Attribute.Sentiment.NEUTRAL; // safe fallback
        }
    }

    @Override
    public void appendHoverText(ItemStack stack, TooltipContext context,
                                List<Component> lines, TooltipFlag flag) {
        CardDefinition def = getDefinition();

        lines.add(Component.literal("Cost: ").withStyle(ChatFormatting.GRAY).append(
                Component.literal(def.getBpCost() + " " + "◆".repeat(def.getBpCost()))
                        .withStyle(ChatFormatting.GREEN)
        ));
        if (def.getMaxStack() < 100) {lines.add(Component.literal("Max Stack: " + def.getMaxStack()).withStyle(ChatFormatting.GRAY));}
        lines.add(Component.empty());
        lines.add(Component.literal("When in Deck:").withStyle(ChatFormatting.GRAY));

        for (CardDefinition.AttributeEntry entry : def.getAttributeLines()) {
            String opSymbol = (entry.value() >= 0) ? "+" : "";
            ChatFormatting col;

            // get sentiment and display colour accordingly
            if (getSentiment(entry.attribute().value()) == Attribute.Sentiment.NEGATIVE)
                {col = (entry.value() >= 0) ? ChatFormatting.RED : ChatFormatting.BLUE;}
            else
                {col = (entry.value() >= 0) ? ChatFormatting.BLUE : ChatFormatting.RED;}

            String valueStr = entry.operation() == AttributeModifier.Operation.ADD_VALUE
                    ? opSymbol + entry.value()
                    : opSymbol + (entry.value() * 100) + "%";
            valueStr = valueStr.replace(".0", "");
            lines.add(Component.literal(valueStr + " ").append(Component.translatable(entry.attribute().value().getDescriptionId()))
                    .withStyle(col));
        }

        for (CardDefinition.EffectEntry effect : def.getEffectLines()) {
            ChatFormatting col = effect.entry().value().isBeneficial() ? ChatFormatting.BLUE : ChatFormatting.RED;
            lines.add(Component.literal("+Permanent ").append(Component.translatable(effect.entry().value().getDisplayName().getString())).withStyle(col));
        }

        for (CardDefinition.PassiveEntry passive : def.getCustomLines()) {
            lines.add(Component.literal(passive.effectKey() + ": " + passive.value())
                    .withStyle(ChatFormatting.GREEN));
        }

        if (def.isAbility()) {
            lines.add(Component.literal("Ability: " + def.getAbilityLine().effectKey())
                    .withStyle(ChatFormatting.LIGHT_PURPLE));
            lines.add(Component.literal("Cooldown: "
                            + def.getAbilityLine().cooldownTicks() / 20 + "s")
                    .withStyle(ChatFormatting.GRAY));
        }


    }
}