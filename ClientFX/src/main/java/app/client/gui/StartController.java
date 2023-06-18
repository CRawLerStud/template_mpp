package app.client.gui;

import app.model.User;
import app.services.AppObserver;
import javafx.fxml.FXML;
import javafx.scene.control.Label;

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


}
