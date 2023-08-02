package com.ruoyi.travel.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.io.Serializable;
import java.util.Date;

/**
 * 景点
 * @TableName attractions
 */
@Data
public class Attractions implements Serializable {
    /**
     * 
     */
    private Integer id;

    /**
     * 景点名称
     */
    private String name;

    /**
     * 景点图片
     */
    private String image;

    /**
     * 景点描述
     */
    private String desc;

    /**
     * 推荐级别
     */
    private Integer recommended;

    /**
     * 景点地址
     */
    private String address;

    /**
     * 偏好类型
     */
    private String preferenceType;

    /**
     * 春天分数
     */
    private Integer seasonSpring;

    /**
     * 夏天分数
     */
    private Integer seasonSummer;

    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date createDate;

    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss")
    private Date updateDate;

    /**
     * 秋天分数
     */
    private Integer seasonAutumn;

    /**
     * 冬天分数
     */
    private Integer seasonWinter;

    private String[] type;

    private String[] typeName;
}