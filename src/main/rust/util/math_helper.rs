use ape_table_trig::*;
use once_cell::sync::Lazy;

const APPROXIMATION_THRESHOLD: f32 = 1.0E-5;
const PACKED_DEGREES_CONSTANT: f32 = 360.0 / 256.0;
const UNPACKED_DEGREES_CONSTANT: f32 = 256.0 / 360.0;
const MULTIPLY_DE_BRUIJN_BIT_POS: [i32; 32] = [
    0, 1, 28, 2, 29, 14, 24, 3, 30, 22, 20, 15, 25, 17, 4, 8, 31, 27, 13, 23, 21, 19, 16, 7, 26, 12, 18, 6, 11, 5, 10, 9
];
const ROUNDER_256THS: f64 = f64::from_bits(4805340802404319232);
const DOUBLE_PI: f32 = std::f32::consts::PI * 2.0;

static TABLE: [f32; 65536] = trig_table_gen_f32!(65536);
static ARCSIN_TABLE: Lazy<[f64; 257]> = Lazy::new(|| {
    let mut table = [0.0; 257];
    for i in 0..257 {
        let d = i as f64 / 256.0;
        table[i] = d.asin();
    }
    table
});

static COSIN_OF_ARCSIN_TABLE: Lazy<[f64; 257]> = Lazy::new(|| {
    let mut table = [0.0; 257];
    for i in 0..257 {
        let d = i as f64 / 256.0;
        table[i] = d.asin().cos();
    }
    table
});

/// Computes the sine of an input, x, in radians
pub fn sin(x: f32) -> f32 {
    let table = TrigTableF32::new(&TABLE);
    table.sin(x)
}

/// Computes the cosine of an input, x, in radians
pub fn cos(x: f32) -> f32 {
    let table = TrigTableF32::new(&TABLE);
    table.cos(x)
}

/// Computes the tangent of an input, x, in radians
pub fn tan(x: f32) -> f32 {
    let table = TrigTableF32::new(&TABLE);
    table.tan(x)
}

/// Square roots the input x
pub fn sqrt(x: f32) -> f32 {
    x.sqrt()
}

/// Floors the input x
pub fn floor_float(x: f32) -> i32 {
    x.floor() as i32
}

/// Floors the input x
pub fn floor_double(x: f64) -> i32 {
    x.floor() as i32
}

/// Floor the input x
pub fn floor_long(x: f64) -> i64 {
    x.floor() as i64
}

/// Absolute value of the input x
pub fn abs_float(x: f32) -> f32 {
    x.abs()
}

/// Absolute value of the input x
pub fn abs_int(x: i32) -> i32 {
    x.abs()
}

/// Ceiling the input x
pub fn ceil_float(x: f32) -> i32 {
    x.ceil() as i32
}

/// Ceiling the input x
pub fn ceil_double(x: f64) -> i32 {
    x.ceil() as i32
}

/// Limits the input x between a minimum and maximium value
pub fn clamp_int(x: i32, min: i32, max: i32) -> i32 {
    x.clamp(min, max)
}

/// Limits the input x between a minimum and maximium value
pub fn clamp_long(x: i64, min: i64, max: i64) -> i64 {
    x.clamp(min, max)
}

/// Limits the input x between a minimum and maximium value
pub fn clamp_float(x: f32, min: f32, max: f32) -> f32 {
    x.clamp(min, max)
}

/// Limits the input x between a minimum and maximium value
pub fn clamp_double(x: f64, min: f64, max: f64) -> f64 {
    x.clamp(min, max)
}

/// Limits Linear Interpolation
pub fn clamp_lerp_double(start: f64, end: f64, delta: f64) -> f64 {
    if delta < 0.0 {
        start
    } else if delta > 1.0 {
        end
    } else {
        lerp_double(delta, start, end)
    }
}

/// Limits Linear Interpolation
pub fn clamp_lerp_float(start: f32, end: f32, delta: f32) -> f32 {
    if delta < 0.0 {
        start
    } else if delta > 1.0 {
        end
    } else {
        lerp_float(delta, start, end)
    }
}

