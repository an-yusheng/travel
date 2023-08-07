package com.ruoyi.web.controller.travel;

import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import com.ruoyi.travel.domain.Attractions;
import com.ruoyi.travel.domain.AttractionsUser;
import com.ruoyi.travel.domain.EncryptFiles;
import com.ruoyi.travel.domain.UserPreference;
import com.ruoyi.travel.dto.AttractionsQuery;
import com.ruoyi.travel.service.TravelManagerService;
import com.ruoyi.travel.util.MultipartFileToFile;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * 旅行管理
 *
 * @author
 */
@RestController
@RequestMapping("/travel")
public class TravelManagerController extends BaseController {

    @Autowired
    private TravelManagerService travelManagerService;

    @GetMapping("/attractions/list")
    public TableDataInfo list(Attractions attractions)
    {
        List<Attractions> list = travelManagerService.selectAttractionsList(attractions);
        return getDataTable(list);
    }

    @PostMapping("/attractions/saveAttractions")
    public AjaxResult save(@RequestBody Attractions attractions){
        try{
            return travelManagerService.saveAttractions(attractions);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
    @PostMapping("/attractions/removeAttractions")
    public AjaxResult remove(@RequestBody Attractions attractions){
        try{
            return travelManagerService.removeAttractions(attractions);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    @PostMapping("/attractions/editAttractions")
    public AjaxResult edit(@RequestBody Attractions attractions){
        try{
            return travelManagerService.editAttractions(attractions);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    @PostMapping("/attractions/updatePreference")
    public AjaxResult updatePreference(@RequestBody UserPreference userPreference){
        try{
            return travelManagerService.updatePreference(userPreference);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    @GetMapping("/attractions/userPreference")
    public AjaxResult list()
    {
        UserPreference userPreference = travelManagerService.selectUserPreference();
        return AjaxResult.success(userPreference);
    }

    @PostMapping("/attractions/cpmputList")
    public TableDataInfo cpmputList(@RequestBody  AttractionsQuery query)
    {
        List<Attractions> list = travelManagerService.selectCpmputList(query);
        return getDataTable(list);
    }

    @PostMapping("/attractionsUser/save")
    public AjaxResult saveAttractionsUser(@RequestBody AttractionsUser attractions){
        try{
            return travelManagerService.saveAttractionsUser(attractions);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }

    @PostMapping("/attractionsUser/remove")
    public AjaxResult removeAttractionsUser(@RequestBody AttractionsUser attractions){
        try{
            return travelManagerService.removeAttractionsUser(attractions);
        }catch (Exception e){
            e.printStackTrace();
            return AjaxResult.error();
        }
    }
}
