package cn.zicoo.ir2teledemo.model;

import org.apache.commons.lang3.StringUtils;
import org.kymjs.kjframe.database.annotate.Id;

import java.util.ArrayList;
import java.util.List;

public class AppSetting {
    @Id
    public String AppSettingId;

    //店铺名称

    public String ShopName;
    //logo

    public String Logo;
    //电话

    public String Telephone;
    //地址

    public String ShopAddress;
    //税率
    public double Tax;
    //服务费
    public double TipsRate;
    //最低服务费
    public double MinTips;

    public String Locale;

    //邮箱配置

    public String EmailAddress;

    public String FromName;

    public String SmtpHostName;
    public int SmtpHostPort;
    public boolean SmtpEnableSsl;

    public String Account;

    public String Password;

    //默认规格
    public String DefaultSkus;
    //默认属性

    public String DefaultProperties;

    //打印设置

    public String PrinterIp;
    public int PrinterPort;

    public String PrinterCharset;

    public int GuestOrderCount;

    public String getAppSettingId() {
        return AppSettingId;
    }

    public void setAppSettingId(String appSettingId) {
        AppSettingId = appSettingId;
    }

    public String getShopName() {
        return ShopName;
    }

    public void setShopName(String shopName) {
        ShopName = shopName;
    }

    public String getLogo() {
        return Logo;
    }

    public void setLogo(String logo) {
        Logo = logo;
    }

    public String getTelephone() {
        return Telephone;
    }

    public void setTelephone(String telephone) {
        Telephone = telephone;
    }

    public String getShopAddress() {
        return ShopAddress;
    }

    public void setShopAddress(String shopAddress) {
        ShopAddress = shopAddress;
    }

    public double getTax() {
        return Tax;
    }

    public void setTax(double tax) {
        Tax = tax;
    }

    public double getTipsRate() {
        return TipsRate;
    }

    public void setTipsRate(double tipsRate) {
        TipsRate = tipsRate;
    }

    public double getMinTips() {
        return MinTips;
    }

    public void setMinTips(double minTips) {
        MinTips = minTips;
    }

    public String getLocale() {
        return Locale;
    }

    public void setLocale(String locale) {
        Locale = locale;
    }

    public String getEmailAddress() {
        return EmailAddress;
    }

    public void setEmailAddress(String emailAddress) {
        EmailAddress = emailAddress;
    }

    public String getFromName() {
        return FromName;
    }

    public void setFromName(String fromName) {
        FromName = fromName;
    }

    public String getSmtpHostName() {
        return SmtpHostName;
    }

    public void setSmtpHostName(String smtpHostName) {
        SmtpHostName = smtpHostName;
    }

    public int getSmtpHostPort() {
        return SmtpHostPort;
    }

    public void setSmtpHostPort(int smtpHostPort) {
        SmtpHostPort = smtpHostPort;
    }

    public boolean isSmtpEnableSsl() {
        return SmtpEnableSsl;
    }

    public void setSmtpEnableSsl(boolean smtpEnableSsl) {
        SmtpEnableSsl = smtpEnableSsl;
    }

    public String getAccount() {
        return Account;
    }

    public void setAccount(String account) {
        Account = account;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getDefaultSkus() {
        return DefaultSkus;
    }

    public void setDefaultSkus(String defaultSkus) {
        DefaultSkus = defaultSkus;
    }

    public String getDefaultProperties() {
        return DefaultProperties;
    }

    public void setDefaultProperties(String defaultProperties) {
        DefaultProperties = defaultProperties;
    }

    public String getPrinterIp() {
        return PrinterIp;
    }

    public void setPrinterIp(String printerIp) {
        PrinterIp = printerIp;
    }

    public int getPrinterPort() {
        return PrinterPort;
    }

    public void setPrinterPort(int printerPort) {
        PrinterPort = printerPort;
    }

    public String getPrinterCharset() {
        return PrinterCharset;
    }

    public void setPrinterCharset(String printerCharset) {
        PrinterCharset = printerCharset;
    }

    public int getGuestOrderCount() {
        return GuestOrderCount;
    }

    public void setGuestOrderCount(int guestOrderCount) {
        GuestOrderCount = guestOrderCount;
    }

    public String[] ParseSkus()
    {
        if (StringUtils.isNotBlank(DefaultSkus))
            return DefaultSkus.split( ",");
        return new String[0];
    }

    public List<String[]> ParseProperties()
    {
        List<String[]> properties = new ArrayList<>();
        if (StringUtils.isNotBlank(DefaultProperties))
        {
            String[] array = StringUtils.split(DefaultProperties,'|');
            for (String anArray : array) {
                String[] namevalues = anArray.split("[,，]");
                if (namevalues.length >= 2) {
                    properties.add(namevalues);
                }
            }
        }
        return properties;
    }
}
