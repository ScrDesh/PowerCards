package com.desh.powercards;

import com.desh.powercards.deckclasses.*;
import com.desh.powercards.packets.DeckInvKeyPacket;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeModificationEvent;
import net.neoforged.neoforge.event.entity.EntityEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;

@EventBusSubscriber(modid = PowerCards.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModEvents {

    @SubscribeEvent
    public static void onRegisterCommands(RegisterCommandsEvent event) {
        ModCommands.register(event.getDispatcher());
    }

    @SubscribeEvent
    public static void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getData(DeckAttachment.DECK_DATA).setPlayer(player);
            player.getData(DeckAttachment.DECK_DATA).rebuildDerivedState();
        }
    }

    @SubscribeEvent
    public static void onPlayerLogout(PlayerEvent.PlayerLoggedOutEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getData(DeckAttachment.DECK_DATA).rebuildDerivedState();
        }
    }

    @SubscribeEvent
    public static void onClientTick(ClientTickEvent.Post event) {
        while (ClientModEvents.MY_KEY.consumeClick()) {
            PacketDistributor.sendToServer(new DeckInvKeyPacket());
        }
    }

    @SubscribeEvent
    public static void onTick(PlayerTickEvent.Pre event) {
        // COMBAT TIME TICKDOWN
        CompoundTag data = event.getEntity().getPersistentData();
        if (data.contains("combatTime")) {
            if (data.getInt("combatTime") > 0) {data.putInt("combatTime", data.getInt("combatTime")-1);}
        }
        else {data.putInt("combatTime", 0);}
    }

    @SubscribeEvent
    public static void onDamage(LivingIncomingDamageEvent event) {
        if (event.getEntity() instanceof Player player) {player.getPersistentData().putInt("combatTime", 160);}
        if (event.getSource().getEntity() instanceof Player player) {player.getPersistentData().putInt("combatTime", 160);}

        float damageModifier = 1.0f;

        // APPLY DAMAGE-TAKEN / DAMAGE-DEALT MODIFIERS
        damageModifier *= (float) event.getEntity().getAttributeValue(ModAttributes.DAMAGE_TAKEN);
        if (event.getSource().getEntity() instanceof LivingEntity entity)
            {damageModifier *= (float) entity.getAttributeValue(ModAttributes.DAMAGE_DEALT);}

        event.setAmount(event.getAmount()*damageModifier);
    }
}