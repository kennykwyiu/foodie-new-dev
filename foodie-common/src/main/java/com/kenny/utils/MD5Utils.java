package com.kenny.utils;

import org.apache.commons.codec.binary.Base64;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Utils {
    public static String getMD5Str(String  strValue) throws NoSuchAlgorithmException {
        MessageDigest md5 = MessageDigest.getInstance("MD5");
        byte[] binaryData = md5.digest(strValue.getBytes());
        return Base64.encodeBase64String(binaryData);
    }

    public static void main(String[] args) {
        try {
            String md5Str = getMD5Str("Kenny");
            System.out.println(md5Str);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }
}
