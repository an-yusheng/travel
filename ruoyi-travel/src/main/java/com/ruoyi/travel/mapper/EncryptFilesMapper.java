package com.ruoyi.travel.mapper;

import com.ruoyi.travel.domain.EncryptFiles;
import com.ruoyi.travel.domain.TabCase;

import java.util.List;

/**
 * 加密文件业务 数据层
 * 
 * @author
 */
public interface EncryptFilesMapper
{

    List<EncryptFiles> selectEncryptionFileList(EncryptFiles encryptFiles);

    int saveEncryptFiles(EncryptFiles encryptFiles);

    EncryptFiles selectById(Long id);

    int updateToAcceptState(EncryptFiles encryptFiles);

    int updateEncryptFiles(EncryptFiles encryptFiles);

    void updateToBlock(EncryptFiles encryptFiles);

    List<TabCase> selectCaseList(TabCase req);

    void saveCase(TabCase tabCase);

    void updateTabCaseByHash(TabCase tabCase);

    int updateCase(TabCase req);
}
