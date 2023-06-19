package app.rest.dto;

public class AnswerRestDto {

    private Long gameID;
    private String username;
    private String answer;
    private Integer points;

    public AnswerRestDto() {}

    public Long getGameID() {
        return gameID;
    }

    public void setGameID(Long gameID) {
        this.gameID = gameID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
