package com.desh.powercards.packets;

import com.desh.powercards.PowerCards;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record DeckInvKeyPacket() implements CustomPacketPayload {
    public static final Type<DeckInvKeyPacket> TYPE =
            new Type<>(ResourceLocation.fromNamespaceAndPath(PowerCards.MODID, "openinv"));

    public static final StreamCodec<ByteBuf, DeckInvKeyPacket> STREAM_CODEC =
            StreamCodec.unit(new DeckInvKeyPacket());

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
