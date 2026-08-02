package com.desh.powercards;

import com.desh.powercards.deckclasses.DeckAttachment;
import com.desh.powercards.deckclasses.ModMenus;
import com.desh.powercards.effects.ModEffects;
import com.desh.powercards.packets.ModPackets;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;

@Mod(PowerCards.MODID)
public class PowerCards {
    public static final String MODID = "powercards";

    public PowerCards(IEventBus modEventBus, ModContainer modContainer) {
        ModRegistries.register(modEventBus);
        ModCards.CARDS.register(modEventBus);
        ModCards.ITEMS.register(modEventBus);
        DeckAttachment.ATTACHMENT_TYPES.register(modEventBus);
        ModPackets.register(modEventBus);
        ModMenus.MENU_TYPES.register(modEventBus);
        ModEffects.register(modEventBus);
        ModAttributes.register(modEventBus);

        ModCreativeTab.register(modEventBus);
    }
}
