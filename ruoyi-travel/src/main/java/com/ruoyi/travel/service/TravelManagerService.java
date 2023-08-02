package com.ruoyi.travel.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.travel.domain.Attractions;

import java.util.List;

public interface TravelManagerService {
    List<Attractions> selectAttractionsList(Attractions attractions);

    AjaxResult saveAttractions(Attractions attractions);

    AjaxResult removeAttractions(Attractions attractions);

    AjaxResult editAttractions(Attractions attractions);
}
