package cn.zicoo.ir2teledemo.model;

import org.kymjs.kjframe.database.annotate.Id;
import org.kymjs.kjframe.database.annotate.Index;

import java.util.Date;

public class Table {
    @Id
    public int table_id ;
    public String table_state;
    public int contains_number;
    public int table_number ;


    public int Sort;

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

    public int getPlaceId() {
        return PlaceId;
    }

    public void setPlaceId(int placeId) {
        PlaceId = placeId;
    }

    @Index
    public int PlaceId ;


    public Date CreateDate_Table;
    public Date UpdateDate_Table;

    public int getTable_number() {
        return table_number;
    }

    public void setTable_number(int table_number) {
        this.table_number = table_number;
    }



    public Table(){

        CreateDate_Table = new Date();
        UpdateDate_Table = new Date();
    }

    public Date getCreateDate_Table() {
        return CreateDate_Table;
    }

    public void setCreateDate_Table(Date createDate) {
        CreateDate_Table = createDate;
    }

    public Date getUpdateDate_Table() {
        return UpdateDate_Table;
    }

    public void setUpdateDate_Table(Date updateDate) {
        UpdateDate_Table = updateDate;
    }





    public int getTable_id() {
        return table_id;
    }

    public void setTable_id(int table_id) {
        this.table_id = table_id;
    }

    public int getContains_number() {
        return contains_number;
    }

    public void setContains_number(int contains_number) {
        this.contains_number = contains_number;
    }




    public String getTable_state() {
        return table_state;
    }

    public void setTable_state(String table_state) {
        this.table_state = table_state;
    }
}
