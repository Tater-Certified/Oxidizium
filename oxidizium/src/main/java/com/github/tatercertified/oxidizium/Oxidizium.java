package com.github.tatercertified.oxidizium;

import com.github.tatercertified.oxidizium.math.Mth;
import com.github.tatercertified.oxidizium.utils.backend.Backend;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ServiceLoader;

public class Oxidizium implements ModInitializer {
    public static final Logger LOGGER = LoggerFactory.getLogger("Oxidizium");
    public static Mth MTH = ServiceLoader.load(Mth.class).iterator().next();
    public static Backend BACKEND = ServiceLoader.load(Backend.class).iterator().next();

    @Override
    public void onInitialize() {
    }
}
