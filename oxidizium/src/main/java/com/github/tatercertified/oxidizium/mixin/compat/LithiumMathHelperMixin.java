package com.github.tatercertified.oxidizium.mixin.compat;

import com.moulberry.mixinconstraints.annotations.IfBoolean;
import com.moulberry.mixinconstraints.annotations.IfBooleans;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static com.github.tatercertified.oxidizium.Oxidizium.MTH;

@IfBooleans(value = {
        @IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isLithiumOptimizationEnabled"),
        @IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isTestingEnabled", negate = true)
})
@Mixin(MathHelper.class)
public class LithiumMathHelperMixin {
    /**
     * @author QPCrummer
     * @reason Implement in Rust with Lithium compat
     */
    // @Config(name = "lithium sin", dependencies = "sin")
    @Overwrite
    public static float sin(double value) {
        return MTH.lithiumSin(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust with Lithium compat
     */
    // @Config(name = "lithium cos", dependencies = "cos")
    @Overwrite
    public static float cos(double value) {
        return MTH.lithiumCos(value);
    }
}
