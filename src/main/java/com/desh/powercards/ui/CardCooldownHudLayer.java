package com.desh.powercards.ui;

import com.desh.powercards.CardItem;
import com.desh.powercards.ModCards;
import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import java.util.ArrayList;

public class CardCooldownHudLayer implements LayeredDraw.Layer {

    private static final int ICON_SIZE = 32;
    private static final int PADDING = -2;

    @Override
    public void render(GuiGraphics guiGraphics, DeltaTracker deltaTracker) {
        Minecraft mc = Minecraft.getInstance();
        LocalPlayer player = mc.player;

        if (player == null || mc.options.hideGui) return;

        ArrayList<CardItem> cardItems = ModCards.getAllCardItems();

        int x = 10;
        int y = 10;

        for (Item item : cardItems) {
            if (!player.getCooldowns().isOnCooldown(item)) {continue;}

            ItemStack displayStack = new ItemStack(item);

            guiGraphics.pose().pushPose();
            guiGraphics.pose().translate(x, y, 0);
            guiGraphics.pose().scale(2, 2, 2);
            guiGraphics.renderItem(displayStack, 0, 0);
            guiGraphics.pose().popPose();

            float cooldown = player.getCooldowns().getCooldownPercent(item, 1);
            if (cooldown > 0.0f) {
                guiGraphics.pose().pushPose();
                guiGraphics.pose().translate(x+3, y, 310);

                int precision = 100;
                guiGraphics.pose().scale(2f / precision, 2f / precision, 1f);

                int fullHeight = 16 * precision;
                int height = Math.round(fullHeight * cooldown);

                guiGraphics.fill(
                        0, 0,
                        13 * precision, height,
                        0x7F000000
                );

                guiGraphics.pose().popPose();
            }

            x += ICON_SIZE + PADDING;
        }
    }
}