package com.ruoyi.travel.util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author anzepeng
 * @description TODO
 * @date 2023/5/18 9:23 上午
 */
public class SHA256Util {

    public static String digest(byte[] req){
        StringBuilder res = new StringBuilder();
        MessageDigest object = null;
        try {
            object = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] encrypted = object.digest(req);
        for (byte b : encrypted) {
            res.append(String.format("%02x", b));
        }
        return res.toString();
    }
}
