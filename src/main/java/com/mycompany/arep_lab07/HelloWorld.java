/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.arep_lab07;

import static spark.Spark.get;
import static spark.Spark.port;
import static spark.Spark.secure;

/**
 *
 * @author jose.correa-r
 */

public class HelloWorld {
    public static void main(String[] args) {
        secure("certificados/ecikeystore.p12", "123456", null, null); 
        port(getPort()); 
        get("/hello", (req, res) -> "Hello World");
    }
  
    static int getPort() { 
        if (System.getenv("PORT") != null) { 
            return Integer.parseInt(System.getenv("PORT")); 
        } 
        return 5000; //returns default port if heroku-port isn't set (i.e. on localhost) 
    }
}