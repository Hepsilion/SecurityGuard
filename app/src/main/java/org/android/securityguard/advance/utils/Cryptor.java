package org.android.securityguard.advance.utils;

import android.util.Base64;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by Hepsilion on 2017/1/5.
 */
public class Cryptor {
    /**
     * 加密一个文本，返回base64编码后的内容
     * @param seed 种子 密码
     * @param plain 原文
     * @return 密文
     * @throws Exception
     */
    public static String encrypt(String seed, String plain) throws Exception {
        byte[] rawKey=getRawKey(seed.getBytes());
        byte[] encrypted=encrypt(rawKey, plain.getBytes());
        return Base64.encodeToString(encrypted, Base64.DEFAULT);
    }

    private static byte[] encrypt(byte[] raw, byte[] plain) throws Exception {
        SecretKeySpec keySpec=new SecretKeySpec(raw, "AES");
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.ENCRYPT_MODE, keySpec);
        byte[] encrypted=cipher.doFinal(plain);
        return encrypted;
    }

    /**
     * 解密Base64编码后的密文
     * @param seed 种子 密码
     * @param encrypted 密文
     * @return 原文
     * @throws Exception
     */
    public static String decrypt(String seed, String encrypted) throws Exception {
        byte[] rawKey=getRawKey(seed.getBytes());
        byte[] enc=Base64.decode(encrypted.getBytes(), Base64.DEFAULT);
        byte[] result=decrypt(rawKey, enc);
        return new String(result);
    }

    private static byte[] decrypt(byte[] raw, byte[] encrypted) throws Exception {
        SecretKeySpec keySpec=new SecretKeySpec(raw, "AES");
        Cipher cipher=Cipher.getInstance("AES");
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decrypted=cipher.doFinal(encrypted);
        return decrypted;
    }

    private static byte[] getRawKey(byte[] seed) throws Exception {
        SecureRandom rand=SecureRandom.getInstance("SHA1PRNG");
        rand.setSeed(seed);

        KeyGenerator keygen=KeyGenerator.getInstance("AES");
        keygen.init(128, rand);
        SecretKey key=keygen.generateKey();
        byte[] raw=key.getEncoded();
        return raw;
    }
}
