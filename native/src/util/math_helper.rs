use once_cell::sync::Lazy;
use std::ffi::c_ushort;
use std::num::Wrapping;
use std::slice;

const APPROXIMATION_THRESHOLD: f32 = 1.0E-5;
const MULTIPLY_DE_BRUIJN_BIT_POS: [i8; 32] = [
    0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26,
    12, 18, 6, 11, 5, 10, 9,
];
const ROUNDER_256THS: f64 = f64::from_bits(4805340802404319232);
const DOUBLE_PI: f64 = std::f64::consts::PI * 2.0_f64;
const DOUBLE_PI_F32: f32 = std::f32::consts::PI * 2.0_f32;
const INVERSE_SQRT: u64 = 6910469410427058090;
const PACK: f32 = 256.0_f32 / 360.0_f32;

static SINE_TABLE: Lazy<[f32; 65536]> = Lazy::new(|| {
    let mut table = [0.0_f32; 65536];
    for (i, value) in table.iter_mut().enumerate() {
        *value = (i as f64 * DOUBLE_PI / 65536.0).sin() as f32;
    }
    table
});

// From Lithium https://github.com/CaffeineMC/lithium/blob/develop/com/src/main/java/net/caffeinemc/mods/lithium/com/util/math/CompactSineLUT.java
static SINE_TABLE_OPT: Lazy<[u32; 16385]> = Lazy::new(|| {
    let mut table = [0u32; 16385];
    for (i, value) in table.iter_mut().enumerate() {
        *value = (SINE_TABLE[i]).to_bits();
    }
    table
});

// From Lithium https://github.com/CaffeineMC/lithium/blob/develop/com/src/main/java/net/caffeinemc/mods/lithium/com/util/math/CompactSineLUT.java
static SINE_TABLE_MIDPOINT_OPT: Lazy<f32> = Lazy::new(|| SINE_TABLE[SINE_TABLE.len() / 2_usize]);

static ARCSIN_TABLE: Lazy<[f64; 257]> = Lazy::new(|| {
    let mut table = [0.0; 257];
    for (i, value) in table.iter_mut().enumerate() {
        let d = i as f64 / 256.0;
        *value = d.asin();
    }
    table
});

static COSIN_OF_ARCSIN_TABLE: Lazy<[f64; 257]> = Lazy::new(|| {
    let mut table = [0.0; 257];
    for (i, value) in table.iter_mut().enumerate() {
        let d = i as f64 / 256.0;
        *value = d.asin().cos();
    }
    table
});

/// Computes the sine of an input, x, in radians
#[no_mangle]
pub extern "C" fn sin_float(x: f64) -> f32 {
    SINE_TABLE[((x * 10430.378350470453_f64) as i32 & 65535) as usize]
}

fn lookup(index: i32) -> f32 {
    if index == 32768 {
        return *SINE_TABLE_MIDPOINT_OPT;
    }

    let neg: i32 = (index & 0x8000) << 16;
    let mask: i32 = (index << 17) >> 31;
    let mut pos: i32 = (0x8001 & mask) + (index ^ mask);
    pos &= 0x7fff;
    f32::from_bits(SINE_TABLE_OPT[pos as usize] ^ neg as u32)
}

/// Computes the sine of an input, x, in radians
/// Optimized version from Lithium
#[no_mangle]
pub extern "C" fn lithium_sin_float(x: f64) -> f32 {
    lookup((x * 10430.378350470453_f64) as i32 & 0xFFFF)
}

/// Computes the cosine of an input, x, in radians
/// Optimized version from Lithium
#[no_mangle]
pub extern "C" fn lithium_cos_float(x: f64) -> f32 {
    lookup((x * 10430.378350470453_f64 + 16384.0_f64) as i32 & 0xFFFF)
}

/// Computes the cosine of an input, x, in radians
#[no_mangle]
pub extern "C" fn cos_float(x: f64) -> f32 {
    SINE_TABLE[((x * 10430.378350470453_f64 + 16384.0_f64) as i32 & 65535) as usize]
}

/// Square roots the input x
#[no_mangle]
pub extern "C" fn sqrt_float(x: f32) -> f32 {
    x.sqrt()
}

