package com.betfair.aping.api;


import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import com.betfair.aping.exceptions.APINGException;
import com.betfair.aping.util.RestResponseHandler;

@Component
public class HttpUtil {

	private final String HTTP_HEADER_X_APPLICATION = "X-Application";
    private final String HTTP_HEADER_X_AUTHENTICATION = "X-Authentication";
    private final String HTTP_HEADER_CONTENT_TYPE = "Content-Type";
    private final String HTTP_HEADER_ACCEPT = "Accept";
    private final String HTTP_HEADER_ACCEPT_CHARSET = "Accept-Charset";
    
    @Autowired
    private BetfairConfig betfairConfig;

    public HttpUtil() {
        super();
    }

    private String sendPostRequest(String param, String operation, String appKey, String ssoToken, String URL, RestResponseHandler reqHandler){
        String jsonRequest = param;

        HttpPost post = new HttpPost(URL);
        String resp = null;
        try {
            post.setHeader(HTTP_HEADER_CONTENT_TYPE, betfairConfig.getContentType());
            post.setHeader(HTTP_HEADER_ACCEPT, betfairConfig.getContentType());
            post.setHeader(HTTP_HEADER_ACCEPT_CHARSET, betfairConfig.getEncoding());
            post.setHeader(HTTP_HEADER_X_APPLICATION, appKey);
            post.setHeader(HTTP_HEADER_X_AUTHENTICATION, ssoToken);

            post.setEntity(new StringEntity(jsonRequest, betfairConfig.getEncoding()));

            HttpClient httpClient = new DefaultHttpClient();

            HttpParams httpParams = httpClient.getParams();
            HttpConnectionParams.setConnectionTimeout(httpParams, new Integer(betfairConfig.getTimeOut()));
            HttpConnectionParams.setSoTimeout(httpParams, new Integer(betfairConfig.getTimeOut()));

            resp = httpClient.execute(post, reqHandler);

        } catch (UnsupportedEncodingException e1) {
        	e1.printStackTrace(System.out);

        } catch (ClientProtocolException e) {
        	e.printStackTrace(System.out);

        } catch (IOException ioE){
        	ioE.printStackTrace(System.out);

	    } catch (Throwable e){
	    	e.printStackTrace(System.out);
	
	    }


        return resp;

    }

    public String sendPostRequestRescript(String param, String operation, String appKey, String ssoToken) throws APINGException{
        return  sendPostRequest(param, operation, appKey, ssoToken, betfairConfig.getApiUrl() + operation + "/", new RestResponseHandler());
    }
}
