package com.desh.powercards.packets;

import com.desh.powercards.ModEvents;
import com.desh.powercards.PowerCards;
import com.desh.powercards.deckclasses.DeckAttachment;
import com.desh.powercards.deckclasses.DeckMenu;
import com.desh.powercards.deckclasses.PlayerDeckData;
import com.google.common.eventbus.Subscribe;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

public class ModPackets {

    public static void register(IEventBus modBus) {
        modBus.addListener(ModPackets::onRegisterPayloads);
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(PowerCards.MODID);

        registrar.playToClient(
                SyncDeckDataPacket.TYPE,
                SyncDeckDataPacket.STREAM_CODEC,
                ClientPacketHandler::handleSyncDeckData
        );

        registrar.playToServer(
                DeckInvKeyPacket.TYPE,
                DeckInvKeyPacket.STREAM_CODEC,
                ModPackets::openInvKeyAction   // server-side handler
        );
    }

    private static void openInvKeyAction(DeckInvKeyPacket packet, IPayloadContext context) {
        context.enqueueWork(() -> {
            ServerPlayer player = (ServerPlayer) context.player();
            player.openMenu(new SimpleMenuProvider(
                    (id, inv, p) -> new DeckMenu(id, inv, true),
                    Component.literal("Deck")
            ));
        });
    }

    public static void syncDeckData(ServerPlayer player) {
        PlayerDeckData data = player.getData(DeckAttachment.DECK_DATA);
        PacketDistributor.sendToPlayer(player, new SyncDeckDataPacket(
                data.getTotalBP(),
                data.isDeckValid()
        ));
    }
}