/// Floors the input x
#[no_mangle]
pub extern "C" fn floor_float(x: f32) -> i32 {
    x.floor() as i32
}

/// Floors the input x
#[no_mangle]
pub extern "C" fn floor_double(x: f64) -> i32 {
    x.floor() as i32
}

/// Floor the input x
#[no_mangle]
pub extern "C" fn floor_long(x: f64) -> i64 {
    x.floor() as i64
}

/// Absolute value of the input x
#[no_mangle]
pub extern "C" fn abs_float(x: f32) -> f32 {
    x.abs()
}

/// Absolute value of the input x
#[no_mangle]
pub extern "C" fn abs_int(x: i32) -> i32 {
    x.abs()
}

/// Ceiling the input x
#[no_mangle]
pub extern "C" fn ceil_float(x: f32) -> i32 {
    x.ceil() as i32
}

/// Ceiling the input x
#[no_mangle]
pub extern "C" fn ceil_double(x: f64) -> i32 {
    x.ceil() as i32
}

/// Ceiling the input x
#[no_mangle]
pub extern "C" fn ceil_long(x: f64) -> i64 {
    x.ceil() as i64
}

/// Limits the input x between a minimum and maximum value
#[no_mangle]
pub extern "C" fn clamp_int(x: i32, min: i32, max: i32) -> i32 {
    x.max(min).min(max)
}

/// Limits the input x between a minimum and maximum value
#[no_mangle]
pub extern "C" fn clamp_long(x: i64, min: i64, max: i64) -> i64 {
    x.max(min).min(max)
}

/// Limits the input x between a minimum and maximum value
#[no_mangle]
pub extern "C" fn clamp_float(x: f32, min: f32, max: f32) -> f32 {
    if x < min {
        min
    } else {
        x.min(max)
    }
}

/// Limits the input x between a minimum and maximum value
#[no_mangle]
pub extern "C" fn clamp_double(x: f64, min: f64, max: f64) -> f64 {
    if x < min {
        min
    } else {
        x.min(max)
    }
}

/// Limits Linear Interpolation
#[no_mangle]
pub extern "C" fn clamp_lerp_double(start: f64, end: f64, delta: f64) -> f64 {
    if delta < 0.0 {
        start
    } else if delta > 1.0 {
        end
    } else {
        lerp_double(delta, start, end)
    }
}

/// Limits Linear Interpolation
#[no_mangle]
pub extern "C" fn clamp_lerp_float(start: f32, end: f32, delta: f32) -> f32 {
    if delta < 0.0 {
        start
    } else if delta > 1.0 {
        end
    } else {
        lerp_float(delta, start, end)
    }
}

/// Gets the largest value from the absolute value of i and j
#[no_mangle]
pub extern "C" fn abs_max_int(i: i32, j: i32) -> i32 {
    i.abs().max(j.abs())
}

/// Gets the largest value from the absolute value of i and j
#[no_mangle]
pub extern "C" fn abs_max_float(i: f32, j: f32) -> f32 {
    i.abs().max(j.abs())
}

/// Gets the largest value from the absolute value of a and b
#[no_mangle]
pub extern "C" fn abs_max(a: f64, b: f64) -> f64 {
    a.abs().max(b.abs())
}

/// Gets the largest value from the absolute value of k-i and l-j
#[no_mangle]
pub extern "C" fn abs_max_difference(i: i32, j: i32, k: i32, l: i32) -> i32 {
    abs_max_int(k - i, l - j)
}

/// Divides then floors
#[no_mangle]
pub extern "C" fn floor_div(dividend: i32, divisor: i32) -> i32 {
    (dividend as f32 / divisor as f32).floor() as i32
}

// TODO Implement `nextInt`, `nextFloat`, and `nextDouble`

/// If the values are equal excluding floating point errors
#[no_mangle]
pub extern "C" fn approximately_equals_float(a: f32, b: f32) -> bool {
    (b - a).abs() < APPROXIMATION_THRESHOLD
}

/// If the values are equal excluding floating point errors
#[no_mangle]
pub extern "C" fn approximately_equals_double(a: f64, b: f64) -> bool {
    (b - a).abs() < APPROXIMATION_THRESHOLD as f64
}

