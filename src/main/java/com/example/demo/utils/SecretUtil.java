package com.example.demo.utils;

import com.alibaba.fastjson.JSON;
import org.apache.commons.codec.binary.Base64;
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

    //3DES==============================================================================================3DES\\
    public static String DESEncode(String content, String key) {
        try {
            SecretKey secKey = new SecretKeySpec(build3DesKey(key), "DESede"); //生成密钥
            Cipher c1 = Cipher.getInstance("DESede");   //实例化负责加解密的Cipher工具类
            c1.init(Cipher.ENCRYPT_MODE, secKey);   //初始化为加密模式

            byte[] secArr = c1.doFinal(content.getBytes(CHARSET));
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
            Cipher c1 = Cipher.getInstance("DESede");   //实例化负责加解密的Cipher工具类
            c1.init(Cipher.DECRYPT_MODE, secKey);   //初始化为解密模式

            byte[] secArr = c1.doFinal(Base64.decodeBase64(content));
//            byte[] secArr = c1.doFinal(new BASE64Decoder().decodeBuffer(content));
//            byte[] secArr = c1.doFinal(new Base64().decode(content.getBytes(CHARSET)));
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

    public static String AES_Encrypt1(String content, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            Cipher c1 = Cipher.getInstance(AES_ECB);
            c1.init(Cipher.ENCRYPT_MODE, keySpec);
            byte[] encrypt = c1.doFinal(content.getBytes(CHARSET));
            return new String(Base64.encodeBase64(encrypt), CHARSET);
        } catch (Exception e) {
            logger.error("AES_Encrypt1 error: ", e);
            return null;
        }
    }

    public static String AES_Decrypt1(String content, String key) {
        try {
            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            Cipher c1 = Cipher.getInstance(AES_ECB);
            c1.init(Cipher.DECRYPT_MODE, keySpec);
            byte[] decrypt = c1.doFinal(Base64.decodeBase64(content));
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

            Cipher c1 = Cipher.getInstance(AES_CBC);
            c1.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypt = c1.doFinal(content.getBytes(CHARSET));
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

            Cipher c1 = Cipher.getInstance(AES_CBC);
            c1.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

            byte[] decrypt = c1.doFinal(Base64.decodeBase64(content));
            return new String(decrypt, CHARSET);
        } catch (Exception e) {
            logger.error("AES_Decrypt2 error: ", e);
            return null;
        }
    }

    public static String AES_Encrypt3(String content, String key, String iv) {
        try {
            Cipher c1 = Cipher.getInstance(AES_CBC);
            int blockSize = c1.getBlockSize();
            byte[] dataBytes = content.getBytes(CHARSET);
            int dataLength = dataBytes.length;
            if (dataLength % blockSize != 0) {
                dataLength += blockSize - (dataLength % blockSize);
            }
            byte[] plaintext = new byte[dataLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);

            SecretKeySpec keySpec = new SecretKeySpec(key.getBytes(CHARSET), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(iv.getBytes(CHARSET));
            c1.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);

            byte[] encrypt = c1.doFinal(plaintext);
            return new String(Base64.encodeBase64(encrypt), CHARSET);
        } catch (Exception e) {
            logger.error("AES_Encrypt3 error: ", e);
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
        System.out.println(AES_Decrypt2(URLDecoder.decode("CGCaNZYUlNY3ZWD%2Bp7aqT6nrZpy9gNDuLepU1q4bAxd1rfI3rWFCyUho8m7ID7pn", CHARSET),"jdwjdwjdwjdwjdwj", "jdwjdwjdwjdwjdwj"));

        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("name", "金大为");
        paramMap.put("age", 28);
        paramMap.put("sex", "M");
        paramMap.put("interests", JSON.toJSONString(new String[]{"打球", "钓鱼", "阅读"}));
        System.out.println(getSignature(paramMap, "jdwjdwjdwjdwjdwj"));
    }



    public static String getSignature(Map<String, Object> paramMap, String partnerId) {
        String paraStr = ASCIIHandler(paramMap);
        return sha1_1(partnerId + paraStr);
    }

    private static String ASCIIHandler(Map<String, Object> paramMap) {
        if (paramMap.isEmpty()) {
            return null;
        }
        List<String> paramNames = getParamNames(paramMap);
        Collections.sort(paramNames);
        return splitParams(paramNames, paramMap);
    }

    private static String splitParams(List<String> paramNames, Map<String, Object> maps) {
        StringBuilder sb = new StringBuilder();
        for (String paramName : paramNames) {
            sb.append(paramName).append(String.valueOf(maps.get(paramName)));
        }
        return sb.toString();
    }

    private static List<String> getParamNames(Map<String, Object> maps) {
        List<String> paramNames = new ArrayList<>();
        for (Map.Entry<String, Object> entry : maps.entrySet()) {
            paramNames.add(entry.getKey());
        }
        return paramNames;
    }

}