/// Gets the largest value from the absolute value of a and b
pub fn abs_max(mut a: f64, mut b: f64) -> f64 {
    if a < 0.0 {
        a = -a;
    }

    if b < 0.0 {
        b = -b;
    }

    a.max(b)
}

/// Divides then floors
pub fn floor_div(dividend: i32, divisor: i32) -> i32 {
    (dividend as f32 / divisor as f32).floor() as i32
}

// TODO Implement `nextInt`, `nextFloat`, and `nextDouble`

/// If the values are equal excluding floating point errors
pub fn approximately_equals_float(a: f32, b: f32) -> bool {
    (b - a).abs() < APPROXIMATION_THRESHOLD
}

/// If the values are equal excluding floating point errors
pub fn approximately_equals_double(a: f64, b: f64) -> bool {
    (b - a).abs() < APPROXIMATION_THRESHOLD as f64
}

/// Floor mod of the dividend and divisor
pub fn floor_mod_int(dividend: i32, divisor: i32) -> i32 {
    let r: i32 = dividend % divisor;
    if (dividend ^ divisor) < 0 && r != 0 {
        r + divisor
    } else {
        r
    }
}

/// Floor mod of the dividend and divisor
pub fn floor_mod_float(dividend: f32, divisor: f32) -> f32 {
    (dividend % divisor + divisor) % divisor
}

/// Floor mod of the dividend and divisor
pub fn floor_mod_double(dividend: f64, divisor: f64) -> f64 {
    (dividend % divisor + divisor) % divisor
}

/// If a is a multiple of b
pub fn is_multiple_of(a: i32, b: i32) -> bool {
    a % b == 0
}

/// Compacts degrees into a single byte
pub fn pack_degrees(degrees: f32) -> i8 {
    (degrees * PACKED_DEGREES_CONSTANT).floor() as i8
}

/// Converts bytes to degrees
pub fn unpack_degrees(packed_degrees: i8) -> f32 {
    packed_degrees as f32 * UNPACKED_DEGREES_CONSTANT
}

/// Forces degrees into +/- 180
pub fn wrap_degrees_int(degrees: i32) -> i32 {
    ((degrees % 360) + 540) % 360 - 180
}

/// Forces degrees into +/- 180
pub fn wrap_degrees_long(degrees: i64) -> f32 {
    ((degrees % 360_i64) as f32 + 540.0) % 360.0 - 180.0
}

/// Forces degrees into +/- 180
pub fn wrap_degrees_float(degrees: f32) -> f32 {
    ((degrees % 360.0) + 540.0) % 360.0 - 180.0
}

/// Forces degrees into +/- 180
pub fn wrap_degrees_double(degrees: f64) -> f64 {
    ((degrees % 360.0) + 540.0) % 360.0
}

/// Subtracts end from start
pub fn subtract_angles(start: f32, end: f32) -> f32 {
    wrap_degrees_float(end - start)
}

/// Absolute value of second - first
pub fn angle_between (first: f32, second: f32) -> f32 {
    subtract_angles(first, second).abs()
}

/// Clamps mean - value between the delta
pub fn clamp_angle(value: f32, mean: f32, delta: f32) -> f32 {
    let f: f32 = subtract_angles(value, mean);
    mean - clamp_float(f, -delta, delta)
}

/// Steps from towards to, only changing the amount by at most step
pub fn step_towards(from: f32, to: f32, mut step: f32) -> f32 {
    step = step.abs();
    if from < to {
        clamp_float(from + step, from, to)
    } else {
        clamp_float(from - step, to, from)
    }
}

/// Steps from towards to, only changing the amount by at most step
pub fn step_unwrapped_angle_towards(from: f32, to: f32, step: f32) -> f32 {
    step_towards(from, from + subtract_angles(from, to), step)
}

/// Parses the int.
/// If the parse fails, it reverts to fallback
pub fn parse_int(string: String, fallback: i32) -> i32 {
    string.parse::<i32>().unwrap_or(fallback)
}

/// Gets the smallest power of two
pub fn smallest_encompassing_power_of_two(value: i32) -> i32 {
    let mut i = value - 1;
    i |= i >> 1;
    i |= i >> 2;
    i |= i >> 4;
    i |= i >> 8;
    i |= i >> 16;
    i + 1
}

