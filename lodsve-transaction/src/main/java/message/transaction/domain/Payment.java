package message.transaction.domain;

import message.transaction.enums.TradeChannel;
import message.transaction.enums.TradeResult;
import message.transaction.enums.TradeType;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.util.Date;

/**
 * ﻿交易.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/19 下午2:26
 */
@Table(name = "t_payment")
public class Payment implements Serializable {
    @Id
    @GeneratedValue(generator = "s_payment")
    private Long id;
    /**
     * 冗余字段,targetId进行MD5加密后,保证数据唯一
     */
    @Column(unique = true)
    private String unionId;
    @Column
    private String targetId;
    @Column
    private String description;
    @Column
    private String productName;
    @Column
    private TradeType tradeType = TradeType.TRADE_CONSUME;
    @Column
    private String payAccount;
    @Column
    private Long amount;
    @Column
    private TradeChannel tradeChannel = TradeChannel.WX_PUB;
    @Column
    private Long userId;
    @Column
    private String userName;
    @Column
    private Date orderTime;
    @Column
    private Date completeTime;
    @Column
    private TradeResult tradeResult = TradeResult.NO;
    @Column
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
