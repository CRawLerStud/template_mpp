package app.networking.utils;

import app.networking.rpcprotocol.AppClientReflectionWorker;
import app.services.AppException;
import app.services.AppServices;

import java.net.Socket;

public class RpcConcurrentServer extends AbstractConcurrentServer{

    private AppServices server;

    public RpcConcurrentServer(int port, AppServices server) {
        super(port);
        this.server = server;
        System.out.println("Rpc Concurrent Server initialized...");
    }

    @Override
    protected Thread createWorker(Socket client) {
        AppClientReflectionWorker worker =
                new AppClientReflectionWorker(server, client);
        return new Thread(worker);
    }

    @Override
    public void stop() throws AppException {
        System.out.println("Stopping server...");
    }
}