/// Floor mod of the dividend and divisor
#[no_mangle]
pub extern "C" fn floor_mod_int(dividend: i32, divisor: i32) -> i32 {
    let r: i32 = dividend % divisor;
    if (dividend ^ divisor) < 0 && r != 0 {
        r + divisor
    } else {
        r
    }
}

/// Floor mod of the dividend and divisor
#[no_mangle]
pub extern "C" fn floor_mod_float(dividend: f32, divisor: f32) -> f32 {
    (dividend % divisor + divisor) % divisor
}

/// Floor mod of the dividend and divisor
#[no_mangle]
pub extern "C" fn floor_mod_double(dividend: f64, divisor: f64) -> f64 {
    (dividend % divisor + divisor) % divisor
}

/// If a is a multiple of b
#[no_mangle]
pub extern "C" fn is_multiple_of(a: i32, b: i32) -> bool {
    a % b == 0
}

/// Compacts degrees into a single byte
#[no_mangle]
pub extern "C" fn pack_degrees(degrees: f32) -> i8 {
    floor_float(degrees * PACK) as i8
}

/// Converts bytes to degrees
#[no_mangle]
pub extern "C" fn unpack_degrees(packed_degrees: i8) -> f32 {
    (packed_degrees as i32 * 360_i32) as f32 / 256_f32
}

/// Forces degrees into +/- 180
#[no_mangle]
pub extern "C" fn wrap_degrees_int(degrees: i32) -> i32 {
    ((degrees % 360) + 540) % 360 - 180
}

/// Forces degrees into +/- 180
#[no_mangle]
pub extern "C" fn wrap_degrees_long(degrees: i64) -> f32 {
    ((degrees % 360_i64) as f32 + 540.0) % 360.0 - 180.0
}

/// Forces degrees into +/- 180
#[no_mangle]
pub extern "C" fn wrap_degrees_float(degrees: f32) -> f32 {
    let f: f32 = degrees % 360.0_f32;
    if f >= 180.0_f32 {
        f - 360.0_f32
    } else if f < -180.0_f32 {
        f + 360.0_f32
    } else {
        f
    }
}

/// Forces degrees into +/- 180
#[no_mangle]
pub extern "C" fn wrap_degrees_double(degrees: f64) -> f64 {
    let f: f64 = degrees % 360.0_f64;
    if f >= 180.0_f64 {
        f - 360.0_f64
    } else if f < -180.0_f64 {
        f + 360.0_f64
    } else {
        f
    }
}

/// Subtracts end from start
#[no_mangle]
pub extern "C" fn subtract_angles(start: f32, end: f32) -> f32 {
    wrap_degrees_float(end - start)
}

/// Absolute value of second - first
#[no_mangle]
pub extern "C" fn angle_between(first: f32, second: f32) -> f32 {
    subtract_angles(first, second).abs()
}

/// Clamps mean - value between the delta
#[no_mangle]
pub extern "C" fn clamp_angle(value: f32, mean: f32, delta: f32) -> f32 {
    let f: f32 = subtract_angles(value, mean);
    mean - clamp_float(f, -delta, delta)
}

/// Steps from towards to, only changing the amount by at most step
#[no_mangle]
pub extern "C" fn step_towards(from: f32, to: f32, mut step: f32) -> f32 {
    step = step.abs();
    if from < to {
        clamp_float(from + step, from, to)
    } else {
        clamp_float(from - step, to, from)
    }
}

/// Steps from towards to, only changing the amount by at most step
#[no_mangle]
pub extern "C" fn step_unwrapped_angle_towards(from: f32, to: f32, step: f32) -> f32 {
    step_towards(from, from + subtract_angles(from, to), step)
}

