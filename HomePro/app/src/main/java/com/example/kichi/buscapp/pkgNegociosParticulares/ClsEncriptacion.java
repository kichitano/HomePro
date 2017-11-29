package com.example.kichi.buscapp.pkgNegociosParticulares;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 * Created by Kichi on 29/08/2017.
 */

public class ClsEncriptacion {
    public static String MD5(String input){
        try{
            MessageDigest md = MessageDigest.getInstance("MD5");
            byte[] messageDigest = md.digest(input.getBytes());
            BigInteger number = new BigInteger(1, messageDigest);
            String hashtext = number.toString(16);
            while (hashtext.length() < 32){
                hashtext = "0" + hashtext;
            }
            return hashtext;
        }catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}
