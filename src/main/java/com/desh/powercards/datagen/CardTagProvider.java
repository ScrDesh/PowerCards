package com.desh.powercards.datagen;

import com.desh.powercards.ModCards;
import com.desh.powercards.ModTags;
import com.desh.powercards.PowerCards;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.tags.ItemTagsProvider;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.CompletableFuture;

public class CardTagProvider extends ItemTagsProvider {
    public CardTagProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookupProvider, CompletableFuture<TagLookup<Block>> blockTags, @Nullable ExistingFileHelper existingFileHelper) {
        super(output, lookupProvider, blockTags, PowerCards.MODID, existingFileHelper);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        for (Item i : ModCards.getAllCardItems()) {tag(ModTags.Items.CARDS).add(i);}
    }
}
