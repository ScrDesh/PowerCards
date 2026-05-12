package com.desh.powercards;

import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class CardDefinition {

    // --- Identity ---
    private final String displayName;
    private final Integer colour;
    private final int bpCost;
    private final int maxStack;       // how many copies can be equipped at once
    private final CardRarity rarity;

    // --- The three line types ---
    private final List<AttributeEntry> attributeLines;
    private final List<PassiveEntry> customLines;
    private final List<EffectEntry> effectLines;
    @Nullable
    private final AbilityEntry abilityLine;   // null if card has no ability

    // --- Nested types for the three line kinds ---

    public record AttributeEntry(
            Holder<Attribute> attribute,
            double value,
            AttributeModifier.Operation operation
    ) {}

    public record EffectEntry(
            Holder<MobEffect> entry
    ) {}

    public record PassiveEntry(
            String effectKey,
            double value,
            boolean upIsPositive
    ) {}

    public record AbilityEntry(
            String effectKey,    // looked up in your AbilityEffectRegistry later
            int cooldownTicks
    ) {}

    public enum CardRarity { COMMON, UNCOMMON, RARE, EPIC, LEGENDARY }

    // --- Private constructor, use the Builder ---
    private CardDefinition(Builder builder) {
        this.displayName    = builder.displayName;
        this.colour         = builder.colour;
        this.bpCost         = builder.bpCost;
        this.maxStack       = builder.maxStack;
        this.rarity         = builder.rarity;
        this.attributeLines = List.copyOf(builder.attributeLines);
        this.customLines    = List.copyOf(builder.customLines);
        this.abilityLine    = builder.abilityLine;
        this.effectLines    = List.copyOf(builder.effectLines);
    }

    // --- Getters ---
    public String getDisplayName()              { return displayName; }
    public Integer getColour()                  { return colour; }
    public int getBpCost()                      { return bpCost; }
    public int getMaxStack()                    { return maxStack; }
    public CardRarity getRarity()               { return rarity; }
    public List<AttributeEntry> getAttributeLines() { return attributeLines; }
    public List<PassiveEntry> getCustomLines()  { return customLines; }
    public List<EffectEntry> getEffectLines()   { return effectLines; }
    public boolean isAbility()                  { return abilityLine != null; }
    @Nullable
    public AbilityEntry getAbilityLine()        { return abilityLine; }

    // --- Builder ---
    public static Builder builder(String displayName, Integer colour, int bpCost) {
        return new Builder(displayName, colour, bpCost);
    }

    public static class Builder {
        private final String displayName;
        private final Integer colour;
        private final int bpCost;
        private int maxStack = Integer.MAX_VALUE;
        private CardRarity rarity = CardRarity.COMMON;
        private final List<AttributeEntry> attributeLines = new ArrayList<>();
        private final List<EffectEntry> effectLines = new ArrayList<>();
        private final List<PassiveEntry> customLines = new ArrayList<>();
        @Nullable private AbilityEntry abilityLine = null;

        private Builder(String displayName, Integer colour, int bpCost) {
            this.displayName = displayName;
            this.colour      = colour;
            this.bpCost      = bpCost;
        }

        public Builder maxStack(int maxStack) {
            this.maxStack = maxStack;
            return this;
        }

        public Builder rarity(CardRarity rarity) {
            this.rarity = rarity;
            return this;
        }

        public Builder attribute(Holder<Attribute> attr, double value, AttributeModifier.Operation op) {
            this.attributeLines.add(new AttributeEntry(attr, value, op));
            return this;
        }

        public Builder effect(Holder<MobEffect> effect) {
            this.effectLines.add(new EffectEntry(effect));
            return this;
        };

        public Builder passive(String key, double value, boolean upIsPositive) {
            this.customLines.add(new PassiveEntry(key, value, upIsPositive));
            return this;
        }

        public Builder ability(String key, int cooldownTicks) {
            this.abilityLine = new AbilityEntry(key, cooldownTicks);
            return this;
        }

        public CardDefinition build() {
            return new CardDefinition(this);
        }
    }
}