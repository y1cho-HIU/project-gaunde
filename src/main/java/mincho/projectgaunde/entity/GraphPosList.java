package mincho.projectgaunde.entity;

import java.util.List;

public class GraphPosList {
    private Integer trafficType;
    private Integer trafficNumber;
    private List<GraphPos> GPList;

    public GraphPosList(Integer trafficType, Integer trafficNumber, List<GraphPos> GPList){
        this.trafficType = trafficType;
        this.trafficNumber = trafficNumber;
        this.GPList = GPList;
    }

    public Integer getTrafficType() {
        return trafficType;
    }

    public Integer getTrafficNumber() {
        return trafficNumber;
    }

    public List<GraphPos> getGPList() {
        return GPList;
    }
}