/// Returns if the value is a power of two
pub fn is_power_of_two(value: i32) -> bool {
    value != 0 && (value & (value - 1)) == 0
}

/// Takes the ceiling of the log2(value) using the de Bruijn sequence
pub fn ceil_log_2(mut value: i32) -> i32 {
    value = if is_power_of_two(value) {
        value
    } else {
        smallest_encompassing_power_of_two(value)
    };
    MULTIPLY_DE_BRUIJN_BIT_POS[(((value as i64 * 125613361) >> 27) as i32 & 31) as usize]
}

/// Takes the floor of log2(value) using the de Bruijn sequence
pub fn floor_log_2(value: i32) -> i32 {
    ceil_log_2(value) - if is_power_of_two(value) {0} else {1}
}

/// Gets the fractional part of a float
pub fn fractional_part_float(value: f32) -> f32 {
    value.fract()
}

/// Gets the fractional part of a float
pub fn fractional_part_double(value: f64) -> f64 {
    value.fract()
}

/// Generates a hash code
pub fn hash_code(x: i32, y: i32, z: i32) -> i64 {
    let mut l: i64 = (x * 3129871) as i64 ^ (z as i64 * 116129781) ^ y as i64;
    l = l * l * 42317861 + l * 11_i64;
    l >> 16
}

// TODO Generate random UUID

/// Gets the fraction of the way value is between start and end
pub fn get_lerp_progress_double(value: f64, start: f64, end: f64) -> f64 {
    (value - start) / (end - start)
}

/// Gets the fraction of the way value is between start and end
pub fn get_lerp_progress_float(value: f32, start: f32, end: f32) -> f32 {
    (value - start) / (end - start)
}

// TODO Implement method_34945 with Vec3d

/// Approximates the atan2 function
pub fn atan_2(mut y: f64, mut x: f64) -> f64 {
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
            let e: f64 = x;
            std::mem::swap(&mut x, &mut y)
        }

        // TODO Fast inverse square
        let e: f64 = inverse_sqrt_double(d);
        x *= e;
        y *= e;
        let f: f64 = ROUNDER_256THS + y;
        let i: i32 = f.to_bits() as i32;
        let asin_table: ArcSinTable = ArcSinTable::new(&ARCSIN_TABLE[..], &COSIN_OF_ARCSIN_TABLE[..]);
        let g: f64 = asin_table.asin(i as usize);
        let h: f64 = asin_table.cos_of_asin(i as usize);
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
pub fn inverse_sqrt_float(x: f32) -> f32 {
    1.0 / x.sqrt()
}

/// Gets the inverse of the square root of x
pub fn inverse_sqrt_double(x: f64) -> f64 {
    1.0 / x.sqrt()
}

/// Approximation of 1 / cbrt(x)
pub fn fast_inverse_cbrt(x: f32) -> f32 {
    let mut i: i32 = x.to_bits() as i32;
    i = 1419967116 - i / 3;
    let mut f: f32 = f32::from_bits(i.try_into().unwrap());
    f = 0.6666667 * f + 1.0 / (3.0 * f * f * x);
    0.6666667 * f + 1.0 / (3.0 * f * f * x)
}

/// Converts HSV to RGB Values
pub fn hsv_to_rgb(hue: f32, saturation: f32, value: f32) -> i32 {
    hsv_to_argb(hue, saturation, value, 0)
}

/// Converts HSV to ARGB values
pub fn hsv_to_argb(hue: f32, saturation: f32, value: f32, alpha: i32) -> i32 {
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
        _ => panic!("Something went wrong when converting from HSV to RGB. Input was {}, {}, {}", hue, saturation, value),
    };

    let r = (k * 255.0).clamp(0.0, 255.0) as i32;
    let g = (l * 255.0).clamp(0.0, 255.0) as i32;
    let b = (m * 255.0).clamp(0.0, 255.0) as i32;

    (alpha << 24) | (r << 16) | (g << 8) | b
}

