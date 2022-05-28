package cn.zicoo.ir2teledemo.model;

import org.kymjs.kjframe.database.annotate.Id;
import org.kymjs.kjframe.database.annotate.Index;

public class FoodMenuItem {
    @Id
    public int FoodMenuItemId;
    @Index
    public int FoodMenuId;
    //名称
    public String Title;

    //价格
    public double Price ;
        
    public int Sort;

    public int getFoodMenuItemId() {
        return FoodMenuItemId;
    }

    public void setFoodMenuItemId(int foodMenuItemId) {
        FoodMenuItemId = foodMenuItemId;
    }

    public int getFoodMenuId() {
        return FoodMenuId;
    }

    public void setFoodMenuId(int foodMenuId) {
        FoodMenuId = foodMenuId;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }
}
