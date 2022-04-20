package com.behaviosec.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

public class ForgeRockUtils {
	private Log log = LogFactory.getLog(this.getClass());
	
	public String doLogin(String userName, String password, String forgerockurl) {
		if (forgerockurl == null) {
			forgerockurl = "http://openam2.example.com:8080/";
		}
		String loginUrl = forgerockurl + "openam/json/realms/root/authenticate";

        String cookie = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(loginUrl);

        httpPost.setHeader("content-type", "application/json");
        httpPost.setHeader("X-OpenAM-Username", userName);
        httpPost.setHeader("X-OpenAM-Password", password);
        httpPost.setHeader("Accept-API-Version", "resource=2.0, protocol=1.0");

        List<NameValuePair> paramList = new ArrayList<NameValuePair>();

        try {
        	String responseStr = null;
            httpPost.setEntity(new UrlEncodedFormEntity(paramList));
            HttpResponse httpResponse = client.execute(httpPost);
            Iterator headerIterator = httpResponse.headerIterator();
            
            while (headerIterator.hasNext()) {
            	String header = "" + headerIterator.next();
            	log.debug("header " + header);
            	int ind = header.indexOf("iPlanetDirectoryPro");
            	if (ind > 0) {
            		header = header.substring(ind + "iPlanetDirectoryPro=".length());
            		ind = header.indexOf(";");
            		cookie = header.substring(0, ind);
            	}
            }
            HttpEntity responseEntity = httpResponse.getEntity();
            if(responseEntity!=null) {
            	responseStr = EntityUtils.toString(responseEntity);	
            }
            log.debug("Response " + responseStr);
            int ind = responseStr.indexOf("tokenId");
            responseStr = responseStr.substring(ind + "tokenId\":\"".length());
            ind = responseStr.indexOf("\"");
            String tokenId = responseStr.substring(0, ind);
            log.debug("TokenId = " + tokenId);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cookie;
    }

	public String getSessionInfo(String tokenId, String userTokenId, String forgerockurl) {
		if (forgerockurl == null) {
			forgerockurl = "http://openam2.example.com:8080/";
		}
		String sessionUrl = forgerockurl + "openam/json/realms/root/sessions/?_action=getSessionInfo";
		
        String userName = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(sessionUrl);

        httpPost.setHeader("content-type", "application/json");
        httpPost.setHeader("iplanetDirectoryPro", tokenId);
        httpPost.setHeader("Accept-API-Version", "resource=3.1, protocol=1.0");

        String json = "{ \"tokenId\": \"" + userTokenId + "\" }";
        StringEntity stringEntity = new StringEntity(json, "utf-8");

        try {
        	String responseStr = null;
            httpPost.setEntity(stringEntity);
            HttpResponse httpResponse = client.execute(httpPost);
            Iterator headerIterator = httpResponse.headerIterator();
            
            while (headerIterator.hasNext()) {
            	String header = "" + headerIterator.next();
            	log.debug("header " + header);
            }
            HttpEntity responseEntity = httpResponse.getEntity();
            if(responseEntity!=null) {
            	responseStr = EntityUtils.toString(responseEntity);	
            }
            log.debug("Response " + responseStr);
            int ind = responseStr.indexOf("username\":\"");
            responseStr = responseStr.substring(ind + "username\":\"".length());
            ind = responseStr.indexOf("\"");
            userName = responseStr.substring(0, ind);
            log.debug("userName = " + userName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return userName;
    }
	
	public String doLogout(String tokenId, String forgerockurl) {
		String logoutUrl = forgerockurl + "openam/json/realms/root/sessions?_action=logout";
		
        String userName = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(logoutUrl);

        httpPost.setHeader("content-type", "application/json");
        httpPost.setHeader("Cache-Control", "no-cache");
        httpPost.setHeader("iplanetDirectoryPro", tokenId);
        httpPost.setHeader("Accept-API-Version", "resource=3.1, protocol=1.0");

        try {
        	String responseStr = null;
            HttpResponse httpResponse = client.execute(httpPost);
            Iterator headerIterator = httpResponse.headerIterator();
            
            while (headerIterator.hasNext()) {
            	String header = "" + headerIterator.next();
            	log.debug("header " + header);
            }
            HttpEntity responseEntity = httpResponse.getEntity();
            if(responseEntity!=null) {
            	responseStr = EntityUtils.toString(responseEntity);	
            }
            log.debug("Response " + responseStr);
            int ind = responseStr.indexOf("username\":\"");
            responseStr = responseStr.substring(ind + "username\":\"".length());
            ind = responseStr.indexOf("\"");
            userName = responseStr.substring(0, ind);
            log.debug("userName = " + userName);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return userName;
    }
	

    public String doStandaloneLogin(String userName, String password, String standaloneURL) {
    	
    	String samlUrl = standaloneURL + "login";
        String cookie = null;
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost httpPost = new HttpPost(samlUrl);

        httpPost.setHeader("origin", "https://http://ec2-54-183-136-168.us-west-1.compute.amazonaws.com:9031/");
        httpPost.setHeader("host", "ec2-54-183-136-168.us-west-1.compute.amazonaws.com");
        httpPost.setHeader("accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3");
        httpPost.setHeader("content-type", "application/x-www-form-urlencoded");
        httpPost.setHeader("user-agent", "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_14_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/76.0.3809.132 Safari/537.36");
        httpPost.setHeader("accept-encoding", "gzip, deflate");
        httpPost.setHeader("accept-language", "en-US,en;q=0.9");

        List<NameValuePair> paramList = new ArrayList<NameValuePair>();

        paramList.add(new BasicNameValuePair("username", userName));
        paramList.add(new BasicNameValuePair("password", password));

        try {
            httpPost.setEntity(new UrlEncodedFormEntity(paramList));
            CloseableHttpResponse response1 = client.execute(httpPost);
            String responseStr  = response1.toString();
            System.out.println("Response " + responseStr);
            int ind = responseStr.indexOf("JSESSIONID");
            responseStr = responseStr.substring(ind + "JSESSIONID=".length());
            ind = responseStr.indexOf(";");
            cookie = responseStr.substring(0, ind);
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return cookie;
    }
}
