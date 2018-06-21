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
	private Config config;
	
	public Handler(List<WebSec> servers) 
	{
		config = new Config();
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
					//log.error("#### Error: " + exception.getMessage());
				} 
			}
		}
	}

	private void verifyRedirect(HttpServletRequest request, HttpServletResponse response)
	{
		try{
			if(request.getRequestURL().toString().contains(config.getSecOneName()) && 
					request.getRequestURL().toString().contains(Integer.toString(config.getSecTwoPort()))){
				log.error("#### Redirect to: " + config.getSecOneUrl());
				response.sendRedirect(config.getSecOneUrl());
			}

			if(request.getRequestURL().toString().contains(config.getSecTwoName()) && 
					request.getRequestURL().toString().contains(Integer.toString(config.getSecOnePort()))){
				log.error("#### Redirect to: " + config.getSecTwoUrl());
				response.sendRedirect(config.getSecTwoUrl());
			}
		} 
		catch (Exception exception) 
		{
			log.error("#### Error: " + exception.getMessage());
		} 
	}
}
