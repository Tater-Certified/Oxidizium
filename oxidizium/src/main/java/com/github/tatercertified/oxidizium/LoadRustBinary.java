package com.github.tatercertified.oxidizium;

import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.SystemUtils;
import com.github.tatercertified.oxidizium.utils.HashUtils;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Locale;

public class LoadRustBinary {
    public static void onPreLaunch() {
        String osName = System.getProperty("os.name").toLowerCase(Locale.ENGLISH);
        String arch = System.getProperty("os.arch").toLowerCase(Locale.ENGLISH);

        String binaryName;
        String outputName;

        if (SystemUtils.IS_OS_WINDOWS) {
            binaryName = switch (arch) {
                case "amd64", "x86_64" -> "oxidizium_windows_x86.dll";
                case "arm", "aarch64" -> "oxidizium_windows_arm64.dll";
                default -> throw new IllegalStateException("Unsupported architecture: " + arch);
            };
            outputName = "oxidizium";
        } else if (SystemUtils.IS_OS_LINUX) {
            binaryName = switch (arch) {
                case "amd64", "x86_64" -> "liboxidizium_linux_x86.so";
                case "arm64", "aarch64" -> "liboxidizium_linux_arm64.so";
                default -> throw new IllegalStateException("Unsupported architecture: " + arch);
            };
            outputName = "liboxidizium";
        } else if (SystemUtils.IS_OS_MAC) {
            binaryName = switch (arch) {
                case "x86_64" -> "liboxidizium_mac_x86.dylib";
                case "arm64", "aarch64" -> "liboxidizium_mac_arm64.dylib";
                default -> throw new IllegalStateException("Unsupported architecture: " + arch);
            };
            outputName = "liboxidizium";
        } else {
            throw new IllegalStateException("Unsupported OS: " + osName);
        }

        String binaryNameNoExtension = FilenameUtils.removeExtension(binaryName);

        if (Config.isNalim() || Config.isMembrane()) {
            copyNativeLibToMinecraftLibs(binaryName, outputName, binaryNameNoExtension);
        } else {
            copyNativeLib(binaryName, outputName, binaryNameNoExtension);
        }
    }

    public static Path getWorkingDir() {
        return Paths.get("").toAbsolutePath();
    }

    private static void copyNativeLib(String binaryName, String outputName, String binaryNameNoExt) {
        try (InputStream inputStream = LoadRustBinary.class.getResourceAsStream("/" + binaryName)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: /" + binaryName);
            }
            Path workingDir = getWorkingDir();
            int lastDotIndex = binaryName.lastIndexOf('.');
            String extension = binaryName.substring(lastDotIndex + 1);
            Path destinationPath = workingDir.resolve(outputName + "." + extension);
            if (Files.notExists(destinationPath) || !HashUtils.checkHash(destinationPath, binaryNameNoExt)) {
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static Path getMinecraftLibrariesDir() {
        // Fabric / Minecraft root usually uses "libraries" folder
        // Adjust this if using a custom launcher folder
        Path mcRoot = Paths.get("").toAbsolutePath(); // current working directory
        Path libsDir = mcRoot.resolve("libraries").resolve("oxidizium");
        try {
            Files.createDirectories(libsDir);
        } catch (IOException e) {
            throw new RuntimeException("Failed to create libraries/oxidizium directory", e);
        }
        return libsDir;
    }

    private static void copyNativeLibToMinecraftLibs(String binaryName, String outputName, String binaryNameNoExt) {
        try (InputStream inputStream = LoadRustBinary.class.getResourceAsStream("/" + binaryName)) {
            if (inputStream == null) {
                throw new RuntimeException("Resource not found: /" + binaryName);
            }

            Path libsDir = getMinecraftLibrariesDir();
            int lastDotIndex = binaryName.lastIndexOf('.');
            String extension = binaryName.substring(lastDotIndex + 1);

            Path destinationPath = libsDir.resolve(outputName + "." + extension);

            if (Files.notExists(destinationPath) || !HashUtils.checkHash(destinationPath, binaryNameNoExt)) {
                Files.copy(inputStream, destinationPath, StandardCopyOption.REPLACE_EXISTING);
            }

            if (Config.isMembrane()) {
                System.loadLibrary("oxidizium");
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
