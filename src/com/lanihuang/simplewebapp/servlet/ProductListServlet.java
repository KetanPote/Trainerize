package com.lanihuang.simplewebapp.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.security.SecureRandom;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.riversun.oauth2.google.OAuthSession;

import com.fasterxml.jackson.annotation.JsonValue;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import com.lanihuang.simplewebapp.beans.Product;
import com.lanihuang.simplewebapp.utils.DBUtils;
import com.lanihuang.simplewebapp.utils.MyUtils;
import com.lanihuang.simplewebapp.controller.Controller;

@WebServlet(urlPatterns = { "/productList" })
public class ProductListServlet extends HttpServlet 
{
  private static final long serialVersionUID = 1L;

  public ProductListServlet() 
  {
    super();
  }

  private GoogleAuthorizationCodeFlow flow;
  
  @Override  
  protected void doGet(HttpServletRequest request, HttpServletResponse response)throws ServletException, IOException 
  {
	  Controller controller = new Controller();
	 String Out[]= new String[2];
	 
	 try 
	  {		
		  ServletContext  SC = getServletContext();
		  
		  controller.scrapWrite(SC,Out);
	  }
	  catch(Exception E)
	  {
		  E.printStackTrace();
	  }
	
    Connection conn = MyUtils.getStoredConnection(request);

    String errorString = null;
    List<Product> list = null;
    try 
    {
      list = DBUtils.queryProduct(conn);
    } catch (SQLException e) {
      e.printStackTrace();
      errorString = e.getMessage();
    }

    // Store info in request attribute, before forward to views
    request.setAttribute("errorString", errorString);
    request.setAttribute("productList", list);
  
    System.out.println("Excuted . . . !");

    response.setContentType("application/json"); 
    PrintWriter out = response.getWriter(); 
    JSONObject obj = new JSONObject();
    obj.put("productList", list);
    out.println(obj.toString());
    
    request.setAttribute("Status", "Yes");
   request.setAttribute("productList", obj.toString());
      
	  
    // Forward to /WEB-INF/views/productListView.jsp
    RequestDispatcher dispatcher = request.getServletContext().getRequestDispatcher("/WEB-INF/views/productListView.jsp");
    dispatcher.forward(request, response);
    
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException {
    doGet(request, response);
  }

}