package com.ruoyi.travel.dto;

import lombok.Data;

import java.util.Date;

/**
 * @author
 * @description TODO
 */
@Data
public class AttractionsQuery {


    /**
     * 景点名称
     */
    private String name;

    private String address;

    private Date biginDate;

    private Date endDate;

}
