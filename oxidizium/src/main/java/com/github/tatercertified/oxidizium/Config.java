package com.github.tatercertified.oxidizium;

import com.github.tatercertified.oxidizium.utils.backend.Backend;
import com.github.tatercertified.oxidizium.utils.backend.BackendType;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.impl.FabricLoaderImpl;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;
import java.util.Properties;

public record Config(String version, boolean debug, boolean test, boolean reducedMemoryUsage, boolean enhancedLithiumSupport) {
    private static Config instance;
    private static final Path CONFIG_PATH = FabricLoader.getInstance().getConfigDir().resolve("oxidizium.properties");
    public static void init() {
        final Properties properties = new Properties();
        final String configVerKey = "config-version";
        final String configVer = "1.0";

        if (Files.notExists(CONFIG_PATH)) {
            fillDefaults(configVer, properties);
        } else {
            try {
                loadConfig(properties);
            } catch (IOException e) {
                if (Config.getInstance().debug()) {
                    Oxidizium.LOGGER.error("Config creation failed", e);
                }
            }

            if (!(Objects.equals(properties.getProperty(configVerKey), configVer))) {
                fillDefaults(configVer, properties);
            } else {
                parse(configVer, properties);
            }
        }
    }

    private static void storeConfig(String configVer, Properties properties) throws IOException {
        try (OutputStream output = Files.newOutputStream(CONFIG_PATH, StandardOpenOption.CREATE)) {
            properties.store(output, null);
        }
        parse(configVer, properties);
    }

    private static void fillDefaults(String configVer, Properties properties) {
        if (checkProperty("config-version", configVer, properties) ||
                checkProperty("debug", "false", properties) ||
                checkProperty("test-mode", "false", properties) ||
                checkProperty("reduced-memory-usage", "true", properties) ||
                checkProperty("enhanced-lithium-compat", "true", properties)) {
            try {
                storeConfig(configVer, properties);
            } catch (IOException e) {
                if (Config.getInstance().debug()) {
                    Oxidizium.LOGGER.error("Config storing failed", e);
                }
            }
        }
    }

    private static boolean checkProperty(String key, String defaultValue, Properties properties) {
        boolean missingKey = !properties.containsKey(key);
        if (missingKey) {
            properties.setProperty(key, defaultValue);
        }
        return missingKey;
    }

    private static void loadConfig(Properties properties) throws IOException {
        try (InputStream input = Files.newInputStream(CONFIG_PATH)) {
            properties.load(input);
        }
    }

    private static void parse(String configVer, Properties properties) {
        fillDefaults(configVer, properties);
        boolean debug = Boolean.parseBoolean(properties.getProperty("debug"));
        boolean testingMode = Boolean.parseBoolean(properties.getProperty("test-mode"));
        boolean reducedMemUse = Boolean.parseBoolean(properties.getProperty("reduced-memory-usage"));
        boolean enhancedLithiumCompat = Boolean.parseBoolean(properties.getProperty("enhanced-lithium-compat"));
        instance = new Config(configVer, debug, testingMode, reducedMemUse, enhancedLithiumCompat);
    }

    public static Config getInstance() {
        return instance;
    }

    public static boolean isLithiumOptimizationEnabled() {
        return instance.enhancedLithiumSupport() && FabricLoaderImpl.InitHelper.get().isModLoaded("lithium");
    }

    public static boolean isTestingEnabled() {
        return instance.test();
    }

    public static boolean isNalim() {
        return Oxidizium.BACKEND.backendType() == BackendType.Nalim;
    }

    public static boolean isPanama() {
        return Oxidizium.BACKEND.backendType() == BackendType.Panama;
    }
}
