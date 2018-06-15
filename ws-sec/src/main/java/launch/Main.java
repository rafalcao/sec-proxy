package launch;

import java.io.File;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.apache.catalina.WebResourceRoot;
import org.apache.catalina.WebResourceSet;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.startup.Tomcat;
import org.apache.catalina.Service;
import org.apache.catalina.connector.Connector;
import org.apache.catalina.webresources.DirResourceSet;
import org.apache.catalina.webresources.EmptyResourceSet;
import org.apache.catalina.webresources.StandardRoot;
import org.apache.tomcat.util.scan.Constants;
import org.apache.tomcat.util.scan.StandardJarScanFilter;

public class Main {

    private static String wsPass        = "031192";
    private static String wsNameOne     = "ws-one";
    private static int wsPortOne        = 8081;
    private static String wsNameTwo     = "ws-two";
    private static int wsPortTwo        = 8082;
        

    public static void main(String[] args) throws Exception {

        initService(wsNameOne, wsPortOne);

        Tomcat ws = initService(wsNameTwo, wsPortTwo);

        ws.getServer().await();

    }

    public static Tomcat initService(String wsName, int wsPort) {

        logger("Init "+ wsName +" service...");
        Tomcat webService = new Tomcat();

        try {

            logger("Generate connector for "+ wsName +"...");
            configService(webService, getConnector(wsName, wsPort));
            logger("Start "+ wsName +"...");
            webService.start();
            logger(wsName +" ON in "+ wsPort + " !!!");

            
            
        } catch (Exception e) {
            logger("Error: " + e.getMessage());
        }

        return webService;

    }

    private static void configService(Tomcat tomcat, Connector connector){
        try {

            File root = getRootFolder();
            Service service = tomcat.getService();

            service.addConnector(connector);
            Path tempPath = Files.createTempDirectory("tomcat-base-dir");
            tomcat.setBaseDir(tempPath.toString());

            File webContentFolder = new File(root.getAbsolutePath(), "src/main/webapp/");
            if (!webContentFolder.exists()) {
                webContentFolder = Files.createTempDirectory("default-doc-base").toFile();
            }
            StandardContext ctx = (StandardContext) tomcat.addWebapp("", webContentFolder.getAbsolutePath());
            ctx.setParentClassLoader(Main.class.getClassLoader());

            File additionWebInfClassesFolder = new File(root.getAbsolutePath(), "target/classes");
            WebResourceRoot resources = new StandardRoot(ctx);

            WebResourceSet resourceSet;
            if (additionWebInfClassesFolder.exists()) {
                resourceSet = new DirResourceSet(resources, "/WEB-INF/classes", additionWebInfClassesFolder.getAbsolutePath(), "/");
            } else {
                resourceSet = new EmptyResourceSet(resources);
            }
            resources.addPreResources(resourceSet);
            ctx.setResources(resources);

        } catch (Exception e) {
            logger("Error: " + e.getMessage());
        }

    }

    private static Connector getConnector(String wsName, int wsPort){
        Connector connector = new Connector();
        
        connector.setPort(wsPort);
        connector.setSecure(true);
        connector.setScheme("https");
        connector.setAttribute("keyAlias", wsName);
        connector.setAttribute("keystorePass", wsPass);
        connector.setAttribute("keystoreType", "JKS");
        connector.setAttribute("keystoreFile", wsName + ".jks");
        connector.setAttribute("clientAuth", "false");
        connector.setAttribute("protocol", "HTTP/1.1");
        connector.setAttribute("sslProtocol", "TLS");
        connector.setAttribute("maxThreads", "200");
        connector.setAttribute("protocol", "org.apache.coyote.http11.Http11AprProtocol");
        connector.setAttribute("SSLEnabled", true);

        return connector;
    }

    private static File getRootFolder() {
        try {
            File root;
            String runningJarPath = Main.class.getProtectionDomain().getCodeSource().getLocation().toURI().getPath().replaceAll("\\\\", "/");
            int lastIndexOf = runningJarPath.lastIndexOf("/target/");
            if (lastIndexOf < 0) {
                root = new File("");
            } else {
                root = new File(runningJarPath.substring(0, lastIndexOf));
            }
            return root;
        } catch (URISyntaxException ex) {
            throw new RuntimeException(ex);
        }
    }

    private static void logger(String msg){
        System.out.println("#### SEC PROXY | " + msg);
    }

}
