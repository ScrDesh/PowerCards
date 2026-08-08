package com.desh.powercards;

import com.desh.powercards.deckclasses.*;
import com.desh.powercards.packets.DeckInvKeyPacket;
import com.mojang.logging.LogUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.npc.WanderingTrader;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Projectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.trading.ItemCost;
import net.minecraft.world.item.trading.MerchantOffer;
import net.minecraft.world.item.trading.MerchantOffers;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.ClientTickEvent;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.neoforge.event.entity.living.LivingDeathEvent;
import net.neoforged.neoforge.event.entity.living.LivingEquipmentChangeEvent;
import net.neoforged.neoforge.event.entity.living.LivingHealEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.apache.logging.log4j.core.jmx.Server;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

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
    public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getData(DeckAttachment.DECK_DATA).setPlayer(player);
            player.getData(DeckAttachment.DECK_DATA).rebuildDerivedState();
        }
    }

    @SubscribeEvent
    public static void cardTraderOverwrite(EntityJoinLevelEvent event) {
        if (!event.loadedFromDisk() && !event.getLevel().isClientSide() && event.getEntity() instanceof WanderingTrader trader && Math.random() <= 0.5) {
            MerchantOffers offers = new MerchantOffers();

            ArrayList<Item> chosen = new ArrayList<>();

            // stop potentially infinite loops if all pools are somehow empty
            int limit = 20;

            for (int i = 0; i < 6; i++) {

                if (limit <= 0) {break;}
                limit -= 1;

                double chance = Math.random();
                ArrayList<CardItem> pool;
                int cost = 0;

                if (chance < 0.35) {pool = ModCards.getAllCardItems(CardDefinition.CardRarity.COMMON); cost = 16;}
                else if (chance < 0.60) {pool = ModCards.getAllCardItems(CardDefinition.CardRarity.UNCOMMON); cost = 24;}
                else if (chance < 0.80) {pool = ModCards.getAllCardItems(CardDefinition.CardRarity.RARE); cost = 32;}
                else if (chance < 0.95) {pool = ModCards.getAllCardItems(CardDefinition.CardRarity.EPIC); cost = 48;}
                else {pool = ModCards.getAllCardItems(CardDefinition.CardRarity.LEGENDARY); cost = 64;}

                int choice = (int)(Math.random() * pool.size());

                if (chosen.contains(pool.get(choice))) {i--; continue;}
                chosen.add(pool.get(choice));

                final ItemStack item = new ItemStack(pool.get(choice).asItem());
                final int finalCost = cost;

                offers.add(new MerchantOffer(
                        new ItemCost(Items.EMERALD, finalCost),
                        item, 1, 3, 0.2f));

            }

            offers.add(new MerchantOffer(
                    new ItemCost(Items.EMERALD, 24),
                    ModCards.POWER_CARD.toStack(), 3, 3, 0.2f));

            trader.getOffers().clear();
            trader.getOffers().addAll(offers);
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
    public static void onCraft(PlayerEvent.ItemCraftedEvent event) {
        if (event.getCrafting().getItem() == ModCards.CARD_SHREDS.asItem()) {
                event.getEntity().level().playSound(
                null,
                event.getEntity().getBlockPosBelowThatAffectsMyMovement(),
                SoundEvents.SNOW_GOLEM_SHEAR,
                SoundSource.PLAYERS,
                0.4f,
                1f
        );};
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

    private static final List<String> EXPLOSION_TYPES = Arrays.asList("explosion", "explosion.player", "fireball", "fireworks", "witherSkull");

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onArmourChange(LivingEquipmentChangeEvent event) {
        if (event.getEntity() instanceof ServerPlayer player && event.getEntity().getServer() != null) {

            ServerTaskScheduler.scheduleIn(event.getEntity().getServer(), 1, () ->
                    player.getData(DeckAttachment.DECK_DATA).rebuildDerivedState());
        }
    }

    @SubscribeEvent
    public static void onHeal(LivingHealEvent event) {
        float healModifier = 1.0f;

        healModifier *= (float) event.getEntity().getAttributeValue(ModAttributes.HEALING_TAKEN);

        event.setAmount(event.getAmount()*healModifier);
    }

    @SubscribeEvent
    public static void onTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        CompoundTag data = event.getEntity().getPersistentData();
        PlayerDeckData abilities = event.getEntity().getData(DeckAttachment.DECK_DATA);

        if (player.isSprinting() && player.isAlive() && abilities.hasEffect("passive.powercards.sweet_stepping")) {player.heal(0.02f*abilities.getEffect("passive.powercards.sweet_stepping"));}

        if (player.isCrouching() && player.onGround()) {
            data.putInt("crouchTime", data.getInt("crouchTime")+1);
        }
        else {
            data.putInt("crouchTime", 0);
        }

        if (data.getInt("crouchTime") > 30 && player.getData(DeckAttachment.DECK_DATA).hasEffect("passive.powercards.spring_jump")) {
            int level = player.getData(DeckAttachment.DECK_DATA).getEffect("passive.powercards.spring_jump");
            int wantlevel = (data.getInt("crouchTime")-10)/20;

            player.addEffect(new MobEffectInstance(
                    MobEffects.JUMP,
                    3,
                    Math.min(level, wantlevel)*2,
                    true,
                    false,
                    true));

            player.displayClientMessage(Component.translatable("ui.powercards.chargelevel").withStyle(ChatFormatting.GREEN).append(Component.literal(": " + Math.min(level, wantlevel))), true);
        }
    }

    @SubscribeEvent
    public static void onDeath(LivingDeathEvent event) {

        if (event.getEntity().getData(DeckAttachment.DECK_DATA).hasEffect("passive.powercards.tenacity")
            && event.getEntity() instanceof Player player
            && !player.getCooldowns().isOnCooldown(ModCards.getCardItem(ModCards.TENACITY)))
            {
                event.setCanceled(true);
                player.setHealth(0.1f);
                player.getCooldowns().addCooldown(ModCards.getCardItem(ModCards.TENACITY), 1200);
            }

        if (!event.isCanceled() && event.getEntity() instanceof ServerPlayer player) {
            PlayerDeckData deckdata = player.getData(DeckAttachment.DECK_DATA);
            if (deckdata.hasEffect("passive.powercards.holding")) {return;}
            for (int i = 0; i < deckdata.getDeckInventory().getSlots(); i++) {
                ItemStack item = deckdata.getDeckInventory().extractItem(i, 1, false);

                if (!item.is(Items.AIR)) {ItemEntity ientity = new ItemEntity(player.level(), player.getX(), player.getY(), player.getZ(), item); ientity.spawnAtLocation(item);}
            }
        }
    }

    // HELPER METHOD TO ENGAGE COMBAT FOR A PLAYER
    // future hana here why did you write that in all caps
    public static void combatEngaged(Player player) {
        player.getPersistentData().putInt("combatTime", 160);
        if (player.containerMenu instanceof DeckMenu) {player.closeContainer();
        player.displayClientMessage(Component.translatable("ui.powercards.still_in_combat").withStyle(ChatFormatting.RED), true);
        player.level().playSound(
                player,
                player.getBlockPosBelowThatAffectsMyMovement(),
                SoundEvents.NOTE_BLOCK_BASS.value(),
                SoundSource.PLAYERS,
                1.0f,
                0.5f
        );}
    }

    @SubscribeEvent
    public static void onDamage(LivingIncomingDamageEvent event) {
        CompoundTag data = event.getEntity().getPersistentData(); // this will probably be used at some point idk

        if (event.isCanceled()) {return;}

        if (event.getEntity() instanceof Player player) {combatEngaged(player);}
        if (event.getSource().getEntity() instanceof Player player) {combatEngaged(player);}

        float damageModifier = 1.0f;

        // APPLY DAMAGE-TAKEN / DAMAGE-DEALT MODIFIERS
        damageModifier *= (float) event.getEntity().getAttributeValue(ModAttributes.DAMAGE_TAKEN);
        if (event.getSource().getEntity() instanceof LivingEntity attacker)
            {damageModifier *= (float) attacker.getAttributeValue(ModAttributes.DAMAGE_DEALT);}

        // MELEE/PROJECTILE DAMAGE AMOUNTS
        if (event.getSource().getEntity() instanceof LivingEntity attacker) {
            if (event.getSource().getDirectEntity() instanceof Projectile projectile)
                {damageModifier *= (float) attacker.getAttributeValue(ModAttributes.RANGED_DAMAGE_DEALT);}
            else {damageModifier *= (float) attacker.getAttributeValue(ModAttributes.MELEE_DAMAGE_DEALT);}
        }

        // DAMAGE TYPE REDUCTIONS
        if (EXPLOSION_TYPES.contains(event.getSource().type().msgId())) // EXPLOSION DAMAGES
            {damageModifier *= (float) event.getEntity().getAttributeValue(ModAttributes.EXPLOSION_DAMAGE_TAKEN);}

        event.setAmount(event.getAmount()*damageModifier);

        if (event.getSource().getEntity() instanceof LivingEntity attacker && attacker.getAttributeValue(ModAttributes.LIFESTEAL) > 1) {
            attacker.heal((float) (event.getOriginalAmount()*damageModifier*( attacker.getAttributeValue(ModAttributes.LIFESTEAL) - 1.0f )));
        }
    }
}