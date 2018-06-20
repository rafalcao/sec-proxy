package secproxy;

import java.util.Map;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.client.HttpClient;
import org.eclipse.jetty.client.api.ContentResponse;
import org.eclipse.jetty.client.api.Request;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class WebSec 
{
	private HttpClient httpClient;
	private String name;
	private Integer port;
	private String backendSchema = 'http://';
	private String domain = ".rfalcao.com";
	
	public WebSec(String serverName, Integer port) throws Exception 
	{
		this.serverName = serverName;
		this.port = port;
		this.httpClient = new HttpClient();		
	}
	
	public void up() throws Exception 
	{
		httpClient.start();
	}

	public String getPath()
	{
		return serverName + domain;
	}

	public Integer getPort() 
	{
		return port;
	}

	protected String getContentURL(String target)
	{
		return schema + getPath() + ":" + port + target;
	}
	
	public ContentResponse getContentResponse(String target, HttpServletRequest request) throws Exception
	{
		String contentURL = getContentURL(target);
		return httpClient.GET(contentURL);
	}
	
}
