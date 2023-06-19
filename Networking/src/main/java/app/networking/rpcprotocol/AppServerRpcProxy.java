package app.networking.rpcprotocol;

import app.model.Game;
import app.model.SecretWord;
import app.model.User;
import app.networking.dto.DtoUtils;
import app.networking.dto.GameDto;
import app.networking.dto.SecretWordDto;
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
import java.util.Arrays;
import java.util.List;
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
    public void logout(Long playerID) throws AppException {
        String idString = playerID.toString();
        Request request = new Request.Builder().type(RequestType.LOGOUT).data(idString).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AppException(err);
        }
    }

    @Override
    public Integer setUserReady(Long userID, String word) throws AppException {
        String data = userID.toString() + "," + word;
        Request request = new Request.Builder().type(RequestType.SET_USER_READY).data(data).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.OK){
            return (Integer) response.data();
        }
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AppException(err);
        }
        throw new AppException("Error while setting user ready!");
    }

    @Override
    public Game getGameInstanceForUser(Long userID) throws AppException{
        Request request = new Request.Builder().type(RequestType.GAME_FOR_USER).data(userID).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.OK){
            GameDto gameDto = (GameDto) response.data();
            return DtoUtils.getFromDto(gameDto);
        }
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AppException(err);
        }
        throw new AppException("Error while getting game instance for a user!");
    }

    @Override
    public List<SecretWord> getInformationForUserAndGame(Long userID, Long gameID) throws AppException {
        String data = userID.toString() + "," + gameID.toString();
        Request request = new Request.Builder().type(RequestType.GET_INFO_FOR_USER).data(data).build();
        sendRequest(request);
        Response response = readResponse();
        if(response.type() == ResponseType.OK){
            SecretWordDto[] secretWords = (SecretWordDto[]) response.data();
            return Arrays.asList(secretWords).stream()
                    .map(DtoUtils::getFromDto).toList();
        }
        if(response.type() == ResponseType.ERROR){
            String err = response.data().toString();
            throw new AppException(err);
        }
        throw new AppException("Error while getting information for user and game!");
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

    private void handleUpdate(Response response){
        if(ResponseType.USER_READY == response.type()){
            System.out.println("Handling update user ready");
            Integer size = (Integer) response.data();
            if(client != null){
                try {
                    client.notifyUserReady(size);
                }
                catch(AppException ex){
                    System.out.println("Error while handling user update!");
                }
            }
        }
    }

    private boolean isUpdate(Response response){
        return ResponseType.USER_READY == response.type();
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
