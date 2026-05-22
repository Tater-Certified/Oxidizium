package com.github.tatercertified.nalim;

import com.github.tatercertified.oxidizium.Oxidizium;
import com.github.tatercertified.oxidizium.utils.backend.Backend;
import com.github.tatercertified.oxidizium.utils.backend.BackendType;

public class BackendImpl implements Backend {
    @Override
    public BackendType backendType() {
        return BackendType.Nalim;
    }

    @Override
    public void onStartup() {
        try {
            Class.forName("one.nalim.Linker");
        } catch (ClassNotFoundException e) {
            Oxidizium.LOGGER.error("Nalim java agent not found. Please follow this guide when using the Nalim backend: https://github.com/Tater-Certified/Oxidizium#nalim");
            throw new RuntimeException(e);
        }
    }
}
