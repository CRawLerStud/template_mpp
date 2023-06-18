package app.client.gui;

import app.client.gui.dto.GameTable;
import app.model.Game;
import app.model.User;
import app.services.AppException;
import app.services.AppObserver;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.time.LocalDate;
import java.util.List;

public class StartController extends Controller implements AppObserver {

    private User user;
    private Game game;

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;
        gameLabel.setText("Game ID: " + game.getId());

        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        triesColumn.setCellValueFactory(new PropertyValueFactory<>("points"));

        try {
            List<Game> games = services.getGamesForConfiguration(game.getConfiguration().getId());
            System.out.println(games.size());
            for (Game currGame : games) {
                GameTable gameTable = new GameTable();
                gameTable.setDate(currGame.getDate());
                gameTable.setUsername(currGame.getUser().getUsername());
                gameTable.setPoints(currGame.getPoints());
                observableList.add(gameTable);
            }
        }
        catch(AppException ex){
            try {
                services.logout(user.getId());
            }
            catch(AppException ignored){}
            System.exit(0);
        }

        scoreTableView.setItems(observableList);
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
    public TableView<GameTable> scoreTableView;
    @FXML
    public TableColumn<GameTable, String> usernameColumn;
    @FXML
    public TableColumn<GameTable, LocalDate> dateColumn;
    @FXML
    public TableColumn<GameTable, Integer> triesColumn;

    private ObservableList<GameTable> observableList = FXCollections.observableArrayList();


    @FXML
    public void action0(ActionEvent event){
        try {
            sendMove(0, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action1(ActionEvent event){
        try {
            sendMove(1, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action2(ActionEvent event){
        try {
            sendMove(2, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action3(ActionEvent event){
        try {
            sendMove(3, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action4(ActionEvent event){
        try {
            sendMove(4, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action5(ActionEvent event){
        try {
            sendMove(5, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action6(ActionEvent event){
        try {
            sendMove(6, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action7(ActionEvent event){
        try {
            sendMove(7, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action8(ActionEvent event){
        try {
            sendMove(8, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action9(ActionEvent event){
        try {
            sendMove(9, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action10(ActionEvent event){
        try {
            sendMove(10, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action11(ActionEvent event){
        try {
            sendMove(11, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action12(ActionEvent event){
        try {
            sendMove(12, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action13(ActionEvent event){
        try {
            sendMove(13, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action14(ActionEvent event){
        try {
            sendMove(14, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    @FXML
    public void action15(ActionEvent event){
        try {
            sendMove(15, (Button) event.getSource());
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }
    }

    private void sendMove(Integer position, Button clickedButton) throws AppException {
        Object answer = services.sendMoveForUser(user.getId(), position);
        if(answer instanceof Integer){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "Game is finished! You lost!");
            alert.showAndWait();

            game.setWon(false);
            game.setFinished(true);
        }
        if(answer instanceof Double){
            Double distance = (Double) answer;
            clickedButton.setText("Distance: " + distance.toString());
        }
        if(answer instanceof String){
            Alert alert = new Alert(Alert.AlertType.INFORMATION, "YOU WON!");
            alert.showAndWait();

            game.setWon(true);
            game.setFinished(true);
        }
    }

    @Override
    public synchronized void notifyFinishedGame(Game game) throws AppException {
        Platform.runLater(() -> {
            System.out.println("Notifying!");
            if(this.game.getConfiguration().equals(game.getConfiguration())){
                System.out.println("This configuration!");
                GameTable gameTable = new GameTable();
                gameTable.setDate(game.getDate());
                gameTable.setUsername(game.getUser().getUsername());
                gameTable.setPoints(game.getPoints());
                observableList.add(gameTable);
            }
        });
    }
}