/// Parses the int.
/// If the parse fails, it reverts to fallback
/// Here is the Java code to call this:
/// ```java
/// String str = " -123 ";
/// char[] chars = str.toCharArray(); // UTF-16 characters
///
/// // You can pass this pointer + length into Rust
/// int result = lib_h.parse_int_utf16(chars_ptr, chars.length, 0);
///```
#[no_mangle]
pub extern "C" fn parse_int_utf16(ptr: *const c_ushort, len: usize, fallback: i32) -> i32 {
    if ptr.is_null() || len == 0 {
        return fallback;
    }

    let utf16: &[u16] = unsafe { slice::from_raw_parts(ptr, len) };

    let mut result: i32 = 0;
    let mut negative = false;
    let mut started = false;

    for &u in utf16 {
        let ch = u as u32;

        match ch {
            48..=57 => {
                // '0'..='9' as u32
                started = true;
                match result
                    .checked_mul(10)
                    .and_then(|r| r.checked_add((ch - 48) as i32))
                {
                    Some(v) => result = v,
                    None => return fallback, // overflow
                }
            }
            45 if !started => {
                // '-' as u32
                negative = true;
                started = true;
            }
            32 | 9 if !started => continue, // allow leading space/tab
            _ => break,                     // stop on invalid input
        }
    }

    if started {
        if negative {
            -result
        } else {
            result
        }
    } else {
        fallback
    }
}

/// Gets the smallest power of two
#[no_mangle]
pub extern "C" fn smallest_encompassing_power_of_two(value: i32) -> i32 {
    let mut i = value - 1;
    i |= i >> 1;
    i |= i >> 2;
    i |= i >> 4;
    i |= i >> 8;
    i |= i >> 16;
    i + 1
}

/// Gets the smallest square side length
#[no_mangle]
pub extern "C" fn smallest_encompassing_square_side_length(value: i32) -> i32 {
    ceil_double((value as f64).sqrt())
}

/// Returns if the value is a power of two
#[no_mangle]
pub extern "C" fn is_power_of_two(value: i32) -> bool {
    value != 0 && (value & (value - 1)) == 0
}

/// Takes the ceiling of the log2(value) using the de Bruijn sequence
#[no_mangle]
pub extern "C" fn ceil_log_2(mut value: i32) -> i32 {
    value = if is_power_of_two(value) {
        value
    } else {
        smallest_encompassing_power_of_two(value)
    };
    MULTIPLY_DE_BRUIJN_BIT_POS[(((value as u32 * 125613361) >> 27) & 31) as usize] as i32
}

/// Takes the floor of log2(value) using the de Bruijn sequence
#[no_mangle]
pub extern "C" fn floor_log_2(value: i32) -> i32 {
    ceil_log_2(value) - if is_power_of_two(value) { 0 } else { 1 }
}

/// Gets the fractional part of a float
#[no_mangle]
pub extern "C" fn fractional_part_float(value: f32) -> f32 {
    value - value.floor()
}

/// Gets the fractional part of a float
#[no_mangle]
pub extern "C" fn fractional_part_double(value: f64) -> f64 {
    value - value.floor()
}

/// Generates a hash code
#[no_mangle]
pub extern "C" fn hash_code(x: i32, y: i32, z: i32) -> i64 {
    let mut l: i64 = (x * 3129871) as i64 ^ (z as i64 * 116129781) ^ y as i64;
    let wrapping_l = Wrapping(l);
    l = (wrapping_l * wrapping_l * Wrapping(42317861) + wrapping_l * Wrapping(11_i64)).0;
    l >> 16
}

// TODO Generate random UUID

/// Gets the fraction of the way value is between start and end
#[no_mangle]
pub extern "C" fn get_lerp_progress_double(value: f64, start: f64, end: f64) -> f64 {
    (value - start) / (end - start)
}

/// Gets the fraction of the way value is between start and end
#[no_mangle]
pub extern "C" fn get_lerp_progress_float(value: f32, start: f32, end: f32) -> f32 {
    (value - start) / (end - start)
}

// TODO Implement method_34945 with Vec3d

