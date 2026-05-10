package com.desh.powercards.deckclasses;

import com.desh.powercards.packets.ClientPacketHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public class DeckScreen extends AbstractContainerScreen<DeckMenu> {

    //
    public DeckScreen(DeckMenu menu, Inventory playerInventory, Component title) {
        super(menu, playerInventory, title);
        this.imageWidth  = 284;
        this.imageHeight = 134;
        this.inventoryLabelY = 32;
        this.titleLabelX = 186;
        this.titleLabelY = 7;
    }

    private static final ResourceLocation CONTAINER_TEXTURE =
            ResourceLocation.fromNamespaceAndPath("powercards", "textures/gui/container/deck.png");

    @Override
    protected void renderBg(GuiGraphics graphics, float partialTick, int mouseX, int mouseY) {
        graphics.blit(CONTAINER_TEXTURE, leftPos, topPos, 0, 0, imageWidth, imageHeight, imageWidth, imageHeight);



        if (ClientPacketHandler.getClientTotalBP() > 27) {
            String bpText = ClientPacketHandler.isClientDeckValid() ?
                    "BP: " + (ClientPacketHandler.getClientTotalBP()-menu.getDeckInventory().getTotalEquippedCost())
                            + "/" + ClientPacketHandler.getClientTotalBP(): "Too Expensive!";
            int color = ClientPacketHandler.isClientDeckValid() ? 0x55FF55 : 0xFF5555;
            graphics.drawString(Minecraft.getInstance().font, bpText, leftPos + 8, topPos + 12, color);
        }
        else {
            int underlayColour = ClientPacketHandler.isClientDeckValid() ? 0x222222 : 0xFF5555;
            String bpBarUnderlay = "◆".repeat(ClientPacketHandler.getClientTotalBP());
            graphics.drawString(Minecraft.getInstance().font,
                    bpBarUnderlay, leftPos + 9, topPos + 13, underlayColour, false);

            String bpBarOverlay = "◆".repeat(Math.max(0, ClientPacketHandler.getClientTotalBP()-menu.getDeckInventory().getTotalEquippedCost()));
            graphics.drawString(Minecraft.getInstance().font,
                    bpBarOverlay, leftPos + 8, topPos + 12, 0x55FF55, false);
        }
    }

    @Override
    public void render(GuiGraphics graphics, int mouseX, int mouseY,
                       float partialTick) {
        renderBackground(graphics, mouseX, mouseY, partialTick);
        super.render(graphics, mouseX, mouseY, partialTick);
        renderTooltip(graphics, mouseX, mouseY);
    }
}