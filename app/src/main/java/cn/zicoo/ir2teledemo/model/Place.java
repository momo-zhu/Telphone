package cn.zicoo.ir2teledemo.model;

import org.kymjs.kjframe.database.annotate.Id;

public class Place {

    @Id
    public int PlaceId;


    //后厨打印机
    //
    //public int PrinterId ;

    public int PlaceType ;

    //名称
    public String PlaceTable;

    //排序

    public int Sort;

    public int getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(int placeId) {
        PlaceId = placeId;
    }

    public int getPlaceType() {
        return PlaceType;
    }

    public void setPlaceType(int placeType) {
        PlaceType = placeType;
    }

    public String getPlaceTable() {
        return PlaceTable;
    }

    public void setPlaceTable(String placeTable) {
        this.PlaceTable = placeTable;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }
}
