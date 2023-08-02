package com.ruoyi.travel.mapper;

import com.ruoyi.travel.domain.Attractions;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

/**
* @author mac
* @description 针对表【attractions(景点)】的数据库操作Mapper
* @createDate 2023-08-02 18:11:36
* @Entity com.ruoyi.travel.domain.Attractions
*/
@Mapper
public interface AttractionsMapper {

    int deleteByPrimaryKey(Long id);

    int insert(Attractions record);

    int insertSelective(Attractions record);

    Attractions selectByPrimaryKey(Long id);

    int updateByPrimaryKeySelective(Attractions record);

    int updateByPrimaryKey(Attractions record);

    List<Attractions> selectList(Attractions attractions);
}
