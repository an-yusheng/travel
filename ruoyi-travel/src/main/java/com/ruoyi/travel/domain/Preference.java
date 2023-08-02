package com.ruoyi.travel.domain;

import java.io.Serializable;

/**
 * 旅行偏好表
 * @TableName preference
 */
public class Preference implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 偏好类型
     */
    private Integer preferenceType;

    /**
     * 偏好名称
     */
    private String preferenceName;

    /**
     * 季节类型1:春、2:夏、3:秋、4:冬
     */
    private Integer season;

    /**
     * 季节名称
     */
    private String seasonName;

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
     * 偏好类型
     */
    public Integer getPreferenceType() {
        return preferenceType;
    }

    /**
     * 偏好类型
     */
    public void setPreferenceType(Integer preferenceType) {
        this.preferenceType = preferenceType;
    }

    /**
     * 偏好名称
     */
    public String getPreferenceName() {
        return preferenceName;
    }

    /**
     * 偏好名称
     */
    public void setPreferenceName(String preferenceName) {
        this.preferenceName = preferenceName;
    }

    /**
     * 季节类型1:春、2:夏、3:秋、4:冬
     */
    public Integer getSeason() {
        return season;
    }

    /**
     * 季节类型1:春、2:夏、3:秋、4:冬
     */
    public void setSeason(Integer season) {
        this.season = season;
    }

    /**
     * 季节名称
     */
    public String getSeasonName() {
        return seasonName;
    }

    /**
     * 季节名称
     */
    public void setSeasonName(String seasonName) {
        this.seasonName = seasonName;
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
        Preference other = (Preference) that;
        return (this.getId() == null ? other.getId() == null : this.getId().equals(other.getId()))
            && (this.getPreferenceType() == null ? other.getPreferenceType() == null : this.getPreferenceType().equals(other.getPreferenceType()))
            && (this.getPreferenceName() == null ? other.getPreferenceName() == null : this.getPreferenceName().equals(other.getPreferenceName()))
            && (this.getSeason() == null ? other.getSeason() == null : this.getSeason().equals(other.getSeason()))
            && (this.getSeasonName() == null ? other.getSeasonName() == null : this.getSeasonName().equals(other.getSeasonName()));
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((getId() == null) ? 0 : getId().hashCode());
        result = prime * result + ((getPreferenceType() == null) ? 0 : getPreferenceType().hashCode());
        result = prime * result + ((getPreferenceName() == null) ? 0 : getPreferenceName().hashCode());
        result = prime * result + ((getSeason() == null) ? 0 : getSeason().hashCode());
        result = prime * result + ((getSeasonName() == null) ? 0 : getSeasonName().hashCode());
        return result;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append(" [");
        sb.append("Hash = ").append(hashCode());
        sb.append(", id=").append(id);
        sb.append(", preferenceType=").append(preferenceType);
        sb.append(", preferenceName=").append(preferenceName);
        sb.append(", season=").append(season);
        sb.append(", seasonName=").append(seasonName);
        sb.append(", serialVersionUID=").append(serialVersionUID);
        sb.append("]");
        return sb.toString();
    }
}