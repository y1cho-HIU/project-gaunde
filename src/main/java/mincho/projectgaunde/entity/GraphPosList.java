package mincho.projectgaunde.entity;

import java.util.List;

public class GraphPosList {
    private Integer tType;
    private Integer tNumber;
    private List<GraphPos> GPList;

    public GraphPosList(Integer tType, Integer tNumber, List<GraphPos> GPList){
        this.tType = tType;
        this.tNumber = tNumber;
        this.GPList = GPList;
    }
}
