package com.ruoyi.travel.util;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.Cipher;
import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RSAUtil {
    public static String data = "d7c030d60cf83e6407514e79c31270aea2b276b310e09a9b6132814c29ebb396";
    public static BASE64Encoder base64Encoder = new BASE64Encoder();
    public static BASE64Decoder base64Decoder = new BASE64Decoder();
    // 公钥
    private static String publicKeyStr = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCAnW6W4UiQs+XXtvTf5Zh+0kX/m/BY6+DKy+mPmnBf2aNcI8qsJZjkbkoFdM3thW3H45KqZgAMW9rC456nVBxE2uaDonVF6n1yNjl2Kzef2E7dffJQjUXHK5wTyjwDNHlIrf9cIyXWqFmo0qEZZtYyZw9qU3j2YKnKJ2i9yi6SmQIDAQAB";
    // 私钥
    private static String privateKeyStr = "MIICdwIBADANBgkqhkiG9w0BAQEFAASCAmEwggJdAgEAAoGBAICdbpbhSJCz5de29N/lmH7SRf+b8Fjr4MrL6Y+acF/Zo1wjyqwlmORuSgV0ze2FbcfjkqpmAAxb2sLjnqdUHETa5oOidUXqfXI2OXYrN5/YTt198lCNRccrnBPKPAM0eUit/1wjJdaoWajSoRlm1jJnD2pTePZgqconaL3KLpKZAgMBAAECgYADgsirC7lMYezLR65FkmHoTFsm0B3uf3nib1CP3/pjU3CXKk71Bg23Zp9UqMyaSEMn6rltN12W+Mbu/QGutagT2w9kjSgMjSzBaeJghhJF+HZaG9igyJ1YQKYMSMMBxIIs2ljoICUrc1lK7pY1tsOLc30tRMgbQ2gPke2fmrMmlQJBANXl6fL5FhnvbLMc5FL24K4/I/Yld0p1kivy0hcqa0MEA6iqrBNkGC1L4VtQj5guXRPQMFz4hGbDsQiG75jZ/ZsCQQCZ7jJKhBlRAv2M30QUPU5xImOAQPP8P44mHHWHGKwwbcbgu4Gw12jVq7e+II7RxEdeUZPVZIjw+v5c9VVfcU3bAkEAvmHxpY4HGfZ9Lb9dimqq+OzCOWiHkQKso+lxDaV36yI7j3i5yFijHE4Zu4ktBireNbBsG5QnZMBxZkGUKNx+WQJBAJIXHkXYf4qCcw9a6mrnEGaRQ6v6eVVjIE8NXYG9KdbAqSqq6thiaI257qYUkleFmFJszAiP5OQI4pZpBQCTMVcCQAV6zSUVHAKjsBdSGcM+EiYKP3pNZLmQClQ8KbDug+oCraVdns9wosHqkbmQDKpaM5bZsTJRckvS1FAdHO/EPUs=";
    private static RSAUtil ourInstance = new RSAUtil();

    public static RSAUtil getInstance() {
        return ourInstance;
    }

    // 生成密钥对
    private void generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGenerator;
        keyPairGenerator = KeyPairGenerator.getInstance("RSA");
        keyPairGenerator.initialize(1024);
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 获取公钥，并以base64格式打印出来
        PublicKey publicKey = keyPair.getPublic();
        publicKeyStr = new String(base64Encoder.encode(publicKey.getEncoded()));
        // 获取私钥，并以base64格式打印出来
        PrivateKey privateKey = keyPair.getPrivate();
        privateKeyStr = new String(base64Encoder.encode(privateKey.getEncoded()));
    }

    // 将base64编码后的公钥字符串转成PublicKey实例
    private static PublicKey getPublicKey(String publicKey) throws Exception {
        byte[] keyBytes = base64Decoder.decodeBuffer(publicKey);
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePublic(keySpec);
    }

    // 将base64编码后的私钥字符串转成PrivateKey实例
    private static PrivateKey getPrivateKey(String privateKey) throws Exception {
        byte[] keyBytes = base64Decoder.decodeBuffer(privateKey);
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(keyBytes);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return keyFactory.generatePrivate(keySpec);
    }

    // 公钥加密
    public static String encryptByPublicKey(String content) throws Exception {
        // 获取公钥
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, publicKey);
        byte[] cipherText = cipher.doFinal(content.getBytes());
        String cipherStr = base64Encoder.encode(cipherText);
        return cipherStr;
    }

    // 私钥加密
    public static String encryptByPrivateKey(String content) throws Exception {
        // 获取私钥
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.ENCRYPT_MODE, privateKey);
        byte[] cipherText = cipher.doFinal(content.getBytes());
        String cipherStr = base64Encoder.encode(cipherText);
        return cipherStr;
    }

    public static String encryptByPrivateKey(String content, String paramsPrivateKey){
        // 获取私钥
        String cipherStr = null;
        try {
            PrivateKey privateKey = getPrivateKey(paramsPrivateKey);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            byte[] cipherText = cipher.doFinal(content.getBytes());
            cipherStr = base64Encoder.encode(cipherText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherStr;
    }

    // 私钥解密
    public static String decryptByPrivateKey(String content) throws Exception {
        // 获取私钥
        PrivateKey privateKey = getPrivateKey(privateKeyStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, privateKey);
        byte[] cipherText = base64Decoder.decodeBuffer(content);
        byte[] decryptText = cipher.doFinal(cipherText);
        return new String(decryptText);
    }

    // 公钥解密
    public static String decryptByPublicKey(String content) throws Exception {
        // 获取公钥
        PublicKey publicKey = getPublicKey(publicKeyStr);
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(Cipher.DECRYPT_MODE, publicKey);
        byte[] cipherText = base64Decoder.decodeBuffer(content);
        byte[] decryptText = cipher.doFinal(cipherText);
        return new String(decryptText);
    }

    // 公钥解密
    public static String decryptByPublicKey(String content, String paramsPublicKeyy) {
        // 获取公钥
        String res = null;
        try {
            PublicKey publicKey = getPublicKey(paramsPublicKeyy);
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.DECRYPT_MODE, publicKey);
            byte[] cipherText = base64Decoder.decodeBuffer(content);
            byte[] decryptText = cipher.doFinal(cipherText);
            res = new String(decryptText);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }

    public static void main(String[] args) throws Exception {
        /*System.out.println("初始数据："+data);
        // 公钥加密
        String encryptedBytes = encryptByPublicKey(data);
        System.out.println("公钥加密后：" + encryptedBytes);
        // 私钥解密
        String decryptedBytes = decryptByPrivateKey(encryptedBytes);
        System.out.println("私钥解密后：" + decryptedBytes);
        // 私钥加密
        String encryptedBytes2 = encryptByPrivateKey(data);
        System.out.println("私钥加密后：" + encryptedBytes2);
        // 公钥解密
        String decryptedBytes2 = decryptByPublicKey(encryptedBytes2);
        System.out.println("公钥解密后：" + decryptedBytes2);*/
        String fileHash = "d7c030d60cf83e6407514e79c31270aea2b276b310e09a9b6132814c29ebb396";
        String fileHashDecry = RSAUtil.encryptByPrivateKey(fileHash,"MIICdgIBADANBgkqhkiG9w0BAQEFAASCAmAwggJcAgEAAoGBAIqZzOPp9ukHgFCse75cjl73ePIoS1DIA49SWRva1i+hRqiTQcvSs1O7I3vO0ExbWco/BL+BMtBSrmeoHd0v+GCzTmNSQ3ZKV2rm/c1idHEFs8dsBv7M/uUTiujtWpsz9fTA3ytXpLUcTxghap4QqpH0IuPlWHWXq2SDqT3Dd8itAgMBAAECgYEAioQk8Y5Mth1KDtYioV/s5Vob5J+CjcwnSqA48A6nylg6vpTUx/feQosfbkeFgxo0xVxvyVg8yVgodu3cUsbzSW698iqS4m9Es9yVDzF1OnRB5aO/jH2yygOOMuWsJBwTcs5aTvm/8HbjTuYqzGkueSdJwNpNKzbt2kw/MqveZd0CQQDRAlHHEV6tabKNDajmr27rrHZWCw2YZMbotovPjJoZWzmJtXkZqmevIOW9OtQJ3rbtmz9EkvHUXCZCxQqmqsV3AkEAqcMTxAOyiWCauSO5kPbQxwdSMIHLW5bG8jKGAinBTKrMyAmnCDYyVjntZDMX87APdMGHTj/r5RNR5cjTBhh7+wJAMGYhGb/j9LQtLIylOAYX4WsmW/ZjR47OoMTky0L7zDNbt4/WYyMar2smyBL/VunD4nG4zOBaL5rHIV8PgFi7MQJAFZEpA7AMkJNrVLYLLzWMFJ0DfhIfScWABOkSSVgQrLLOTWOcitiH0JiPZjlvg5AMhqLHk9NfHr0znGPFG3Mk3QJAfHTfnypqXY0J8TmM58jd586HzUlc9OdeboqgzmCjN6R++mCKpHYiTeKMK6SRoR8e7ELKxjZVRFtHJsTC2/i+5A==");
        System.out.println(decryptByPublicKey(fileHashDecry,"MIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQCKmczj6fbpB4BQrHu+XI5e93jyKEtQyAOPUlkb2tYvoUaok0HL0rNTuyN7ztBMW1nKPwS/gTLQUq5nqB3dL/hgs05jUkN2Sldq5v3NYnRxBbPHbAb+zP7lE4ro7VqbM/X0wN8rV6S1HE8YIWqeEKqR9CLj5Vh1l6tkg6k9w3fIrQIDAQAB"));
    }
}

