package com.desh.powercards.effects;

import com.mojang.logging.LogUtils;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;

public class EffectIdleHeal extends MobEffect {
    public EffectIdleHeal(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (entity.getPersistentData().getInt("combatTime") < 1 && entity.isAlive()) {entity.heal(0.04f*(amplifier+1));}
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return (true);
    }
}
