package app.networking.rpcprotocol;

import app.model.Game;
import app.model.User;
import app.networking.dto.DtoUtils;
import app.networking.dto.GameDto;
import app.networking.dto.UserDto;
import app.networking.rpcprotocol.request.Request;
import app.networking.rpcprotocol.response.Response;
import app.networking.rpcprotocol.response.ResponseType;
import app.services.AppException;
import app.services.AppObserver;
import app.services.AppServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.Socket;

public class AppClientReflectionWorker implements Runnable, AppObserver {

    private AppServices server;
    private Socket connection;
    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;

    public AppClientReflectionWorker(AppServices server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            connected = true;
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void run() {
        while(connected){
            try{
                Object request = input.readObject();
                Response response = handleRequest((Request) request);
                if(response != null){
                    sendResponse(response);
                }
            }
            catch(IOException | ClassNotFoundException ex){
                System.out.println("Error while handling request! (CLIENT WORKER)");
                System.out.println("Error: " + ex.getMessage());
            }

            try{
                Thread.sleep(1000);
            }
            catch(InterruptedException ex){
                System.out.println("Error while trying to sleep!");
                System.out.println("Error: " + ex.getMessage());
            }
        }

        try{
            input.close();
            output.close();
            connection.close();
        }
        catch(IOException ex){
            System.out.println("Error while closing connection!");
            System.out.println("Error: " + ex.getMessage());
        }
    }

    private synchronized void sendResponse(Response response) throws IOException{
        System.out.println("Sending response -> " + response);
        output.writeObject(response);
        output.flush();
    }

    private Response handleRequest(Request request) {
        Response response = null;
        String handlerName = "handle" + (request).type();
        System.out.println("HandlerName: " + handlerName);
        try{
            Method method = this.getClass().getDeclaredMethod(handlerName, Request.class);
            response = (Response) method.invoke(this, request);
            System.out.println("Method " + handlerName + " invoked!");
        }
        catch(InvocationTargetException | IllegalAccessException | NoSuchMethodException ex){
            System.out.println("Error while handling request!");
            System.out.println("Error: " + ex.getMessage());
        }
        return response;
    }

    private static final Response okResponse =
            new Response.Builder().type(ResponseType.OK).build();

    private Response handleLOGIN(Request request){
        System.out.println("Handling login!");
        String[] data = request.data().toString().split(",");
        try{
            String username = data[0];
            String password = data[1];
            User user = server.login(username, password, this);
            UserDto userDto = DtoUtils.getDto(user);
            return new Response.Builder().type(ResponseType.OK).data(userDto).build();
        }
        catch(AppException ex){
            return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }

    private Response handleLOGOUT(Request request){
        System.out.println("Handling LOGOUT!");
        String idString = request.data().toString();
        Long playerID = Long.parseLong(idString);
        try{
            server.logout(playerID);
            return okResponse;
        }
        catch (AppException ex){
            return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }

    private Response handleSTART_GAME_FOR_USER(Request request){
        System.out.println("Handling start game for user!");
        String playerIdString = request.data().toString();
        Long playerID = Long.parseLong(playerIdString);
        try{
            Game game = server.startGameForUser(playerID);
            GameDto gameDto = DtoUtils.getDto(game);
            return new Response.Builder().type(ResponseType.OK).data(gameDto).build();
        }
        catch(AppException ex){
            return new Response.Builder().type(ResponseType.ERROR).data(ex.getMessage()).build();
        }
    }


}
