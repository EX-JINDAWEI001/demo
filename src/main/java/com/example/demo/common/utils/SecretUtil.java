package com.example.demo.common.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;
import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.*;

public class SecretUtil {

    private static final Logger logger = LoggerFactory.getLogger(SecretUtil.class);

    private static final String CHARSET = "UTF-8";

    //SHA1签名========================================================================================SHA1签名\\
    public static String sha1_1(String str) {
        if (StringUtils.isEmpty(str)) {
            return null;
        }
        char[] hexDigest = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA1");
            sha.update(str.getBytes(CHARSET));
            byte[] md = sha.digest();
            int j = md.length;
            char[] buf = new char[j * 2];
            int k = 0;
            for (int i = 0; i < j; i++) {
                byte byte0 = md[i];
                buf[k++] = hexDigest[byte0 >>> 4 & 0xf];
                buf[k++] = hexDigest[byte0 & 0xf];
            }
            return new String(buf);
        } catch (Exception e) {
            logger.error("sha1_1 error: ", e);
            return null;
        }
    }

    public static String sha1_2(String str) {
        try {
            MessageDigest sha = MessageDigest.getInstance("SHA1");
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
        } catch (Exception e) {
            logger.error("sha1_2 error: ", e);
            return null;
        }
    }

    public static String md5(String str) {
        try {
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
        } catch (Exception e) {
            logger.error("sha1_2 error: ", e);
            return null;
        }
    }

    //3DES==============================================================================================3DES\\
    public static String DESEncode(String content, String key) {
        try {
            SecretKey secKey = new SecretKeySpec(build3DesKey(key), "DESede"); //生成密钥
            Cipher cipher = Cipher.getInstance("DESede");   //实例化负责加解密的Cipher工具类
            cipher.init(Cipher.ENCRYPT_MODE, secKey);   //初始化为加密模式

            byte[] secArr = cipher.doFinal(content.getBytes(CHARSET));
            return new String(Base64.encodeBase64(secArr));
//            return new BASE64Encoder().encode(secArr);
//            return new Base64().encodeToString(secArr);
        } catch (Exception e) {
            logger.error("DESEncode error:", e);
            return null;
        }
    }

    public static String DESDecode(String content, String key) {
        try {
            SecretKey secKey = new SecretKeySpec(build3DesKey(key), "DESede"); //生成密钥
            Cipher cipher = Cipher.getInstance("DESede");   //实例化负责加解密的Cipher工具类
            cipher.init(Cipher.DECRYPT_MODE, secKey);   //初始化为解密模式

            byte[] secArr = cipher.doFinal(Base64.decodeBase64(content));
//            byte[] secArr = cipher.doFinal(new BASE64Decoder().decodeBuffer(content));
//            byte[] secArr = cipher.doFinal(new Base64().decode(content.getBytes(CHARSET)));
            return new String(secArr, CHARSET);
        } catch (Exception e) {
            logger.error("DESDecode error:", e);
            return null;
        }
    }

    private static byte[] build3DesKey(String secKey) throws UnsupportedEncodingException {
        byte[] key = new byte[24];
        byte[] temp = secKey.getBytes(CHARSET);
        if (key.length > temp.length) {
            System.arraycopy(temp, 0, key, 0, temp.length);
        } else {
            System.arraycopy(temp, 0, key, 0, key.length);
        }
        return key;
    }

    //AES================================================================================================AES\\
    /**
     * 算法，模式，补码方式
     */
    private static final String AES_ECB = "AES/ECB/PKCS5Padding";

    private static final String AES_CBC = "AES/CBC/PKCS5Padding";

    private static final String AES_256_ECB = "AES/ECB/PKCS7Padding";

    static {
        Security.addProvider(new BouncyCastleProvider());
    }

    public static String AES_Encrypt_WX(String content, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            Cipher cipher = Cipher.getInstance(AES_256_ECB, "BC");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
            byte[] encrypt = cipher.doFinal(content.getBytes(CHARSET));
            return new String(Base64.encodeBase64(encrypt), CHARSET);
        } catch (Exception e) {
            logger.error("AES_Encrypt_WX error: ", e);
            return null;
        }
    }

    public static String AES_Decrypt_WX(byte[] content, String key) {
        try {
            SecretKeySpec secretKeySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            Cipher cipher = Cipher.getInstance(AES_256_ECB, "BC");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
            byte[] decrypt = cipher.doFinal(content);
            return new String(decrypt, CHARSET);
        } catch (Exception e) {
            logger.error("AES_Decrypt_WX error: ", e);
            return null;
        }
    }

    public static String AES_Encrypt1(String content, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            Cipher cipher = Cipher.getInstance(AES_ECB);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypt = cipher.doFinal(content.getBytes(CHARSET));
            return new String(Base64.encodeBase64(encrypt), CHARSET);
        } catch (Exception e) {
            logger.error("AES_Encrypt1 error: ", e);
            return null;
        }
    }

    public static String AES_Decrypt1(String content, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            Cipher cipher = Cipher.getInstance(AES_ECB);
            cipher.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypt = cipher.doFinal(Base64.decodeBase64(content));
            return new String(decrypt, CHARSET);
        } catch (Exception e) {
            logger.error("AES_Decrypt1 error: ", e);
            return null;
        }
    }

    public static String AES_Encrypt2(String content, String key, String iv) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(CHARSET));

            Cipher cipher = Cipher.getInstance(AES_CBC);
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypt = cipher.doFinal(content.getBytes(CHARSET));
            return new String(Base64.encodeBase64(encrypt), CHARSET);
        } catch (Exception e) {
            logger.error("AES_Encrypt2 error: ", e);
            return null;
        }
    }

    public static String AES_Decrypt2(String content, String key, String iv) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(CHARSET));

            Cipher cipher = Cipher.getInstance(AES_CBC);
            cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decrypt = cipher.doFinal(Base64.decodeBase64(content));
            return new String(decrypt, CHARSET);
        } catch (Exception e) {
            logger.error("AES_Decrypt2 error: ", e);
            return null;
        }
    }

    public static String AES_Encrypt3(String content, String key, String iv) {
        try {
            Cipher cipher = Cipher.getInstance(AES_CBC);
            int blockSize = cipher.getBlockSize();
            byte[] dataBytes = content.getBytes(CHARSET);
            int dataLength = dataBytes.length;
            if (dataLength % blockSize != 0) {
                dataLength += blockSize - (dataLength % blockSize);
            }
            byte[] plaintext = new byte[dataLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(CHARSET));
            cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypt = cipher.doFinal(plaintext);
            return new String(Base64.encodeBase64(encrypt), CHARSET);
        } catch (Exception e) {
            logger.error("AES_Encrypt3 error: ", e);
            return null;
        }
    }

    //RSA================================================================================================RSA\\
    private static Map<Integer, String> keyMap = new HashMap<>();

    static {
        try {
            genKeyPair();
        } catch (NoSuchAlgorithmException e) {
            logger.error("genKeyPair error: {}", e);
        }
    }

    /**
     * 随机生成密钥对
     *
     * @throws NoSuchAlgorithmException
     */
    public static void genKeyPair() throws NoSuchAlgorithmException {
        // KeyPairGenerator类用于生成公钥和私钥对，基于RSA算法生成对象
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        // 初始化密钥对生成器，密钥大小为96-1024位
        keyPairGen.initialize(1024, new SecureRandom());
        // 生成一个密钥对，保存在keyPair中
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        String privateKeyString = new String(Base64.encodeBase64((privateKey.getEncoded())));
        // 将公钥和私钥保存到Map
        keyMap.put(0, publicKeyString);
        keyMap.put(1, privateKeyString);
    }

    public static String RSA_Encrypt(String str, String publicKey) {
        try {
            //base64编码的公钥
            byte[] decoded = Base64.decodeBase64(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, pubKey);
            return Base64.encodeBase64String(cipher.doFinal(str.getBytes(CHARSET)));
        } catch (Exception e) {
            logger.error("RSA_Encrypt error: ", e);
            return null;
        }
    }

    public static String RSA_Decrypt(String str, String privateKey) {
        try {
            //64位解码加密后的字符串
            byte[] inputByte = Base64.decodeBase64(str.getBytes(CHARSET));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, priKey);
            return new String(cipher.doFinal(inputByte));
        } catch (Exception e) {
            logger.error("RSA_Decrypt error: ", e);
            return null;
        }
    }

    public static String RSA_Sign(String data, String privateKey) {
        try {
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));

            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initSign(priKey);
            signature.update(data.getBytes(CHARSET));
            return new String(Base64.encodeBase64(signature.sign()), CHARSET);
        } catch (Exception e) {
            logger.error("RSA_Sign error: ", e);
            return null;
        }
    }

    public static boolean RSA_Verify(String data, String publicKey, String sign) {
        try {
            byte[] decoded = Base64.decodeBase64(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));

            Signature signature = Signature.getInstance("SHA256WithRSA");
            signature.initVerify(pubKey);
            signature.update(data.getBytes(CHARSET));
            return signature.verify(Base64.decodeBase64(sign));
        } catch (Exception e) {
            logger.error("RSA_Verify error: ", e);
            return false;
        }
    }

    public static String RSA_Encrypt1(String str, String privateKey) {
        try {
            //base64编码的公钥
            byte[] decoded = Base64.decodeBase64(privateKey);
            RSAPrivateKey priKey = (RSAPrivateKey) KeyFactory.getInstance("RSA").generatePrivate(new PKCS8EncodedKeySpec(decoded));
            //RSA加密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, priKey);
            return Base64.encodeBase64String(cipher.doFinal(str.getBytes(CHARSET)));
        } catch (Exception e) {
            logger.error("RSA_Encrypt1 error: ", e);
            return null;
        }
    }

    public static String RSA_Decrypt1(String str, String publicKey) {
        try {
            //64位解码加密后的字符串
            byte[] inputByte = Base64.decodeBase64(str.getBytes(CHARSET));
            //base64编码的私钥
            byte[] decoded = Base64.decodeBase64(publicKey);
            RSAPublicKey pubKey = (RSAPublicKey) KeyFactory.getInstance("RSA").generatePublic(new X509EncodedKeySpec(decoded));
            //RSA解密
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, pubKey);
            return new String(cipher.doFinal(inputByte));
        } catch (Exception e) {
            logger.error("RSA_Decrypt1 error: ", e);
            return null;
        }
    }

    public static void main(String[] args) throws UnsupportedEncodingException {
        System.out.println(sha1_1("abcdef"));
        System.out.println(sha1_2("abcdef"));

        System.out.println(DESEncode("金大为哈哈哈哈哈", "jdw"));
        System.out.println(DESDecode("o7db5WRfuXn0CB3tX5PYvib7gYwqkLAlRx4daR1Yx8Y=", "jdw"));

        System.out.println(AES_Encrypt1("金大为哈哈哈哈哈", "jdwjdwjdwjdwjdwj"));
        System.out.println(AES_Decrypt1("bTvsFi0XoMFM4aa0Fo2WRWc+BcRaiogVfBeRzU/iLOI=", "jdwjdwjdwjdwjdwj"));

        System.out.println(AES_Encrypt2("金大为哈哈哈哈哈", "jdwjdwjdwjdwjdwj", "jdwjdwjdwjdwjdwj"));
        System.out.println(AES_Decrypt2("CGCaNZYUlNY3ZWD+p7aqT6nrZpy9gNDuLepU1q4bAxd1rfI3rWFCyUho8m7ID7pn", "jdwjdwjdwjdwjdwj", "jdwjdwjdwjdwjdwj"));

        System.out.println(AES_Encrypt3("金大为哈哈哈哈哈", "jdwjdwjdwjdwjdwj", "jdwjdwjdwjdwjdwj"));
        System.out.println(URLEncoder.encode(AES_Encrypt3("金大为哈哈哈哈哈", "jdwjdwjdwjdwjdwj", "jdwjdwjdwjdwjdwj"), CHARSET));
        System.out.println(AES_Decrypt2(URLDecoder.decode("CGCaNZYUlNY3ZWD%2Bp7aqT6nrZpy9gNDuLepU1q4bAxd1rfI3rWFCyUho8m7ID7pn", CHARSET), "jdwjdwjdwjdwjdwj", "jdwjdwjdwjdwjdwj"));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "金大为");
        paramMap.put("age", 28);
        paramMap.put("sex", "M");
        paramMap.put("interests", JSON.toJSONString(new String[]{"打球", "钓鱼", "阅读"}));
        System.out.println(getSignature(paramMap, "jdwjdwjdwjdwjdwj"));

        String rsaen = RSA_Encrypt("金大为哈哈哈哈哈", keyMap.get(0));
        System.out.println(rsaen);
        System.out.println(RSA_Decrypt(rsaen, keyMap.get(1)));

        String rsaen1 = RSA_Encrypt1("金大为哈哈哈哈哈", keyMap.get(1));
        System.out.println(rsaen1);
        System.out.println(RSA_Decrypt1(rsaen1, keyMap.get(0)));

        String sign = RSA_Sign(rsaen, keyMap.get(1));
        System.out.println(sign);
        System.out.println(RSA_Verify(rsaen, keyMap.get(0), sign));

        String content = "wvm81DNoEyMiBbF7gjHh51JWMpdVsxwGuQ/Ej4DcGR408/bJzsu0zsCGcm9t3OMuDCoI+oXQKZBX/iBFo/+TfOf0w8YMbeDZELHUBShN+Pw7It0rzsBY0/ScN9EFRAg9uFE5AKFzlEMdDnHdEk5IX8bn+q/iNyuZ20Zfatx/n1u1r/+IDKtrxUDyPpRxzeHlc0eIzN7tNfnW+BjWenmlK+/qzqb58s8VEJ8vwKjXnIuBRUnt8IXouCqR4pZjQ7VY/pasfJcb8wBvCMOJ8szpoZv2kB0UglhQ/f8VgMNVzSd7Jlbr/Gvikb4reFPoZuSQgCkL0wcT1txmPqF2hNd7Ct91Gp3CYwjGo1DSIl6UVedPYb9XRh2vhUkmn1L9ri4VtXvKxMZeda+gzcpzFkQtroQUHCtKvWYyDUzTIDT2uLvyusZLPrhI1t3r7Gqu4b7J9aGADUuCh4CmeoR5eIcOoagtiNcrd6H5HBnYuqHCw6mf4/9/+3E60SOkiTr2hhijwkaqif7r2jpwluHBZ2bxQ896Lnlh7WiwFZ6KxVL9TwKTUa0EvMW+xcT7Sh4hawjgEuZCzOjixNl1tcfqwBGxJDMHODTV2GBX7uHntPCGfiMbuuU/0CVn5VGuJETQXmOM7KT6kaK22TCJj7d3yw7dtROSJ2WIg7O5ieodfHYSrBlw3ZLnaLUx63K0BcJCrNUYJ8tO28x3swgVrNZHcQccsR/35rdrUuyd2E1CCdFW2/XnniY8ZJSrd33Xy+9dCjR5almlJwyLWAXHTIQJHMQjs5yhBNL9PJRYk9lxb3DulIAS37fHEzKiuNm8aA46SMz7EsDnTYdkPA7I+vf3GED6IiKeTjLD4z/LBN2FvUXxwmInaGAZJjZeWUa3cUSJCR+hyxRvAcUwWr9w1Yu+KHqXIAbMdrPYO+SCnK9nDBOVO/JL62Nb5gV5xpAIpeJW9dvABDPpEJyxtPTmL3J+5KPF0fNt678eLbYHJdp731j3JojVYvNx++eDJGg9LxLT88ECqFX1II+x2FiCSt2MG2JTR1OlWCtOMrpO1e5lzg0ScSU=";
        System.out.println(AES_Decrypt_WX(Base64.decodeBase64(content), md5("3c6e0b8a9c15224a8228b9a98ca1531d")));

    }

    public static String getSignature(Map<String, Object> paramMap, String partnerId) {
        String paraStr = SortByASCII(paramMap);
        return sha1_1(partnerId + paraStr);
//        return RSA_Sign(partnerId + paraStr, keyMap.get(1));
    }

    private static String SortByASCII(Map<String, Object> paramMap) {
        if (paramMap.isEmpty()) {
            return null;
        }
        List<String> paramNames = new ArrayList<>(paramMap.keySet());
        Collections.sort(paramNames);
        return splitParams(paramNames, paramMap);
    }

    private static String splitParams(List<String> paramNames, Map<String, Object> maps) {
        StringBuilder sb = new StringBuilder();
        for (String paramName : paramNames) {
            sb.append(paramName).append(maps.get(paramName));
        }
        return sb.toString();
    }

}
