package cn.zicoo.ir2teledemo.model;

import org.kymjs.kjframe.database.annotate.Id;
import org.kymjs.kjframe.database.annotate.Index;

import java.util.Date;

public class OrderItem {
    @Id
    public int OrderItemId ;
    @Index
    public int OrderId ;

    //打印机ID
    public int PrinterId ;

    //绑定的终端号
    public int MachineNO ;

    //是否套餐
    public int GoodsType ;

    //品名
    public String Name ;

    //品名
    public int RelationId ;

    //数量
    public int Quantity ;

    //产品单价
    public double Price ;

    public double Tax ;
    //小计
    public double SubTotal ;

    //退单
    public boolean IsReturn ;

    //备注
    public String Remark ;
    //选择的规格属性
    public String Properties;
    //创建日期
    public Date CreateDate;

    //修改时间
    public Date UpdateDate ;

    //店铺
    public int ShopId ;

    public OrderItem() {
        CreateDate = new Date();
        UpdateDate = new Date();
        Quantity = 1;
    }

    public int getOrderItemId() {
        return OrderItemId;
    }

    public void setOrderItemId(int orderItemId) {
        OrderItemId = orderItemId;
    }

    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getProperties() {
        return Properties;
    }

    public void setProperties(String properties) {
        Properties = properties;
    }

    public int getPrinterId() {
        return PrinterId;
    }

    public void setPrinterId(int printerId) {
        PrinterId = printerId;
    }

    public int getMachineNO() {
        return MachineNO;
    }

    public void setMachineNO(int machineNO) {
        MachineNO = machineNO;
    }

    public int getGoodsType() {
        return GoodsType;
    }

    public void setGoodsType(int goodsType) {
        GoodsType = goodsType;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getRelationId() {
        return RelationId;
    }

    public void setRelationId(int relationId) {
        RelationId = relationId;
    }

    public int getQuantity() {
        return Quantity;
    }

    public void setQuantity(int quantity) {
        Quantity = quantity;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(double subTotal) {
        SubTotal = subTotal;
    }

    public boolean isIsReturn() {
        return IsReturn;
    }

    public void setIsReturn(boolean aReturn) {
        IsReturn = aReturn;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public Date getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(Date createDate) {
        CreateDate = createDate;
    }

    public Date getUpdateDate() {
        return UpdateDate;
    }

    public void setUpdateDate(Date updateDate) {
        UpdateDate = updateDate;
    }

    public int getShopId() {
        return ShopId;
    }

    public void setShopId(int shopId) {
        ShopId = shopId;
    }

    public double getTax() {
        return Tax;
    }

    public void setTax(double tax) {
        Tax = tax;
    }
}