/// Approximates the atan2 function
#[no_mangle]
pub extern "C" fn atan_2(mut y: f64, mut x: f64) -> f64 {
    let d: f64 = x * x + y * y;
    if d.is_nan() {
        f64::NAN
    } else {
        let bl: bool = y < 0.0;
        let bl2: bool = x < 0.0;

        if bl {
            y = -y;
        }

        if bl2 {
            x = -x;
        }

        let bl3: bool = y > x;
        if bl3 {
            std::mem::swap(&mut x, &mut y)
        }

        let e: f64 = fast_inverse_sqrt(d);
        x *= e;
        y *= e;
        let f: f64 = ROUNDER_256THS + y;
        let i: usize = (f.to_bits() as u32) as usize;
        let g: f64 = ARCSIN_TABLE[i];
        let h: f64 = COSIN_OF_ARCSIN_TABLE[i];
        let j: f64 = f - ROUNDER_256THS;
        let k: f64 = y * h - x * j;
        let l: f64 = (6.0 + k * k) * k * 0.16666666666666666;

        let mut m: f64 = g + l;
        if bl3 {
            m = std::f64::consts::FRAC_PI_2 - m;
        }

        if bl2 {
            m = std::f64::consts::PI - m;
        }

        if bl {
            m = -m;
        }

        m
    }
}

/// Gets the inverse of the square root of x
#[no_mangle]
pub extern "C" fn inverse_sqrt_float(x: f32) -> f32 {
    1.0 / x.sqrt()
}

/// Gets the inverse of the square root of x
#[no_mangle]
pub extern "C" fn inverse_sqrt_double(x: f64) -> f64 {
    1.0 / x.sqrt()
}

pub fn fast_inverse_sqrt(mut x: f64) -> f64 {
    let d: f64 = 0.5_f64 * x;
    let mut l: u64 = x.to_bits();
    l = INVERSE_SQRT - (l >> 1);
    x = f64::from_bits(l);
    x *= 1.5 - d * x * x;
    x
}

/// Approximation of 1 / cbrt(x)
#[no_mangle]
pub extern "C" fn fast_inverse_cbrt(x: f32) -> f32 {
    let mut i: i32 = x.to_bits() as i32;
    i = 1419967116 - i / 3;
    let mut f: f32 = f32::from_bits(i.try_into().unwrap());
    f = 0.6666667 * f + 1.0 / (3.0 * f * f * x);
    0.6666667 * f + 1.0 / (3.0 * f * f * x)
}

/// Converts HSV to RGB Values
#[no_mangle]
pub extern "C" fn hsv_to_rgb(hue: f32, saturation: f32, value: f32) -> i32 {
    hsv_to_argb(hue, saturation, value, 0)
}

/// Converts HSV to ARGB values
#[no_mangle]
pub extern "C" fn hsv_to_argb(hue: f32, saturation: f32, value: f32, alpha: i32) -> i32 {
    let i = (hue * 6.0).floor() as i32 % 6;
    let f = hue * 6.0 - i as f32;
    let g = value * (1.0 - saturation);
    let h = value * (1.0 - f * saturation);
    let j = value * (1.0 - (1.0 - f) * saturation);

    let (k, l, m) = match i {
        0 => (value, j, g),
        1 => (h, value, g),
        2 => (g, value, j),
        3 => (g, h, value),
        4 => (j, g, value),
        5 => (value, g, h),
        _ => panic!(
            "Something went wrong when converting from HSV to RGB. Input was {}, {}, {}",
            hue, saturation, value
        ),
    };

    let r = (k * 255.0).clamp(0.0, 255.0) as i32;
    let g = (l * 255.0).clamp(0.0, 255.0) as i32;
    let b = (m * 255.0).clamp(0.0, 255.0) as i32;

    (alpha << 24) | (r << 16) | (g << 8) | b
}

/// Creates an ideal hash
#[no_mangle]
pub extern "C" fn ideal_hash(mut value: i32) -> i32 {
    value ^= signed_shift(value, 16) as i32;
    value = (Wrapping(value) * Wrapping(-2048144789)).0;
    value ^= signed_shift(value, 13) as i32;
    value = (Wrapping(value) * Wrapping(-1028477387)).0;
    value ^ signed_shift(value, 16) as i32
}

/// Rust equivalent of the Java ">>>" operator
fn signed_shift(value: i32, shift: u32) -> u32 {
    (value as u32) >> shift
}

/// Linear Interpolation from start to end over delta time
#[no_mangle]
pub extern "C" fn lerp_int(delta: f32, start: i32, end: i32) -> i32 {
    start + (delta * (end - start) as f32).floor() as i32
}

