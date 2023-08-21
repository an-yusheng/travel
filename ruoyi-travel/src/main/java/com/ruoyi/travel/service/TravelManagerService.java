package com.ruoyi.travel.service;

import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.travel.domain.Attractions;
import com.ruoyi.travel.domain.AttractionsUser;
import com.ruoyi.travel.domain.UserPreference;
import com.ruoyi.travel.dto.AttractionsQuery;

import java.util.List;
import java.util.Map;

public interface TravelManagerService {
    List<Attractions> selectAttractionsList(Attractions attractions);

    AjaxResult saveAttractions(Attractions attractions);

    AjaxResult removeAttractions(Attractions attractions);

    AjaxResult editAttractions(Attractions attractions);

    AjaxResult updatePreference(UserPreference userPreference);

    UserPreference selectUserPreference();

    List<Attractions> selectCpmputList(AttractionsQuery query);

    AjaxResult saveAttractionsUser(AttractionsUser attractions);

    AjaxResult removeAttractionsUser(AttractionsUser attractions);

    List wishList();
}
