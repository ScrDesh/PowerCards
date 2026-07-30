package com.desh.powercards.datagen;

import com.desh.powercards.ModCards;
import com.desh.powercards.PowerCards;
import net.minecraft.data.PackOutput;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

public class CardModelProvider extends ItemModelProvider {
    public CardModelProvider(PackOutput output, ExistingFileHelper existingFileHelper) {
        super(output, PowerCards.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        for (Item i : ModCards.getAllCardItems()) {basicItem(i);}

        basicItem(ModCards.CARD_PACK.getId());
        basicItem(ModCards.CARD_SHREDS.getId());
        basicItem(ModCards.POWER_CARD.getId());
    }
}
