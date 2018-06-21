package secproxy;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import secproxy.Proxy;
import secproxy.WebSec;

public class Main
{
	private static final Logger log = LoggerFactory.getLogger(Main.class);
	private static List<WebSec> servers;

    public static void main(String[] args) throws Exception
    {
    	Config config = new Config();
    	servers = new ArrayList<WebSec>();
		
		newSec(config.getBackendOneName(), config.getBackendOnePort());
		newSec(config.getBackendTwoName(), config.getBackendTwoPort());
   
    	Proxy proxy = new Proxy(servers);
    	proxy.up();
    }

    private static void newSec(String name, Integer port)
    {
    	try{
	    	WebSec webSec = new WebSec(name, port);
			webSec.up();
			servers.add(webSec);

		} catch (Exception exception) 
		{
			log.info("#### Error: " + exception.getMessage());
		} 
    }

}
