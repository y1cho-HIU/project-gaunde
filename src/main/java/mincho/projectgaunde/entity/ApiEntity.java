package mincho.projectgaunde.entity;

import com.fasterxml.jackson.databind.JsonNode;

import java.util.List;

public class ApiEntity {
    private Coordinate destination;
    private List<Coordinate> departures;
    private List<JsonNode> jsonNodeList;
    private Double score;

    public ApiEntity(Coordinate destination, List<Coordinate> departures, List<JsonNode> jsonNodeList, Double score){
        this.destination = destination;
        this.departures = departures;
        this.jsonNodeList = jsonNodeList;
        this.score = score;
    }

    public Coordinate getDestination() {
        return destination;
    }

    public List<Coordinate> getDepartures() {
        return departures;
    }

    public List<JsonNode> getJsonNodeList() {
        return jsonNodeList;
    }

}
