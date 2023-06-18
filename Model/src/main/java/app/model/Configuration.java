package app.model;

import jakarta.persistence.*;

import java.util.Objects;

@Entity
@Table(name = "configurations")
public class Configuration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "position")
    private Integer position; // calculated 4 * X + Y

    @Column(name = "hint")
    private String hint;

    public Configuration() {}

    public Configuration(Long id, Integer position, String hint) {
        this.id = id;
        this.position = position;
        this.hint = hint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getPosition() {
        return position;
    }

    public void setPosition(Integer position) {
        this.position = position;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Configuration that)) return false;
        return getId().equals(that.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId());
    }

    @Override
    public String toString() {
        return "Configuration{" +
                "id=" + id +
                ", position=" + position +
                ", hint='" + hint + '\'' +
                '}';
    }
}
