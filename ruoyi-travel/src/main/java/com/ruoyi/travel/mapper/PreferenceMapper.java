package com.ruoyi.travel.mapper;

import com.ruoyi.travel.domain.Preference;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author mac
* @description 针对表【preference(旅行偏好表)】的数据库操作Mapper
* @createDate 2023-08-02 18:11:36
* @Entity com.ruoyi.travel.domain.Preference
*/
@Mapper
public interface PreferenceMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Preference record);

    int insertSelective(Preference record);

    Preference selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Preference record);

    int updateByPrimaryKey(Preference record);

    String[] selectByName(@Param("types") String[] type);
}
