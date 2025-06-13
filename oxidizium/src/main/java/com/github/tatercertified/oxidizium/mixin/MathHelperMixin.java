package com.github.tatercertified.oxidizium.mixin;

import com.github.tatercertified.mixin_config.annotations.Config;
import com.github.tatercertified.oxidizium.utils.annotation.annotation.*;
import com.github.tatercertified.rust.lib_h;
import com.moulberry.mixinconstraints.annotations.IfBoolean;
import net.minecraft.util.math.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

import java.lang.foreign.MemorySegment;

@Config(name = "Native Math")
@IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isTestingEnabled", negate = true)
@Mixin(MathHelper.class)
public class MathHelperMixin {
    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "sin")
    @IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isLithiumOptimizationEnabled", negate = true)
    @Overwrite
    public static float sin(float value) {
        return lib_h.sin_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "cos")
    @IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isLithiumOptimizationEnabled", negate = true)
    @Overwrite
    public static float cos(float value) {
        return lib_h.cos_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "sqrt")
    @Overwrite
    public static float sqrt(@PositiveOnly float value) {
        return lib_h.sqrt_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float floor")
    @Overwrite
    public static int floor(float value) {
        return lib_h.floor_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double floor")
    @Overwrite
    public static int floor(double value) {
        return lib_h.floor_double(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double lfloor")
    @Overwrite
    public static long lfloor(double value) {
        return lib_h.floor_long(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float lfloor")
    @Overwrite
    public static float abs(float value) {
        return lib_h.abs_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "abs")
    @Overwrite
    public static int abs(int value) {
        return lib_h.abs_int(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float ceil")
    @Overwrite
    public static int ceil(float value) {
        return lib_h.ceil_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double ceil")
    @Overwrite
    public static int ceil(double value) {
        return lib_h.ceil_double(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "int clamp")
    @Overwrite
    public static int clamp(int value, @Min int min, @Max int max) {
        return lib_h.clamp_int(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "long clamp")
    @Overwrite
    public static long clamp(long value, @Min long min, @Max long max) {
        return lib_h.clamp_long(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float clamp")
    @Overwrite
    public static float clamp(float value, @Min float min, @Max float max) {
        return lib_h.clamp_float(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double clamp")
    @Overwrite
    public static double clamp(double value, @Min double min, @Max double max) {
        return lib_h.clamp_double(value, min, max);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double clamped lerp")
    @Overwrite
    public static double clampedLerp(double start, double end, double delta) {
        return lib_h.clamp_lerp_double(start, end, delta);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float clamped lerp")
    @Overwrite
    public static float clampedLerp(float start, float end, float delta) {
        return lib_h.clamp_lerp_float(start, end, delta);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "abs max")
    @Overwrite
    public static double absMax(double a, double b) {
        return lib_h.abs_max(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "floor div")
    @Overwrite
    public static int floorDiv(int dividend, @NonZero int divisor) {
        return lib_h.floor_div(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float approx eq")
    @Overwrite
    public static boolean approximatelyEquals(float a, float b) {
        return lib_h.approximately_equals_float(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double approx eq")
    @Overwrite
    public static boolean approximatelyEquals(double a, double b) {
        return lib_h.approximately_equals_double(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "int floor mod")
    @Overwrite
    public static int floorMod(int dividend, @NonZero int divisor) {
        return lib_h.floor_mod_int(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float floor mod")
    @Overwrite
    public static float floorMod(float dividend, @NonZero float divisor) {
        return lib_h.floor_mod_float(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double floor mod")
    @Overwrite
    public static double floorMod(double dividend, @NonZero double divisor) {
        return lib_h.floor_mod_double(dividend, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "is multiple of")
    @Overwrite
    public static boolean isMultipleOf(int a, @NonZero int b) {
        return lib_h.is_multiple_of(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "pack degrees")
    @Overwrite
    public static byte packDegrees(float degrees) {
        return lib_h.pack_degrees(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "unpack degrees")
    @Overwrite
    public static float unpackDegrees(byte packedDegrees) {
        return lib_h.unpack_degrees(packedDegrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "int wrap degrees")
    @Overwrite
    public static int wrapDegrees(int degrees) {
        return lib_h.wrap_degrees_int(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "long wrap degrees")
    @Overwrite
    public static float wrapDegrees(long degrees) {
        return lib_h.wrap_degrees_long(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float wrap degrees")
    @Overwrite
    public static float wrapDegrees(float degrees) {
        return lib_h.wrap_degrees_float(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double wrap degrees")
    @Overwrite
    public static double wrapDegrees(double degrees) {
        return lib_h.wrap_degrees_double(degrees);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "subtract angles")
    @Overwrite
    public static float subtractAngles(float start, float end) {
        return lib_h.subtract_angles(start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "angle between")
    @Overwrite
    public static float angleBetween(float first, float second) {
        return lib_h.angle_between(first, second);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "clamp angle")
    @Overwrite
    public static float clampAngle( float value, float mean, float delta) {
        return lib_h.clamp_angle(value, mean, delta);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "step towards")
    @Overwrite
    public static float stepTowards(float from, float to, float step) {
        return lib_h.step_towards(from, to, step);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "step unwrapped towards")
    @Overwrite
    public static float stepUnwrappedAngleTowards(float from, float to, float step) {
        return lib_h.step_unwrapped_angle_towards(from, to, step);
    }

    // TODO Determine if this is faster than Java implementation
    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "parse int", enabled = false)
    @Overwrite
    public static int parseInt(String string, int fallback) {
        char[] chars = string.toCharArray();
        return lib_h.parse_int_utf16(MemorySegment.ofArray(chars), chars.length, fallback);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "smallest power of 2")
    @Overwrite
    public static int smallestEncompassingPowerOfTwo(int value) {
        return lib_h.smallest_encompassing_power_of_two(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "is power of 2")
    @Overwrite
    public static boolean isPowerOfTwo(int value) {
        return lib_h.is_power_of_two(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "ceil log 2")
    @Overwrite
    public static int ceilLog2(int value) {
        return lib_h.ceil_log_2(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "floor log 2")
    @Overwrite
    public static int floorLog2(int value) {
        return lib_h.floor_log_2(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     * Mojang, if you are reading this, learn how to get the fractional part.
     * Negative values should return negative fractional parts!!!
     */
    @Config(name = "float fraction")
    @Overwrite
    public static float fractionalPart(float value) {
        return lib_h.fractional_part_float(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     * Mojang, if you are reading this, learn how to get the fractional part.
     * Negative values should return negative fractional parts!!!
     */
    @Config(name = "double fraction")
    @Overwrite
    public static double fractionalPart(double value) {
        return lib_h.fractional_part_double(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "hashcode")
    @Deprecated
    @Overwrite
    public static long hashCode(int x, int y, int z) {
        return lib_h.hash_code(x, y, z);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double get lerp")
    @Overwrite
    public static double getLerpProgress(double value, double start, double end) {
        return lib_h.get_lerp_progress_double(value, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float get lerp")
    @Overwrite
    public static float getLerpProgress(float value, float start, float end) {
        return lib_h.get_lerp_progress_float(value, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "atan 2")
    @Overwrite
    public static double atan2(double y, double x) {
        return lib_h.atan_2(y, x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float inverse sqrt")
    @Overwrite
    public static float inverseSqrt(@Bounded(minInclusive = 1, maxExclusive = 100) float x) {
        return lib_h.inverse_sqrt_float(x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double inverse sqrt")
    @Overwrite
    public static double inverseSqrt(@Bounded(minInclusive = 1, maxExclusive = 100) double x) {
        return lib_h.inverse_sqrt_double(x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "fast inverse cbrt")
    @Overwrite
    public static float fastInverseCbrt(float x) {
        return lib_h.fast_inverse_cbrt(x);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "hsv to rgb")
    @Overwrite
    public static int hsvToRgb(@PositiveOnly float hue, @PositiveOnly float saturation, @PositiveOnly float value) {
        return lib_h.hsv_to_rgb(hue, saturation, value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "hsv to argb")
    @Overwrite
    public static int hsvToArgb(@PositiveOnly float hue, @PositiveOnly float saturation, @PositiveOnly float value, @PositiveOnly int alpha) {
        return lib_h.hsv_to_argb(hue, saturation, value, alpha);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "ideal hash")
    @Overwrite
    public static int idealHash(int value) {
        return lib_h.ideal_hash(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "int lerp")
    @Overwrite
    public static int lerp(float delta, int start, int end) {
        return lib_h.lerp_int(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "lerp positive")
    @Overwrite
    public static int lerpPositive(float delta, int start, int end) {
        return lib_h.lerp_positive(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float lerp")
    @Overwrite
    public static float lerp(float delta, float start, float end) {
        return lib_h.lerp_float(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double lerp")
    @Overwrite
    public static double lerp(double delta, double start, double end) {
        return lib_h.lerp_double(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "lerp 2")
    @Overwrite
    public static double lerp2(double deltaX, double deltaY, double x0y0, double x1y0, double x0y1, double x1y1) {
        return lib_h.lerp_2(deltaX, deltaY, x0y0, x1y0, x0y1, x1y1);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "lerp 3")
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
        return lib_h.lerp_3(deltaX, deltaY, deltaZ, x0y0z0, x1y0z0, x0y1z0, x1y1z0, x0y0z1, x1y0z1, x0y1z1, x1y1z1);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "catmull rom")
    @Overwrite
    public static float catmullRom(float delta, float p0, float p1, float p2, float p3) {
        return lib_h.catmull_rom(delta, p0, p1, p2, p3);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "perlin fade")
    @Overwrite
    public static double perlinFade(double value) {
        return lib_h.perlin_fade(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "perlin fade derive")
    @Overwrite
    public static double perlinFadeDerivative(double value) {
        return lib_h.perlin_fade_derivative(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "sign")
    @Overwrite
    public static int sign(double value) {
        return lib_h.sign(value);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float lerp deg")
    @Overwrite
    public static float lerpAngleDegrees(float delta, float start, float end) {
        return lib_h.lerp_angle_degrees_float(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double lerp deg")
    @Overwrite
    public static double lerpAngleDegrees(double delta, double start, double end) {
        return lib_h.lerp_angle_degrees_double(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "lerp rad")
    @Overwrite
    public static float lerpAngleRadians(float delta, float start, float end) {
        return lib_h.lerp_angle_radians(delta, start, end);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "wrap")
    @Overwrite
    public static float wrap(float value, float maxDeviation) {
        return lib_h.wrap(value, maxDeviation);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float square")
    @Overwrite
    public static float square(float n) {
        return lib_h.square_float(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double square")
    @Overwrite
    public static double square(double n) {
        return lib_h.square_double(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "int square")
    @Overwrite
    public static int square(int n) {
        return lib_h.square_int(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "long square")
    @Overwrite
    public static long square(long n) {
        return lib_h.square_long(n);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double clamped map")
    @Overwrite
    public static double clampedMap(double value, double oldStart, double oldEnd, double newStart, double newEnd) {
        return lib_h.clamped_map_double(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float clamped map")
    @Overwrite
    public static float clampedMap(float value, float oldStart, float oldEnd, float newStart, float newEnd) {
        return lib_h.clamped_map_float(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double map")
    @Overwrite
    public static double map(double value, double oldStart, double oldEnd, double newStart, double newEnd) {
        return lib_h.map_double(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float map")
    @Overwrite
    public static float map(float value, float oldStart, float oldEnd, float newStart, float newEnd) {
        return lib_h.map_float(value, oldStart, oldEnd, newStart, newEnd);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "round up")
    @Overwrite
    public static int roundUpToMultiple(int value, @NonZero int divisor) {
        return lib_h.round_up_to_multiple(value, divisor);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "ceilDiv")
    @Overwrite
    public static int ceilDiv(int a, @NonZero int b) {
        return lib_h.ceil_div(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "squared hypot")
    @Overwrite
    public static double squaredHypot(double a, double b) {
        return lib_h.squared_hypot(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double hypot")
    @Overwrite
    public static double hypot(double a, double b) {
        return lib_h.hypot_double(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float hypot")
    @Overwrite
    public static float hypot(float a, float b) {
        return lib_h.hypot_float(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "squared mag")
    @Overwrite
    public static double squaredMagnitude(double a, double b, double c) {
        return lib_h.squared_magnitude(a, b, c);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "double mag")
    @Overwrite
    public static double magnitude(double a, double b, double c) {
        return lib_h.magnitude_double(a, b, c);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "float mag")
    @Overwrite
    public static float magnitude(float a, float b, float c) {
        return lib_h.magnitude_float(a, b, c);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "round down")
    @Overwrite
    public static int roundDownToMultiple(double a, int b) {
        return lib_h.round_down_to_multiple(a, b);
    }

    /**
     * @author QPCrummer
     * @reason Implement in Rust
     */
    @Config(name = "ease sin")
    @IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isLithiumOptimizationEnabled", negate = true)
    @Overwrite
    public static float easeInOutSine(float value) {
        return lib_h.ease_in_out_sine(value);
    }
}
