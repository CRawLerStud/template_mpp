package app.server;

import app.networking.utils.AbstractServer;
import app.networking.utils.RpcConcurrentServer;
import app.persistance.UserRepository;
import app.persistance.implementation.HibernateUserRepository;
import app.server.implementation.DefaultAppServer;
import app.services.AppException;
import app.services.AppServices;

import java.io.IOException;
import java.util.Properties;

public class StartRpcServer {

    private static int defaultPort = 55555;

    public static void main(String[] args) {

        Properties properties = new Properties();

        try{
            properties.load(StartRpcServer.class.getResourceAsStream("/server.properties"));
            System.out.println("Server properties set: ");
            properties.list(System.out);
        }
        catch(IOException ex){
            System.err.println("Cannot find server.properties");
            return;
        }

        UserRepository userRepository = new HibernateUserRepository();

        AppServices services = new DefaultAppServer(userRepository);

        int serverPort = defaultPort;
        try{
            serverPort = Integer.parseInt(properties.getProperty("server.port"));
        }
        catch(NumberFormatException nef){
            System.out.println("Wrong port number: " + nef.getMessage());
            System.out.println("Using default port: " + defaultPort);
        }

        System.out.println("Using port: " + serverPort);

        AbstractServer server = new RpcConcurrentServer(serverPort, services);

        try{
            server.start();
        }
        catch(AppException ex){
            System.out.println("Error while starting server: " + ex.getMessage());
        }
        finally{
            try{
                server.stop();
            }
            catch(AppException ex){
                System.out.println("Error while stopping the server: " + ex.getMessage());
            }
        }

    }

}
