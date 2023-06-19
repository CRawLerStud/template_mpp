package app.client.gui;

import app.model.User;
import app.services.AppException;
import app.services.AppObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

public class StartController extends Controller implements AppObserver {
    private User user;

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
    public Label playersReadyLabel;
    @FXML
    public TextField wordTextField;


    @FXML
    public void readyAction(){
        String word = wordTextField.getText();
        if(word.length() < 4 || word.length() > 6){
            Alert alert = new Alert(Alert.AlertType.WARNING, "The word size must be between 4 and 6 letters!");
            alert.showAndWait();
            return;
        }
        try{
            Integer playersReady = services.setUserReady(user.getId(), word);
            if(playersReady == 3){
                //TODO: Start game logic for player
            }
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @Override
    public void notifyUserReady(Integer noOfPlayersReady) throws AppException {
        Platform.runLater(() -> {
            playersReadyLabel.setText("Players Ready: " + noOfPlayersReady.toString());
        });
    }
}
