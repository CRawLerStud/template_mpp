package app.client.gui.dto;

public class LeaderBoardEntry {

    private String username;
    private String currentWord;

    public LeaderBoardEntry() {}

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCurrentWord() {
        return currentWord;
    }

    public void setCurrentWord(String currentWord) {
        this.currentWord = currentWord;
    }
}
