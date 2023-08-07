package com.ruoyi.travel.domain;

import java.io.Serializable;

/**
 * 
 * @TableName user_preference
 */
public class UserPreference implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 用户id
     */
    private Integer userId;

    /**
     * 自然与冒险偏好
     */
    private String preference1;

    /**
     * 文化与历史偏好
     */
    private String preference2;

    /**
     * 美食与品酒偏好
     */
    private String preference3;

    /**
     * 休闲与度假偏好
     */
    private String preference4;

    /**
     * 娱乐与购物偏好
     */
    private String preference5;

    /**
     * 家庭与亲子偏好
     */
    private String preference6;

    private static final long serialVersionUID = 1L;

    /**
     * 
     */
    public Integer getId() {
        return id;
    }

    /**
     * 
     */
    public void setId(Integer id) {
        this.id = id;
    }

    /**
     * 用户id
     */
    public Integer getUserId() {
        return userId;
    }

    /**
     * 用户id
     */
    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    /**
     * 自然与冒险偏好
     */
    public String getPreference1() {
        return preference1;
    }

    /**
     * 自然与冒险偏好
     */
    public void setPreference1(String preference1) {
        this.preference1 = preference1;
    }

    /**
     * 文化与历史偏好
     */
    public String getPreference2() {
        return preference2;
    }

    /**
     * 文化与历史偏好
     */
    public void setPreference2(String preference2) {
        this.preference2 = preference2;
    }

    /**
     * 美食与品酒偏好
     */
    public String getPreference3() {
        return preference3;
    }

    /**
     * 美食与品酒偏好
     */
    public void setPreference3(String preference3) {
        this.preference3 = preference3;
    }

    /**
     * 休闲与度假偏好
     */
    public String getPreference4() {
        return preference4;
    }

    /**
     * 休闲与度假偏好
     */
    public void setPreference4(String preference4) {
        this.preference4 = preference4;
    }

    /**
     * 娱乐与购物偏好
     */
    public String getPreference5() {
        return preference5;
    }

    /**
     * 娱乐与购物偏好
     */
    public void setPreference5(String preference5) {
        this.preference5 = preference5;
    }

    /**
     * 家庭与亲子偏好
     */
    public String getPreference6() {
        return preference6;
    }

    /**
     * 家庭与亲子偏好
     */
    public void setPreference6(String preference6) {
        this.preference6 = preference6;
    }

    @Override
    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null) {
            return false;
        }
        if (getClass() != that.getClass()) {
            return false;
        }
        UserPreference other = (UserPreference) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getUserId() == null ? other.getUserId() == null : this.getUserId().equals(other.getUserId()))
            && (this.getPreference1() == null ? other.getPreference1() == null : this.getPreference1().equals(other.getPreference1()))
            && (this.getPreference2() == null ? other.getPreference2() == null : this.getPreference2().equals(other.getPreference2()))
            && (this.getPreference3() == null ? other.getPreference3() == null : this.getPreference3().equals(other.getPreference3()))
            && (this.getPreference4() == null ? other.getPreference4() == null : this.getPreference4().equals(other.getPreference4()))
            && (this.getPreference5() == null ? other.getPreference5() == null : this.getPreference5().equals(other.getPreference5()))
            && (this.getPreference6() == null ? other.getPreference6() == null : this.getPreference6().equals(other.getPreference6()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getUserId() == null) ? 0 : getUserId().hashCode());
        result = prime * result + ((getPreference1() == null) ? 0 : getPreference1().hashCode());
        result = prime * result + ((getPreference2() == null) ? 0 : getPreference2().hashCode());
        result = prime * result + ((getPreference3() == null) ? 0 : getPreference3().hashCode());
        result = prime * result + ((getPreference4() == null) ? 0 : getPreference4().hashCode());
        result = prime * result + ((getPreference5() == null) ? 0 : getPreference5().hashCode());
        result = prime * result + ((getPreference6() == null) ? 0 : getPreference6().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", userId=").append(userId);
        sb.append(", preference1=").append(preference1);
        sb.append(", preference2=").append(preference2);
        sb.append(", preference3=").append(preference3);
        sb.append(", preference4=").append(preference4);
        sb.append(", preference5=").append(preference5);
        sb.append(", preference6=").append(preference6);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}