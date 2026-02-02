package com.github.tatercertified.panama.mixin;

import com.github.tatercertified.oxidizium.LoadRustBinary;
import com.github.tatercertified.rust.lib_h;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.nio.file.Path;

@Mixin(value = lib_h.class, remap = false)
public class LinuxFixMixin {
    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/lang/foreign/SymbolLookup;libraryLookup(Ljava/lang/String;Ljava/lang/foreign/Arena;)Ljava/lang/foreign/SymbolLookup;"))
    private static SymbolLookup fixForLinuxSystems(String name, Arena arena) {
        Path libraryPath = LoadRustBinary.getWorkingDir().resolve(name);
        return SymbolLookup.libraryLookup(libraryPath, arena);
    }
}
