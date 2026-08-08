package com.desh.powercards;

import com.desh.powercards.deckclasses.DeckAttachment;
import com.desh.powercards.deckclasses.PlayerDeckData;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Vex;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.MaceItem;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingChangeTargetEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.living.MobEffectEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.Map;

@EventBusSubscriber(modid = PowerCards.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModEventsCards {

    // TARGET MANEGEMENT EVENTS
    @SubscribeEvent
    public static void onTarget(LivingChangeTargetEvent event) {
        if (event.getEntity().getPersistentData().contains("singleTarget")) {
            if ((event.getEntity().level() instanceof ServerLevel level && level.getEntity(event.getEntity().getPersistentData().getUUID("singleTarget")) instanceof LivingEntity target))
            {event.setNewAboutToBeSetTarget(target);}
            else {event.getEntity().kill();}
        }
    }

    @SubscribeEvent
    public static void cosmeticCards(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        PlayerDeckData data = event.getEntity().getData(DeckAttachment.DECK_DATA);
        if (event.getEntity().level() instanceof ServerLevel level) {
            // BLOOM
            if (data.hasEffect("passive.powercards.cosmetic_bloom")) {
                level.sendParticles(
                        ParticleTypes.CHERRY_LEAVES,
                        player.getX(), player.getY(), player.getZ(),
                        1,
                        5, 3, 5,
                        0
                );
            }

            // BAD DAY
            if (data.hasEffect("passive.powercards.cosmetic_badday")) {
                double scale = player.getScale();
                level.sendParticles(
                        ParticleTypes.CLOUD,
                        player.getX(), player.getY()+0.5+(2*scale), player.getZ(),
                        6,
                        scale/4, 0, scale/4,
                        0
                );

                level.sendParticles(
                        ParticleTypes.FALLING_DRIPSTONE_WATER,
                        player.getX(), player.getY()+0.5+(2*scale), player.getZ(),
                        2,
                        scale/3, 0, scale/3,
                        0
                );
            }
        }
    }

    @SubscribeEvent
    public static void isEffectApplicable(MobEffectEvent.Applicable event) {
        String effectID = event.getEffectInstance().getEffect().value().getDescriptionId().replace("effect.minecraft.", "");
        if (!(event.getEntity() instanceof ServerPlayer player)) {return;}

        if (player.getData(DeckAttachment.DECK_DATA).hasEffect("passive.powercards.immunityto." + effectID)) {event.setResult(MobEffectEvent.Applicable.Result.DO_NOT_APPLY);}
    }

    @SubscribeEvent
    public static void onDamage(LivingIncomingDamageEvent event) {

        Entity attacker = event.getSource().getEntity();


        // REQUIRE ATTACKER STUFF BELOW, EVERYTHING ELSE ABOVE
        if (attacker == null || attacker.getWeaponItem() == null) {return;}

        // SPECIFIC EFFECT - NO MACE
        if (attacker.getWeaponItem().getItem() instanceof MaceItem &&
                attacker.getData(DeckAttachment.DECK_DATA).hasEffect("passive.powercards.cannotusemace")) {event.setCanceled(true);}

        // SPECIFIC EFFECT - SPITE SPIRIT
        if (event.getEntity().getData(DeckAttachment.DECK_DATA).hasEffect("passive.powercards.spiritofspite")
                && event.getEntity() instanceof Player player
                && !player.getCooldowns().isOnCooldown(ModCards.getCardItem(ModCards.VENGEFUL_GHOST))
                && (!event.getEntity().level().isClientSide)
                && (event.getEntity().level() instanceof ServerLevel level)) {
            player.getCooldowns().addCooldown(ModCards.getCardItem(ModCards.VENGEFUL_GHOST), 200);
            for (int i = 0; i < event.getEntity().getData(DeckAttachment.DECK_DATA).getEffect("passive.powercards.spiritofspite"); i++) {
                BlockPos pos = new BlockPos(player.getBlockX(), player.getBlockY(), player.getBlockZ());
                Vex ghost = EntityType.VEX.spawn(level, pos, MobSpawnType.REINFORCEMENT);
                ghost.setCustomName(player.getName());
                ghost.setPos(player.getEyePosition());
                ghost.getPersistentData().putUUID("singleTarget", attacker.getUUID());
                ghost.setLimitedLife(120);
            }}

        // ATTACK POTION EFFECTS
        // did i go through a phase of writing everything in caps???
        if (event.getSource().getEntity() instanceof Player player && event.getSource().isDirect()) {
            for (Map.Entry<ResourceKey<MobEffect>, MobEffect> entry : BuiltInRegistries.MOB_EFFECT.entrySet()) {

                Integer stacksOfEffect = player.getData(DeckAttachment.DECK_DATA).getEffect("passive.powercards.attacks_" +
                        entry.getKey().location().getPath());

                if (stacksOfEffect < 1) {continue;}

                MobEffectInstance attack_effect = new MobEffectInstance(
                        BuiltInRegistries.MOB_EFFECT.wrapAsHolder(entry.getValue()),
                        300,
                        stacksOfEffect-1
                );

                event.getEntity().addEffect(attack_effect);

            }
        }
    }
}
