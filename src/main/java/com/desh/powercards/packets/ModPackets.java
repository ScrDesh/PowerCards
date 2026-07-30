package com.desh.powercards.packets;

import com.desh.powercards.deckclasses.DeckAttachment;
import com.desh.powercards.deckclasses.DeckMenu;
import com.desh.powercards.deckclasses.PlayerDeckData;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.SimpleMenuProvider;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

import static com.desh.powercards.PowerCards.MODID;

public class ModPackets {

    public static void register(IEventBus modBus) {
        modBus.addListener(ModPackets::onRegisterPayloads);
    }

    private static void onRegisterPayloads(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(MODID);

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
            if (player.getPersistentData().getInt("combatTime") > 0) {
                player.displayClientMessage(Component.translatable("ui.powercards.still_in_combat").withStyle(ChatFormatting.RED), true);
                player.level().playSound(
                        player,
                        player.getBlockPosBelowThatAffectsMyMovement(),
                        SoundEvents.NOTE_BLOCK_BASS.value(),
                        SoundSource.PLAYERS,
                        1.0f,
                        0.5f
                );
            }
            else {
                player.openMenu(new SimpleMenuProvider(
                        (id, inv, p) -> new DeckMenu(id, inv, true),
                        Component.translatable("ui.powercards.decklabel")
                 ));
            }
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