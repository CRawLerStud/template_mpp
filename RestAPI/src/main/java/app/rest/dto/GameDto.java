package app.rest.dto;

import java.util.List;

public class GameDto {

    private Long id;
    private Integer noOfTries;
    private List<Integer> positions;
    private String hint;

    public GameDto() {}

    public GameDto(Long id, Integer noOfTries, List<Integer> positions, String hint) {
        this.id = id;
        this.noOfTries = noOfTries;
        this.positions = positions;
        this.hint = hint;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getNoOfTries() {
        return noOfTries;
    }

    public void setNoOfTries(Integer noOfTries) {
        this.noOfTries = noOfTries;
    }

    public List<Integer> getPositions() {
        return positions;
    }

    public void setPositions(List<Integer> positions) {
        this.positions = positions;
    }

    public String getHint() {
        return hint;
    }

    public void setHint(String hint) {
        this.hint = hint;
    }

    @Override
    public String toString() {
        return "GameDto{" +
                "id=" + id +
                ", noOfTries=" + noOfTries +
                ", positions=" + positions +
                ", hint='" + hint + '\'' +
                '}';
    }
}
