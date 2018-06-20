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
	private static String backendOneName = "ws-one";
	private static int backendOnePort = 8081;
	private static String backendTwoName = "ws-two";
	private static int backendTwoPort = 8082;

    public static void main(String[] args) throws Exception
    {
    	servers = new ArrayList<WebSec>();
		
		newSec(backendOneName, backendOnePort);
		newSec(backendTwoName, backendTwoPort);
   
    	Proxy proxy = new Proxy(servers);
    	proxy.up();
    }

    private function void newSec(String name, int port){
    	WebSec webSec = new WebSec(name, port);
		webSec.up();
		servers.add(webSec);
    }

}
