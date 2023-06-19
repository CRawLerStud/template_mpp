package app.networking.dto;

import java.io.Serializable;

public class GameDto implements Serializable {

    private Long id;

    public GameDto(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
}
