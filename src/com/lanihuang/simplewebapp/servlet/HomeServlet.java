package com.lanihuang.simplewebapp.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.openqa.selenium.chrome.ChromeOptions;
@WebServlet(urlPatterns = { "/home"})
public class HomeServlet extends HttpServlet {
  private static final long serialVersionUID = 1L;
 
  public HomeServlet() {
    super();
  }

  @Override
  protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException 
  {
	  ChromeOptions chromeOptions = new ChromeOptions();	   	  
	  
	  /*
	  String path = getServletContext().getRealPath("/");
	  
	  System.out.println("*************************");
	  System.out.println("*************************");
	  
	  File file = new File(path + "Alexander.json");

	  if(!file.exists())
	  {
		  PrintWriter writer = new PrintWriter(file);
		  writer.println("{\"installed\":{\"client_id\":\"938131041382-3atkcnckvcshks4c6afkuhqe1i6vmcup.apps.googleusercontent.com\",\"project_id\":\"scrappy-dummy193-1536707684089\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://www.googleapis.com/oauth2/v3/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"WzQ3aSAVSbkWdDvQ9mTwN75-\",\"redirect_uris\":[\"urn:ietf:wg:oauth:2.0:oob\",\"http://localhost\"]}}");
		  //writer.println("{\"web\":{\"client_id\":\"938131041382-81q46dhqm7hjlkmkcfhfiu3v24k6o9e2.apps.googleusercontent.com\",\"project_id\":\"scrappy-dummy193-1536707684089\",\"auth_uri\":\"https://accounts.google.com/o/oauth2/auth\",\"token_uri\":\"https://www.googleapis.com/oauth2/v3/token\",\"auth_provider_x509_cert_url\":\"https://www.googleapis.com/oauth2/v1/certs\",\"client_secret\":\"5GVl2So6ROdzv4VSrKp-5vtS\",\"redirect_uris\":[\"http://localhost:8080/Trainerize\",\"http://september19.herokuapp.com\",\"http://localhost/Trainerize\"],\"javascript_origins\":[\"http://localhost\",\"https://september19.herokuapp.com\"]}}");
		  writer.close ();
	  }
	  
	  System.out.println("Alexander.json Path-Path 		: " + file.getPath());
	  System.out.println("Alexander.json Path-Absolute 	: " + file.getAbsolutePath());
	  
	  System.out.println("*************************");
	  System.out.println("*************************");	  
	  
	  */
	  
	  
	  
	  RequestDispatcher dispatcher = this.getServletContext().getRequestDispatcher("/WEB-INF/views/homeView.jsp");
	  dispatcher.forward(request, response);
  }

  @Override
  protected void doPost(HttpServletRequest request, HttpServletResponse response)
  throws ServletException, IOException 
  {
    doGet(request, response);
  }

}