/// Linear Interpolation from start to end over delta time
#[no_mangle]
pub extern "C" fn lerp_float(delta: f32, start: f32, end: f32) -> f32 {
    start + (delta * (end - start))
}

/// Linear Interpolation from start to end over delta time
#[no_mangle]
pub extern "C" fn lerp_double(delta: f64, start: f64, end: f64) -> f64 {
    start + (delta * (end - start))
}

// lerp_vec3d

/// Linear Interpolation that always returns positive if delta is positive
#[no_mangle]
pub extern "C" fn lerp_positive(delta: f32, start: i32, end: i32) -> i32 {
    start + (delta * (end - start - 1) as f32).floor() as i32 + if delta > 0.0 { 1 } else { 0 }
}

/// Two-dimensional Linear Interpolation
#[no_mangle]
pub extern "C" fn lerp_2(
    deltax: f64,
    deltay: f64,
    x0y0: f64,
    x1y0: f64,
    x0y1: f64,
    x1y1: f64,
) -> f64 {
    lerp_double(
        deltay,
        lerp_double(deltax, x0y0, x1y0),
        lerp_double(deltax, x0y1, x1y1),
    )
}

/// Three-dimensional Linear Interpolation
#[no_mangle]
pub extern "C" fn lerp_3(
    delta_x: f64,
    delta_y: f64,
    delta_z: f64,
    x0y0z0: f64,
    x1y0z0: f64,
    x0y1z0: f64,
    x1y1z0: f64,
    x0y0z1: f64,
    x1y0z1: f64,
    x0y1z1: f64,
    x1y1z1: f64,
) -> f64 {
    lerp_double(
        delta_z,
        lerp_2(delta_x, delta_y, x0y0z0, x1y0z0, x0y1z0, x1y1z0),
        lerp_2(delta_x, delta_y, x0y0z1, x1y0z1, x0y1z1, x1y1z1),
    )
}

/// Interpolates a point on the Catmull-Rom Spline
#[no_mangle]
pub extern "C" fn catmull_rom(delta: f32, p0: f32, p1: f32, p2: f32, p3: f32) -> f32 {
    0.5 * (2.0 * p1
        + (p2 - p0) * delta
        + (2.0 * p0 - 5.0 * p1 + 4.0 * p2 - p3) * delta * delta
        + (3.0 * p1 - p0 - 3.0 * p2 + p3) * delta * delta * delta)
}

/// Fades a value using Perlin
#[no_mangle]
pub extern "C" fn perlin_fade(value: f64) -> f64 {
    value * value * value * (value * (value * 6.0 - 15.0) + 10.0)
}

/// Derivative of the Perlin Fade function
#[no_mangle]
pub extern "C" fn perlin_fade_derivative(value: f64) -> f64 {
    30.0 * value * value * (value - 1.0) * (value - 1.0)
}

/// Gets the sign of a value
#[no_mangle]
pub extern "C" fn sign(value: f64) -> i32 {
    if value == 0.0 {
        0
    } else {
        value.signum() as i32
    }
}

/// Performs Linear Interpolation on an angle
#[no_mangle]
pub extern "C" fn lerp_angle_degrees_float(delta: f32, start: f32, end: f32) -> f32 {
    start + delta * wrap_degrees_float(end - start)
}

/// Performs Linear Interpolation on an angle
#[no_mangle]
pub extern "C" fn lerp_angle_degrees_double(delta: f64, start: f64, end: f64) -> f64 {
    start + delta * wrap_degrees_double(end - start)
}

/// Performs Linear Interpolation on an angle
#[no_mangle]
pub extern "C" fn lerp_angle_radians(delta: f32, start: f32, end: f32) -> f32 {
    let mut f: f32 = end - start;

    while f < -std::f32::consts::PI {
        f += DOUBLE_PI_F32;
    }

    while f >= std::f32::consts::PI {
        f -= DOUBLE_PI_F32;
    }

    start + delta * f
}

/// Wraps a number around after it hits the max deviation
#[no_mangle]
pub extern "C" fn wrap(value: f32, max_deviation: f32) -> f32 {
    ((value % max_deviation - max_deviation * 0.5).abs() - max_deviation * 0.25)
        / (max_deviation * 0.25)
}

