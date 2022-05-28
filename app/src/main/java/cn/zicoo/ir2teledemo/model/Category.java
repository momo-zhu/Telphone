package cn.zicoo.ir2teledemo.model;

import org.kymjs.kjframe.database.annotate.Id;

public class Category {

    @Id
    public int CategoryId;


    //后厨打印机
    //
    //public int PrinterId ;

    public int CategoryType ;

    //名称
    public String Name;
    public String Description;

    //排序

    public int Sort;

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public int getCategoryType() {
        return CategoryType;
    }

    public void setCategoryType(int categoryType) {
        CategoryType = categoryType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }
}
