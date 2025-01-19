package com.modsen.userstorageservice.utils;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class EncryptionUtil {

    public static String hashString(String message) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            System.err.println("Unable to get instance of SHA-256");
            e.printStackTrace();
        }
        byte[] resultByteArray = messageDigest.digest(message.getBytes());
        return (new BigInteger(1, resultByteArray)).toString(16);
    }
}
