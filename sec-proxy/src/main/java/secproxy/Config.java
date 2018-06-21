package secproxy;

import java.io.FileInputStream;
import java.util.Properties;
import java.io.InputStream;

public class Config
{
	private static String backendOneName, backendTwoName;
	private static int backendOnePort, backendTwoPort;
	private static Properties prop;

    public Config() 
    {
    	try {
	    	prop = new Properties();
			InputStream input = new FileInputStream("config.properties");

			prop.load(input);
		} catch (Exception exception) 
		{
			System.out.println("Error: " + exception.getMessage());
		} 
    }

    public String getBackendOneName(){
    	return prop.getProperty("backend-one-name");
    }

    public String getBackendTwoName(){
    	return prop.getProperty("backend-two-name");
    }

    public int getBackendOnePort(){
    	return Integer.parseInt(prop.getProperty("backend-one-port"));
    }

    public int getBackendTwoPort(){
    	return Integer.parseInt(prop.getProperty("backend-two-port"));
    }

    public String getBackendSchema(){
    	return prop.getProperty("backend-schema") + "://";
    }

    public String getSecOneName(){
    	return prop.getProperty("sec-one-name");
    }

    public int getSecOnePort(){
    	return Integer.parseInt(prop.getProperty("sec-one-port"));
    }

    public String getSecTwoName(){
    	return prop.getProperty("sec-two-name");
    }

    public int getSecTwoPort(){
    	return Integer.parseInt(prop.getProperty("sec-two-port"));
    }

    public String getSecPass(){
    	return prop.getProperty("sec-pass");
    }

    public String getDomain(){
    	return prop.getProperty("domain");
    }

    public String getSecOneUrl(){
    	return 
    		getSecSchema() + 
    		getSecOneName() + "." + 
    		getDomain() + ":" +
    		getSecOnePort() + "/";
    }

    public String getSecTwoUrl(){
    	return 
    		getSecSchema() + 
    		getSecTwoName() + "." + 
    		getDomain() + ":" +
    		getSecTwoPort() + "/";
    }

    public String getSecSchema(){
    	return prop.getProperty("sec-schema") + "://";
    }
}
