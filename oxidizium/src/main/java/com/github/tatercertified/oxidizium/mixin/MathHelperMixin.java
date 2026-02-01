package com.github.tatercertified.oxidizium.mixin;

import com.github.tatercertified.oxidizium.utils.annotation.*;
import com.moulberry.mixinconstraints.annotations.IfBoolean;
import com.moulberry.mixinconstraints.annotations.IfMinecraftVersion;
import net.minecraft.util.math.MathHelper;
import org.apache.commons.lang3.math.Fraction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import static com.github.tatercertified.oxidizium.Oxidizium.MTH;

// @Config(name = "Native Math")
@IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isTestingEnabled", negate = true)
@Mixin(MathHelper.class)
public class MathHelperMixin {
    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @IfMinecraftVersion(minVersion = "1.21.11")
    @IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isLithiumOptimizationEnabled", negate = true)
    @Overwrite
    public static float sin(double value) {
        return MTH.sin(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @IfMinecraftVersion(minVersion = "1.21.11")
    @IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isLithiumOptimizationEnabled", negate = true)
    @Overwrite
    public static float cos(double value) {
        return MTH.cos(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @IfMinecraftVersion(maxVersion = "1.21.10")
    @IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isLithiumOptimizationEnabled", negate = true)
    @Overwrite
    public static float sin(float value) {
        return MTH.sin(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @IfMinecraftVersion(maxVersion = "1.21.10")
    @IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isLithiumOptimizationEnabled", negate = true)
    @Overwrite
    public static float cos(float value) {
        return MTH.cos(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "sqrt")
    @Overwrite
    public static float sqrt(@PositiveOnly float value) {
        return MTH.sqrt(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float floor")
    @Overwrite
    public static int floor(float value) {
        return MTH.floor(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double floor")
    @Overwrite
    public static int floor(double value) {
        return MTH.floor(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double lfloor")
    @Overwrite
    public static long lfloor(double value) {
        return MTH.lfloor(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float lfloor")
    @Overwrite
    public static float abs(float value) {
        return MTH.abs(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "abs")
    @Overwrite
    public static int abs(int value) {
        return MTH.abs(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float ceil")
    @Overwrite
    public static int ceil(float value) {
        return MTH.ceil(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double ceil")
    @Overwrite
    public static int ceil(double value) {
        return MTH.ceil(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "int clamp")
    @Overwrite
    public static int clamp(int value, @Min int min, @Max int max) {
        return MTH.clamp(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "long clamp")
    @IfMinecraftVersion(minVersion = "1.20.2")
    @Overwrite
    public static long clamp(long value, @Min long min, @Max long max) {
        return MTH.clamp(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float clamp")
    @Overwrite
    public static float clamp(float value, @Min float min, @Max float max) {
        return MTH.clamp(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double clamp")
    @Overwrite
    public static double clamp(double value, @Min double min, @Max double max) {
        return MTH.clamp(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double clamped lerp")
    @Overwrite
    public static double clampedLerp(double delta, double start, double end) {
        return MTH.clampedLerp(start, end, delta);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float clamped lerp")
    @Overwrite
    public static float clampedLerp(float delta, float start, float end) {
        return MTH.clampedLerp(start, end, delta);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @IfMinecraftVersion(minVersion = "1.21.11")
    @Overwrite
    public static int method_76800(int i, int j) {
        return MTH.method_76800(i, j);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @IfMinecraftVersion(minVersion = "1.21.11")
    @Overwrite
    public static float method_76799(float f, float g) {
        return MTH.method_76799(f, g);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @IfMinecraftVersion(minVersion = "1.21.11")
    @Overwrite
    public static int method_76801(int i, int j, int k, int l) {
        return MTH.method_76801(i, j, k, l);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "abs max")
    @Overwrite
    public static double absMax(double a, double b) {
        return MTH.absMax(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "floor div")
    @Overwrite
    public static int floorDiv(int dividend, @NonZero int divisor) {
        return MTH.floorDiv(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float approx eq")
    @Overwrite
    public static boolean approximatelyEquals(float a, float b) {
        return MTH.approximatelyEquals(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double approx eq")
    @Overwrite
    public static boolean approximatelyEquals(double a, double b) {
        return MTH.approximatelyEquals(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "int floor mod")
    @Overwrite
    public static int floorMod(int dividend, @NonZero int divisor) {
        return MTH.floorMod(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float floor mod")
    @Overwrite
    public static float floorMod(float dividend, @NonZero float divisor) {
        return MTH.floorMod(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double floor mod")
    @Overwrite
    public static double floorMod(double dividend, @NonZero double divisor) {
        return MTH.floorMod(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "is multiple of")
    @IfMinecraftVersion(minVersion = "1.19.3")
    @Overwrite
    public static boolean isMultipleOf(int a, @NonZero int b) {
        return MTH.isMultipleOf(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "pack degrees")
    @IfMinecraftVersion(minVersion = "1.21.2")
    @Overwrite
    public static byte packDegrees(float degrees) {
        return MTH.packDegrees(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "unpack degrees")
    @IfMinecraftVersion(minVersion = "1.21.2")
    @Overwrite
    public static float unpackDegrees(byte packedDegrees) {
        return MTH.unpackDegrees(packedDegrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "int wrap degrees")
    @Overwrite
    public static int wrapDegrees(int degrees) {
        return MTH.wrapDegrees(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "long wrap degrees")
    @IfMinecraftVersion(minVersion = "1.21.2")
    @Overwrite
    public static float wrapDegrees(long degrees) {
        return MTH.wrapDegrees(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float wrap degrees")
    @Overwrite
    public static float wrapDegrees(float degrees) {
        return MTH.wrapDegrees(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double wrap degrees")
    @Overwrite
    public static double wrapDegrees(double degrees) {
        return MTH.wrapDegrees(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "subtract angles")
    @Overwrite
    public static float subtractAngles(float start, float end) {
        return MTH.subtractAngles(start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "angle between")
    @Overwrite
    public static float angleBetween(float first, float second) {
        return MTH.angleBetween(first, second);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "clamp angle")
    @Overwrite
    public static float clampAngle( float value, float mean, float delta) {
        return MTH.clampAngle(value, mean, delta);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "step towards")
    @Overwrite
    public static float stepTowards(float from, float to, float step) {
        return MTH.stepTowards(from, to, step);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "step unwrapped towards")
    @Overwrite
    public static float stepUnwrappedAngleTowards(float from, float to, float step) {
        return MTH.stepUnwrappedAngleTowards(from, to, step);
    }

    // TODO Determine if this is faster than Java implementation
    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "parse int", enabled = false)
    /*
    @Overwrite
    public static int parseInt(String string, int fallback) {
        char[] chars = string.toCharArray();
        return lib_h.parse_int_utf16(MemorySegment.ofArray(chars), chars.length, fallback);
    }

     */

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "smallest power of 2")
    @Overwrite
    public static int smallestEncompassingPowerOfTwo(int value) {
        return MTH.smallestEncompassingPowerOfTwo(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "smallest square side len")
    @IfMinecraftVersion(minVersion = "1.21.6")
    @Overwrite
    public static int smallestEncompassingSquareSideLength(@PositiveOnly int value) {
        return MTH.smallestEncompassingSquareSideLength(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "is power of 2")
    @Overwrite
    public static boolean isPowerOfTwo(int value) {
        return MTH.isPowerOfTwo(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "ceil log 2")
    @Overwrite
    public static int ceilLog2(int value) {
        return MTH.ceilLog2(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "floor log 2")
    @Overwrite
    public static int floorLog2(int value) {
        return MTH.floorLog2(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @IfMinecraftVersion(minVersion = "1.21.9")
    @Overwrite
    public static long ceilLong(double value) {
        return MTH.ceilLong(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @IfMinecraftVersion(maxVersion = "1.21.1")
    @Overwrite
    public static int packRgb(float r, float g, float b) {
        return MTH.packRgb(r, g, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     * Mojang, if you are reading this, learn how to get the fractional part.
     * Negative values should return negative fractional parts!!!
     */
    // @Config(name = "float fraction")
    @Overwrite
    public static float fractionalPart(float value) {
        return MTH.fractionalPart(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     * Mojang, if you are reading this, learn how to get the fractional part.
     * Negative values should return negative fractional parts!!!
     */
    // @Config(name = "double fraction")
    @Overwrite
    public static double fractionalPart(double value) {
        return MTH.fractionalPart(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "hashcode")
    @Deprecated
    @Overwrite
    public static long hashCode(int x, int y, int z) {
        return MTH.hashCode(x, y, z);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double get lerp")
    @Overwrite
    public static double getLerpProgress(double value, double start, double end) {
        return MTH.getLerpProgress(value, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float get lerp")
    @Overwrite
    public static float getLerpProgress(float value, float start, float end) {
        return MTH.getLerpProgress(value, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "atan 2")
    @Overwrite
    public static double atan2(double y, double x) {
        return MTH.atan2(y, x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float inverse sqrt")
    @IfMinecraftVersion(minVersion = "1.19.4")
    @Overwrite
    public static float inverseSqrt(@Bounded(minInclusive = 1, maxExclusive = 100) float x) {
        return MTH.inverseSqrt(x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double inverse sqrt")
    @IfMinecraftVersion(minVersion = "1.19.4")
    @Overwrite
    public static double inverseSqrt(@Bounded(minInclusive = 1, maxExclusive = 100) double x) {
        return MTH.inverseSqrt(x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Deprecated
    @Overwrite
    public static double fastInverseSqrt(double x) {
        return MTH.fastInverseSqrt(x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @IfMinecraftVersion(maxVersion = "1.19.3")
    @Overwrite
    public static float fastInverseSqrt(float x) {
        return MTH.fastInverseSqrt(x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "fast inverse cbrt")
    @Overwrite
    public static float fastInverseCbrt(float x) {
        return MTH.fastInverseCbrt(x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "hsv to rgb")
    @Overwrite
    public static int hsvToRgb(@PositiveOnly float hue, @PositiveOnly float saturation, @PositiveOnly float value) {
        return MTH.hsvToRgb(hue, saturation, value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "hsv to argb")
    @IfMinecraftVersion(minVersion = "1.21")
    @Overwrite
    public static int hsvToArgb(@PositiveOnly float hue, @PositiveOnly float saturation, @PositiveOnly float value, @PositiveOnly int alpha) {
        return MTH.hsvToArgb(hue, saturation, value, alpha);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "ideal hash")
    @Overwrite
    public static int idealHash(int value) {
        return MTH.idealHash(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "int lerp")
    @IfMinecraftVersion(minVersion = "1.19.4")
    @Overwrite
    public static int lerp(float delta, int start, int end) {
        return MTH.lerp(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "lerp positive")
    @IfMinecraftVersion(minVersion = "1.20.2")
    @Overwrite
    public static int lerpPositive(float delta, int start, int end) {
        return MTH.lerpPositive(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float lerp")
    @Overwrite
    public static float lerp(float delta, float start, float end) {
        return MTH.lerp(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double lerp")
    @Overwrite
    public static double lerp(double delta, double start, double end) {
        return MTH.lerp(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "lerp 2")
    @Overwrite
    public static double lerp2(double deltaX, double deltaY, double x0y0, double x1y0, double x0y1, double x1y1) {
        return MTH.lerp2(deltaX, deltaY, x0y0, x1y0, x0y1, x1y1);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "lerp 3")
    @Overwrite
    public static double lerp3(
            double deltaX,
            double deltaY,
            double deltaZ,
            double x0y0z0,
            double x1y0z0,
            double x0y1z0,
            double x1y1z0,
            double x0y0z1,
            double x1y0z1,
            double x0y1z1,
            double x1y1z1
    ) {
        return MTH.lerp3(deltaX, deltaY, deltaZ, x0y0z0, x1y0z0, x0y1z0, x1y1z0, x0y0z1, x1y0z1, x0y1z1, x1y1z1);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "catmull rom")
    @IfMinecraftVersion(minVersion = "1.19.2")
    @Overwrite
    public static float catmullRom(float delta, float p0, float p1, float p2, float p3) {
        return MTH.catmullRom(delta, p0, p1, p2, p3);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "perlin fade")
    @Overwrite
    public static double perlinFade(double value) {
        return MTH.perlinFade(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "perlin fade derive")
    @Overwrite
    public static double perlinFadeDerivative(double value) {
        return MTH.perlinFadeDerivative(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "sign")
    @Overwrite
    public static int sign(double value) {
        return MTH.sign(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float lerp deg")
    @Overwrite
    public static float lerpAngleDegrees(float delta, float start, float end) {
        return MTH.lerpAngleDegrees(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double lerp deg")
    @IfMinecraftVersion(minVersion = "1.20.2")
    @Overwrite
    public static double lerpAngleDegrees(double delta, double start, double end) {
        return MTH.lerpAngleDegrees(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "lerp rad")
    @IfMinecraftVersion(minVersion = "1.21.2")
    @Overwrite
    public static float lerpAngleRadians(float delta, float start, float end) {
        return MTH.lerpAngleRadians(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "wrap")
    @Overwrite
    public static float wrap(float value, float maxDeviation) {
        return MTH.wrap(value, maxDeviation);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float square")
    @Overwrite
    public static float square(float n) {
        return MTH.square(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double square")
    @Overwrite
    public static double square(double n) {
        return MTH.square(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "int square")
    @Overwrite
    public static int square(int n) {
        return MTH.square(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "long square")
    @Overwrite
    public static long square(long n) {
        return MTH.square(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double clamped map")
    @IfMinecraftVersion(minVersion = "1.19.2")
    @Overwrite
    public static double clampedMap(double value, double oldStart, double oldEnd, double newStart, double newEnd) {
        return MTH.clampedMap(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float clamped map")
    @IfMinecraftVersion(minVersion = "1.19.2")
    @Overwrite
    public static float clampedMap(float value, float oldStart, float oldEnd, float newStart, float newEnd) {
        return MTH.clampedMap(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double map")
    @IfMinecraftVersion(minVersion = "1.19.2")
    @Overwrite
    public static double map(double value, double oldStart, double oldEnd, double newStart, double newEnd) {
        return MTH.map(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float map")
    @IfMinecraftVersion(minVersion = "1.19.2")
    @Overwrite
    public static float map(float value, float oldStart, float oldEnd, float newStart, float newEnd) {
        return MTH.map(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "round up")
    @Overwrite
    public static int roundUpToMultiple(int value, @NonZero int divisor) {
        return MTH.roundUpToMultiple(value, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "ceilDiv")
    @Overwrite
    public static int ceilDiv(int a, @NonZero int b) {
        return MTH.ceilDiv(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "squared hypot")
    @Overwrite
    public static double squaredHypot(double a, double b) {
        return MTH.squaredHypot(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double hypot")
    @Overwrite
    public static double hypot(double a, double b) {
        return MTH.hypot(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float hypot")
    @IfMinecraftVersion(minVersion = "1.21.2")
    @Overwrite
    public static float hypot(float a, float b) {
        return MTH.hypot(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "squared mag")
    @Overwrite
    public static double squaredMagnitude(double a, double b, double c) {
        return MTH.squaredMagnitude(a, b, c);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "double mag")
    @Overwrite
    public static double magnitude(double a, double b, double c) {
        return MTH.magnitude(a, b, c);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "float mag")
    @IfMinecraftVersion(minVersion = "1.21")
    @Overwrite
    public static float magnitude(float a, float b, float c) {
        return MTH.magnitude(a, b, c);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "round down")
    @Overwrite
    public static int roundDownToMultiple(double a, int b) {
        return MTH.roundDownToMultiple(a, b);
    }

    // TODO Figure out how to implement in Rust
    /*
    @Overwrite
    public static Quaternionf rotateAround(Vector3f axis, Quaternionf rotation, Quaternionf result) {
        // TODO Implement in Rust
    }

     */

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    // @Config(name = "multiply fract")
    @IfMinecraftVersion(minVersion = "1.20.5")
    @Overwrite
    public static int multiplyFraction(Fraction fraction, int multiplier) {
        return MTH.multiplyFraction(fraction.getNumerator(), fraction.getDenominator(), multiplier);
    }
}
