package cn.zicoo.ir2teledemo.model;

import org.kymjs.kjframe.database.annotate.Id;
import org.kymjs.kjframe.database.annotate.Index;
import org.kymjs.kjframe.database.annotate.Transient;

import java.util.ArrayList;
import java.util.List;

public class FoodMenu {
    @Id
    public int FoodMenuId ;

    //产品分类
    @Index
    public int CategoryId ;

    //产品名称255
    public String Name ;

    //1 配料，2 套餐中菜品明细
//    public boolean IsIngredient ;


    //名称索引
    @Index
    public String Code ;

    //品牌
    public String Brand ;

    //1.份  2.斤  
    //计量方式
    public String Unit ;

    //价格 
    public double Price ;

    //包含不同规格
    public boolean HasItems ;


    //0 Percentage, 1 Fixed
    public int TaxType ;

    public double TaxRate ;

    //推荐指数
    public int Star ;

    //辣味程度
    public int Hot ;

    //简述
    public String Description ;

    //缩略图文件名
    public String ImageUrl ;

    //点单次数
    public int Counter ;

    //排序
    public int Sort ;

    //是否热销
//    public boolean IsHotSale ;
//
//    //售罄
//    public boolean IsSoldout ;
//
//    //有效的
//    public boolean IsEnabled ;

    //站点
    public int ShopId ;


    //配料，或套餐明细
    @Transient
    public List<FoodMenuItem> ProductSkus ;
    @Transient
    public List<FoodMenuProperty> ProductProperties ;

    public boolean hasSkus() {
        return ProductSkus != null && !ProductSkus.isEmpty();
    }

    public double getMinPrice() {
        if (hasSkus()) {
            double price = Double.MAX_VALUE;
            for (FoodMenuItem sku : ProductSkus)
                price = Math.min(price, sku.Price);
            return price;
        }
        return Price;
    }

    public boolean hasProperties() {
        return ProductProperties != null && !ProductProperties.isEmpty();
    }

    public FoodMenu() {
        Sort = 10000;
        ProductSkus = new ArrayList<>();
        ProductProperties = new ArrayList<>();
    }

    public int getFoodMenuId() {
        return FoodMenuId;
    }

    public void setFoodMenuId(int foodMenuId) {
        FoodMenuId = foodMenuId;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

//    public boolean getIsIngredient() {
//        return IsIngredient;
//    }
//
//    public void setIngredient(boolean ingredient) {
//        IsIngredient = ingredient;
//    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }

    public String getBrand() {
        return Brand;
    }

    public void setBrand(String brand) {
        Brand = brand;
    }

    public String getUnit() {
        return Unit;
    }

    public void setUnit(String unit) {
        Unit = unit;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public boolean isHasItems() {
        return HasItems;
    }

    public void setHasItems(boolean hasItems) {
        HasItems = hasItems;
    }

    public int getTaxType() {
        return TaxType;
    }

    public void setTaxType(int taxType) {
        TaxType = taxType;
    }

    public double getTaxRate() {
        return TaxRate;
    }

    public void setTaxRate(double taxRate) {
        TaxRate = taxRate;
    }

    public int getStar() {
        return Star;
    }

    public void setStar(int star) {
        Star = star;
    }

    public int getHot() {
        return Hot;
    }

    public void setHot(int hot) {
        Hot = hot;
    }

    public String getDescription() {
        return Description;
    }

    public void setDescription(String description) {
        Description = description;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

    public int getCounter() {
        return Counter;
    }

    public void setCounter(int counter) {
        Counter = counter;
    }

    public int getSort() {
        return Sort;
    }

    public void setSort(int sort) {
        Sort = sort;
    }

//    public boolean getIsHotSale() {
//        return IsHotSale;
//    }
//
//    public void setHotSale(boolean hotSale) {
//        IsHotSale = hotSale;
//    }
//
//    public boolean getIsSoldout() {
//        return IsSoldout;
//    }
//
//    public void setSoldout(boolean soldout) {
//        IsSoldout = soldout;
//    }
//
//    public boolean getIsEnabled() {
//        return IsEnabled;
//    }
//
//    public void setEnabled(boolean enabled) {
//        IsEnabled = enabled;
//    }

    public int getShopId() {
        return ShopId;
    }

    public void setShopId(int shopId) {
        ShopId = shopId;
    }
}
