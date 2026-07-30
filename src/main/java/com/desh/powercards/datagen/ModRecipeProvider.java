package com.desh.powercards.datagen;

import com.desh.powercards.ModCards;
import com.desh.powercards.ModTags;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.conditions.IConditionBuilder;

import java.util.concurrent.CompletableFuture;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {

    public ModRecipeProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries);
    }

    @Override
    public void buildRecipes(RecipeOutput recipeOutput) {

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModCards.CARD_PACK)
                .requires(ModCards.CARD_SHREDS, 8).requires(Items.IRON_INGOT)
                .unlockedBy("has_card_shreds", has(ModCards.CARD_SHREDS))
                .save(recipeOutput);

        ShapelessRecipeBuilder.shapeless(RecipeCategory.MISC, ModCards.CARD_SHREDS)
                .requires(ModTags.Items.CARDS)
                .unlockedBy("has_card", has(ModTags.Items.CARDS))
                .save(recipeOutput, "powercards:cut_up_card");

    }
}
