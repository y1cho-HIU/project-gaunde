package mincho.projectgaunde.entity;

import java.util.List;

public class SubPath {
    private Integer trafficType;
    private Integer distance;
    private List<Lane> laneList;
    private List<PassStop> passStopList;

    /**
     *
     * @param trafficType : 1 -> subway, 2 -> bus, 3 -> walk
     * @param distance : (meter)
     * @param laneList : lane information
     * @param passStopList : passStop information
     */
    SubPath(Integer trafficType, Integer distance, List<Lane> laneList, List<PassStop> passStopList){
        this.trafficType = trafficType;
        this.distance = distance;
        this.laneList = laneList;
        this.passStopList = passStopList;
    }

    SubPath(Integer trafficType, Integer distance){
        this.trafficType = trafficType;
        this.distance = distance;
    }
}
