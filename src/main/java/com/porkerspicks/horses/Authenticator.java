package com.porkerspicks.horses;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ClientConnectionManager;
import org.apache.http.conn.scheme.Scheme;
import org.apache.http.conn.ssl.SSLSocketFactory;
import org.apache.http.conn.ssl.StrictHostnameVerifier;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.betfair.aping.api.BetfairConfig;
import com.betfair.aping.entities.EventTypeResult;
import com.betfair.aping.util.JsonConverter;
import com.google.gson.reflect.TypeToken;
import com.porkerspicks.horses.domain.Session;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;

@Component
public class Authenticator {
	 
    private static int PORT = 443;

	@Autowired
	private BetfairConfig betfairConfig;
	
    public Session login() throws Exception {
 
        DefaultHttpClient httpClient = new DefaultHttpClient();
        Session session = null;
 
        try {
            SSLContext ctx = SSLContext.getInstance("TLS");
            KeyManager[] keyManagers = getKeyManagers("pkcs12", new FileInputStream(new File("C:\\dev\\OpenSSL-Win64\\bin\\client-2048.p12")), "norrie");
            ctx.init(keyManagers, null, new SecureRandom());
            SSLSocketFactory factory = new SSLSocketFactory(ctx, new StrictHostnameVerifier());
 
            ClientConnectionManager manager = httpClient.getConnectionManager();
            manager.getSchemeRegistry().register(new Scheme("https", PORT, factory));
            HttpPost httpPost = new HttpPost("https://identitysso-cert.betfair.com/api/certlogin");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("username", "willparker"));
            nvps.add(new BasicNameValuePair("password", "14norrie"));
 
            httpPost.setEntity(new UrlEncodedFormEntity(nvps));
 
 
 
            httpPost.setHeader("X-Application",betfairConfig.getApplicationKey());
 
 
            System.out.println("executing request" + httpPost.getRequestLine());
            System.out.println("executing request" + httpPost.getEntity() );

            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            
 
            System.out.println("----------------------------------------");
            System.out.println(response.getStatusLine());
            if (entity != null) {
                String responseString = EntityUtils.toString(entity);
                //extract the session token from responsestring
                System.out.println("responseString" + responseString);
                session = JsonConverter.convertFromJson(responseString, new TypeToken<Session>() {}.getType());
            }
 
        } finally {
            httpClient.getConnectionManager().shutdown();
        }
        return session;
    }
 
 
 
    private KeyManager[] getKeyManagers(String keyStoreType, InputStream keyStoreFile, String keyStorePassword) throws Exception {
        KeyStore keyStore = KeyStore.getInstance(keyStoreType);
        keyStore.load(keyStoreFile, keyStorePassword.toCharArray());
        KeyManagerFactory kmf = KeyManagerFactory.getInstance(KeyManagerFactory.getDefaultAlgorithm());
        kmf.init(keyStore, keyStorePassword.toCharArray());
        return kmf.getKeyManagers();
    }
}

