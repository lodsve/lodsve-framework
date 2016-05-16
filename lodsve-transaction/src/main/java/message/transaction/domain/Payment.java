package message.transaction.domain;

import java.io.Serializable;
import java.util.Date;
import message.transaction.enums.TradeChannel;
import message.transaction.enums.TradeResult;
import message.transaction.enums.TradeType;

/**
 * ﻿交易.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/19 下午2:26
 */
public class Payment implements Serializable {
    private Long id;
    /**
     * 冗余字段,targetId进行MD5加密后,保证数据唯一
     */
    private String unionId;
    private String targetId;
    private String description;
    private String productName;
    private TradeType tradeType = TradeType.TRADE_CONSUME;
    private String payAccount;
    private Long amount;
    private TradeChannel tradeChannel = TradeChannel.WX_PUB;
    private Long userId;
    private String userName;
    private Date orderTime;
    private Date completeTime;
    private TradeResult tradeResult = TradeResult.NO;
    private String chargeId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUnionId() {
        return unionId;
    }

    public void setUnionId(String unionId) {
        this.unionId = unionId;
    }

    public String getTargetId() {
        return targetId;
    }

    public void setTargetId(String targetId) {
        this.targetId = targetId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public TradeType getTradeType() {
        return tradeType;
    }

    public void setTradeType(TradeType tradeType) {
        this.tradeType = tradeType;
    }

    public String getPayAccount() {
        return payAccount;
    }

    public void setPayAccount(String payAccount) {
        this.payAccount = payAccount;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    public TradeChannel getTradeChannel() {
        return tradeChannel;
    }

    public void setTradeChannel(TradeChannel tradeChannel) {
        this.tradeChannel = tradeChannel;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public Date getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Date completeTime) {
        this.completeTime = completeTime;
    }

    public TradeResult getTradeResult() {
        return tradeResult;
    }

    public void setTradeResult(TradeResult tradeResult) {
        this.tradeResult = tradeResult;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }
}
