# Oxidizium - Inject Ferris into your server! 🦀
---
## Summary
This Fabric mod replaces certain Minecraft Java Edition methods with native rust equivalents. 
Every rust function is tested with the in-house testing utility to ensure that it is functionally equal to vanilla Minecraft.
This mod is intended to be used server-side, but can be used on the client (with minor rendering bugs).

Currently, this mod replaces the following Java classes with Rust:
- MathHelper

Many more classes are planned in the future, however mostly low-level classes will be tackled in the beginning.

---

## Compatibility
As of now, Oxidizium is very compatible with the majority of mods, with specific compatibility with Lithium.
Feel free to PR any of your own Rust native code!

---

## Backends
Oxidizium comes with 3 backends by default: Panama, Nalim, and Membrane. The backend will be in the jar and mod name.
Each backend is slightly different, so read about them below:

### Project Panama
Project Panama is a Java 22+ feature that allows for low latency native code execution. This works on any Minecraft version
from 1.14.3 to modern MC. 
#### Usage
**You must install Java 22 or above to use Project Panama!**
The following JVM arguments are recommended, but not *currently* required for Java 25 and below:
`--enable-preview --enable-native-access=ALL-UNNAMED`
Using a newer Java version is likely to improve performance more than Java 22. I recommend using [GraalVM](https://www.graalvm.org/downloads/) for the
best performance, however be warned that it is possible (though very unlikely) that GraalVM could break mods. Use a
distribution such as [Temurin](https://adoptium.net/) if you want to be completely safe.

### Nalim
Nalim is a 3rd-party library that enables near-native latency when executing native code. It is roughly 3x faster than
Project Panama for leaf (quick) calls and was ~50x faster vs pre-FFM Panama on JDK 19, per the [Nalim README](https://github.com/apangin/nalim#performance).

Nalim historically broke on Java 21+ because Loom [JEP 444](https://openjdk.org/jeps/444) made the JVMCI nmethod entry barrier mandatory for every GC.
Oxidizium ships a [Nalim jar patched by FurryMileon](https://github.com/FurryMileon/nalim) that emits a "no-op" barrier sequence after the tail-call so JVMCI validation passes
without any runtime overhead — the barrier bytes are dead code (placed after `jmp rax` / `br x9`).
#### Usage
The following JVM arguments are **required** to run Nalim: `-XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI`

### Membrane
Membrane works similar to Nalim, though may not be as affective as Nalim (benchmarks soon). It is an **experimental** backend and is not recommended to be used in production.
Like Nalim, it used to require Java 20 and below, but thanks to an included [patched Membrane jar by QPCrummer](https://github.com/QPCrummer/MembraneFFI),
it works great in all Minecraft versions using Java 21 and 25!
#### Usage
The following JVM arguments are **required** to run Nalim: `-XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI`
The following JVM argument is **recommended**, but not required yet: `-XX:+EnableDynamicAgentLoading`

---

## Testing
The `Oxidizium-Tester` mod is a utility mod that can be loaded **with** `Oxidizium` (any backend is fine). It will create a GUI on startup instead
of launching the vanilla game. In this GUI, the user can input how many "runs" (trials) to execute. Pressing "Run" will run the desired
amount of runs and report back any feedback on the left pane. If there happens to be a serious error in the program, the program will crash
and a Rust debug log will be printed in the console.<p>
In the left panel, there will be two kinds of errors: Precision (white) and Logical (red). Precision errors can be disregarded as
these will not significantly impact the game in any meaningful way and are typically *more* precise than their Java counterparts.
Logical errors are a major issue and can cause instability and crashes within the game. **Report these on GitHub if found!**

---

## Downloading
[Modrinth](https://modrinth.com/mod/oxidizium) is the only distribution site for this mod<p>
GitHub releases can also be used, but Modrinth is recommended to support the developers<p>
The latest releases can be found on [our Discord Server](https://discord.gg/XGw3Te7QYr) or on GitHub Actions

---

## Compiling
Run `gradlew compileLocally` to compile the mod locally. This will build `Oxidizium` and `Oxidizium-Test` in
`oxidizium/build` and `testMod/build` respectfully. This build script has only been verified to work on Windows
x86_64 and Linux x86_64, but should work with macOS x86_64 (Intel).

---

## Running in Dev Environment
**NOTE:** These are broken as of 1.2.0!
To run the `Oxidizium` mod, use `gradlew :oxidizium:runClient` or `gradlew :oxidizium:runServer`<p>
To run the `Oxidizium-Test` mod, use `gradlew :testMod:runClient`. This will automatically enable the testing GUI

# Licensing
This project is licensed under MIT; however, the Nalim jar is licensed under GPLv3 to be in compliance with the original codebase.