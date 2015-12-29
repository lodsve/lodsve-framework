package message.transaction.domain;

import message.transaction.enums.TradeChannel;
import message.transaction.enums.TradeResult;
import message.transaction.enums.TradeType;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * ﻿交易.
 *
 * @author sunhao(sunhao.java@gmail.com)
 * @version V1.0, 15/8/19 下午2:26
 */
@Table(name = "t_payment")
public class Payment implements Serializable {
    @Id
    private Long id;
    /**
     * 冗余字段,targetId进行MD5加密后,保证数据唯一
     */
    @Column(unique = true)
    private String unionId;
    @Column(name = "order_id", length = 3000)
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
    private TradeChannel payCode = TradeChannel.WX_PUB;
    @Column
    private Long userId;
    @Column
    private String userName;
    @Column
    private Long orderTime;
    @Column
    private Long completeTime;
    @Column
    private TradeResult status = TradeResult.NO;
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

    public TradeChannel getPayCode() {
        return payCode;
    }

    public void setPayCode(TradeChannel payCode) {
        this.payCode = payCode;
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

    public Long getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Long orderTime) {
        this.orderTime = orderTime;
    }

    public Long getCompleteTime() {
        return completeTime;
    }

    public void setCompleteTime(Long completeTime) {
        this.completeTime = completeTime;
    }

    public TradeResult getStatus() {
        return status;
    }

    public void setStatus(TradeResult status) {
        this.status = status;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }
}
