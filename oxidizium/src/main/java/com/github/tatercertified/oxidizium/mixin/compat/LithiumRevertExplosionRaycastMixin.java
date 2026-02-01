package com.github.tatercertified.oxidizium.mixin.compat;

import com.bawnorton.mixinsquared.TargetHandler;
import com.moulberry.mixinconstraints.annotations.IfBoolean;
import com.moulberry.mixinconstraints.annotations.IfBooleans;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@IfBooleans(value = {
        @IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isLithiumOptimizationEnabled"),
        @IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isTestingEnabled", negate = true)
})
@Mixin(targets = "net.minecraft.world.explosion.ExplosionImpl", priority = 1500)
public class LithiumRevertExplosionRaycastMixin {
    @TargetHandler(
            mixin = "net.caffeinemc.mods.lithium.mixin.world.explosions.block_raycast.ServerExplosionMixin",
            name = "performRayCast"
    )
    @Redirect(method = "@MixinSquared:Handler", at = @At(value = "INVOKE", target = "Lnet/minecraft/util/math/MathHelper;floor(D)I"))
    private int oxidizium$useVanillaMethod(double value) {
        int i = (int)value;
        return value < (double)i ? i - 1 : i;
    }
}
