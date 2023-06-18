package app.model;

import jakarta.persistence.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;

@Entity
@Table(name = "games")
public class Game {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "config_ig")
    private Configuration configuration;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "won")
    private Boolean won;

    @Column(name = "finished")
    private Boolean finished;

    @Column(name = "date")
    private LocalDate date;

    @OneToMany(mappedBy = "game", fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Move> moves;

    public Game() {}

    public Game(Long id, Configuration configuration, User user, Boolean won, Boolean finished, LocalDate date) {
        this.id = id;
        this.configuration = configuration;
        this.user = user;
        this.won = won;
        this.finished = finished;
        this.date = date;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

    public void setConfiguration(Configuration configuration) {
        this.configuration = configuration;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User player) {
        this.user = player;
    }

    public Boolean getWon() {
        return won;
    }

    public void setWon(Boolean won) {
        this.won = won;
    }

    public Boolean getFinished() {
        return finished;
    }

    public void setFinished(Boolean finished) {
        this.finished = finished;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Game game)) return false;
        return getId().equals(game.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Game{" +
                "id=" + id +
                ", configuration=" + configuration +
                ", user=" + user +
                ", won=" + won +
                ", finished=" + finished +
                ", date=" + date +
                '}';
    }
}
