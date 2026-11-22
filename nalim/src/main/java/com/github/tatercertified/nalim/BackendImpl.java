package com.github.tatercertified.nalim;

import com.github.tatercertified.oxidizium.utils.backend.Backend;
import com.github.tatercertified.oxidizium.utils.backend.BackendType;

public class BackendImpl implements Backend {
    @Override
    public BackendType backendType() {
        return BackendType.Nalim;
    }

    @Override
    public void onStartup() {
        if (ModuleLayer.boot().findModule("jdk.internal.vm.ci").isEmpty()) {
            throw new IllegalStateException("The Nalim backend requires JVMCI. Use a HotSpot JDK with "
                    + "--add-modules=jdk.internal.vm.ci; see https://github.com/Tater-Certified/Oxidizium#nalim");
        }
    }
}
