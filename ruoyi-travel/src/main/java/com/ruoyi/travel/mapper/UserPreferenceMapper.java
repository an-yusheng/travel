package com.ruoyi.travel.mapper;

import com.ruoyi.travel.domain.UserPreference;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
* @author mac
* @description 针对表【user_preference】的数据库操作Mapper
* @createDate 2023-08-03 16:47:33
* @Entity com.ruoyi.travel.domain.UserPreference
*/
@Mapper
public interface UserPreferenceMapper {

    int deleteByPrimaryKey(Long id);

    int insert(UserPreference record);

    int insertSelective(UserPreference record);

    UserPreference selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(UserPreference record);

    int updateByPrimaryKey(UserPreference record);

    UserPreference selectByUserId(@Param("userId") Long userId);
}
