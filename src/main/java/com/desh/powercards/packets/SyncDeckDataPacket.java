package com.desh.powercards.packets;

import com.desh.powercards.PowerCards;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record SyncDeckDataPacket(int totalBP, boolean deckValid)
        implements CustomPacketPayload {


    public static final Type<SyncDeckDataPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(PowerCards.MODID, "sync_deck_data"));

    public static final StreamCodec<ByteBuf, SyncDeckDataPacket> STREAM_CODEC =
            StreamCodec.composite(
                    ByteBufCodecs.INT,     SyncDeckDataPacket::totalBP,
                    ByteBufCodecs.BOOL, SyncDeckDataPacket::deckValid,
                    SyncDeckDataPacket::new
            );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}