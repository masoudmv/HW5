package server;

import server.socket.SocketResponseSender;
import shared.Model.User;
import shared.request.HiRequest;
import shared.request.LoginRequest;
import shared.request.RequestHandler;
import shared.request.SignInRequest;
import shared.response.*;

public class ClientHandler extends Thread implements RequestHandler {
    private SocketResponseSender socketResponseSender;
    private DataBase dataBase;

    public ClientHandler(SocketResponseSender socketResponseSender, DataBase dataBase) {
        this.dataBase = dataBase;
        this.socketResponseSender = socketResponseSender;
    }


    @Override
    public void run() {
        try {
            while (true) {
                Response response = socketResponseSender.getRequest().run(this);
                socketResponseSender.sendResponse(response);
            }
        } catch (Exception e) {
            socketResponseSender.close();
        }
    }


    @Override
    public Response handleHiRequest(HiRequest hiRequest) {
        System.out.println("naisbdaVGFYDVS");
        return new HiResponse();
    }

    @Override
    public Response handleSignInRequest(SignInRequest signInRequest) { //todo
        String username = signInRequest.getUsername();
        String password = signInRequest.getPassword();
        boolean successful = dataBase.addUser(username, password);
        return new SignInResponse(successful);
    }

    @Override
    public Response handleLoginRequest(LoginRequest loginRequest) {
        String username = loginRequest.getUsername();
        String password = loginRequest.getPassword();
        boolean successful = dataBase.authenticate(username, password);
        return new LoginResponse(successful);
    }
}
