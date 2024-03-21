package mincho.projectgaunde.entity;

import java.util.List;

public class Destination {
    private Integer destIndex;
    private Coordinate destInfo;

    private List<StartPoint> startPoint;

    public Destination(Integer destIndex, Coordinate destInfo, List<StartPoint> startPoint){
        this.destIndex = destIndex;
        this.destInfo = destInfo;
        this.startPoint = startPoint;
    }
}
