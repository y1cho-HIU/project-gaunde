package mincho.projectgaunde.entity;

public class Coordinate {
    private String name;
    private Double lat;
    private Double lng;

    public Coordinate(Double lat, Double lng){
        this.lat = lat;
        this.lng = lng;
    }

    public Coordinate(String name, Double lng, Double lat){
        this.name = name;
        this.lat = lat;
        this.lng = lng;
    }

    public String getName() {
        return name;
    }

    public Double getLat() {
        return lat;
    }

    public Double getLng() {
        return lng;
    }
}
