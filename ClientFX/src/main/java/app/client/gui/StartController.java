package app.client.gui;

import app.model.Game;
import app.model.User;
import app.services.AppObserver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class StartController extends Controller implements AppObserver {

    private User user;
    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        gameLabel.setText("Game ID: " + game.getId());
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
        usernameLabel.setText("Your Username: " + user.getUsername());
    }

    @FXML
    public Label usernameLabel;
    @FXML
    public Label gameLabel;


    @FXML
    public void action0(){
    }

    @FXML
    public void action1(){
    }

    @FXML
    public void action2(){
    }

    @FXML
    public void action3(){
    }

    @FXML
    public void action4(){
    }

    @FXML
    public void action5(){
    }

    @FXML
    public void action6(){
    }

    @FXML
    public void action7(){
    }

    @FXML
    public void action8(){
    }

    @FXML
    public void action9(){
    }

    @FXML
    public void action10(){
    }

    @FXML
    public void action11(){
    }

    @FXML
    public void action12(){
    }

    @FXML
    public void action13(){
    }

    @FXML
    public void action14(){
    }

    @FXML
    public void action15(){
    }

}
