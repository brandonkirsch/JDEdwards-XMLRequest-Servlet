package com.hennypenny.jde.bridge;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.jdedwards.system.xml.XMLRequest;


/**
 * Servlet implementation class JDEBridge
 */
public class JDEBridge extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public JDEBridge() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		
		
		ServletOutputStream output = response.getOutputStream();
		
		response.setContentType("text/html");
		
		output.println("<form method=\"post\">");
		output.println("server <input name=\"server\"><br>");
		output.println("port <input name=\"port\"><br>");
		output.println("xml <textarea name=\"xml\"></textarea><br>");
		output.println("<input type=\"submit\"></form>");
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		ServletOutputStream output = response.getOutputStream();
		
		
		String server = request.getParameter("server");
		String xml = request.getParameter("xml");
		String requestedPort = request.getParameter("port");
		
		Integer port = 6016;
		
		
		Boolean errorExists = false;
		
		if(server == null){
			errorExists = true;
			output.println("Error: server not specified");
		}
		
		if(xml == null){
			errorExists = true;
			output.println("Error: xml not specified");
		}
		
		if(requestedPort != null){
			try{
				port = new Integer(requestedPort);
			}
			catch(Exception e){
				errorExists = true;
				output.println("Error: Invalid port specified");
			}
		}
		
		if(errorExists == false){
			com.jdedwards.system.xml.XMLRequest jdeXmlRequest;
			
			try{
				jdeXmlRequest = new com.jdedwards.system.xml.XMLRequest(server, port, xml);
			}
			catch(Exception e){
				output.print("An exception occurred initializing the JDE XMLRequest: ");
				output.println(e.getMessage());
				return;
			}
			
			try{
				String XMLResponse = jdeXmlRequest.execute();
				
				response.setContentType("text/xml");
				
				output.print(XMLResponse);
			}
			catch(Exception e){
				output.print("An exception occured with the XML Request: ");
				output.println(e.getMessage());
			}
		}
	}

}