/// Creates an ideal hash
pub fn ideal_hash(mut value: i32) -> i32 {
    value ^= signed_shift(value, 16) as i32;
    value *= -2048144789;
    value ^= signed_shift(value, 13) as i32;
    value *= -1028477387;
    value ^ signed_shift(value, 16) as i32
}

/// Rust equivalent of the Java ">>>" operator
fn signed_shift(value: i32, shift: u32) -> u32 {
    (value as u32) >> shift
}

/// Performs a binary search looking for the predicate F
/// The function can be used as show here:
/// ```rust
/// let result = binary_search(0, 100, |x| x >= 50);
/// ```
pub fn binary_search<F>(mut min: i32, max: i32, predicate: F) -> i32
where
    F: Fn(i32) -> bool,
{
    let mut i = max - min;
    while i > 0 {
        let j = i / 2;
        let k = min + j;
        if predicate(k) {
            i = j;
        } else {
            min = k + 1;
            i -= j + 1;
        }
    }
    min
}

/// Linear Interpolation f-rom start to end over delta time
pub fn lerp_float(delta: f32, start: f32, end: f32) -> f32 {
    start + (delta * (end - start))
}

/// Linear Interpolation from start to end over delta time
pub fn lerp_double(delta: f64, start: f64, end: f64) -> f64 {
    start + (delta * (end - start))
}

/// Linear Interpolation that always returns positive if delta is positive
pub fn lerp_positive(delta: f32, start: f32, end: f32) -> f32 {
    start + (delta * (end - start - 1.0)).floor() + if delta > 0.0 { 1.0 } else { 0.0 }
}

/// Two-dimensional Linear Interpolation
pub fn lerp_2(deltax: f64, deltay: f64, x0y0: f64, x1y0: f64, x0y1: f64, x1y1: f64) -> f64 {
    lerp_double(deltay, lerp_double(deltax, x0y0, x1y0), lerp_double(deltax, x0y1, x1y1))
}

/// Three-dimensional Linear Interpolation
pub fn lerp_3(
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
    x1y1z1: f64
) -> f64 {
    lerp_double(delta_z, lerp_2(delta_x, delta_y, x0y0z0, x1y0z0, x0y1z0, x1y1z0), lerp_2(delta_x, delta_y, x0y0z1, x1y0z1, x0y1z1, x1y1z1))
}

/// Interpolates a point on the Catmull-Rom Spline
pub fn catmull_rom(delta: f32, p0: f32, p1: f32, p2: f32, p3: f32) -> f32 {
    0.5 * (2.0 * p1 + (p2 - p0) * delta + (2.0 * p0 - 5.0 * p1 + 4.0 * p2 - p3) * delta * delta + (3.0 * p1 - p0 - 3.0 * p2 + p3) * delta * delta * delta)
}

/// Fades a value using Perlin
pub fn perlin_fade(value: f64) -> f64 {
    value * value * value * (value * (value * 6.0 - 15.0) + 10.0)
}

/// Derivative of the Perlin Fade function
pub fn perlin_fade_derivative(value: f64) -> f64 {
    30.0 * value * value * (value - 1.0) * (value - 1.0)
}

/// Gets the sign of a value
pub fn sign(value: f64) -> i32 {
    if value == 0.0 {
        0
    } else {
        value.signum() as i32
    }
}

/// Performs Linear Interpolation on an angle
pub fn lerp_angle_degrees_float(delta: f32, start: f32, end: f32) -> f32 {
    start + delta * wrap_degrees_float(end - start)
}

/// Performs Linear Interpolation on an angle
pub fn lerp_angle_degrees_double(delta: f64, start: f64, end: f64) -> f64 {
    start + delta * wrap_degrees_double(end - start)
}

/// Performs Linear Interpolation on an angle
pub fn lerp_angle_radians(delta: f32, start: f32, end: f32) -> f32 {
    let mut f: f32 = end - start;

    while f < -std::f32::consts::PI {
        f += DOUBLE_PI;
    }

    while f >= std::f32::consts::PI {
        f -= DOUBLE_PI;
    }

    start + delta * f
}

