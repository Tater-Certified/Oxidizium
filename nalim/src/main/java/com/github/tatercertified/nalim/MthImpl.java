package com.github.tatercertified.nalim;

import com.github.tatercertified.nalim.rust.OxidiziumLib;
import com.github.tatercertified.oxidizium.math.Mth;

public class MthImpl implements Mth {
    @Override
    public float sin(double value) {
        return OxidiziumLib.sin_double(value);
    }

    @Override
    public float cos(double value) {
        return OxidiziumLib.cos_double(value);
    }

    @Override
    public float sin(float value) {
        return OxidiziumLib.sin_float(value);
    }

    @Override
    public float cos(float value) {
        return OxidiziumLib.cos_float(value);
    }

    @Override
    public float sqrt(float value) {
        return OxidiziumLib.sqrt_float(value);
    }

    @Override
    public int floor(float value) {
        return OxidiziumLib.floor_float(value);
    }

    @Override
    public int floor(double value) {
        return OxidiziumLib.floor_double(value);
    }

    @Override
    public long lfloor(double value) {
        return OxidiziumLib.floor_long(value);
    }

    @Override
    public float abs(float value) {
        return OxidiziumLib.abs_float(value);
    }

    @Override
    public int abs(int value) {
        return OxidiziumLib.abs_int(value);
    }

    @Override
    public int ceil(float value) {
        return OxidiziumLib.ceil_float(value);
    }

    @Override
    public int ceil(double value) {
        return OxidiziumLib.ceil_double(value);
    }

    @Override
    public int clamp(int value, int min, int max) {
        return OxidiziumLib.clamp_int(value, min, max);
    }

    @Override
    public long clamp(long value, long min, long max) {
        return OxidiziumLib.clamp_long(value, min, max);
    }

    @Override
    public float clamp(float value, float min, float max) {
        return OxidiziumLib.clamp_float(value, min, max);
    }

    @Override
    public double clamp(double value, double min, double max) {
        return OxidiziumLib.clamp_double(value, min, max);
    }

    @Override
    public double clampedLerp(double delta, double start, double end) {
        return OxidiziumLib.clamp_lerp_double(start, end, delta);
    }

    @Override
    public float clampedLerp(float delta, float start, float end) {
        return OxidiziumLib.clamp_lerp_float(start, end, delta);
    }

    @Override
    public int method_76800(int i, int j) {
        return OxidiziumLib.abs_max_int(i, j);
    }

    @Override
    public float method_76799(float f, float g) {
        return OxidiziumLib.abs_max_float(f, g);
    }

    @Override
    public int method_76801(int i, int j, int k, int l) {
        return OxidiziumLib.abs_max_difference(i, j, k, l);
    }

    @Override
    public double absMax(double a, double b) {
        return OxidiziumLib.abs_max(a, b);
    }

    @Override
    public int floorDiv(int dividend, int divisor) {
        return OxidiziumLib.floor_div(dividend, divisor);
    }

    @Override
    public boolean approximatelyEquals(float a, float b) {
        return OxidiziumLib.approximately_equals_float(a, b);
    }

    @Override
    public boolean approximatelyEquals(double a, double b) {
        return OxidiziumLib.approximately_equals_double(a, b);
    }

    @Override
    public int floorMod(int dividend, int divisor) {
        return OxidiziumLib.floor_mod_int(dividend, divisor);
    }

    @Override
    public float floorMod(float dividend, float divisor) {
        return OxidiziumLib.floor_mod_float(dividend, divisor);
    }

    @Override
    public double floorMod(double dividend, double divisor) {
        return OxidiziumLib.floor_mod_double(dividend, divisor);
    }

    @Override
    public boolean isMultipleOf(int a, int b) {
        return OxidiziumLib.is_multiple_of(a, b);
    }

    @Override
    public byte packDegrees(float degrees) {
        return OxidiziumLib.pack_degrees(degrees);
    }

    @Override
    public float unpackDegrees(byte packedDegrees) {
        return OxidiziumLib.unpack_degrees(packedDegrees);
    }

    @Override
    public int wrapDegrees(int degrees) {
        return OxidiziumLib.wrap_degrees_int(degrees);
    }

    @Override
    public float wrapDegrees(long degrees) {
        return OxidiziumLib.wrap_degrees_long(degrees);
    }

    @Override
    public float wrapDegrees(float degrees) {
        return OxidiziumLib.wrap_degrees_float(degrees);
    }

