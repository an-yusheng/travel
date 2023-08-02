package com.ruoyi.travel.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.travel.domain.Attractions;
import com.ruoyi.travel.mapper.AttractionsMapper;
import com.ruoyi.travel.mapper.PreferenceMapper;
import com.ruoyi.travel.service.TravelManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 旅行管理实现
 *
 * @author
 */
@Slf4j
@Service
public class TravelManagerServiceImpl implements TravelManagerService {


    @Autowired
    private AttractionsMapper attractionsMapper;

    @Autowired
    private PreferenceMapper preferenceMapper;

    @Override
    public List<Attractions> selectAttractionsList(Attractions attractions) {
        List<Attractions> list = attractionsMapper.selectList(attractions);
        list.forEach(attractions1 -> {
            String[] type = attractions1.getPreferenceType().split(",");
            attractions1.setType(type);
            if (type.length>0){
                String[] names = preferenceMapper.selectByName(type);
                attractions1.setTypeName(names);
            }
        });
        return list;
    }

    @Override
    public AjaxResult saveAttractions(Attractions attractions) {
        attractions.setCreateDate(new Date());
        attractions.setUpdateDate(new Date());
        if (attractions.getType().length>0){
            attractions.setPreferenceType(StringUtils.join(attractions.getType(), ","));
        }
        attractionsMapper.insertSelective(attractions);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult removeAttractions(Attractions attractions) {
        attractionsMapper.deleteByPrimaryKey(attractions.getId().longValue());
        return AjaxResult.success();
    }

    @Override
    public AjaxResult editAttractions(Attractions attractions) {
        attractions.setUpdateDate(new Date());
        if (attractions.getType().length>0){
            attractions.setPreferenceType(StringUtils.join(attractions.getType(), ","));
        }
        attractionsMapper.updateByPrimaryKeySelective(attractions);
        return AjaxResult.success();
    }
}
