package com.desh.powercards.effects;

import com.desh.powercards.PowerCards;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS =
            DeferredRegister.create(BuiltInRegistries.MOB_EFFECT, PowerCards.MODID);

    public static final Holder<MobEffect> IDLE_HEAL = MOB_EFFECTS.register("idle_heal",
            () -> new EffectIdleHeal(MobEffectCategory.BENEFICIAL, 0xFF5555));

    public static final Holder<MobEffect> IDLE_ABSORPTION = MOB_EFFECTS.register("idle_absorption",
            () -> new EffectIdleAbsorption(MobEffectCategory.BENEFICIAL, 0xFDF55F));

    public static void register(IEventBus eventBus) {
        MOB_EFFECTS.register(eventBus);
    }
}
