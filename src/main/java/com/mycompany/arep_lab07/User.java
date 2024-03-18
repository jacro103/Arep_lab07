package com.mycompany.arep_lab07;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.HashMap;

import static spark.Spark.*;

public class User {
    private static final HashMap<String, byte[]> users = new HashMap<>();

    public static void main(String[] args) throws NoSuchAlgorithmException {
        addUser("Jose", "123456");
        addUser("Alejandro", "654321");
        port(getPort());
        secure("certificados/ecikeystore.p12", "123456", null, null);
        get("/user", (req, res) -> {
            res.type("application/json");
            boolean result = verifyPassword(req.queryParams("name"), req.queryParams("password"));
            return "{\"result\":" + result + "}";
        });
    }

    public static byte[] hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        byte[] bytes = password.getBytes();
        return md.digest(bytes);
    }

    public static boolean verifyPassword(String userName, String password) throws NoSuchAlgorithmException {
        byte[] hash = users.get(userName);
        byte[] attemptedHash = hashPassword(password);
        return Arrays.equals(hash, attemptedHash);
    }

    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 8088; 
    }

    public static void addUser(String name, String password) throws NoSuchAlgorithmException {
        users.put(name, hashPassword(password));
    }
}
