/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.arep_lab07;

import java.math.BigInteger;
import java.security.MessageDigest;

/**
 *
 * @author jose.correa-r
 */
public class SHA2 {
    public static void main(String[] args) throws Exception
    {
        String password = "SHA-256";
 
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] digest = md.digest(password.getBytes(StandardCharsets.UTF_8));
        String sha256 = DatatypeConverter.printHexBinary(digest).toLowerCase();
 
        // imprimir resumen de mensaje SHA-256
        System.out.println(sha256);
    }
}
