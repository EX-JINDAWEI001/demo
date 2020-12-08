package com.example.demo.common.utils;

import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.security.Security;

// 解密工具类(微信);
public class Crypto {
    // 加解密算法;
    private static final String AES_CBC = "AES/CBC/PKCS5Padding";
    private static final String AES_256_ECB = "AES/ECB/PKCS7Padding";
    // 字符集;
    private static final String CHARSET = "UTF-8";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    // 小程序用户信息和手机号解密(AES-128-CBC-PKCS5Padding);
    public static String decryptData(String sessionKey, String encryptedData, String iv) throws Exception {
        // 初始化;
        Cipher cipher = Cipher.getInstance(AES_CBC);
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(Base64.decodeBase64(sessionKey), "AES"), new IvParameterSpec(Base64.decodeBase64(iv)));
        // 解密;
        return new String(cipher.doFinal(Base64.decodeBase64(encryptedData)), CHARSET);
    }

    // 微信支付退款结果通知信息解密(AES-256-ECB-PKCS7Padding);
    public static String decryptData(String key, String encryptedData) throws Exception {
        // 初始化;
        Cipher cipher = Cipher.getInstance(AES_256_ECB, "BC");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(md5(key).getBytes(CHARSET), "AES"));
        // 解密;
        return new String(cipher.doFinal(Base64.decodeBase64(encryptedData)), CHARSET);
    }

    // SHA1验签;
    public static String digestSign(String sessionKey, String rawData, String signature) throws Exception {
        // 定义相关;
        StringBuilder sign = new StringBuilder();
        // 初始化;
        MessageDigest sha = MessageDigest.getInstance("SHA1");
        sha.update(rawData.concat(sessionKey).getBytes(CHARSET));
        // 哈希运算;
        byte[] md = sha.digest();
        // 遍历(生成签名);
        for (int i = 0; i < md.length; i++) {
            String shaHex = Integer.toHexString(md[i] & 0xff);
            if (shaHex.length() < 2) {
                sign.append(0);
            }
            sign.append(shaHex);
        }
        // 验证签名;
        if (!sign.toString().equals(signature)) {
            throw new Exception("Illegal signature !");
        }
        return sign.toString();
    }

    // MD5签名;
    public static String md5(String str) throws Exception {
        MessageDigest sha = MessageDigest.getInstance("MD5");
        sha.update(str.getBytes(CHARSET));
        byte[] md = sha.digest();

        StringBuilder buf = new StringBuilder();
        String shaHex = null;
        for (int i = 0; i < md.length; i++) {
            shaHex = Integer.toHexString(md[i] & 0xff);
            if (shaHex.length() < 2) {
                buf.append(0);
            }
            buf.append(shaHex);
        }
        return buf.toString();
    }
}
