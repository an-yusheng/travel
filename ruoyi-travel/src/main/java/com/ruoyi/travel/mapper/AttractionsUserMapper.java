package com.ruoyi.travel.mapper;

import com.ruoyi.travel.domain.Attractions;
import com.ruoyi.travel.domain.AttractionsUser;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author mac
* @description 针对表【attractions_user】的数据库操作Mapper
* @createDate 2023-08-04 10:26:18
* @Entity com.ruoyi.travel.domain.AttractionsUser
*/
@Mapper
public interface AttractionsUserMapper {

    int deleteByPrimaryKey(Long id);

    int insert(AttractionsUser record);

    int insertSelective(AttractionsUser record);

    AttractionsUser selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(AttractionsUser record);

    int updateByPrimaryKey(AttractionsUser record);

    void deleteByUserId(AttractionsUser attractions);

    AttractionsUser selectByUserId(AttractionsUser attractions);

    List<Attractions> selectByUserIdList(Long userId);
}
