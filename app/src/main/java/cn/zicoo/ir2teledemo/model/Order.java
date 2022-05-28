package cn.zicoo.ir2teledemo.model;

import org.kymjs.kjframe.database.annotate.Id;
import org.kymjs.kjframe.database.annotate.Index;
import org.kymjs.kjframe.database.annotate.Transient;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class Order {
    @Id
    public int OrderId ;
    //操作员ID
    public String Name;

    //单据号
    public int ReferenceNO;

    //发票号
    public String InvoiceNO;

    //Actions员ID
    public String Username;
    //绑定的终端号
    public int MachineNO;

    //客户
    @Index
    public int CustomerId ;
    //数据同步识别码
    //订单合计
    public double SubTotal ;

    //优惠金额
    public double Discount ;

    //人数
    public int People ;

    //投送模式
    public int DeliveryMethod ;

    //配送费
    public double DeliveryCharge ;

    //打包费
    public double PickupCharge ;

    //服务费税率
    public double TipsRate ;

    //服务费
    public double TipsTotal ;

    //支付金额
    public double Cash ;

    //找零金额
    public double Due ;

    //税费
    public double TaxTotal ;

    //其他费用
    public double OtherFee ;

    //实收，毛收入
    public double Amount ;

    //支付方法
    public int PaymentMethod ;

    //订单来源
    public int OrderFrom ;

    //创建日期
    public Date CreateDate ;

    //修改时间
    public Date UpdateDate ;

    //配送时间
    public Date DeliveryDate;

    public int TableId;

    //订单支付完成
    public boolean IsPaid ;

    //退款金额
    public double ReturnTotal ;

    //处理状态
    public int OrderStatus ;

    //是否已经同步
    public boolean Synchronized ;

    //备注
    public String Remark ;


    //商家
    public int ShopId ;

    @Transient
    public List<OrderItem> Items;


    public int getOrderId() {
        return OrderId;
    }

    public void setOrderId(int orderId) {
        OrderId = orderId;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getReferenceNO() {
        return ReferenceNO;
    }

    public void setReferenceNO(int referenceNO) {
        ReferenceNO = referenceNO;
    }

    public String getInvoiceNO() {
        return InvoiceNO;
    }

    public void setInvoiceNO(String invoiceNO) {
        InvoiceNO = invoiceNO;
    }

    public String getUsername() {
        return Username;
    }

    public void setUsername(String username) {
        Username = username;
    }

    public int getMachineNO() {
        return MachineNO;
    }

    public void setMachineNO(int machineNO) {
        MachineNO = machineNO;
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public double getSubTotal() {
        return SubTotal;
    }

    public void setSubTotal(double subTotal) {
        SubTotal = subTotal;
    }

    public double getDiscount() {
        return Discount;
    }

    public void setDiscount(double discount) {
        Discount = discount;
    }

    public int getPeople() {
        return People;
    }

    public void setPeople(int people) {
        People = people;
    }

    public int getDeliveryMethod() {
        return DeliveryMethod;
    }

    public void setDeliveryMethod(int deliveryMethod) {
        DeliveryMethod = deliveryMethod;
    }

    public double getDeliveryCharge() {
        return DeliveryCharge;
    }

    public void setDeliveryCharge(double deliveryCharge) {
        DeliveryCharge = deliveryCharge;
    }

    public double getPickupCharge() {
        return PickupCharge;
    }

    public void setPickupCharge(double pickupCharge) {
        PickupCharge = pickupCharge;
    }

    public double getTipsRate() {
        return TipsRate;
    }

    public void setTipsRate(double tipsRate) {
        TipsRate = tipsRate;
    }

    public double getTipsTotal() {
        return TipsTotal;
    }

    public void setTipsTotal(double tipsTotal) {
        TipsTotal = tipsTotal;
    }

    public double getCash() {
        return Cash;
    }

    public void setCash(double cash) {
        Cash = cash;
    }

    public double getDue() {
        return Due;
    }

    public void setDue(double due) {
        Due = due;
    }

    public double getTaxTotal() {
        return TaxTotal;
    }

    public void setTaxTotal(double taxTotal) {
        TaxTotal = taxTotal;
    }

    public double getOtherFee() {
        return OtherFee;
    }

    public void setOtherFee(double otherFee) {
        OtherFee = otherFee;
    }

    public double getAmount() {
        return Amount;
    }

    public void setAmount(double amount) {
        Amount = amount;
    }

    public int getPaymentMethod() {
        return PaymentMethod;
    }

    public void setPaymentMethod(int paymentMethod) {
        PaymentMethod = paymentMethod;
    }

    public int getOrderFrom() {
        return OrderFrom;
    }

    public void setOrderFrom(int orderFrom) {
        OrderFrom = orderFrom;
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

    public Date getDeliveryDate() {
        return DeliveryDate;
    }

    public void setDeliveryDate(Date deliveryDate) {
        DeliveryDate = deliveryDate;
    }

    public int getTableId() {
        return TableId;
    }

    public void setTableId(int tableId) {
        TableId = tableId;
    }

    public boolean isIsPaid() {
        return IsPaid;
    }

    public void setIsPaid(boolean paid) {
        IsPaid = paid;
    }

    public double getReturnTotal() {
        return ReturnTotal;
    }

    public void setReturnTotal(double returnTotal) {
        ReturnTotal = returnTotal;
    }

    public int getOrderStatus() {
        return OrderStatus;
    }

    public void setOrderStatus(int orderStatus) {
        OrderStatus = orderStatus;
    }

    public boolean isSynchronized() {
        return Synchronized;
    }

    public void setSynchronized(boolean aSynchronized) {
        Synchronized = aSynchronized;
    }

    public String getRemark() {
        return Remark;
    }

    public void setRemark(String remark) {
        Remark = remark;
    }

    public int getShopId() {
        return ShopId;
    }

    public void setShopId(int shopId) {
        ShopId = shopId;
    }

    public Order() {
        Items = new ArrayList<>();
        CreateDate = new Date();
        UpdateDate = new Date();
    }
}
