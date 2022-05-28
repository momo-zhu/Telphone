package cn.zicoo.ir2teledemo.model;

import org.kymjs.kjframe.database.annotate.Id;
import org.kymjs.kjframe.database.annotate.Index;

public class FoodMenuProperty {
    @Id
    public int FoodMenuPropertyId;
    @Index
    public int FoodMenuId;
    //名称
    public String Name ;

    //值
    public String ValueStr;

    public int getFoodMenuPropertyId() {
        return FoodMenuPropertyId;
    }

    public void setFoodMenuPropertyId(int foodMenuPropertyId) {
        FoodMenuPropertyId = foodMenuPropertyId;
    }

    public int getFoodMenuId() {
        return FoodMenuId;
    }

    public void setFoodMenuId(int foodMenuId) {
        FoodMenuId = foodMenuId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getValueStr() {
        return ValueStr;
    }

    public void setValueStr(String valueStr) {
        ValueStr = valueStr;
    }
}
