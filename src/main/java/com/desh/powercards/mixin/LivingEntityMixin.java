package com.desh.powercards.mixin;

import com.desh.powercards.ModAttributes;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import javax.annotation.Nullable;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin {

    @Inject(
            method = "addEffect(Lnet/minecraft/world/effect/MobEffectInstance;Lnet/minecraft/world/entity/Entity;)Z",
            at = @At("HEAD")
    )
    private void onAddEffect(MobEffectInstance effectInstance, @Nullable Entity source, CallbackInfoReturnable<Boolean> cir) {

        LivingEntity self = (LivingEntity) (Object) this;

        if (effectInstance.isInfiniteDuration()) return;

        boolean isBeneficial = effectInstance.getEffect().value().isBeneficial();

        Holder<Attribute> attribute = isBeneficial
                ? ModAttributes.POSITIVE_EFFECT_DURATION
                : ModAttributes.NEGATIVE_EFFECT_DURATION;

        AttributeInstance attrInstance = self.getAttribute(attribute);
        if (attrInstance == null) return;

        double multiplier = attrInstance.getValue();
        int scaledDuration = (int) (effectInstance.getDuration() * multiplier);

        ((MobEffectAccessorMixin.MobEffectInstanceAccessor) effectInstance).setDuration(scaledDuration);
    }
}