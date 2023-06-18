package app.client.gui.dto;

import java.time.LocalDate;

public class GameTable {

    private String username;
    private LocalDate date;
    private Integer points;

    public GameTable(){}

    public GameTable(String username, LocalDate date, Integer points) {
        this.username = username;
        this.date = date;
        this.points = points;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }
}
