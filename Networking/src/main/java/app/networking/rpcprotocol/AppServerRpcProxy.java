package app.networking.rpcprotocol;

import app.model.Game;
import app.model.User;
import app.networking.dto.DtoUtils;
import app.networking.dto.GameDto;
import app.networking.dto.UserDto;
import app.networking.rpcprotocol.request.Request;
import app.networking.rpcprotocol.request.RequestType;
import app.networking.rpcprotocol.response.Response;
import app.networking.rpcprotocol.response.ResponseType;
import app.services.AppException;
import app.services.AppObserver;
import app.services.AppServices;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingDeque;

public class AppServerRpcProxy implements AppServices {

    private String host;
    private int port;
    private AppObserver client;
    private ObjectOutputStream output;
    private ObjectInputStream input;
    private Socket connection;
    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public AppServerRpcProxy(String host, int port) {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingDeque<Response>();
    }

    @Override
    public User login(String username, String password, AppObserver client) throws AppException {
        initializeConnection();
        String data = username + "," + password;
        Request request = new Request.Builder().type(RequestType.LOGIN).data(data).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.OK){
            User user = DtoUtils.getFromDto((UserDto) response.data());
            this.client = client;
            return user;
        }
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AppException(err);
        }
        throw new AppException("Error while logging in user!");
    }

    @Override
    public void logout(Long userID) throws AppException {
        String idString = userID.toString();
        Request request = new Request.Builder().type(RequestType.LOGOUT).data(idString).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AppException(err);
        }
    }

    @Override
    public Game startGameForUser(Long userID) throws AppException {
        String idString = userID.toString();
        Request request = new Request.Builder().type(RequestType.START_GAME_FOR_USER).data(idString).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.OK){
            GameDto gameDto = (GameDto) response.data();
            Game game = DtoUtils.getFromDto(gameDto);
            return game;
        }
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AppException(err);
        }
        throw new AppException("Error while starting game for a user!");
    }

    private void initializeConnection() {
        try{
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        }
        catch(IOException ex){
            ex.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void closeConnection() {
        finished = true;
        try{
            input.close();
            output.close();
            connection.close();
            client = null;
        }
        catch(IOException ex){
            ex.printStackTrace();
            System.out.println("Error while closing connection!");
        }
    }

    private synchronized void sendRequest(Request request) throws AppException {
        try{
            output.writeObject(request);
            output.flush();
        }
        catch(IOException ex){
            throw new AppException("Error sending request: " + ex.getMessage());
        }
    }

    private Response readResponse() {
        Response response = null;
        try{
            System.out.println("Taking response!");
            response = qresponses.take();
            System.out.println("Response has been taken!");
        }
        catch(InterruptedException ex){
            ex.printStackTrace();
        }
        return response;
    }

    private void handleUpdate(Response response){}

    private boolean isUpdate(Response response){
        return false;
    }

    private class ReaderThread implements Runnable {
        public void run(){
            while(!finished){
                try{
                    Object response = input.readObject();
                    System.out.println("Response received!" + response);
                    if(isUpdate((Response) response)){
                        System.out.println("Handling an update response!");
                        handleUpdate((Response) response);
                    }
                    else{
                        try{
                            qresponses.put((Response) response);
                            System.out.println("Qresponses updated! (PUT)");
                        }
                        catch(InterruptedException ex){
                            System.out.println("Reading error: " + ex.getMessage());
                        }
                    }
                }
                catch(IOException | ClassNotFoundException ex){
                    System.out.println("Reading error: " + ex.getMessage());
                }
            }
        }
    }


}
