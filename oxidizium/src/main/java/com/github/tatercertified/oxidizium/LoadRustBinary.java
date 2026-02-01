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

        Path output = copyNativeLib(binaryName, outputName, binaryNameNoExtension);

        if (Config.isNalim()) {
            System.load(output.toAbsolutePath().toString());
        }
    }

    public static Path getWorkingDir() {
        return Paths.get("").toAbsolutePath();
    }

    private static Path copyNativeLib(String binaryName, String outputName, String binaryNameNoExt) {
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
            return destinationPath;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
