package com.github.tatercertified.nalim;
import com.github.tatercertified.oxidizium.NativeInterface;
import com.github.tatercertified.oxidizium.utils.annotation.*;
public class NativeImpl implements NativeInterface {

@Override
public float sin_double(double x) {
	return OxidiziumLib.sin_double(x);
}

@Override
public float sin_float(float x) {
	return OxidiziumLib.sin_float(x);
}

@Override
public float cos_float(float x) {
	return OxidiziumLib.cos_float(x);
}

@Override
public float cos_double(double x) {
	return OxidiziumLib.cos_double(x);
}

@Override
public float lithium_sin_float(float x) {
	return OxidiziumLib.lithium_sin_float(x);
}

@Override
public float lithium_cos_float(float x) {
	return OxidiziumLib.lithium_cos_float(x);
}

@Override
public float lithium_sin_double(double x) {
	return OxidiziumLib.lithium_sin_double(x);
}

@Override
public float lithium_cos_double(double x) {
	return OxidiziumLib.lithium_cos_double(x);
}

@Override
public float sqrt_float(@PositiveOnly float x) {
	return OxidiziumLib.sqrt_float(x);
}

@Override
public int floor_float(float x) {
	return OxidiziumLib.floor_float(x);
}

@Override
public int floor_double(double x) {
	return OxidiziumLib.floor_double(x);
}

@Override
public long floor_long(double x) {
	return OxidiziumLib.floor_long(x);
}

@Override
public float abs_float(float x) {
	return OxidiziumLib.abs_float(x);
}

@Override
public int abs_int(int x) {
	return OxidiziumLib.abs_int(x);
}

@Override
public int ceil_float(float x) {
	return OxidiziumLib.ceil_float(x);
}

@Override
public int ceil_double(double x) {
	return OxidiziumLib.ceil_double(x);
}

@Override
public long ceil_long(double x) {
	return OxidiziumLib.ceil_long(x);
}

@Override
public int clamp_int(int x, @Min int min, @Max int max) {
	return OxidiziumLib.clamp_int(x, min, max);
}

@Override
public long clamp_long(long x, @Min long min, @Max long max) {
	return OxidiziumLib.clamp_long(x, min, max);
}

@Override
public float clamp_float(float x, @Min float min, @Max float max) {
	return OxidiziumLib.clamp_float(x, min, max);
}

@Override
public double clamp_double(double x, @Min double min, @Max double max) {
	return OxidiziumLib.clamp_double(x, min, max);
}

@Override
public double clamp_lerp_double(double start, double end, double delta) {
	return OxidiziumLib.clamp_lerp_double(start, end, delta);
}

@Override
public float clamp_lerp_float(float start, float end, float delta) {
	return OxidiziumLib.clamp_lerp_float(start, end, delta);
}

@Override
public int abs_max_int(int i, int j) {
	return OxidiziumLib.abs_max_int(i, j);
}

@Override
public float abs_max_float(float i, float j) {
	return OxidiziumLib.abs_max_float(i, j);
}

@Override
public double abs_max(double a, double b) {
	return OxidiziumLib.abs_max(a, b);
}

@Override
public int abs_max_difference(int i, int j, int k, int l) {
	return OxidiziumLib.abs_max_difference(i, j, k, l);
}

@Override
public int floor_div(int dividend, @NonZero int divisor) {
	return OxidiziumLib.floor_div(dividend, divisor);
}

@Override
public boolean approximately_equals_float(float a, float b) {
	return OxidiziumLib.approximately_equals_float(a, b);
}

@Override
public boolean approximately_equals_double(double a, double b) {
	return OxidiziumLib.approximately_equals_double(a, b);
}

@Override
public int floor_mod_int(int dividend, @NonZero int divisor) {
	return OxidiziumLib.floor_mod_int(dividend, divisor);
}

@Override
public float floor_mod_float(float dividend, @NonZero float divisor) {
	return OxidiziumLib.floor_mod_float(dividend, divisor);
}

@Override
public double floor_mod_double(double dividend, @NonZero double divisor) {
	return OxidiziumLib.floor_mod_double(dividend, divisor);
}

@Override
public boolean is_multiple_of(int a, @NonZero int b) {
	return OxidiziumLib.is_multiple_of(a, b);
}

@Override
public byte pack_degrees(float degrees) {
	return OxidiziumLib.pack_degrees(degrees);
}

@Override
public float unpack_degrees(byte packed_degrees) {
	return OxidiziumLib.unpack_degrees(packed_degrees);
}

@Override
public int wrap_degrees_int(int degrees) {
	return OxidiziumLib.wrap_degrees_int(degrees);
}

@Override
public float wrap_degrees_long(long degrees) {
	return OxidiziumLib.wrap_degrees_long(degrees);
}

@Override
public float wrap_degrees_float(float degrees) {
	return OxidiziumLib.wrap_degrees_float(degrees);
}

@Override
public double wrap_degrees_double(double degrees) {
	return OxidiziumLib.wrap_degrees_double(degrees);
}

@Override
public float subtract_angles(float start, float end) {
	return OxidiziumLib.subtract_angles(start, end);
}

@Override
public float angle_between(float first, float second) {
	return OxidiziumLib.angle_between(first, second);
}

@Override
public float clamp_angle(float value, float mean, float delta) {
	return OxidiziumLib.clamp_angle(value, mean, delta);
}

@Override
public float step_towards(float from, float to, float step) {
	return OxidiziumLib.step_towards(from, to, step);
}

@Override
public float step_unwrapped_angle_towards(float from, float to, float step) {
	return OxidiziumLib.step_unwrapped_angle_towards(from, to, step);
}

@Override
public int smallest_encompassing_power_of_two(int value) {
	return OxidiziumLib.smallest_encompassing_power_of_two(value);
}

@Override
public int smallest_encompassing_square_side_length(@PositiveOnly int value) {
	return OxidiziumLib.smallest_encompassing_square_side_length(value);
}

@Override
public boolean is_power_of_two(int value) {
	return OxidiziumLib.is_power_of_two(value);
}

@Override
public int ceil_log_2(int value) {
	return OxidiziumLib.ceil_log_2(value);
}

@Override
public int floor_log_2(int value) {
	return OxidiziumLib.floor_log_2(value);
}

@Override
public int pack_rgb(float red, float green, float blue) {
	return OxidiziumLib.pack_rgb(red, green, blue);
}

@Override
public float fractional_part_float(float value) {
	return OxidiziumLib.fractional_part_float(value);
}

@Override
public double fractional_part_double(double value) {
	return OxidiziumLib.fractional_part_double(value);
}

@Override
public long hash_code(int x, int y, int z) {
	return OxidiziumLib.hash_code(x, y, z);
}

@Override
public double get_lerp_progress_double(double value, double start, double end) {
	return OxidiziumLib.get_lerp_progress_double(value, start, end);
}

@Override
public float get_lerp_progress_float(float value, float start, float end) {
	return OxidiziumLib.get_lerp_progress_float(value, start, end);
}

@Override
public double atan_2(double y, double x) {
	return OxidiziumLib.atan_2(y, x);
}

@Override
public float inverse_sqrt_float(@PositiveOnly float x) {
	return OxidiziumLib.inverse_sqrt_float(x);
}

@Override
public double inverse_sqrt_double(@PositiveOnly double x) {
	return OxidiziumLib.inverse_sqrt_double(x);
}

@Override
public double fast_inverse_sqrt(double x) {
	return OxidiziumLib.fast_inverse_sqrt(x);
}

@Override
public float fast_inverse_sqrt_float(float x) {
	return OxidiziumLib.fast_inverse_sqrt_float(x);
}

@Override
public float fast_inverse_cbrt(float x) {
	return OxidiziumLib.fast_inverse_cbrt(x);
}

@Override
public int hsv_to_rgb(@PositiveOnly float hue, @PositiveOnly float saturation, @PositiveOnly float value) {
	return OxidiziumLib.hsv_to_rgb(hue, saturation, value);
}

@Override
public int hsv_to_argb(@PositiveOnly float hue, @PositiveOnly float saturation, @PositiveOnly float value, @PositiveOnly int alpha) {
	return OxidiziumLib.hsv_to_argb(hue, saturation, value, alpha);
}

@Override
public int ideal_hash(int value) {
	return OxidiziumLib.ideal_hash(value);
}

@Override
public int lerp_int(float delta, int start, int end) {
	return OxidiziumLib.lerp_int(delta, start, end);
}

@Override
public float lerp_float(float delta, float start, float end) {
	return OxidiziumLib.lerp_float(delta, start, end);
}

@Override
public double lerp_double(double delta, double start, double end) {
	return OxidiziumLib.lerp_double(delta, start, end);
}

@Override
public int lerp_positive(float delta, int start, int end) {
	return OxidiziumLib.lerp_positive(delta, start, end);
}

@Override
public double lerp_2(double deltax, double deltay, double x0y0, double x1y0, double x0y1, double x1y1) {
	return OxidiziumLib.lerp_2(deltax, deltay, x0y0, x1y0, x0y1, x1y1);
}

@Override
public double lerp_3(double delta_x, double delta_y, double delta_z, double x0y0z0, double x1y0z0, double x0y1z0, double x1y1z0, double x0y0z1, double x1y0z1, double x0y1z1, double x1y1z1) {
	return OxidiziumLib.lerp_3(delta_x, delta_y, delta_z, x0y0z0, x1y0z0, x0y1z0, x1y1z0, x0y0z1, x1y0z1, x0y1z1, x1y1z1);
}

@Override
public float catmull_rom(float delta, float p0, float p1, float p2, float p3) {
	return OxidiziumLib.catmull_rom(delta, p0, p1, p2, p3);
}

@Override
public double perlin_fade(double value) {
	return OxidiziumLib.perlin_fade(value);
}

@Override
public double perlin_fade_derivative(double value) {
	return OxidiziumLib.perlin_fade_derivative(value);
}

@Override
public int sign(double value) {
	return OxidiziumLib.sign(value);
}

@Override
public float lerp_angle_degrees_float(float delta, float start, float end) {
	return OxidiziumLib.lerp_angle_degrees_float(delta, start, end);
}

@Override
public double lerp_angle_degrees_double(double delta, double start, double end) {
	return OxidiziumLib.lerp_angle_degrees_double(delta, start, end);
}

@Override
public float lerp_angle_radians(float delta, float start, float end) {
	return OxidiziumLib.lerp_angle_radians(delta, start, end);
}

@Override
public float wrap(float value, float max_deviation) {
	return OxidiziumLib.wrap(value, max_deviation);
}

@Override
public float square_float(float value) {
	return OxidiziumLib.square_float(value);
}

@Override
public double square_double(double value) {
	return OxidiziumLib.square_double(value);
}

@Override
public int square_int(int value) {
	return OxidiziumLib.square_int(value);
}

@Override
public long square_long(long value) {
	return OxidiziumLib.square_long(value);
}

@Override
public double clamped_map_double(double value, double old_start, double old_end, double new_start, double new_end) {
	return OxidiziumLib.clamped_map_double(value, old_start, old_end, new_start, new_end);
}

@Override
public float clamped_map_float(float value, float old_start, float old_end, float new_start, float new_end) {
	return OxidiziumLib.clamped_map_float(value, old_start, old_end, new_start, new_end);
}

@Override
public double map_double(double value, double old_start, double old_end, double new_start, double new_end) {
	return OxidiziumLib.map_double(value, old_start, old_end, new_start, new_end);
}

@Override
public float map_float(float value, float old_start, float old_end, float new_start, float new_end) {
	return OxidiziumLib.map_float(value, old_start, old_end, new_start, new_end);
}

@Override
public int round_up_to_multiple(int value, @NonZero int divisor) {
	return OxidiziumLib.round_up_to_multiple(value, divisor);
}

@Override
public int ceil_div(int a, @NonZero int b) {
	return OxidiziumLib.ceil_div(a, b);
}

@Override
public double squared_hypot(double a, double b) {
	return OxidiziumLib.squared_hypot(a, b);
}

@Override
public double hypot_double(double a, double b) {
	return OxidiziumLib.hypot_double(a, b);
}

@Override
public float hypot_float(float a, float b) {
	return OxidiziumLib.hypot_float(a, b);
}

@Override
public double squared_magnitude(double a, double b, double c) {
	return OxidiziumLib.squared_magnitude(a, b, c);
}

@Override
public double magnitude_double(double a, double b, double c) {
	return OxidiziumLib.magnitude_double(a, b, c);
}

@Override
public float magnitude_float(float a, float b, float c) {
	return OxidiziumLib.magnitude_float(a, b, c);
}

@Override
public double magnitude_int(int a, double b, int c) {
	return OxidiziumLib.magnitude_int(a, b, c);
}

@Override
public int round_down_to_multiple(double a, int b) {
	return OxidiziumLib.round_down_to_multiple(a, b);
}

@Override
public int multiply_fraction(int numerator, int denominator, int multiplier) {
	return OxidiziumLib.multiply_fraction(numerator, denominator, multiplier);
}

@Override
public float wrap_degrees_90(float angle) {
	return OxidiziumLib.wrap_degrees_90(angle);
}

@Override
public boolean is_power_of_2(long value) {
	return OxidiziumLib.is_power_of_2(value);
}

@Override
public long round_towards_long(long input, long multiple) {
	return OxidiziumLib.round_towards_long(input, multiple);
}

@Override
public int round_towards_int(int input, int multiple) {
	return OxidiziumLib.round_towards_int(input, multiple);
}

@Override
public long positive_ceil_div_long(long input, long divisor) {
	return OxidiziumLib.positive_ceil_div_long(input, divisor);
}

@Override
public int positive_ceil_div_int(int input, int divisor) {
	return OxidiziumLib.positive_ceil_div_int(input, divisor);
}
}
