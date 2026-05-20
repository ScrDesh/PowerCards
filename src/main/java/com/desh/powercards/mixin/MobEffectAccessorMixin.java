package com.desh.powercards.mixin;

import net.minecraft.world.effect.MobEffectInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

// oh dear god i suck at mixins here we go

public class MobEffectAccessorMixin {
    @Mixin(MobEffectInstance.class)
    public interface MobEffectInstanceAccessor {
        @Accessor("duration")
        void setDuration(int duration);
    }
}
