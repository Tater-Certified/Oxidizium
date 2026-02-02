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
As of now, Oxidizium is very compatible with the majority of mods, with specific compatibility with Lithium

---

## Backends
Oxidizium comes with 2 backends by default: Panama and Nalim. The backend will be in the jar and mod name.
Each backend has its own pros and cons. Read about them below:

### Project Panama
Project Panama is a Java 22+ feature that allows for low latency native code execution. This works on any Minecraft version
from 1.14.3 to modern MC. 
#### Usage
**<u>It requires Java 22 or above</u>**.<p>
The following JVM arguments are recommended, but not *currently* required for Java 24 and below:
```markdown
--enable-preview --enable-native-access=ALL-UNNAMED
```
Using a newer Java version is likely to improve performance more than Java 22. I recommend using [GraalVM](https://www.graalvm.org/downloads/) for the
best performance, however be warned that it is possible (though very unlikely) that GraalVM could break mods. Use a
distribution such as [Temurin](https://adoptium.net/) if you want to be completely safe.

### Nalim
Nalim is a 3rd-party library that enables near-native latency when executing native code. It is close to 50x faster than
Project Panama. However, Nalim only works on Java 20 and below, meaning only 1.20.4 and below can use Nalium as of right
now. Nalim also requires a bit more configuration to get working:
**<u>It requires Java 20 or below</u>**.<p>
The following JVM start command structure is recommended:
```markdown
java -Xmx2G -XX:+UnlockExperimentalVMOptions -XX:+EnableJVMCI \
--add-exports=jdk.internal.vm.ci/jdk.vm.ci.code=ALL-UNNAMED \
--add-exports=jdk.internal.vm.ci/jdk.vm.ci.code.site=ALL-UNNAMED \
--add-exports=jdk.internal.vm.ci/jdk.vm.ci.hotspot=ALL-UNNAMED \
--add-exports=jdk.internal.vm.ci/jdk.vm.ci.meta=ALL-UNNAMED \
--add-exports=jdk.internal.vm.ci/jdk.vm.ci.runtime=ALL-UNNAMED \
-javaagent:nalim.jar \
-Djava.library.path=./libraries/oxidizium \
-cp nalim.jar:fabric-server-launch.jar \
net.fabricmc.loader.impl.launch.server.FabricServerLauncher nogui
```
Feel free to change the RAM amount `-Xmx2G` and add more JVM arguments.

---

## Testing
The `Oxidizium-Tester` mod is a utility mod that can be loaded **with** `Oxidizium` (either backend is fine). It will create a GUI on startup instead
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