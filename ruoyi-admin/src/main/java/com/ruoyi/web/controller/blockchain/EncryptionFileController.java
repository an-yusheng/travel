package com.ruoyi.web.controller.blockchain;

import com.ruoyi.travel.domain.EncryptFiles;
import com.ruoyi.travel.domain.TabCase;
import com.ruoyi.travel.service.IEncryptFilesService;
import com.ruoyi.travel.util.MultipartFileToFile;
import com.ruoyi.common.core.controller.BaseController;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.page.TableDataInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;

/**
 * 加密文件
 *
 * @author
 */
@RestController
@RequestMapping("/encryption/file")
public class EncryptionFileController extends BaseController {

    @Autowired
    private IEncryptFilesService encryptFilesService;

    @GetMapping("/list")
    public TableDataInfo list(EncryptFiles encryptFiles)
    {
        startPage();
        List<EncryptFiles> list = encryptFilesService.selectEncryptionFileList(encryptFiles);
        return getDataTable(list);
    }

    @PostMapping("/upload")
    public AjaxResult upload(@RequestParam("file") MultipartFile file){
        String url = MultipartFileToFile.saveMultipartFile(file,"../file");
        HashMap hashMap = new HashMap(){{
            put("url",url);
            put("fileName",file.getOriginalFilename());
        }};
        return AjaxResult.success(hashMap);
    }

    @PostMapping("/save")
    public AjaxResult save(@RequestBody EncryptFiles encryptFiles){
        return encryptFilesService.saveEncryptFiles(encryptFiles);
    }

    @PostMapping("/downloadEncryFile")
    public AjaxResult downloadEncryFile(@RequestBody EncryptFiles encryptFiles){
        return encryptFilesService.downloadEncryFile(encryptFiles);
    }

    @PostMapping("/checkDownloadEncryFile")
    public AjaxResult checkDownloadEncryFile(@RequestBody EncryptFiles encryptFiles){
        return encryptFilesService.checkDownloadEncryFile(encryptFiles);
    }

    @PostMapping("/transferFile")
    public AjaxResult transferFile(@RequestBody EncryptFiles encryptFiles){
        return encryptFilesService.transferFile(encryptFiles);
    }

    @PostMapping("/upperChain")
    public AjaxResult upperChain(@RequestBody EncryptFiles encryptFiles){
        return encryptFilesService.upperChain(encryptFiles);
    }


    @GetMapping("/caseList")
    public TableDataInfo caseList(TabCase req)
    {
        startPage();
        List<TabCase> list = encryptFilesService.selectCaseList(req);
        return getDataTable(list);
    }


    @PostMapping("/updateCase")
    public AjaxResult updateCase(@RequestBody TabCase req){
        return encryptFilesService.updateCase(req);
    }

}
