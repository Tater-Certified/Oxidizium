package com.github.tatercertified.panama.mixin;

import com.github.tatercertified.oxidizium.LoadRustBinary;
import com.moulberry.mixinconstraints.annotations.IfBoolean;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.lang.foreign.Arena;
import java.lang.foreign.SymbolLookup;
import java.nio.file.Path;

@IfBoolean(booleanPath = "com.github.tatercertified.oxidizium.Config", booleanMethodName = "isPanama")
@Mixin(targets = {"com.github.tatercertified.rust.lib_h"}, remap = false)
public class LinuxFixMixin {
    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Ljava/lang/foreign/SymbolLookup;libraryLookup(Ljava/lang/String;Ljava/lang/foreign/Arena;)Ljava/lang/foreign/SymbolLookup;"))
    private static SymbolLookup fixForLinuxSystems(String name, Arena arena) {
        Path libraryPath = LoadRustBinary.getWorkingDir().resolve(name);
        return SymbolLookup.libraryLookup(libraryPath, arena);
    }
}
