package app.client.gui;

import app.client.gui.dto.LeaderBoardEntry;
import app.model.Game;
import app.model.SecretWord;
import app.model.User;
import app.services.AppException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.util.List;

public class GameController extends Controller{

    private User user;
    private Game game;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Game getGame() {
        return game;
    }

    public void setGame(Game game) {
        this.game = game;

        wordColumn.setCellValueFactory(new PropertyValueFactory<>("currentWord"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));

        try{
            List<SecretWord> words = services.getInformationForUserAndGame(user.getId(), game.getId());
            for(SecretWord word : words){
                LeaderBoardEntry entry = new LeaderBoardEntry();
                entry.setUsername(word.getUser().getUsername());
                entry.setCurrentWord(word.getWord());
                observableList.add(entry);
            }
        }
        catch(AppException ex){
            Alert alert = new Alert(Alert.AlertType.ERROR, ex.getMessage());
            alert.showAndWait();
        }

        leaderboard.setItems(observableList);
    }

    @FXML
    public Label usernameLabel;
    @FXML
    public TableView<LeaderBoardEntry> leaderboard;
    @FXML
    public TableColumn<LeaderBoardEntry, String> usernameColumn;
    @FXML
    public TableColumn<LeaderBoardEntry, String> wordColumn;

    private ObservableList<LeaderBoardEntry> observableList = FXCollections.observableArrayList();
}
