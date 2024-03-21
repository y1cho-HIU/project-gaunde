package mincho.projectgaunde.entity;

public class Lane {
    private Integer subwayCode;
    private String subwayName;
    private String busNo;

    Lane(Integer subwayCode, String subwayName){
        this.subwayCode = subwayCode;
        this.subwayName = subwayName;
    }

    Lane(String busNo){
        this.busNo = busNo;
    }

}
