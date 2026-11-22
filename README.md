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
The Nalim backend uses HotSpot's JVM Compiler Interface (JVMCI) to call Oxidizium's Rust functions.
It bundles [Nalim v2](https://github.com/FurryMileon/nalim/tree/84fb642e623218f05c2a04439d5153439e906cef)
(`1.2.5-patched-autoinline`). The vendored [`nalim/libs/nalim.jar`](nalim/libs/nalim.jar) is the
[JAR from that commit](https://github.com/FurryMileon/nalim/blob/84fb642e623218f05c2a04439d5153439e906cef/nalim.jar);
its SHA-256 is `22de70a9b7d9c56c16af6d2c5c269f1643858793b9530ca47fc25d252d4489b9`.
On Linux x86-64, the backend asks Nalim to recognize the compiled `abs_int` function once.
If it finds an exact match, HotSpot can inline it into the Java caller. On other supported
platforms or when the compiled code doesn't match, it calls the original Rust function.

#### Usage
For Minecraft 26.3, use **HotSpot JDK 25** and install only the `oxidizium-nalim` variant.
You do not need a separate Nalim mod or `-javaagent`. Add these JVM arguments to your launcher
or server startup command (before `-jar`):

```text
--add-modules=jdk.internal.vm.ci --add-opens=java.base/java.lang=ALL-UNNAMED --enable-native-access=ALL-UNNAMED
```

On HotSpot JDK 26, also add `-XX:-UseCodeCacheFlushing` so the JVM does not discard idle native bindings.
[Stock JDK 27 removed JVMCI](https://bugs.openjdk.org/browse/JDK-8385142); use JDK 25 or switch to the Panama variant.

Nalim supports Linux and macOS on x86-64 and AArch64, and Windows on x86-64.
Windows on Arm is unsupported by Nalim. To switch backends, replace `oxidizium-nalim` with
`oxidizium-panama` or `oxidizium-membrane` and follow that backend's requirements.

### Membrane
Membrane works similar to Nalim, though may not be as affective as Nalim (benchmarks soon). It is an **experimental** backend and is not recommended to be used in production.
Like Nalim, it used to require Java 20 and below, but thanks to an included [patched Membrane jar by QPCrummer](https://github.com/QPCrummer/MembraneFFI),
it works great in all Minecraft versions using Java 21+!
#### Usage
The following JVM arguments are **required** to run Membrane: `-XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI`
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
The Nalim backend JAR ships Oxidizium's [MIT notice](LICENSE.txt), the
[GPLv3 notice from the `nalim/` directory](nalim/LICENSE.txt), and the bundled Nalim
library's [GPL-2.0-or-later notice](nalim/libs/LICENSE-Nalim.txt). The source and JAR
for that library are linked [above](#nalim).
