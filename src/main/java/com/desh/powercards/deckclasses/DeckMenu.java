package com.desh.powercards.deckclasses;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.SlotItemHandler;

public class DeckMenu extends AbstractContainerMenu {

    private final DeckInventory deckInventory;

    // Server constructor gets real inventory from attachment
    public DeckMenu(int containerId, Inventory playerInventory, boolean isServer) {
        super(ModMenus.DECK_MENU.get(), containerId);
        ServerPlayer player = (ServerPlayer) playerInventory.player;
        this.deckInventory = player.getData(DeckAttachment.DECK_DATA).getDeckInventory();
        setupSlots(playerInventory);
    }

    // Client constructor referenced by MenuType registration
    public DeckMenu(int containerId, Inventory playerInventory) {
        super(ModMenus.DECK_MENU.get(), containerId);
        this.deckInventory = new DeckInventory();
        setupSlots(playerInventory);
    }

    public DeckInventory getDeckInventory() { return deckInventory; }

    private void setupSlots(Inventory playerInventory) {
        // --- Deck slots (4x4 grid) ---
        for (int row = 0; row < 6; row++) {
            for (int col = 0; col < 5; col++) {
                int slotIndex = row * 5 + col;
                addSlot(new SlotItemHandler(deckInventory, slotIndex,
                        187 + col * 18, 19 + row * 18));
            }
        }

        // player inv
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 9; col++) {
                addSlot(new Slot(playerInventory,
                        col + row * 9 + 9,
                        9 + col * 18, 44 + row * 18));
            }
        }

        // player hotbar
        for (int col = 0; col < 9; col++) {
            addSlot(new Slot(playerInventory, col, 9 + col * 18, 102));
        }
    }

    // called when shift-clicking..... moves item between deck and inventory
    @Override
    public ItemStack quickMoveStack(Player player, int slotIndex) {
        ItemStack result = ItemStack.EMPTY;
        Slot slot = slots.get(slotIndex);

        if (slot.hasItem()) {
            ItemStack stack = slot.getItem();
            result = stack.copy();

            int deckSlots = DeckInventory.DECK_SIZE;

            if (slotIndex < deckSlots) {
                if (!moveItemStackTo(stack, deckSlots, slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else {
                if (!moveItemStackTo(stack, 0, deckSlots, false)) {
                    return ItemStack.EMPTY;
                }
            }

            if (stack.getCount() == result.getCount()) {
                return ItemStack.EMPTY;
            }

            slot.onTake(player, stack);

            if (stack.isEmpty()) slot.set(ItemStack.EMPTY);
            else slot.setChanged();
        }

        return result;
    }

    // whether the player is still valid to have this menu open
    @Override
    public boolean stillValid(Player player) {
        return true;
    }
}