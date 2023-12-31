package app.client;

import app.client.gui.LoginController;
import app.networking.rpcprotocol.AppServerRpcProxy;
import app.services.AppServices;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX extends Application {

    private static int defaultPort = 55555;
    private static String defaultServer = "localhost";

    public void start(Stage primaryStage) throws Exception {

        System.out.println("Client starting...!");
        Properties clientProperties = new Properties();
        try{
            clientProperties.load(StartRpcClientFX.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set: ");
            clientProperties.list(System.out);
        }
        catch(IOException ex){
            System.err.println("Cannot find client.properties");
            return;
        }

        String serverIP = clientProperties.getProperty("server.host");

        int serverPort = defaultPort;
        try{
            serverPort = Integer.parseInt(clientProperties.getProperty("server.port"));
        }
        catch(NumberFormatException nef){
            System.out.println("Wrong port number: " + nef.getMessage());
            System.out.println("Using default port number: " + defaultPort);
        }

        AppServices server = new AppServerRpcProxy(serverIP, serverPort);

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/login.fxml"));
        Parent root = loader.load();

        LoginController loginController = loader.getController();
        loginController.set(primaryStage, server);

        primaryStage.setScene(new Scene(root));
        primaryStage.show();

    }

}
