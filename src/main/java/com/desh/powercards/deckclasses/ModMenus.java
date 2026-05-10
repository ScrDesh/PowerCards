package com.desh.powercards.deckclasses;

import com.desh.powercards.PowerCards;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModMenus {

    public static final DeferredRegister<MenuType<?>> MENU_TYPES =
            DeferredRegister.create(Registries.MENU, PowerCards.MODID);

    public static final DeferredHolder<MenuType<?>, MenuType<DeckMenu>> DECK_MENU =
            MENU_TYPES.register("deck_menu", () ->
                    new MenuType<>(DeckMenu::new, FeatureFlags.DEFAULT_FLAGS));
}