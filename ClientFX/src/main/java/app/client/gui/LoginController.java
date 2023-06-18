package app.client.gui;

import app.model.Game;
import app.model.User;
import app.services.AppException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.io.IOException;

public class LoginController extends Controller{

    @FXML
    public TextField usernameTextField;
    @FXML
    public PasswordField passwordTextField;

    @FXML
    public void loginAction() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/start.fxml"));
            Parent root = loader.load();
            Scene startScene = new Scene(root);
            StartController startController = loader.getController();

            User user = services.login(username, password, startController);
            Game game = services.startGameForUser(user.getId());

            startController.set(stage, services);
            startController.setUser(user);
            startController.setGame(game);

            stage.setScene(startScene);

            stage.setOnCloseRequest(eh -> {
                try{
                    services.logout(user.getId());
                    stage.close();
                    System.exit(0);
                }
                catch(AppException ex){
                    Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
                    alert.showAndWait();
                }
            });

        }
        catch(IOException | AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }

    }

}
