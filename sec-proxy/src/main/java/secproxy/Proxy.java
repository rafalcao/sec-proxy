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
	private Config config;
	
	public Proxy(List<WebSec> servers) 
	{
        this.server = new Server();
        this.servers = servers;
        config = new Config();
        init();
	}

	private void init() 
	{
		SslContextFactory sslWsOne = getSSLContextFactory(config.getSecOneName());
		HttpConfiguration httpsConfigurationOne = getHTTPSConfiguration();
        ServerConnector oneHttp = getHTTPSConnector(sslWsOne, config.getSecOnePort() , httpsConfigurationOne);
        
        SslContextFactory sslWsTwo = getSSLContextFactory(config.getSecTwoName());
        HttpConfiguration httpsConfigurationTwo = getHTTPSConfiguration();
        ServerConnector twoHttp = getHTTPSConnector(sslWsTwo, config.getSecTwoPort(), httpsConfigurationTwo);

        server.setConnectors(new Connector[] { oneHttp, twoHttp });
        server.setHandler(new Handler(servers));
	}

	public void up() throws Exception
	{
		server.start();
	}

	private SslContextFactory getSSLContextFactory(String sec) 
	{
		SslContextFactory sslContextFactory = new SslContextFactory();
        sslContextFactory.setKeyStoreResource(newClassPathResource(sec + ".jks"));
        String password = System.getProperty("keystore.password", config.getSecPass());
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

	private ServerConnector getHTTPSConnector(SslContextFactory sslContextFactory, int port, HttpConfiguration httpsConfiguration) 
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
