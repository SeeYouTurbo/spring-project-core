package com.king.common.util;

import com.king.common.support.Charsets;
import com.king.common.support.Exceptions;
import org.springframework.lang.Nullable;
import org.springframework.util.DigestUtils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * 加密计算util
 *
 * @author jinfeng.wu
 * @date 2020/8/17 14:10
 */
public class DigestUtil extends DigestUtils {
    private static final char[] HEX_DIGITS = "0123456789abcdef".toCharArray();

    public DigestUtil() {
    }

    public static String md5Hex(final String data) {
        return md5DigestAsHex(data.getBytes(Charsets.UTF_8));
    }

    public static String md5Hex(final byte[] bytes) {
        return md5DigestAsHex(bytes);
    }

    public static String sha1(String srcStr) {
        return hash("SHA-1", srcStr);
    }

    public static String sha256(String srcStr) {
        return hash("SHA-256", srcStr);
    }

    public static String sha384(String srcStr) {
        return hash("SHA-384", srcStr);
    }

    public static String sha512(String srcStr) {
        return hash("SHA-512", srcStr);
    }

    public static String hash(String algorithm, String srcStr) {
        try {
            MessageDigest md = MessageDigest.getInstance(algorithm);
            byte[] bytes = md.digest(srcStr.getBytes(Charsets.UTF_8));
            return toHex(bytes);
        } catch (NoSuchAlgorithmException var4) {
            throw Exceptions.unchecked(var4);
        }
    }

    public static String toHex(byte[] bytes) {
        StringBuilder ret = new StringBuilder(bytes.length * 2);

        for(int i = 0; i < bytes.length; ++i) {
            ret.append(HEX_DIGITS[bytes[i] >> 4 & 15]);
            ret.append(HEX_DIGITS[bytes[i] & 15]);
        }

        return ret.toString();
    }

    public static boolean slowEquals(@Nullable String a, @Nullable String b) {
        return a != null && b != null ? slowEquals(a.getBytes(Charsets.UTF_8), b.getBytes(Charsets.UTF_8)) : false;
    }

    public static boolean slowEquals(@Nullable byte[] a, @Nullable byte[] b) {
        if (a != null && b != null) {
            if (a.length != b.length) {
                return false;
            } else {
                int diff = a.length ^ b.length;

                for(int i = 0; i < a.length && i < b.length; ++i) {
                    diff |= a[i] ^ b[i];
                }

                return diff == 0;
            }
        } else {
            return false;
        }
    }

    public static String encrypt(String data) {
        return sha1(md5Hex(data));
    }
}
