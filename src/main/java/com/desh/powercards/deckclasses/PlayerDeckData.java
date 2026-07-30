package com.desh.powercards.deckclasses;

import com.desh.powercards.CardDefinition;
import com.desh.powercards.ModAttributes;
import com.desh.powercards.ModRegistries;
import com.desh.powercards.PowerCards;
import com.desh.powercards.packets.ModPackets;
import com.mojang.logging.LogUtils;
import net.minecraft.client.renderer.EffectInstance;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;

import java.util.*;

public class PlayerDeckData {

    private ServerPlayer player;
    private final DeckInventory deckInventory;
    private int totalBP = 3;
    private boolean deckValid = true;
    private final Map<String, Integer> activeEffects = new HashMap<>();
    private final Map<ResourceLocation, AbilityState> abilityStates = new HashMap<>();

    public record AbilityState(long cooldownEndTick) {
        public static final AbilityState READY = new AbilityState(0);

        public boolean isReady(long currentTick) {
            return currentTick >= cooldownEndTick;
        }
    }

    public PlayerDeckData() {
        deckInventory = new DeckInventory();
        deckInventory.setOnChanged(this::rebuildDerivedState);
    }

    public void setPlayer(ServerPlayer player) {
        this.player = player;
    }

    private void syncToClient() {
        if (player != null) ModPackets.syncDeckData(player);
    }


    // Derived State Rebuild

    public void rebuildDerivedState() {
        deckValid = deckInventory.getTotalEquippedCost() <= totalBP;
        activeEffects.clear();

        if (!deckValid) {
            abilityStates.clear();
            syncToClient();
            applyAttributeModifiers();
            return;
        }

        for (CardDefinition def : deckInventory.getEquippedDefinitions()) {
            for (CardDefinition.PassiveEntry passive : def.getCustomLines()) {
                activeEffects.merge(passive.effectKey(), 1, Integer::sum);
            }
        }

        List<ResourceLocation> equippedAbilities = deckInventory.getEquippedAbilities();
        abilityStates.keySet().retainAll(equippedAbilities);
        for (ResourceLocation id : equippedAbilities) {
            abilityStates.putIfAbsent(id, AbilityState.READY);
        }

        applyAttributeModifiers();
        syncToClient();
    }

    // BP

    public void awardBP(int amount) {
        totalBP += amount;
        rebuildDerivedState();
    }

    public void setBP(int amount) {
        totalBP = Math.max(0, amount);
        rebuildDerivedState();
    }

    public void addBP() {
        totalBP = Math.min(27, totalBP+1);
        rebuildDerivedState();
    }

    public int getBaseBP() {
        return totalBP;
    }

    public int getTotalBP() {
        return (int) (totalBP + Math.floor(player.getAttributeValue(ModAttributes.ADDITIONAL_BP)));
    }

    public boolean isDeckValid() {
        return deckValid;
    }

    // Effect and Ability Queries

    public Integer getEffect(String key) {
        return activeEffects.getOrDefault(key, 0);
    }

    public boolean hasEffect(String key) {
        return activeEffects.containsKey(key);
    }

    public AbilityState getAbilityState(ResourceLocation cardId) {
        return abilityStates.getOrDefault(cardId, AbilityState.READY);
    }

    public void setAbilityState(ResourceLocation cardId, AbilityState state) {
        if (abilityStates.containsKey(cardId)) abilityStates.put(cardId, state);
    }

    public DeckInventory getDeckInventory() {
        return deckInventory;
    }

    // NBT Save / Load

    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("totalBP", totalBP);
        tag.put("deck", deckInventory.serializeNBT(provider));
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, CompoundTag tag) {
        totalBP = tag.getInt("totalBP");
        deckInventory.deserializeNBT(provider, tag.getCompound("deck"));
        rebuildDerivedState(); // rebuild after loading so derived state is fresh !!
    }

    private void applyAttributeModifiers() {
        if (player == null) return;

        // Always remove all card modifiers first
        removeAttributeModifiers();

        // Only reapply if deck is valid
        if (!deckValid) return;

        HashMap<MobEffect, Integer> effectsToAdd = new HashMap<>();

        for (CardDefinition def : deckInventory.getEquippedDefinitions()) {

            for (CardDefinition.EffectEntry effect : def.getEffectLines()) {
                MobEffect key = effect.entry().value();
                LogUtils.getLogger().debug(key + " - " + effectsToAdd.getOrDefault(key, 0)+1);
                effectsToAdd.put(key, effectsToAdd.getOrDefault(key, 0)+1);
            }

            for (MobEffect effect : effectsToAdd.keySet()) {
                MobEffectInstance effectInst = new MobEffectInstance(BuiltInRegistries.MOB_EFFECT.wrapAsHolder(effect), -1, effectsToAdd.get(effect)-1, true, true);
                player.addEffect(effectInst);
            }

            for (CardDefinition.AttributeEntry entry : def.getAttributeLines()) {
                var instance = player.getAttributes().getInstance(entry.attribute());
                if (instance == null) continue;

                // Use card ID + attribute path as a stable unique modifier ID
                ResourceLocation modifierId = ResourceLocation.fromNamespaceAndPath(
                        PowerCards.MODID,
                        ModRegistries.CARDS.getKey(def).getPath()
                                + "/" + entry.attribute().getKey().location().getPath() + UUID.randomUUID()
                );

                // Only add if not already present (handles duplicate cards)
                if (instance.getModifier(modifierId) == null) {
                    instance.addPermanentModifier(new AttributeModifier(
                            modifierId, entry.value(), entry.operation()
                    ));
                }
            }
        }
    }

    private void removeAttributeModifiers() {
        if (player == null) return;

        List<Holder<MobEffect>> toRemove = new ArrayList<>();
        for (MobEffectInstance effect : player.getActiveEffects()) {
            if (effect.getDuration() == -1 && effect.isAmbient() && effect.isVisible()) {
                toRemove.add(effect.getEffect());
            }
        }
        for (Holder<MobEffect> effect : toRemove) {
            player.removeEffect(effect);
        }

        Collection <AttributeInstance> currentAtts = player.getAttributes().getSyncableAttributes();
        for (AttributeInstance instance : currentAtts) {

            // Collect IDs to remove (avoid modifying while referencing)
            List<ResourceLocation> toRemove2 = new ArrayList<>();

            for (AttributeModifier modifier : instance.getModifiers()) {
                if (modifier.id().getNamespace().equals("powercards")) {
                    toRemove2.add(modifier.id());
                }
            }

            for (ResourceLocation id : toRemove2) {
                instance.removeModifier(id);
            }

        }
    }
}