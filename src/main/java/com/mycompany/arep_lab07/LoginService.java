package com.mycompany.arep_lab07;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static spark.Spark.*;

public class LoginService {
    public static void main(String[] args) throws NoSuchAlgorithmException, KeyStoreException, IOException, KeyManagementException, CertificateException {
        staticFiles.location("/public");
        port(getPort());
        secure("certificados/ecikeystore.p12", "123456", null, null);
        configureTrustedSSLContext();
        get("/login", (req, res) -> {
            res.type("application/json");
            return readURL("name=" + req.queryParams("name") + "&password=" + req.queryParams("password"));
        });
    }

    public static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 8087; //returns default port if heroku-port isn't set (i.e. on localhost)
    }

    private static void configureTrustedSSLContext() throws KeyStoreException, IOException, NoSuchAlgorithmException, KeyManagementException, CertificateException {
        File trustStoreFile = new File("certificados/myTrustStore.p12");
        char[] trustStorePassword = "123456".toCharArray();
        KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
        trustStore.load(new FileInputStream(trustStoreFile), trustStorePassword);
        TrustManagerFactory tmf = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
        tmf.init(trustStore);
        for (TrustManager t : tmf.getTrustManagers()) System.out.println(t);
        SSLContext sslContext = SSLContext.getInstance("TLS");
        sslContext.init(null, tmf.getTrustManagers(), null);
        SSLContext.setDefault(sslContext);
    }

    public static String readURL(String query) throws IOException {
        URL siteURL = new URL("https://localhost:8088/user?" + query);
        URLConnection urlConnection = siteURL.openConnection();
        Map<String, List<String>> headers = urlConnection.getHeaderFields();
        Set<Map.Entry<String, List<String>>> entrySet = headers.entrySet();
        for (Map.Entry<String, List<String>> entry : entrySet) {
            String headerName = entry.getKey();
            if (headerName != null) System.out.print(headerName + ":");
            List<String> headerValues = entry.getValue();
            for (String value : headerValues) System.out.print(value);
            System.out.println("");
        }
        System.out.println("-------message-body------");
        StringBuffer response = new StringBuffer();
        BufferedReader reader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
        String inputLine;
        while ((inputLine = reader.readLine()) != null) response.append(inputLine);
        reader.close();
        System.out.println(response);
        System.out.println("GET DONE");
        return response.toString();
    }

    
}
