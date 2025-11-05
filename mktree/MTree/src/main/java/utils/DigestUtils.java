package utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class DigestUtils {
    // MurmurHash2 seed constant
    static final int SEED = -1756908916;
    private int flag = 0;
    private MessageDigest digest = null;
    private String algorithm;

    public DigestUtils(String algorithm) throws NoSuchAlgorithmException {
        this.algorithm = algorithm.toUpperCase();

        switch (this.algorithm) {
            case "MURMUR":
            case "MURMURHASH2":
                flag = 1;
                break;

            case "SHA256":
            case "SHA-256":
                digest = MessageDigest.getInstance("SHA-256");
                break;

            case "SHA512":
            case "SHA-512":
                digest = MessageDigest.getInstance("SHA-512");
                break;

            case "MD5":
                digest = MessageDigest.getInstance("MD5");
                break;

            default:
                throw new NoSuchAlgorithmException("Unsupported algorithm: " + algorithm);
        }
    }

    public String getHash(String input) {
        byte[] hashBytes;

        if (flag == 1) { // MurmurHash2 mode
            byte[] data = input.getBytes(StandardCharsets.UTF_8);
            hashBytes = murmurHash2_32(data, SEED);
        } else {
            hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
        }

        return bytesToHex(hashBytes);
    }

    public List<String> getHashList(List<String> inputs) {
        List<String> hashes = new ArrayList<>();
        for (String s : inputs) {
            hashes.add(getHash(s));
        }
        return hashes;
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (byte b : bytes) {
            String hex = Integer.toHexString(255 & b);
            if (hex.length() == 1) sb.append('0');
            sb.append(hex);
        }
        return sb.toString();
    }

    private byte[] murmurHash2_32(byte[] data, int seed) {
        int m = 0x5bd1e995;
        int r = 24;
        int len = data.length;
        int h = seed ^ len;

        int i = 0;
        while (len >= 4) {
            int k = (data[i] & 0xFF) |
                    ((data[i + 1] & 0xFF) << 8) |
                    ((data[i + 2] & 0xFF) << 16) |
                    ((data[i + 3] & 0xFF) << 24);

            k *= m;
            k ^= k >>> r;
            k *= m;

            h *= m;
            h ^= k;

            i += 4;
            len -= 4;
        }

        switch (len) {
            case 3:
                h ^= (data[i + 2] & 0xFF) << 16;
            case 2:
                h ^= (data[i + 1] & 0xFF) << 8;
            case 1:
                h ^= (data[i] & 0xFF);
                h *= m;
        }

        h ^= h >>> 13;
        h *= m;
        h ^= h >>> 15;

        return intToBytesLE(h);
    }

    public static byte[] intToBytesLE(int value) {
        return new byte[]{
                (byte) value,
                (byte) (value >> 8),
                (byte) (value >> 16),
                (byte) (value >> 24)
        };
    }
}