/// Squares a value
#[no_mangle]
pub extern "C" fn square_float(value: f32) -> f32 {
    value * value
}

/// Squares a value
#[no_mangle]
pub extern "C" fn square_double(value: f64) -> f64 {
    value * value
}

/// Squares a value
#[no_mangle]
pub extern "C" fn square_int(value: i32) -> i32 {
    value * value
}

/// Squares a value
#[no_mangle]
pub extern "C" fn square_long(value: i64) -> i64 {
    value * value
}

/// Linearly maps a value from one number range to another and clamps the result
#[no_mangle]
pub extern "C" fn clamped_map_double(
    value: f64,
    old_start: f64,
    old_end: f64,
    new_start: f64,
    new_end: f64,
) -> f64 {
    clamp_lerp_double(
        new_start,
        new_end,
        get_lerp_progress_double(value, old_start, old_end),
    )
}

/// Linearly maps a value from one number range to another and clamps the result
#[no_mangle]
pub extern "C" fn clamped_map_float(
    value: f32,
    old_start: f32,
    old_end: f32,
    new_start: f32,
    new_end: f32,
) -> f32 {
    clamp_lerp_float(
        new_start,
        new_end,
        get_lerp_progress_float(value, old_start, old_end),
    )
}

/// Linearly maps a value from one number range to another, unclamped
#[no_mangle]
pub extern "C" fn map_double(
    value: f64,
    old_start: f64,
    old_end: f64,
    new_start: f64,
    new_end: f64,
) -> f64 {
    lerp_double(
        get_lerp_progress_double(value, old_start, old_end),
        new_start,
        new_end,
    )
}

/// Linearly maps a value from one number range to another, unclamped
#[no_mangle]
pub extern "C" fn map_float(
    value: f32,
    old_start: f32,
    old_end: f32,
    new_start: f32,
    new_end: f32,
) -> f32 {
    lerp_float(
        get_lerp_progress_float(value, old_start, old_end),
        new_start,
        new_end,
    )
}

// method_34957

/// Returns a value farther than or as far as value from zero that is a multiple of divisor
#[no_mangle]
pub extern "C" fn round_up_to_multiple(value: i32, divisor: i32) -> i32 {
    ceil_div(value, divisor) * divisor
}

/// Divides then ceilings
#[no_mangle]
pub extern "C" fn ceil_div(a: i32, b: i32) -> i32 {
    -floor_div(-a, b)
}

// nextBetween
// nextGaussian

/// A^2 + B^2
#[no_mangle]
pub extern "C" fn squared_hypot(a: f64, b: f64) -> f64 {
    a * a + b * b
}

/// Gets the hypotenuse length
#[no_mangle]
pub extern "C" fn hypot_double(a: f64, b: f64) -> f64 {
    squared_hypot(a, b).sqrt()
}

/// Gets the hypotenuse length
#[no_mangle]
pub extern "C" fn hypot_float(a: f32, b: f32) -> f32 {
    squared_hypot(a as f64, b as f64).sqrt() as f32
}

/// Gets the magnitude squared
#[no_mangle]
pub extern "C" fn squared_magnitude(a: f64, b: f64, c: f64) -> f64 {
    a * a + b * b + c * c
}

/// Gets the magnitude of the vector
#[no_mangle]
pub extern "C" fn magnitude_double(a: f64, b: f64, c: f64) -> f64 {
    squared_magnitude(a, b, c).sqrt()
}

/// Gets the magnitude of the vector
#[no_mangle]
pub extern "C" fn magnitude_float(a: f32, b: f32, c: f32) -> f32 {
    a * a + b * b + c * c
}

/// Returns a rounded down to the nearest multiple of b.
#[no_mangle]
pub extern "C" fn round_down_to_multiple(a: f64, b: i32) -> i32 {
    (a / b as f64).floor() as i32 * b
}

// stream

// rotateAround

/// Multiplies a fraction
#[no_mangle]
pub extern "C" fn multiply_fraction(numerator: i32, denominator: i32, multiplier: i32) -> i32 {
    numerator * multiplier / denominator
}
