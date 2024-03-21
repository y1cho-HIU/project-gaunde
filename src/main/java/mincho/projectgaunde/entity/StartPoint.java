package mincho.projectgaunde.entity;

import java.util.List;

public class StartPoint {
    private Integer startPointIndex;
    private String mapObj;
    private Coordinate depInfo;
    private Integer payment;
    private Integer totalTime;

    private List<SubPath> subPathList;
    private List<GraphPos> graphPosList;

    public StartPoint(Integer startPointIndex, String mapObj,
                      Coordinate depInfo,
                      Integer payment, Integer totalTime,
                      List<SubPath> subPathList, List<GraphPos> graphPosList){

        this.startPointIndex = startPointIndex;
        this.mapObj = mapObj;
        this.depInfo = depInfo;
        this.payment = payment;
        this.totalTime = totalTime;
        this.subPathList = subPathList;
        this.graphPosList = graphPosList;
    }
}
