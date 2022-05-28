package cn.zicoo.ir2teledemo.model;

import org.kymjs.kjframe.database.annotate.Id;
import org.kymjs.kjframe.database.annotate.Index;

import java.util.Date;

public class Customer {
    @Id
    public int CustomerId;
    @Index
    public String CardNO;
    //名称

    public String Name;
    public int CategoryId;
    @Index
    public String Telephone;

    public String LettetIndex;
    public int OrderCount;

    public String Address;

    public String Avatar;
    public String EmailAddress;
    public Date CreateDate;

    public Date UpdateDate;

    public Customer() {
        CreateDate = new Date();
        UpdateDate = new Date();
    }

    public int getCustomerId() {
        return CustomerId;
    }

    public void setCustomerId(int customerId) {
        CustomerId = customerId;
    }

    public String getCardNO() {
        return CardNO;
    }

    public void setCardNO(String cardNO) {
        CardNO = cardNO;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getCategoryId() {
        return CategoryId;
    }

    public void setCategoryId(int categoryId) {
        CategoryId = categoryId;
    }

    public String getAvatar() {
        return Avatar;
    }

    public void setAvatar(String avatar) {
        Avatar = avatar;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getLettetIndex() {
        return LettetIndex;
    }

    public void setLettetIndex(String lettetIndex) {
        LettetIndex = lettetIndex;
    }

    public int getOrderCount() {
        return OrderCount;
    }

    public void setOrderCount(int orderCount) {
        OrderCount = orderCount;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
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
}
