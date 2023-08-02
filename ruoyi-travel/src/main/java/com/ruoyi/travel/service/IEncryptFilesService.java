package com.ruoyi.travel.service;

import com.ruoyi.travel.domain.EncryptFiles;
import com.ruoyi.travel.domain.TabCase;
import com.ruoyi.common.core.domain.AjaxResult;

import java.util.List;

public interface IEncryptFilesService {
    List<EncryptFiles> selectEncryptionFileList(EncryptFiles encryptFiles);

    AjaxResult saveEncryptFiles(EncryptFiles encryptFiles);

    AjaxResult downloadEncryFile(EncryptFiles encryptFiles);

    AjaxResult checkDownloadEncryFile(EncryptFiles encryptFiles);

    AjaxResult transferFile(EncryptFiles encryptFiles);

    AjaxResult upperChain(EncryptFiles encryptFiles);

    List<TabCase> selectCaseList(TabCase req);

    AjaxResult updateCase(TabCase req);
}
