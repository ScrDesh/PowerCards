package com.desh.powercards.deckclasses;

import com.desh.powercards.CardDefinition;
import com.desh.powercards.CardItem;
import com.desh.powercards.ModRegistries;
import com.mojang.logging.LogUtils;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.ItemStackHandler;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;

public class DeckInventory extends ItemStackHandler {

    public static final int DECK_SIZE = 30;

    @Nullable
    private Runnable onChanged;

    public DeckInventory() {
        super(DECK_SIZE);
    }

    public void setOnChanged(Runnable onChanged) {
        this.onChanged = onChanged;
    }

    @Override
    public boolean isItemValid(int slot, ItemStack stack) {

        if (stack.getItem() instanceof CardItem && ((CardItem) stack.getItem()).getDefinition().getMaxStack() > 30) {return true;}
        else if (stack.getItem() instanceof CardItem cardItem) {
            return (getCopies(cardItem.getDefinition().getDisplayName()) < cardItem.getDefinition().getMaxStack());
        };
        return false;
    }

    @Override
    protected void onContentsChanged(int slot) {
        if (onChanged != null) {
            onChanged.run();
        }
    }

    public Integer getCopies(String type) {
        Integer count = 0;
        for (int i = 0; i < getSlots(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack.getItem() instanceof CardItem cardItem) {
                if (cardItem.getDefinition().getDisplayName().equals(type)) {count++;}
            }
        }
        return count;
    }

    public List<CardDefinition> getEquippedDefinitions() {
        List<CardDefinition> result = new ArrayList<>();
        for (int i = 0; i < getSlots(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack.getItem() instanceof CardItem cardItem) {
                for (int copy = 0; copy < stack.getCount(); copy++) {
                    result.add(cardItem.getDefinition());
                }
            }
        }
        return result;
    }

    public int getTotalEquippedCost() {
        return getEquippedDefinitions().stream()
                .mapToInt(CardDefinition::getBpCost)
                .sum();
    }

    public List<ResourceLocation> getEquippedAbilities() {
        List<ResourceLocation> result = new ArrayList<>();
        for (int i = 0; i < getSlots(); i++) {
            ItemStack stack = getStackInSlot(i);
            if (stack.getItem() instanceof CardItem cardItem) {
                CardDefinition def = cardItem.getDefinition();
                if (def.isAbility()) {
                    result.add(ModRegistries.CARDS.getKey(def));
                }
            }
        }
        return result;
    }
}