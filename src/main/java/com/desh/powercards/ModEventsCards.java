package com.desh.powercards;

import com.desh.powercards.deckclasses.DeckAttachment;
import com.mojang.logging.LogUtils;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.living.LivingDamageEvent;
import net.neoforged.neoforge.event.entity.living.LivingIncomingDamageEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Map;

@EventBusSubscriber(modid = PowerCards.MODID, bus = EventBusSubscriber.Bus.GAME)
public class ModEventsCards {

    @SubscribeEvent
    public static void onDamage(LivingIncomingDamageEvent event) {
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
