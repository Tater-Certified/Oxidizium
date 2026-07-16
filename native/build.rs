extern crate cbindgen;

use std::env;
use std::fs;
use std::path::Path;

fn main() {
    let crate_dir = env::var("CARGO_MANIFEST_DIR").unwrap();

    cbindgen::Builder::new()
        .with_crate(crate_dir)
        .with_language(cbindgen::Language::C)
        .generate()
        .expect("Unable to generate bindings")
        .write_to_file("lib.h");

    // Pre-compute lookup tables at compile time for zero-overhead access (like Java's static final)
    let out_dir = env::var("OUT_DIR").unwrap();
    let dest_path = Path::new(&out_dir).join("math_tables.rs");

    let double_pi = std::f64::consts::PI * 2.0;

    // SINE_TABLE: 65536 f32 values
    let mut sine_table = String::from("const SINE_TABLE: [f32; 65536] = [\n    ");
    let sine_values: Vec<String> = (0..65536)
        .map(|i| {
            let val = (i as f64 * double_pi / 65536.0).sin() as f32;
            format!("{:.16}f32", val)
        })
        .collect();
    sine_table.push_str(&sine_values.join(", "));
    sine_table.push_str("\n];\n\n");

    // SINE_TABLE_OPT: 16385 u32 values (bits of first 16385 sine values)
    let mut sine_opt = String::from("const SINE_TABLE_OPT: [u32; 16385] = [\n    ");
    let opt_values: Vec<String> = (0..16385)
        .map(|i| {
            let val = (i as f64 * double_pi / 65536.0).sin() as f32;
            format!("{}u32", val.to_bits())
        })
        .collect();
    sine_opt.push_str(&opt_values.join(", "));
    sine_opt.push_str("\n];\n\n");

    // SINE_TABLE_MIDPOINT_OPT
    let midpoint = (32768.0 * double_pi / 65536.0).sin() as f32;
    let midpoint_str = format!(
        "const SINE_TABLE_MIDPOINT_OPT: f32 = {:.16}f32;\n\n",
        midpoint
    );

    // ARCSIN_TABLE: 257 f64 values
    let mut arcsin = String::from("const ARCSIN_TABLE: [f64; 257] = [\n    ");
    let arcsin_values: Vec<String> = (0..=256)
        .map(|i| {
            let d = i as f64 / 256.0;
            format!("{:.17}f64", d.asin())
        })
        .collect();
    arcsin.push_str(&arcsin_values.join(", "));
    arcsin.push_str("\n];\n\n");

    // COSIN_OF_ARCSIN_TABLE: 257 f64 values
    let mut cosin_arcsin = String::from("const COSIN_OF_ARCSIN_TABLE: [f64; 257] = [\n    ");
    let cosin_values: Vec<String> = (0..=256)
        .map(|i| {
            let d = i as f64 / 256.0;
            format!("{:.17}f64", d.asin().cos())
        })
        .collect();
    cosin_arcsin.push_str(&cosin_values.join(", "));
    cosin_arcsin.push_str("\n];\n");

    let content = format!(
        "{}{}{}{}{}",
        sine_table, sine_opt, midpoint_str, arcsin, cosin_arcsin
    );
    fs::write(&dest_path, content).unwrap();

    // Tell cargo to rerun only if build.rs changes (tables are deterministic)
    println!("cargo:rerun-if-changed=build.rs");

    // We love Windows >:(
    #[cfg(target_os = "windows")]
    println!("cargo:rustc-link-lib=advapi32");
}