    @Override
    public double wrapDegrees(double degrees) {
        return OxidiziumLib.wrap_degrees_double(degrees);
    }

    @Override
    public float subtractAngles(float start, float end) {
        return OxidiziumLib.subtract_angles(start, end);
    }

    @Override
    public float angleBetween(float first, float second) {
        return OxidiziumLib.angle_between(first, second);
    }

    @Override
    public float clampAngle(float value, float mean, float delta) {
        return OxidiziumLib.clamp_angle(value, mean, delta);
    }

    @Override
    public float stepTowards(float from, float to, float step) {
        return OxidiziumLib.step_towards(from, to, step);
    }

    @Override
    public float stepUnwrappedAngleTowards(float from, float to, float step) {
        return OxidiziumLib.step_unwrapped_angle_towards(from, to, step);
    }

    @Override
    public int parseInt(String string, int fallback) {
        return 0; // TODO Implement
    }

    @Override
    public int smallestEncompassingPowerOfTwo(int value) {
        return OxidiziumLib.smallest_encompassing_power_of_two(value);
    }

    @Override
    public int smallestEncompassingSquareSideLength(int value) {
        return OxidiziumLib.smallest_encompassing_square_side_length(value);
    }

    @Override
    public boolean isPowerOfTwo(int value) {
        return OxidiziumLib.is_power_of_two(value);
    }

    @Override
    public int ceilLog2(int value) {
        return OxidiziumLib.ceil_log_2(value);
    }

    @Override
    public int floorLog2(int value) {
        return OxidiziumLib.floor_log_2(value);
    }

    @Override
    public int packRgb(float r, float g, float b) {
        return OxidiziumLib.pack_rgb(r, g, b);
    }

    @Override
    public long ceilLong(double value) {
        return OxidiziumLib.ceil_long(value);
    }

    @Override
    public float fractionalPart(float value) {
        return OxidiziumLib.fractional_part_float(value);
    }

    @Override
    public double fractionalPart(double value) {
        return OxidiziumLib.fractional_part_double(value);
    }

    @Override
    public long hashCode(int x, int y, int z) {
        return OxidiziumLib.hash_code(x, y, z);
    }

    @Override
    public double getLerpProgress(double value, double start, double end) {
        return OxidiziumLib.get_lerp_progress_double(value, start, end);
    }

    @Override
    public float getLerpProgress(float value, float start, float end) {
        return OxidiziumLib.get_lerp_progress_float(value, start, end);
    }

    @Override
    public double atan2(double y, double x) {
        return OxidiziumLib.atan_2(y, x);
    }

    @Override
    public float inverseSqrt(float x) {
        return OxidiziumLib.inverse_sqrt_float(x);
    }

    @Override
    public double inverseSqrt(double x) {
        return OxidiziumLib.inverse_sqrt_double(x);
    }

    @Override
    public double fastInverseSqrt(double x) {
        return OxidiziumLib.fast_inverse_sqrt(x);
    }

    @Override
    public float fastInverseCbrt(float x) {
        return OxidiziumLib.fast_inverse_cbrt(x);
    }

    @Override
    public int hsvToRgb(float hue, float saturation, float value) {
        return OxidiziumLib.hsv_to_rgb(hue, saturation, value);
    }

    @Override
    public int hsvToArgb(float hue, float saturation, float value, int alpha) {
        return OxidiziumLib.hsv_to_argb(hue, saturation, value, alpha);
    }

    @Override
    public int idealHash(int value) {
        return OxidiziumLib.ideal_hash(value);
    }

    @Override
    public int lerp(float delta, int start, int end) {
        return OxidiziumLib.lerp_int(delta, start, end);
    }

    @Override
    public int lerpPositive(float delta, int start, int end) {
        return OxidiziumLib.lerp_positive(delta, start, end);
    }

    @Override
    public float lerp(float delta, float start, float end) {
        return OxidiziumLib.lerp_float(delta, start, end);
    }

    @Override
    public double lerp(double delta, double start, double end) {
        return OxidiziumLib.lerp_double(delta, start, end);
    }

    @Override
    public double lerp2(double deltaX, double deltaY, double x0y0, double x1y0, double x0y1, double x1y1) {
        return OxidiziumLib.lerp_2(deltaX, deltaY, x0y0, x1y0, x0y1, x1y1);
    }

