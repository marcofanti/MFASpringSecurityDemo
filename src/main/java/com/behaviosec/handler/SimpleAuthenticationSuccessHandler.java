package com.behaviosec.handler;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Iterator;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.DefaultRedirectStrategy;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.behaviosec.config.BehavioSecException;
import com.behaviosec.config.Constants;
import com.behaviosec.entities.ReportRequest;
import com.behaviosec.entities.Response;
import com.behaviosec.model.User;
import com.behaviosec.service.UserService;
import com.behaviosec.utils.Helper;

import lombok.NonNull;

@Component
public class SimpleAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
	@Value( "${behaviosec.behaviosecurl}" )
	@NonNull public String behaviosecurl;
	@Value( "${behaviosec.tenantId}" )
	public String tenantId;

	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	private static final String TAG = SimpleAuthenticationSuccessHandler.class.getName();
    private final Logger log = LoggerFactory.getLogger(TAG);
    
    @Autowired
    private UserService userService;


	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {

		callGetResponse(request, null, behaviosecurl, tenantId, userService, log);

		String redirection = "/home/homepage";

		/*
	
		Iterator <String> itr = request.getAttributeNames().asIterator();

		itr = request.getHeaderNames().asIterator();

		String userAgent = "";

		while (itr.hasNext()) {
			Object header = itr.next();
			String headerValue = request.getHeader(header.toString());
			System.out.println(header + " " + headerValue);
			if (header.equals("user-agent")) {
				userAgent = headerValue;
			} else {
        		userAgent = request.getHeader("User-Agent");
        	}
		}
		
		itr = request.getParameterNames().asIterator();
		
		String timingData = "";
		String userName = "";
		String clientIp = Helper.getClientIpAddress(request);
		
		while (itr.hasNext()) {
			Object parameters = itr.next();
			if (parameters.equals("bdata") || parameters.equals("other")) {
				timingData = request.getParameter(parameters.toString());
			} else if (parameters.equals("username")) {
				userName = request.getParameter(parameters.toString());
			} 
			log.info(parameters + " " + request.getParameter(parameters.toString()));
		}
		
        if (timingData != null && timingData.trim().length() > 0) {
        	userAgent = request.getHeader("User-Agent");

			Response r = getResponse(behaviosecurl, tenantId, 
					clientIp, userAgent, userName, timingData, log);
			
	        User user = userService.findUserByUsername(userName);
	        
	        if(r != null && r.hasReport()){
	        	String report = r.getReponseString();
	        	userService.updateUser(user, report);
	        }
        }
		*/
		
		try {
			log.info("Redirection " + redirection);		
			redirectStrategy.sendRedirect(request, response, redirection);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
	
	public static Response callGetResponse(HttpServletRequest request, String userName, String behaviosecurl, String tenantId, 
			UserService userService, Logger log) {
		Iterator <String> itr = request.getAttributeNames().asIterator();

		itr = request.getHeaderNames().asIterator();

		String userAgent = "";

		while (itr.hasNext()) {
			Object header = itr.next();
			String headerValue = request.getHeader(header.toString());
			System.out.println(header + " " + headerValue);
			if (header.equals("user-agent")) {
				userAgent = headerValue;
			} else {
        		userAgent = request.getHeader("User-Agent");
        	}
		}
		
		itr = request.getParameterNames().asIterator();
		
		String timingData = "";
		String clientIp = Helper.getClientIpAddress(request);
		
		while (itr.hasNext()) {
			Object parameters = itr.next();
			if (parameters.equals("bdata") || parameters.equals("other")) {
				timingData = request.getParameter(parameters.toString());
			} else if (parameters.equals("username")) {
				userName = request.getParameter(parameters.toString());
			} 
			log.info(parameters + " " + request.getParameter(parameters.toString()));
		}
		
        if (timingData != null && timingData.trim().length() > 0) {
        	userAgent = request.getHeader("User-Agent");

			Response r = getResponse(behaviosecurl, tenantId, 
					clientIp, userAgent, userName, timingData, log);
			
			if (userService != null) {
		        User user = userService.findUserByUsername(userName);
		        
		        if(r != null && r.hasReport()){
		        	String report = r.getReponseString();
		        	userService.updateUser(user, report);
		        }
			}
			return r;
        }
        return null;
	}
	
	
	public static Response getResponse(String url, String tenantID, String clientIp, String agent, String userName, String bdata, Logger log) {
        Response r = null;
		com.behaviosec.client.ClientConfiguration clientConfig =
                new com.behaviosec.client.ClientConfiguration(url, tenantID);
        com.behaviosec.client.Client client = new com.behaviosec.client.Client(clientConfig);
        ReportRequest reportRequest = new ReportRequest();
        reportRequest.setUserIp(clientIp);
        reportRequest.setSessionId(UUID.randomUUID().toString());
        reportRequest.setUserAgent(agent);
        reportRequest.setUsername(userName);
        reportRequest.setTimingData(bdata);
        reportRequest.setOperatorFlags(Constants.FINALIZE_DIRECTLY);
        try {
    	    log.info("Calling checkdata " + userName + " " + agent + " " + bdata.substring(0, 20) + "...");
            r = client.getReport(reportRequest);
        } catch (BehavioSecException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r;
	}

    public void addToDB(String dbURL, String dbUser, String dbPassword, String report, String user) {
    	Connection con = null;
    	dbURL = dbURL + "&user=" + dbUser + "&password=" + dbPassword;
    	PreparedStatement stmt = null;
		try {
			log.debug("Before connection");
			con = DriverManager.getConnection(dbURL);
			log.info("Before PreparedStatement");
			stmt = con.prepareStatement(
					"update behaviosecproxyless.user set other=? where username=?");
			stmt.setString(2, user);
			stmt.setString(1, report);
			stmt.execute();
			log.info("Updated user " + user + " " + report);
			con.close();
		} catch (Exception e) {
			log.error(e.toString());
		} finally {
			if (con != null) {
				try {
					con.close();
				} catch (SQLException sqlEx) { } // ignore

				con = null;
			}
			if (stmt != null) {
				try {
					stmt.close();
				} catch (SQLException sqlEx) { } // ignore
				stmt = null;
			}
		}
    }
    

}