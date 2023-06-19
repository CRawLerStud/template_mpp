package app.client.gui;

import app.model.Game;
import app.model.User;
import app.services.AppException;
import app.services.AppObserver;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

import java.io.IOException;

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
            services.setUserReady(user.getId(), word);
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

            try {
                if (noOfPlayersReady == 3) {
                    Game game = services.getGameInstanceForUser(user.getId());

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/game.fxml"));
                    Parent root = loader.load();
                    Scene newScene = new Scene(root);

                    GameController gameController = loader.getController();
                    gameController.set(stage, services);
                    gameController.setUser(user);
                    gameController.setGame(game);

                    stage.setScene(newScene);
                }
            }
            catch(AppException | IOException ex){
                Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                alert.showAndWait();
            }

        });
    }
}
