package secproxy;

import java.io.IOException;
import java.util.List;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.server.Request;
import org.eclipse.jetty.server.handler.AbstractHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secproxy.WebSec;

public class Handler extends AbstractHandler 
{
	protected Logger log = LoggerFactory.getLogger(Handler.class);
	private List<WebSec> servers;
	private String secOneBaseUrl = "https://ws-one.rfalcao.com:8444/";
	private String secTwoBaseUrl = "https://ws-two.rfalcao.com:8445/";
	private String secOneName = "ws-one";
	private String secOnePort = "8444";
	private String secTwoName = "ws-two";
	private String secTwoPort = "8555";

	
	public Handler(List<WebSec> servers) 
	{
		this.servers = servers;
	}
	
	@Override
	public void handle(String target, Request baseRequest, HttpServletRequest request, HttpServletResponse response) throws IOException, ServletException 
	{
		String requestedServerName = request.getServerName();
		for (WebSec webSec : servers) 
		{
			String fullyQualifiedDomainName = webSec.getPath();
			if(requestedServerName.equals(fullyQualifiedDomainName))
			{
				try {
					
					verifyRedirect(request, response);

					ContentResponse contentResponse = webSec.getContentResponse(target, request);
					response.setContentType("text/html;charset=utf-8");
					response.setStatus(HttpServletResponse.SC_OK);
					baseRequest.setHandled(true);
					response.getOutputStream().write(contentResponse.getContent());
					return;
				} 
				catch (Exception exception) 
				{
					log.error("#### Error: " + exception.getMessage());
				} 
			}
		}
	}

	private void verifyRedirect(request, response){
		if(request.getRequestURL().toString().contains(secOneName) && && request.getRequestURL().toString().contains(secTwoPort)){
			response.sendRedirect(secOneBaseUrl);
			return;
		}

		if(request.getRequestURL().toString().contains(secTwoName) && request.getRequestURL().toString().contains(secOnePort)){
			response.sendRedirect(secTwoBaseUrl);
			return;
		}	
	}
}
