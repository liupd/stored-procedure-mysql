package com.wa.edu.domain;

import java.io.Serializable;

/**
 * Created by liupd on 2014/6/7.
 * 年级
 */

public class Grade implements Serializable {

    private String gradeName;

    private Integer orderNo;

    public String getGradeName() {
        return gradeName;
    }

    public void setGradeName(String gradeName) {
        this.gradeName = gradeName;
    }

    public Integer getOrderNo() {
        return orderNo;
    }

    public void setOrderNo(Integer orderNo) {
        this.orderNo = orderNo;
    }

    @Override
    public String toString() {
        return "Grade{" +
                "gradeName='" + gradeName + '\'' +
                ", orderNo=" + orderNo +
                '}';
    }
}
