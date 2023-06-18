package app.client.gui;

import app.services.AppServices;
import javafx.stage.Stage;

public class Controller {

    protected Stage stage;
    protected AppServices services;

    public void set(Stage stage, AppServices services){
        this.stage = stage;
        this.services = services;
    }
}
