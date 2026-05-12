package com.desh.powercards.effects;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class EffectIdleAbsorption extends MobEffect {
    public EffectIdleAbsorption(MobEffectCategory category, int color) {
        super(category, color);
    }

    @Override
    public boolean applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide() && entity.getPersistentData().getInt("combatTime") < 1 && entity.isAlive())
            {entity.setAbsorptionAmount(entity.getAbsorptionAmount()+(0.06f*(amplifier+1)));}
        return true;
    }

    @Override
    public boolean shouldApplyEffectTickThisTick(int tickCount, int amplifier) {
        return (true);
    }
}
