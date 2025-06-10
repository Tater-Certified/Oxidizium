package com.github.tatercertified.oxidizium.utils;

import com.github.tatercertified.oxidizium.Config;
import com.github.tatercertified.oxidizium.Oxidizium;

import java.io.*;
import java.nio.file.Path;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public final class HashUtils {

    public static boolean checkHash(Path libPath, String currentOSLibName) {
        File libOxidizium = new File(libPath.toUri());
        String correctHash = getLibHash(currentOSLibName);
        String presentHash = getFileHash(libOxidizium);
        if (!correctHash.equals(presentHash)) {
            if (Config.getInstance().debug()) {
                Oxidizium.LOGGER.info("Outdated Oxidizium Binary Found. Updating...");
            }
            return false;
        }
        return true;
    }

    private static String getLibHash(String binaryName) {
        try (InputStream inputStream = HashUtils.class.getResourceAsStream("/" + binaryName + ".hash")) {
            if (inputStream == null) {
                throw new IOException("Hash file not found in resources.");
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                return reader.readLine();
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to read " + binaryName + ".hash", e);
        }
    }

    private static String getFileHash(File file) {
        try (FileInputStream fis = new FileInputStream(file);
             DigestInputStream dis = new DigestInputStream(fis, MessageDigest.getInstance("SHA-256"))) {

            byte[] buffer = new byte[8192];
            while (dis.read(buffer) != -1) {}

            byte[] hashBytes = dis.getMessageDigest().digest();
            return bytesToHex(hashBytes);

        } catch (IOException | NoSuchAlgorithmException e) {
            throw new RuntimeException("Failed to compute hash for file: " + file, e);
        }
    }

    private static String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(0xff & b);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }
}
