package com.github.tatercertified.oxidizium_tester.test;
import org.openjdk.jmh.annotations.*;
import java.util.concurrent.TimeUnit;
import org.apache.commons.lang3.math.Fraction;
import net.minecraft.util.Mth;
import static com.github.tatercertified.oxidizium.Oxidizium.NATIVE;
@Warmup(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@Measurement(iterations = 1, time = 1, timeUnit = TimeUnit.SECONDS)
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
public class Benchmarks {

@Benchmark
public float sin() {
    return Mth.sin(100.5);
}
@Benchmark
public float native_sin() {
    return NATIVE.lithium_sin_double(100.5);
}

@Benchmark
public float cos() {
    return Mth.cos(100.5);
}
@Benchmark
public float native_cos() {
    return NATIVE.lithium_cos_double(100.5);
}

@Benchmark
public float sin_1() {
    return Mth.sin(100.5);
}
@Benchmark
public float native_sin_1() {
    return NATIVE.sin_double(100.5);
}

@Benchmark
public float cos_1() {
    return Mth.cos(100.5);
}
@Benchmark
public float native_cos_1() {
    return NATIVE.cos_double(100.5);
}

@Benchmark
public float sqrt() {
    return Mth.sqrt(0.25F);
}
@Benchmark
public float native_sqrt() {
    return NATIVE.sqrt_float(0.25F);
}

@Benchmark
public int floor() {
    return Mth.floor(0.25F);
}
@Benchmark
public int native_floor() {
    return NATIVE.floor_float(0.25F);
}

@Benchmark
public int floor_1() {
    return Mth.floor(100.5);
}
@Benchmark
public int native_floor_1() {
    return NATIVE.floor_double(100.5);
}

@Benchmark
public long lfloor() {
    return Mth.lfloor(100.5);
}
@Benchmark
public long native_lfloor() {
    return NATIVE.floor_long(100.5);
}

@Benchmark
public float abs() {
    return Mth.abs(0.25F);
}
@Benchmark
public float native_abs() {
    return NATIVE.abs_float(0.25F);
}

@Benchmark
public int abs_1() {
    return Mth.abs(10);
}
@Benchmark
public int native_abs_1() {
    return NATIVE.abs_int(10);
}

@Benchmark
public int ceil() {
    return Mth.ceil(0.25F);
}
@Benchmark
public int native_ceil() {
    return NATIVE.ceil_float(0.25F);
}

@Benchmark
public int ceil_1() {
    return Mth.ceil(100.5);
}
@Benchmark
public int native_ceil_1() {
    return NATIVE.ceil_double(100.5);
}

@Benchmark
public int clamp() {
    return Mth.clamp(10, 10, 10);
}
@Benchmark
public int native_clamp() {
    return NATIVE.clamp_int(10, 10, 10);
}

@Benchmark
public long clamp_1() {
    return Mth.clamp(100L, 100L, 100L);
}
@Benchmark
public long native_clamp_1() {
    return NATIVE.clamp_long(100L, 100L, 100L);
}

@Benchmark
public float clamp_2() {
    return Mth.clamp(0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_clamp_2() {
    return NATIVE.clamp_float(0.25F, 0.25F, 0.25F);
}

@Benchmark
public double clamp_3() {
    return Mth.clamp(100.5, 100.5, 100.5);
}
@Benchmark
public double native_clamp_3() {
    return NATIVE.clamp_double(100.5, 100.5, 100.5);
}

@Benchmark
public float absMax() {
    return Mth.absMax(0.25F, 0.25F);
}
@Benchmark
public float native_absMax() {
    return NATIVE.abs_max_float(0.25F, 0.25F);
}

@Benchmark
public int chessboardDistance() {
    return Mth.chessboardDistance(10, 10, 10, 10);
}
@Benchmark
public int native_chessboardDistance() {
    return NATIVE.abs_max_difference(10, 10, 10, 10);
}

@Benchmark
public double absMax_1() {
    return Mth.absMax(100.5, 100.5);
}
@Benchmark
public double native_absMax_1() {
    return NATIVE.abs_max(100.5, 100.5);
}

@Benchmark
public int floorDiv() {
    return Mth.floorDiv(10, 10);
}
@Benchmark
public int native_floorDiv() {
    return NATIVE.floor_div(10, 10);
}

@Benchmark
public boolean equal() {
    return Mth.equal(0.25F, 0.25F);
}
@Benchmark
public boolean native_equal() {
    return NATIVE.approximately_equals_float(0.25F, 0.25F);
}

@Benchmark
public boolean equal_1() {
    return Mth.equal(100.5, 100.5);
}
@Benchmark
public boolean native_equal_1() {
    return NATIVE.approximately_equals_double(100.5, 100.5);
}

@Benchmark
public int positiveModulo() {
    return Mth.positiveModulo(10, 10);
}
@Benchmark
public int native_positiveModulo() {
    return NATIVE.floor_mod_int(10, 10);
}

@Benchmark
public float positiveModulo_1() {
    return Mth.positiveModulo(0.25F, 0.25F);
}
@Benchmark
public float native_positiveModulo_1() {
    return NATIVE.floor_mod_float(0.25F, 0.25F);
}

@Benchmark
public double positiveModulo_2() {
    return Mth.positiveModulo(100.5, 100.5);
}
@Benchmark
public double native_positiveModulo_2() {
    return NATIVE.floor_mod_double(100.5, 100.5);
}

@Benchmark
public boolean isMultipleOf() {
    return Mth.isMultipleOf(10, 10);
}
@Benchmark
public boolean native_isMultipleOf() {
    return NATIVE.is_multiple_of(10, 10);
}

@Benchmark
public byte packDegrees() {
    return Mth.packDegrees(0.25F);
}
@Benchmark
public byte native_packDegrees() {
    return NATIVE.pack_degrees(0.25F);
}

@Benchmark
public float unpackDegrees() {
    return Mth.unpackDegrees((byte) 2);
}
@Benchmark
public float native_unpackDegrees() {
    return NATIVE.unpack_degrees((byte) 2);
}

@Benchmark
public int wrapDegrees() {
    return Mth.wrapDegrees(10);
}
@Benchmark
public int native_wrapDegrees() {
    return NATIVE.wrap_degrees_int(10);
}

@Benchmark
public float wrapDegrees_1() {
    return Mth.wrapDegrees(100L);
}
@Benchmark
public float native_wrapDegrees_1() {
    return NATIVE.wrap_degrees_long(100L);
}

@Benchmark
public float wrapDegrees_2() {
    return Mth.wrapDegrees(0.25F);
}
@Benchmark
public float native_wrapDegrees_2() {
    return NATIVE.wrap_degrees_float(0.25F);
}

@Benchmark
public double wrapDegrees_3() {
    return Mth.wrapDegrees(100.5);
}
@Benchmark
public double native_wrapDegrees_3() {
    return NATIVE.wrap_degrees_double(100.5);
}

@Benchmark
public float degreesDifference() {
    return Mth.degreesDifference(0.25F, 0.25F);
}
@Benchmark
public float native_degreesDifference() {
    return NATIVE.subtract_angles(0.25F, 0.25F);
}

@Benchmark
public float degreesDifferenceAbs() {
    return Mth.degreesDifferenceAbs(0.25F, 0.25F);
}
@Benchmark
public float native_degreesDifferenceAbs() {
    return NATIVE.angle_between(0.25F, 0.25F);
}

@Benchmark
public float rotateIfNecessary() {
    return Mth.rotateIfNecessary(0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_rotateIfNecessary() {
    return NATIVE.clamp_angle(0.25F, 0.25F, 0.25F);
}

@Benchmark
public float approach() {
    return Mth.approach(0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_approach() {
    return NATIVE.step_towards(0.25F, 0.25F, 0.25F);
}

@Benchmark
public float approachDegrees() {
    return Mth.approachDegrees(0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_approachDegrees() {
    return NATIVE.step_unwrapped_angle_towards(0.25F, 0.25F, 0.25F);
}

@Benchmark
public int smallestEncompassingPowerOfTwo() {
    return Mth.smallestEncompassingPowerOfTwo(10);
}
@Benchmark
public int native_smallestEncompassingPowerOfTwo() {
    return NATIVE.smallest_encompassing_power_of_two(10);
}

@Benchmark
public int smallestSquareSide() {
    return Mth.smallestSquareSide(10);
}
@Benchmark
public int native_smallestSquareSide() {
    return NATIVE.smallest_encompassing_square_side_length(10);
}

@Benchmark
public boolean isPowerOfTwo() {
    return Mth.isPowerOfTwo(10);
}
@Benchmark
public boolean native_isPowerOfTwo() {
    return NATIVE.is_power_of_two(10);
}

@Benchmark
public int ceillog2() {
    return Mth.ceillog2(10);
}
@Benchmark
public int native_ceillog2() {
    return NATIVE.ceil_log_2(10);
}

@Benchmark
public int log2() {
    return Mth.log2(10);
}
@Benchmark
public int native_log2() {
    return NATIVE.floor_log_2(10);
}

@Benchmark
public long ceilLong() {
    return Mth.ceilLong(100.5);
}
@Benchmark
public long native_ceilLong() {
    return NATIVE.ceil_long(100.5);
}

@Benchmark
public float frac() {
    return Mth.frac(0.25F);
}
@Benchmark
public float native_frac() {
    return NATIVE.fractional_part_float(0.25F);
}

@Benchmark
public double frac_1() {
    return Mth.frac(100.5);
}
@Benchmark
public double native_frac_1() {
    return NATIVE.fractional_part_double(100.5);
}

@Benchmark
public long getSeed() {
    return Mth.getSeed(10, 10, 10);
}
@Benchmark
public long native_getSeed() {
    return NATIVE.hash_code(10, 10, 10);
}

@Benchmark
public double inverseLerp() {
    return Mth.inverseLerp(100.5, 100.5, 100.5);
}
@Benchmark
public double native_inverseLerp() {
    return NATIVE.get_lerp_progress_double(100.5, 100.5, 100.5);
}

@Benchmark
public float inverseLerp_1() {
    return Mth.inverseLerp(0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_inverseLerp_1() {
    return NATIVE.get_lerp_progress_float(0.25F, 0.25F, 0.25F);
}

@Benchmark
public double atan2() {
    return Mth.atan2(100.5, 100.5);
}
@Benchmark
public double native_atan2() {
    return NATIVE.atan_2(100.5, 100.5);
}

@Benchmark
public float invSqrt() {
    return Mth.invSqrt(0.25F);
}
@Benchmark
public float native_invSqrt() {
    return NATIVE.inverse_sqrt_float(0.25F);
}

@Benchmark
public double invSqrt_1() {
    return Mth.invSqrt(100.5);
}
@Benchmark
public double native_invSqrt_1() {
    return NATIVE.inverse_sqrt_double(100.5);
}

@Benchmark
public double fastInvSqrt() {
    return Mth.fastInvSqrt(100.5);
}
@Benchmark
public double native_fastInvSqrt() {
    return NATIVE.fast_inverse_sqrt(100.5);
}

@Benchmark
public float fastInvCubeRoot() {
    return Mth.fastInvCubeRoot(0.25F);
}
@Benchmark
public float native_fastInvCubeRoot() {
    return NATIVE.fast_inverse_cbrt(0.25F);
}

@Benchmark
public int hsvToRgb() {
    return Mth.hsvToRgb(0.25F, 0.25F, 0.25F);
}
@Benchmark
public int native_hsvToRgb() {
    return NATIVE.hsv_to_rgb(0.25F, 0.25F, 0.25F);
}

@Benchmark
public int hsvToArgb() {
    return Mth.hsvToArgb(0.25F, 0.25F, 0.25F, 10);
}
@Benchmark
public int native_hsvToArgb() {
    return NATIVE.hsv_to_argb(0.25F, 0.25F, 0.25F, 10);
}

@Benchmark
public int murmurHash3Mixer() {
    return Mth.murmurHash3Mixer(10);
}
@Benchmark
public int native_murmurHash3Mixer() {
    return NATIVE.ideal_hash(10);
}

@Benchmark
public int lerpInt() {
    return Mth.lerpInt(0.25F, 10, 10);
}
@Benchmark
public int native_lerpInt() {
    return NATIVE.lerp_int(0.25F, 10, 10);
}

@Benchmark
public int lerpDiscrete() {
    return Mth.lerpDiscrete(0.25F, 10, 10);
}
@Benchmark
public int native_lerpDiscrete() {
    return NATIVE.lerp_positive(0.25F, 10, 10);
}

@Benchmark
public float lerp() {
    return Mth.lerp(0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_lerp() {
    return NATIVE.lerp_float(0.25F, 0.25F, 0.25F);
}

@Benchmark
public double lerp_1() {
    return Mth.lerp(100.5, 100.5, 100.5);
}
@Benchmark
public double native_lerp_1() {
    return NATIVE.lerp_double(100.5, 100.5, 100.5);
}

@Benchmark
public double lerp2() {
    return Mth.lerp2(100.5, 100.5, 100.5, 100.5, 100.5, 100.5);
}
@Benchmark
public double native_lerp2() {
    return NATIVE.lerp_2(100.5, 100.5, 100.5, 100.5, 100.5, 100.5);
}

@Benchmark
public double lerp3() {
    return Mth.lerp3(100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5);
}
@Benchmark
public double native_lerp3() {
    return NATIVE.lerp_3(100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5, 100.5);
}

@Benchmark
public float catmullrom() {
    return Mth.catmullrom(0.25F, 0.25F, 0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_catmullrom() {
    return NATIVE.catmull_rom(0.25F, 0.25F, 0.25F, 0.25F, 0.25F);
}

@Benchmark
public double smoothstep() {
    return Mth.smoothstep(100.5);
}
@Benchmark
public double native_smoothstep() {
    return NATIVE.perlin_fade(100.5);
}

@Benchmark
public double smoothstepDerivative() {
    return Mth.smoothstepDerivative(100.5);
}
@Benchmark
public double native_smoothstepDerivative() {
    return NATIVE.perlin_fade_derivative(100.5);
}

@Benchmark
public int sign() {
    return Mth.sign(100.5);
}
@Benchmark
public int native_sign() {
    return NATIVE.sign(100.5);
}

@Benchmark
public float rotLerp() {
    return Mth.rotLerp(0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_rotLerp() {
    return NATIVE.lerp_angle_degrees_float(0.25F, 0.25F, 0.25F);
}

@Benchmark
public double rotLerp_1() {
    return Mth.rotLerp(100.5, 100.5, 100.5);
}
@Benchmark
public double native_rotLerp_1() {
    return NATIVE.lerp_angle_degrees_double(100.5, 100.5, 100.5);
}

@Benchmark
public float rotLerpRad() {
    return Mth.rotLerpRad(0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_rotLerpRad() {
    return NATIVE.lerp_angle_radians(0.25F, 0.25F, 0.25F);
}

@Benchmark
public float triangleWave() {
    return Mth.triangleWave(0.25F, 0.25F);
}
@Benchmark
public float native_triangleWave() {
    return NATIVE.wrap(0.25F, 0.25F);
}

@Benchmark
public float square() {
    return Mth.square(0.25F);
}
@Benchmark
public float native_square() {
    return NATIVE.square_float(0.25F);
}

@Benchmark
public double square_1() {
    return Mth.square(100.5);
}
@Benchmark
public double native_square_1() {
    return NATIVE.square_double(100.5);
}

@Benchmark
public int square_2() {
    return Mth.square(10);
}
@Benchmark
public int native_square_2() {
    return NATIVE.square_int(10);
}

@Benchmark
public long square_3() {
    return Mth.square(100L);
}
@Benchmark
public long native_square_3() {
    return NATIVE.square_long(100L);
}

@Benchmark
public double clampedMap() {
    return Mth.clampedMap(100.5, 100.5, 100.5, 100.5, 100.5);
}
@Benchmark
public double native_clampedMap() {
    return NATIVE.clamped_map_double(100.5, 100.5, 100.5, 100.5, 100.5);
}

@Benchmark
public float clampedMap_1() {
    return Mth.clampedMap(0.25F, 0.25F, 0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_clampedMap_1() {
    return NATIVE.clamped_map_float(0.25F, 0.25F, 0.25F, 0.25F, 0.25F);
}

@Benchmark
public double map() {
    return Mth.map(100.5, 100.5, 100.5, 100.5, 100.5);
}
@Benchmark
public double native_map() {
    return NATIVE.map_double(100.5, 100.5, 100.5, 100.5, 100.5);
}

@Benchmark
public float map_1() {
    return Mth.map(0.25F, 0.25F, 0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_map_1() {
    return NATIVE.map_float(0.25F, 0.25F, 0.25F, 0.25F, 0.25F);
}

@Benchmark
public int roundToward() {
    return Mth.roundToward(10, 10);
}
@Benchmark
public int native_roundToward() {
    return NATIVE.round_towards_int(10, 10);
}

@Benchmark
public int positiveCeilDiv() {
    return Mth.positiveCeilDiv(10, 10);
}
@Benchmark
public int native_positiveCeilDiv() {
    return NATIVE.positive_ceil_div_int(10, 10);
}

@Benchmark
public double lengthSquared() {
    return Mth.lengthSquared(100.5, 100.5);
}
@Benchmark
public double native_lengthSquared() {
    return NATIVE.squared_hypot(100.5, 100.5);
}

@Benchmark
public double length() {
    return Mth.length(100.5, 100.5);
}
@Benchmark
public double native_length() {
    return NATIVE.hypot_double(100.5, 100.5);
}

@Benchmark
public float length_1() {
    return Mth.length(0.25F, 0.25F);
}
@Benchmark
public float native_length_1() {
    return NATIVE.hypot_float(0.25F, 0.25F);
}

@Benchmark
public double lengthSquared_1() {
    return Mth.lengthSquared(100.5, 100.5, 100.5);
}
@Benchmark
public double native_lengthSquared_1() {
    return NATIVE.squared_magnitude(100.5, 100.5, 100.5);
}

@Benchmark
public double length_2() {
    return Mth.length(100.5, 100.5, 100.5);
}
@Benchmark
public double native_length_2() {
    return NATIVE.magnitude_double(100.5, 100.5, 100.5);
}

@Benchmark
public float lengthSquared_2() {
    return Mth.lengthSquared(0.25F, 0.25F, 0.25F);
}
@Benchmark
public float native_lengthSquared_2() {
    return NATIVE.magnitude_float(0.25F, 0.25F, 0.25F);
}

@Benchmark
public int quantize() {
    return Mth.quantize(100.5, 10);
}
@Benchmark
public int native_quantize() {
    return NATIVE.round_down_to_multiple(100.5, 10);
}

@Benchmark
public int mulAndTruncate() {
    return Mth.mulAndTruncate(Fraction.getFraction(3, 4), 10);
}
@Benchmark
public int native_mulAndTruncate() {
    return NATIVE.multiply_fraction(Fraction.getFraction(3, 4).getNumerator(), Fraction.getFraction(3, 4).getDenominator(), 10);
}

@Benchmark
public float wrapDegrees90() {
    return Mth.wrapDegrees90(0.25F);
}
@Benchmark
public float native_wrapDegrees90() {
    return NATIVE.wrap_degrees_90(0.25F);
}

@Benchmark
public long roundToward_1() {
    return Mth.roundToward(100L, 100L);
}
@Benchmark
public long native_roundToward_1() {
    return NATIVE.round_towards_long(100L, 100L);
}

@Benchmark
public long positiveCeilDiv_1() {
    return Mth.positiveCeilDiv(100L, 100L);
}
@Benchmark
public long native_positiveCeilDiv_1() {
    return NATIVE.positive_ceil_div_long(100L, 100L);
}
}