package com.desh.powercards.deckclasses;

import com.desh.powercards.PowerCards;
import com.desh.powercards.packets.DeckInvKeyPacket;
import net.minecraft.client.KeyMapping;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.lwjgl.glfw.GLFW;

@EventBusSubscriber(modid = PowerCards.MODID, bus = EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModEvents {

    @SubscribeEvent
    public static void onRegisterScreens(RegisterMenuScreensEvent event) {
        event.register(ModMenus.DECK_MENU.get(), DeckScreen::new);
    }

    public static final KeyMapping MY_KEY = new KeyMapping(
            "key.powercards.deck_inventory",
            GLFW.GLFW_KEY_R,
            "key.desh.powercards"
    );

    @SubscribeEvent
    public static void onRegisterBindings(RegisterKeyMappingsEvent event) {
        event.register(MY_KEY);
    }
}
