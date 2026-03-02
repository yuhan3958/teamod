package me.yuhan8954.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;

import java.util.List;

public enum TeaType {
    GREEN(
            SoundEvents.GENERIC_DRINK.value(),
            List.of(
                    new MobEffectInstance(MobEffects.REGENERATION, 10 * 20, 0)
            )
    ),
    OOLONG(
            SoundEvents.GENERIC_DRINK.value(),
            List.of(
                    new MobEffectInstance(MobEffects.HEALTH_BOOST, 60 * 20, 0)
            )
    ),
    BLACK(
            SoundEvents.GENERIC_DRINK.value(),
            List.of(
                    new MobEffectInstance(MobEffects.SPEED, 60 * 20, 0)
            )
    );

    private final SoundEvent drinkSound;
    private final List<MobEffectInstance> effects;

    TeaType(SoundEvent drinkSound, List<MobEffectInstance> effects) {
        this.drinkSound = drinkSound;
        this.effects = effects;
    }

    public SoundEvent drinkSound() {
        return drinkSound;
    }

    public List<MobEffectInstance> effects() {
        return effects;
    }
}
