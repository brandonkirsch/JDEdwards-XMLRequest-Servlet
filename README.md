JDEdwards-XMLRequest-Servlet
============================

This J2EE Servlet exposes the JDEdwards OneWorld & EnterpriseOne XML Interop API via HTTP POST.

INFORMATION

A very simple servlet wraps the JDE XMLRequest object to expose basic functionality via HTTP. This enables cross-platform, cross-language development for the XML Interop API.

The servlet accepts HTTP POST with the following three request variables:
	"server" : The DNS Name (or IP Address) for the JDE XML Interop Server (commonly a JDE application or batch server)
	"xml" : A fully-formed XML Interop jdeRequest document.
	"port" : (Optional) The network port to use for JDE XML Interop services. (Defaults to 6016 for JDE 9.1)

It then invokes the JDE XMLRequest object with the provided parameters, executes the XMLRequest and sends the entire XML jdeResponse back to the HTTP client.


SECURITY NOTICE

*VERY IMPORTANT* This servlet does not provide any amount of network security on its own. It will accept and execute any XML Request document that it receives. 

Please make sure that your Tomcat or J2EE installation is properly secured so that way it only accepts HTTP requests from trusted clients.

Often, you will want to limit Tomcat to only accept requests from localhost and/or your application server. This can prevent a malicious attacker from sending XML Requests to your JDE XML Interop server.


INSTALLATION
1. Copy the servlet/jdebridge.war to your Tomcat deployment folder 
	- For OpenSUSE: /srv/tomcat6/webapps

	- Tomcat will automatically deploy the subfolder "jdebridge"


2. Copy the required JDE Connector JARs to the new path jdebridge/WEB-INF/lib
	- OneWorld XE Requirements: Connector.jar and Kernel.jar

	- EnterpriseOne 9.1 Requirements: ManagementAgent_JAR.jar, EventProcessor_EJB.jar, JdeNet_JAR.jar, xmlbeans-2.3.0.jar, commons-logging.jar, System_JAR.jar, jmxri.jar, Base_JAR.jar, Connector.jar, commons-httpclient-3.0.jar

	- Other versions of JDE Enterprise One (8.10, 8.11, 8.12, 9.0) have requirements similar to E1 9.1

	- These files can be obtained from your JDEdwards deployment server, in the path /JDEdwards/E910_1/SYSTEM/Classes/

3. Open the web browser and browse to your Tomcat/J2EE host.  
	- The default URL for the servlet is /jdebridge/JDEBridge
	- "GET" Requests sent from your web browser will return an HTML <form> fragment for submitting a request.
	- Populate the server & port fields with the correct information.




EXAMPLES

"GetTemplate" jdeRequestDocument - Returns jdeRequest XML template for the function named F3002BeginDocument

<?xml version="1.0" encoding="UTF-8" ?>
<jdeRequest user="JDEUSER" pwd="JDEPASS" role="*ALL" environment="JDEENV" type="callmethod">
<callMethodTemplate name='F3002BeginDocument' app='XMLInterop' />
</jdeRequest>


Using the servlet with PHP fsockopen():

	<?php
	
	// You must provide valid $tomcatHost, $appServer, $port and $xml values
	
	$post = http_build_query(array('server' => $appServer, 'port' => $port, 'xml' => $xml));
	
	fwrite($httpSocket, "POST /jdebridge/JDEBridge HTTP/1.1\r\n");
	
	fwrite($httpSocket, "Host: {$tomcatHost}\r\n");
	
	fwrite($httpSocket, "Accept: text/html,application/xhtml+xml,application/xml;q=0.9,*/*;q=0.8\r\n");
	
	fwrite($httpSocket, "Accept-Encoding: \r\n");
	
	fwrite($httpSocket, "Content-Type: application/x-www-form-urlencoded\r\n");
	
	fwrite($httpSocket, "Content-Length: ".strlen($post)."\r\n");
	
	fwrite($httpSocket, "Connection: Close\r\n");
	
	fwrite($httpSocket, "\r\n");
			
	fwrite($httpSocket, $post);
	
	$response = '';
	
	while( $line = fgets($httpSocket, 4096) ){
	
		$response .= $line;
		
	}
	
	$response = substr($response, strpos($response,"\r\n\r\n") + 4); // strip HTTP headers.
