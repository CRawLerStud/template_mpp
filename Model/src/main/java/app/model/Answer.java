package app.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "answers")
public class Answer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "round_id")
    private Round round;

    @ManyToOne(optional = false)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "answer")
    private String answer;

    @Column(name = "points")
    private Integer points;

    @ManyToOne
    @JoinColumn(name = "for_user_id")
    private User forUser;

    public Answer() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Round getRound() {
        return round;
    }

    public void setRound(Round round) {
        this.round = round;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getPoints() {
        return points;
    }

    public void setPoints(Integer points) {
        this.points = points;
    }

    public User getForUser() {
        return forUser;
    }

    public void setForUser(User forUser) {
        this.forUser = forUser;
    }

    public String getAnswer() {
        return answer;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Answer answer)) return false;
        return getId().equals(answer.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Answer{" +
                "id=" + id +
                ", round=" + round +
                ", user=" + user +
                ", answer='" + answer + '\'' +
                ", answer=" + answer +
                ", forUser=" + forUser +
                '}';
    }
}
