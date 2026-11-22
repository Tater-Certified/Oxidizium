package com.github.tatercertified.nalim;

import one.nalim.Linker;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;

/** Called by the generated bindings, after the loader has extracted the Rust library. */
final class NalimBindings {
    private NalimBindings() {}

    static MethodHandle link() {
        String path = System.getProperty("oxidizium.native.path");
        if (path != null) {
            Linker.loadLibrary(path);
        }
        Linker.linkClass(OxidiziumLib.class);
        return Linker.findIntrinsic("abs_int", MethodType.methodType(int.class, int.class)).orElse(null);
    }

    static int absInt(MethodHandle intrinsic, int value) {
        if (intrinsic == null) {
            return OxidiziumLib.nativeAbs(value);
        }
        try {
            return (int) intrinsic.invokeExact(value);
        } catch (RuntimeException | Error e) {
            throw e;
        } catch (Throwable e) {
            throw new AssertionError(e); // A verified integer operation cannot throw a checked exception.
        }
    }
}