    @Override
    public double lerp3(double deltaX, double deltaY, double deltaZ, double x0y0z0, double x1y0z0, double x0y1z0, double x1y1z0, double x0y0z1, double x1y0z1, double x0y1z1, double x1y1z1) {
        return OxidiziumLib.lerp_3(deltaX, deltaY, deltaZ, x0y0z0, x1y0z0, x0y1z0, x1y1z0, x0y0z1, x1y0z1, x0y1z1, x1y1z1);
    }

    @Override
    public float catmullRom(float delta, float p0, float p1, float p2, float p3) {
        return OxidiziumLib.catmull_rom(delta, p0, p1, p2, p3);
    }

    @Override
    public double perlinFade(double value) {
        return OxidiziumLib.perlin_fade(value);
    }

    @Override
    public double perlinFadeDerivative(double value) {
        return OxidiziumLib.perlin_fade_derivative(value);
    }

    @Override
    public int sign(double value) {
        return OxidiziumLib.sign(value);
    }

    @Override
    public float lerpAngleDegrees(float delta, float start, float end) {
        return OxidiziumLib.lerp_angle_degrees_float(delta, start, end);
    }

    @Override
    public double lerpAngleDegrees(double delta, double start, double end) {
        return OxidiziumLib.lerp_angle_degrees_double(delta, start, end);
    }

    @Override
    public float lerpAngleRadians(float delta, float start, float end) {
        return OxidiziumLib.lerp_angle_radians(delta, start, end);
    }

    @Override
    public float wrap(float value, float maxDeviation) {
        return OxidiziumLib.wrap(value, maxDeviation);
    }

    @Override
    public float square(float n) {
        return OxidiziumLib.square_float(n);
    }

    @Override
    public double square(double n) {
        return OxidiziumLib.square_double(n);
    }

    @Override
    public int square(int n) {
        return OxidiziumLib.square_int(n);
    }

    @Override
    public long square(long n) {
        return OxidiziumLib.square_long(n);
    }

    @Override
    public double clampedMap(double value, double oldStart, double oldEnd, double newStart, double newEnd) {
        return OxidiziumLib.clamped_map_double(value, oldStart, oldEnd, newStart, newEnd);
    }

    @Override
    public float clampedMap(float value, float oldStart, float oldEnd, float newStart, float newEnd) {
        return OxidiziumLib.clamped_map_float(value, oldStart, oldEnd, newStart, newEnd);
    }

    @Override
    public double map(double value, double oldStart, double oldEnd, double newStart, double newEnd) {
        return OxidiziumLib.map_double(value, oldStart, oldEnd, newStart, newEnd);
    }

    @Override
    public float map(float value, float oldStart, float oldEnd, float newStart, float newEnd) {
        return OxidiziumLib.map_float(value, oldStart, oldEnd, newStart, newEnd);
    }

    @Override
    public int roundUpToMultiple(int value, int divisor) {
        return OxidiziumLib.round_up_to_multiple(value, divisor);
    }

    @Override
    public int ceilDiv(int a, int b) {
        return OxidiziumLib.ceil_div(a, b);
    }

    @Override
    public double squaredHypot(double a, double b) {
        return OxidiziumLib.squared_hypot(a, b);
    }

    @Override
    public double hypot(double a, double b) {
        return OxidiziumLib.hypot_double(a, b);
    }

    @Override
    public float hypot(float a, float b) {
        return OxidiziumLib.hypot_float(a, b);
    }

    @Override
    public double squaredMagnitude(double a, double b, double c) {
        return OxidiziumLib.squared_magnitude(a, b, c);
    }

    @Override
    public double magnitude(double a, double b, double c) {
        return OxidiziumLib.magnitude_double(a, b, c);
    }

    @Override
    public float magnitude(float a, float b, float c) {
        return OxidiziumLib.magnitude_float(a, b, c);
    }

    @Override
    public int roundDownToMultiple(double a, int b) {
        return OxidiziumLib.round_down_to_multiple(a, b);
    }

    @Override
    public int multiplyFraction(int numerator, int denominator, int multiplier) {
        return OxidiziumLib.multiply_fraction(numerator, denominator, multiplier);
    }

    @Override
    public float lithiumSin(double value) {
        return OxidiziumLib.lithium_sin_double(value);
    }

    @Override
    public float lithiumCos(double value) {
        return OxidiziumLib.lithium_cos_double(value);
    }

    @Override
    public float lithiumSin(float value) {
        return OxidiziumLib.lithium_sin_float(value);
    }

    @Override
    public float lithiumCos(float value) {
        return OxidiziumLib.lithium_cos_float(value);
    }
}
