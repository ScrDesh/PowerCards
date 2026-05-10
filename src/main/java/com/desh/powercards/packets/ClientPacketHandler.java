package com.desh.powercards.packets;

import net.neoforged.neoforge.network.handling.IPayloadContext;

public class ClientPacketHandler {

    // client-side cache of the player's deck data
    private static int clientTotalBP = 0;
    private static boolean clientDeckValid = true;

    public static void handleSyncDeckData(SyncDeckDataPacket packet, IPayloadContext context) {
        // context.enqueueWork ensures this runs on the main game thread i think idk
        context.enqueueWork(() -> {
            clientTotalBP  = packet.totalBP();
            clientDeckValid = packet.deckValid();
        });
    }

    // getters for the GUI and HUD to read later
    public static int getClientTotalBP()    { return clientTotalBP; }
    public static boolean isClientDeckValid() { return clientDeckValid; }
}