/// Wraps a number around after it hits the max deviation
pub fn wrap(value: f32, max_deviation: f32) -> f32 {
    ((value % max_deviation - max_deviation * 0.5).abs() - max_deviation * 0.25) / (max_deviation * 0.25)
}

/// Squares a value
pub fn square_float(value: f32) -> f32 {
    value * value
}

/// Squares a value
pub fn square_double(value: f64) -> f64 {
    value * value
}

/// Squares a value
pub fn square_int(value: i32) -> i32 {
    value * value
}

/// Squares a value
pub fn square_long(value: i64) -> i64 {
    value * value
}

/// Linearly maps a value from one number range to another and clamps the result
pub fn clamped_map_double(value: f64, old_start: f64, old_end: f64, new_start: f64, new_end: f64) -> f64 {
    clamp_lerp_double(new_start, new_end, get_lerp_progress_double(value, old_start, old_end))
}

/// Linearly maps a value from one number range to another and clamps the result
pub fn clamped_map_float(value: f32, old_start: f32, old_end: f32, new_start: f32, new_end: f32) -> f32 {
    clamp_lerp_float(new_start, new_end, get_lerp_progress_float(value, old_start, old_end))
}

/// Linearly maps a value from one number range to another, unclamped
pub fn map_double(value: f64, old_start: f64, old_end: f64, new_start: f64, new_end: f64) -> f64 {
    lerp_double(get_lerp_progress_double(value, old_start, old_end), new_start, new_end)
}

/// Linearly maps a value from one number range to another, unclamped
pub fn map_float(value: f32, old_start: f32, old_end: f32, new_start: f32, new_end: f32) -> f32 {
    lerp_float(get_lerp_progress_float(value, old_start, old_end), new_start, new_end)
}

// method_34957

/// Returns a value farther than or as far as value from zero that is a multiple of divisor
pub fn round_up_to_multiple(value: i32, divisor: i32) -> i32 {
    ceil_div(value, divisor) * divisor
}

/// Divides then ceilings
pub fn ceil_div(a: i32, b: i32) -> i32 {
    -floor_div(-a, b)
}

// nextBetween
// nextGaussian

/// A^2 + B^2
pub fn squared_hypot(a: f64, b: f64) -> f64 {
    a * a + b * b
}

/// Gets the hypotenuse length
pub fn hypot_double(a: f64, b: f64) -> f64 {
    squared_hypot(a, b).sqrt()
}

/// Gets the hypotenuse length
pub fn hypot_float(a: f32, b: f32) -> f32 {
    squared_hypot(a as f64, b as f64).sqrt() as f32
}

/// Gets the magnitude squared
pub fn squared_magnitude(a: f64, b: f64, c: f64) -> f64 {
    a * a + b * b + c * c
}

/// Gets the magnitude of the vector
pub fn magnitude_double(a: f64, b: f64, c: f64) -> f64 {
    squared_magnitude(a, b, c).sqrt()
}

/// Gets the magnitude of the vector
pub fn magnitude_float(a: f32, b: f32, c: f32) -> f32 {
    squared_magnitude(a as f64, b as f64, c as f64) as f32
}

/// Returns a rounded down to the nearest multiple of b.
pub fn round_down_to_multiple(a: f32, b: i32) -> i32 {
    (a / b as f32).floor() as i32 * b
}

// stream

// rotateAround

// multiplyFraction

/// Gradual sine function
pub fn ease_in_out_sine(value: f32) -> f32 {
    let table = TrigTableF32::new(&TABLE);
    -(table.cos(std::f32::consts::PI * value) - 1.0) / 2.0
}

struct ArcSinTable<'a> {
    arcsin: &'a [f64],
    cosin_of_arcsin: &'a [f64],
}

impl<'a> ArcSinTable<'a> {
    pub fn new(arcsin: &'a [f64], cosin_of_arcsin: &'a [f64]) -> Self {
        ArcSinTable {
            arcsin,
            cosin_of_arcsin,
        }
    }

    pub fn asin(&self, i: usize) -> f64 {
        self.arcsin[i]
    }

    pub fn cos_of_asin(&self, i: usize) -> f64 {
        self.cosin_of_arcsin[i]
    }
}

