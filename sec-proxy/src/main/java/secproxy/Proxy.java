package secproxy;

import static org.eclipse.jetty.util.resource.Resource.newClassPathResource;

import java.util.List;

import org.eclipse.jetty.http.HttpVersion;
import org.eclipse.jetty.server.Connector;
import org.eclipse.jetty.server.HttpConfiguration;
import org.eclipse.jetty.server.HttpConnectionFactory;
import org.eclipse.jetty.server.SecureRequestCustomizer;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.SslConnectionFactory;
import org.eclipse.jetty.util.ssl.SslContextFactory;

import secproxy.Handler;


public class Proxy 
{
	private Server server;
	private List<WebSec> servers;

	private String passSec 		= "031192";
	private String oneSec 		= "ws-one";
	private String onePort 		= 8444;

	private String twoSec 		= "ws-two";
	private String twoPort 		= 8445;
	
	public Proxy(List<WebSec> servers) 
	{
        this.server = new Server();
        this.servers = servers;
        init();
	}

	private void init() 
	{
		SslContextFactory sslWsOne = getSSLContextFactory(oneSec, passSec);
		HttpConfiguration httpsConfiguration = getHTTPSConfiguration();
        ServerConnector oneHttp = getHTTPSConnectorTwo(sslWsOne, onePort , httpConfiguration);
        
        SslContextFactory sslWsTwo = getSSLContextFactory(twoSec, passSec);
        HttpConfiguration httpsConfiguration = getHTTPSConfiguration();
        ServerConnector twoHttp = getHTTPSConnector(sslWsTwo, twoPort, httpsConfiguration);

        server.setConnectors(new Connector[] { oneHttp, twoHttp });
        server.setHandler(new Handler(servers));
	}

	public void start() throws Exception
	{
		server.start();
	}

	private SslContextFactory getSSLContextFactory(String sec, String pass) 
	{
		SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStoreResource(newClassPathResource(sec + ".jks"));
        String password = System.getProperty("keystore.password", pass);
		sslContextFactory.setKeyStorePassword(password);
        sslContextFactory.setKeyManagerPassword(password);
        
		return sslContextFactory;
	}
	
	private HttpConfiguration getHTTPSConfiguration()
	{
		HttpConfiguration https_config = new HttpConfiguration(new HttpConfiguration());
        SecureRequestCustomizer src = new SecureRequestCustomizer();
        src.setStsIncludeSubDomains(true);
        https_config.addCustomizer(src);
        
		return https_config;
	}

	private ServerConnector getHTTPSConnector(SslContextFactory sslContextFactory, String port, HttpConfiguration httpsConfiguration) 
	{
		ServerConnector https = new ServerConnector(
			server, 
			new SslConnectionFactory(
				sslContextFactory,
				HttpVersion.HTTP_1_1.asString()), 
			new HttpConnectionFactory(httpsConfiguration)
		);

        https.setPort(port);
        
		return https;
	}
}
