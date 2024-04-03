package mincho.projectgaunde.entity;

public class PassStop {
    private Integer passStopIndex;
    private Integer stationID;
    private String stationName;

    public PassStop(Integer passStopIndex, Integer stationID, String stationName){
        this.passStopIndex = passStopIndex;
        this.stationID = stationID;
        this.stationName = stationName;
    }
}
