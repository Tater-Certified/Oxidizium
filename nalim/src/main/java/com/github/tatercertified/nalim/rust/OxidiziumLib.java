package com.github.tatercertified.nalim.rust;

import one.nalim.Library;
import one.nalim.Link;

@Library("oxidizium")
public class OxidiziumLib {
    @Link
    public static native float sin_float(float value);

    @Link
    public static native float cos_float(float value);

    @Link
    public static native float sin_double(double value);

    @Link
    public static native float cos_double(double value);

    @Link
    public static native float sqrt_float(float value);

    @Link
    public static native int floor_float(float value);

    @Link
    public static native int floor_double(double value);

    @Link
    public static native long floor_long(double value);

    @Link
    public static native float abs_float(float value);

    @Link
    public static native int abs_int(int value);

    @Link
    public static native int ceil_float(float value);

    @Link
    public static native int ceil_double(double value);

    @Link
    public static native int clamp_int(int value, int min, int max);

    @Link
    public static native long clamp_long(long value, long min, long max);

    @Link
    public static native float clamp_float(float value, float min, float max);

    @Link
    public static native double clamp_double(double value, double min, double max);

    @Link
    public static native double clamp_lerp_double(double start, double end, double delta);

    @Link
    public static native float clamp_lerp_float(float start, float end, float delta);

    @Link
    public static native int abs_max_int(int i, int j);

    @Link
    public static native float abs_max_float(float f, float g);

    @Link
    public static native int abs_max_difference(int i, int j, int k, int l);

    @Link
    public static native double abs_max(double a, double b);

    @Link
    public static native int floor_div(int dividend, int divisor);

    @Link
    public static native boolean approximately_equals_float(float a, float b);

    @Link
    public static native boolean approximately_equals_double(double a, double b);

    @Link
    public static native int floor_mod_int(int dividend, int divisor);

    @Link
    public static native float floor_mod_float(float dividend, float divisor);

    @Link
    public static native double floor_mod_double(double dividend, double divisor);

    @Link
    public static native boolean is_multiple_of(int a, int b);

    @Link
    public static native byte pack_degrees(float degrees);

    @Link
    public static native float unpack_degrees(byte packedDegrees);

    @Link
    public static native int wrap_degrees_int(int degrees);

    @Link
    public static native float wrap_degrees_long(long degrees);

    @Link
    public static native float wrap_degrees_float(float degrees);

    @Link
    public static native double wrap_degrees_double(double degrees);

    @Link
    public static native float subtract_angles(float start, float end);

    @Link
    public static native float angle_between(float first, float second);

    @Link
    public static native float clamp_angle(float value, float mean, float delta);

    @Link
    public static native float step_towards(float from, float to, float step);

    @Link
    public static native float step_unwrapped_angle_towards(float from, float to, float step);

    /*
    @Link
    public static native int parseInt(String string, int fallback) {
        return 0; // TODO Implement
    }

     */

    @Link
    public static native int smallest_encompassing_power_of_two(int value);

    @Link
    public static native int smallest_encompassing_square_side_length(int value);

    @Link
    public static native boolean is_power_of_two(int value);

    @Link
    public static native int ceil_log_2(int value);

    @Link
    public static native int floor_log_2(int value);

    @Link
    public static native long ceil_long(double value);

    @Link
    public static native int pack_rgb(float r, float g, float b);

    @Link
    public static native float fractional_part_float(float value);

    @Link
    public static native double fractional_part_double(double value);

    @Link
    public static native long hash_code(int x, int y, int z);

    @Link
    public static native double get_lerp_progress_double(double value, double start, double end);

    @Link
    public static native float get_lerp_progress_float(float value, float start, float end);

    @Link
    public static native double atan_2(double y, double x);

    @Link
    public static native float inverse_sqrt_float(float x);

    @Link
    public static native double inverse_sqrt_double(double x);

    @Link
    public static native double fast_inverse_sqrt(double x);

    @Link
    public static native float fast_inverse_sqrt_float(float x);

    @Link
    public static native float fast_inverse_cbrt(float x);

    @Link
    public static native int hsv_to_rgb(float hue, float saturation, float value);

    @Link
    public static native int hsv_to_argb(float hue, float saturation, float value, int alpha);

    @Link
    public static native int ideal_hash(int value);

    @Link
    public static native int lerp_int(float delta, int start, int end);

    @Link
    public static native int lerp_positive(float delta, int start, int end);

    @Link
    public static native float lerp_float(float delta, float start, float end);

    @Link
    public static native double lerp_double(double delta, double start, double end);

    @Link
    public static native double lerp_2(double deltaX, double deltaY, double x0y0, double x1y0, double x0y1, double x1y1);

    @Link
    public static native double lerp_3(double deltaX, double deltaY, double deltaZ, double x0y0z0, double x1y0z0, double x0y1z0, double x1y1z0, double x0y0z1, double x1y0z1, double x0y1z1, double x1y1z1);

    @Link
    public static native float catmull_rom(float delta, float p0, float p1, float p2, float p3);

    @Link
    public static native double perlin_fade(double value);

    @Link
    public static native double perlin_fade_derivative(double value);

    @Link
    public static native int sign(double value);

    @Link
    public static native float lerp_angle_degrees_float(float delta, float start, float end);

    @Link
    public static native double lerp_angle_degrees_double(double delta, double start, double end);

    @Link
    public static native float lerp_angle_radians(float delta, float start, float end);

    @Link
    public static native float wrap(float value, float maxDeviation);

    @Link
    public static native float square_float(float n);

    @Link
    public static native double square_double(double n);

    @Link
    public static native int square_int(int n);

    @Link
    public static native long square_long(long n);

    @Link
    public static native double clamped_map_double(double value, double oldStart, double oldEnd, double newStart, double newEnd);

    @Link
    public static native float clamped_map_float(float value, float oldStart, float oldEnd, float newStart, float newEnd);

    @Link
    public static native double map_double(double value, double oldStart, double oldEnd, double newStart, double newEnd);

    @Link
    public static native float map_float(float value, float oldStart, float oldEnd, float newStart, float newEnd);

    @Link
    public static native int round_up_to_multiple(int value, int divisor);

    @Link
    public static native int ceil_div(int a, int b);

    @Link
    public static native double squared_hypot(double a, double b);

    @Link
    public static native double hypot_double(double a, double b);

    @Link
    public static native float hypot_float(float a, float b);

    @Link
    public static native double squared_magnitude(double a, double b, double c);

    @Link
    public static native double magnitude_double(double a, double b, double c);

    @Link
    public static native float magnitude_float(float a, float b, float c);

    @Link
    public static native double magnitude_int(int a, double b, int c);

    @Link
    public static native int round_down_to_multiple(double a, int b);

    @Link
    public static native int multiply_fraction(int numerator, int denominator, int multiplier);

    @Link
    public static native float lithium_sin_float(float value);

    @Link
    public static native float lithium_cos_float(float value);

    @Link
    public static native float lithium_sin_double(double value);

    @Link
    public static native float lithium_cos_double(double value);
}
