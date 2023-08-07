package com.ruoyi.travel.service.impl;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.travel.domain.Attractions;
import com.ruoyi.travel.domain.AttractionsUser;
import com.ruoyi.travel.domain.UserPreference;
import com.ruoyi.travel.dto.AttractionsQuery;
import com.ruoyi.travel.mapper.AttractionsMapper;
import com.ruoyi.travel.mapper.AttractionsUserMapper;
import com.ruoyi.travel.mapper.PreferenceMapper;
import com.ruoyi.travel.mapper.UserPreferenceMapper;
import com.ruoyi.travel.service.TravelManagerService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private UserPreferenceMapper userPreferenceMapper;

    @Override
    public List<Attractions> selectAttractionsList(Attractions attractions) {
        List<Attractions> list = attractionsMapper.selectList(attractions);
        list.forEach(attractions1 -> {
            String[] type = {};
            if (StringUtils.isNotEmpty(attractions1.getPreferenceType())){
                type = attractions1.getPreferenceType().split(",");
                if (type.length>0){
                    String[] names = preferenceMapper.selectByName(type);
                    attractions1.setTypeName(names);
                }
            }
            attractions1.setType(type);
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

    @Override
    public AjaxResult updatePreference(UserPreference userPreference) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        UserPreference res = userPreferenceMapper.selectByUserId(user.getUserId());
        userPreference.setUserId(user.getUserId().intValue());
        if (Objects.isNull(res)){
            userPreferenceMapper.insertSelective(userPreference);
        }else{
            userPreferenceMapper.updateByPrimaryKeySelective(userPreference);
        }
        return AjaxResult.success();
    }

    @Override
    public UserPreference selectUserPreference() {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        return userPreferenceMapper.selectByUserId(user.getUserId());
    }

    @Override
    public List<Attractions> selectCpmputList(AttractionsQuery query) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        UserPreference userPreference = userPreferenceMapper.selectByUserId(user.getUserId());
        List<Attractions> resList = attractionsMapper.selectList(new Attractions(){{
            setName(query.getName());
        }});
        long daysBetween = -1;
        LocalDate startDate = null;
        if (query.getBiginDate() != null && query.getEndDate() != null){
            startDate = getLocalDate(query.getBiginDate());
            LocalDate endDate = getLocalDate(query.getEndDate());
            daysBetween = ChronoUnit.DAYS.between(startDate, endDate);
            daysBetween = daysBetween == 0 ? -1 : daysBetween;
        }
        long finalDaysBetween = daysBetween;
        LocalDate finalStartDate = startDate;
        resList.forEach(attractions -> {
            /**
             * 获取标签
             */
            String[] tagType = {};
            if (StringUtils.isNotEmpty(attractions.getPreferenceType())){
                tagType = attractions.getPreferenceType().split(",");
                if (tagType.length>0){
                    String[] names = preferenceMapper.selectByName(tagType);
                    attractions.setTypeName(names);
                }
            }
            attractions.setType(tagType);
            /**
             * 获取是否记录景点
             */
            AttractionsUser attractionsUser = attractionsUserMapper.selectByUserId(new AttractionsUser(){{
                setUserId(user.getUserId().intValue());
                setAttractionsId(attractions.getId());
            }});
            attractions.setWish(attractionsUser == null ? false : true);
            /**
             * 计算季节得分
             */
            HashMap<String,Integer> hashMap = new HashMap();
            hashMap.put("1",attractions.getSeasonSpring());
            hashMap.put("2",attractions.getSeasonSummer());
            hashMap.put("3",attractions.getSeasonAutumn());
            hashMap.put("4",attractions.getSeasonWinter());
            hashMap.put("0",0);
            String score = "1";
            for (int i = 0; i <= finalDaysBetween; i++) {
                LocalDate date = finalStartDate.plusDays(i);
                String season = getSeason(date);
                // 季节分数/天数=每日得分
                BigDecimal bigDecimal = new BigDecimal(hashMap.get(season)).divide(new BigDecimal(finalDaysBetween)).add(new BigDecimal(score));
                score = bigDecimal.toString();
            }
            /**
             * 计算偏好权重得分
             */
            HashMap<String,String> preferenceMap = new HashMap();
            preferenceMap.put("1",StringUtils.isEmpty(userPreference.getPreference1()) ? "0" : userPreference.getPreference1());
            preferenceMap.put("2",StringUtils.isEmpty(userPreference.getPreference2()) ? "0" : userPreference.getPreference2());
            preferenceMap.put("3",StringUtils.isEmpty(userPreference.getPreference3()) ? "0" : userPreference.getPreference3());
            preferenceMap.put("4",StringUtils.isEmpty(userPreference.getPreference4()) ? "0" : userPreference.getPreference4());
            preferenceMap.put("5",StringUtils.isEmpty(userPreference.getPreference5()) ? "0" : userPreference.getPreference5());
            preferenceMap.put("6",StringUtils.isEmpty(userPreference.getPreference6()) ? "0" : userPreference.getPreference6());
            double dscore = 0.00;
            if (StringUtils.isNotEmpty(attractions.getPreferenceType())){
                String wscore = "0";
                String[] types = attractions.getPreferenceType().split(",");
                for (String type:types) {
                    String c = preferenceMap.get(type);
                    BigDecimal b = new BigDecimal(Double.valueOf(c));
                    BigDecimal bigDecimal = new BigDecimal(score)
                            .multiply(b)
                            .add(new BigDecimal(wscore));
                    wscore = bigDecimal.toString();
                }
                BigDecimal bigDecimal = new BigDecimal(wscore).divide(new BigDecimal(wscore.length()), 4, BigDecimal.ROUND_HALF_UP);
                dscore = bigDecimal.doubleValue();
            }
            attractions.setScore(dscore);
        });
        List collect = resList.stream().sorted((s1, s2) -> s2.getScore().compareTo(s1.getScore())).collect(Collectors.toList());

        return collect;
    }

    @Autowired
    private AttractionsUserMapper attractionsUserMapper;

    @Override
    public AjaxResult saveAttractionsUser(AttractionsUser attractions) {
            SysUser user = SecurityUtils.getLoginUser().getUser();
            attractions.setUserId(user.getUserId().intValue());
        attractions.setCreateDate(new Date());
            attractionsUserMapper.insertSelective(attractions);
        return AjaxResult.success();
    }

    @Override
    public AjaxResult removeAttractionsUser(AttractionsUser attractions) {
        SysUser user = SecurityUtils.getLoginUser().getUser();
        attractions.setUserId(user.getUserId().intValue());
        attractionsUserMapper.deleteByUserId(attractions);
        return AjaxResult.success();
    }


    public static LocalDate getLocalDate(Date date){
        Instant instant = date.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();
        return localDate;
    }

    public static String getSeason(LocalDate date) {
        int month = date.getMonthValue();
        int day = date.getDayOfMonth();

        if (month == 1 || month == 2 || month == 3) {
            return "1";
        } else if (month == 4 || month == 5 || month == 6) {
            return "2";
        } else if (month == 7 || month == 8 || month == 9) {
            return "3";
        } else if (month == 10 || month == 11 || month == 12) {
            return "4";
        } else {
            return "0";
        }
    }


}
