package com.ruoyi.travel.service.impl;

import com.ruoyi.travel.domain.TabCase;
import com.ruoyi.travel.mapper.EncryptFilesMapper;
import com.ruoyi.travel.domain.EncryptFiles;
import com.ruoyi.travel.service.IEncryptFilesService;
import com.ruoyi.travel.util.RSAFileUtil;
import com.ruoyi.travel.util.RSAUtil;
import com.ruoyi.travel.util.SHA256Util;
import com.ruoyi.common.core.domain.AjaxResult;
import com.ruoyi.common.core.domain.entity.SysUser;
import com.ruoyi.common.core.domain.model.LoginUser;
import com.ruoyi.common.utils.SecurityUtils;
import com.ruoyi.common.utils.StringUtils;
import com.ruoyi.system.mapper.SysUserMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.*;
import java.net.*;
import java.util.List;
import java.util.Objects;

/**
 * 加密文件业务实现
 *
 * @author
 */
@Slf4j
@Service
public class EncryptFilesServiceImpl implements IEncryptFilesService {

    @Autowired
    private EncryptFilesMapper encryptFilesMapper;

    @Autowired
    private SysUserMapper sysUserMapper;


    @Override
    public List<EncryptFiles> selectEncryptionFileList(EncryptFiles encryptFiles) {
        return encryptFilesMapper.selectEncryptionFileList(encryptFiles);
    }

    @Override
    public AjaxResult saveEncryptFiles(EncryptFiles encryptFiles) {
        SysUser sysUser = sysUserMapper.selectUserByPublicKey(encryptFiles.getRecipient());
        if (Objects.isNull(sysUser)){
            return AjaxResult.error("没有找到接受人公钥对应用户");
        }
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        if (StringUtils.equals(encryptFiles.getRecipient(),user.getPublicKey())){
            return AjaxResult.error("接受人请不要输入自己公钥");
        }
        if (Objects.isNull(encryptFiles.getPrivateKey())){
            return AjaxResult.error("请输入私钥以进行文件加密");
        }
        encryptFiles.setUploadedBy(user.getPublicKey());
        encryptFiles.setBelongingPerson(user.getPublicKey());
        encryptFiles.setAcceptState(0);
        encryptFiles.setFileStatus(0);
        this.processFileHash(encryptFiles);
        //encryptFiles.setEncryptionResults(this.getEncryResult(encryptFiles.getLocalAddress(),encryptFiles.getRecipient()));
        int rows = encryptFilesMapper.saveEncryptFiles(encryptFiles);
        if (rows>0){
            TabCase tabCase = new TabCase(){{
                setDigitalevidence(encryptFiles.getFileHash());
                setStatus("0");
                setOwner(encryptFiles.getBelongingPerson());
            }};
            encryptFilesMapper.saveCase(tabCase);
        }
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }

    private void processFileHash(EncryptFiles encryptFiles) {
        File file  = new File(encryptFiles.getLocalAddress());
        byte[] fileBytes = new byte[(int) file.length()];
        try(FileInputStream fileInputStream = new FileInputStream(file)){
            fileInputStream.read(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String fileHash = SHA256Util.digest(fileBytes);
        String fileHashDecry = RSAUtil.encryptByPrivateKey(fileHash,encryptFiles.getPrivateKey());
        encryptFiles.setFileHash(fileHash);
        encryptFiles.setFileHashEncry(fileHashDecry);
    }

    @Override
    public AjaxResult downloadEncryFile(EncryptFiles encryptFiles) {
        // 获取登陆人
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        // 校验文件接受人是否是登陆人
        if (!StringUtils.equals(encryptFiles.getRecipient(),user.getPublicKey())){
            return AjaxResult.error("非接受人不允许下载此文件");
        }
        if (StringUtils.isNull(encryptFiles.getPrivateKey())){
            return AjaxResult.error("请输入加密私钥");
        }
        // 获取文件
        EncryptFiles res = encryptFilesMapper.selectById(encryptFiles.getId());
        // 根据私钥解密文件
       /* byte[] encryByte = new byte[0];
        try {
            encryByte = RSAFileUtil.decrypt(res.getEncryptionResults(),encryptFiles.getPrivateKey());
        } catch (Exception e) {
            e.printStackTrace();
        }*/
        // 读取原始文件
        File file  = new File(encryptFiles.getLocalAddress());
        byte[] fileBytes = new byte[(int) file.length()];
        try(FileInputStream fileInputStream = new FileInputStream(file)){
            fileInputStream.read(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        // 校验原始文件流跟解密后文件流是否一致，不一致证明解密识别
        /*if (!equalsArr(encryByte,fileBytes)){
            return AjaxResult.error("解密失败");
        };*/
        String fileHash = SHA256Util.digest(fileBytes);
        String fileHashDecry = RSAUtil.decryptByPublicKey(res.getFileHashEncry(),res.getBelongingPerson());
        if (!StringUtils.equals(fileHash,fileHashDecry)){
            return AjaxResult.error("解密失败");
        }
        // 解密成功修改接受状态为已接受
        encryptFiles.setAcceptState(1);
        encryptFiles.setBelongingPerson(user.getPublicKey());
        encryptFilesMapper.updateToAcceptState(encryptFiles);
        TabCase tabCase = new TabCase(){{
            setDigitalevidence(encryptFiles.getFileHash());
            setStatus("1");
            setOwner(encryptFiles.getBelongingPerson());
            setFilehash(res.getFileHash());
        }};
        encryptFilesMapper.updateTabCaseByHash(tabCase);
        return AjaxResult.success(res.getFileAddress());
    }

    @Override
    public AjaxResult checkDownloadEncryFile(EncryptFiles encryptFiles) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        if (StringUtils.equals(user.getPublicKey(),encryptFiles.getBelongingPerson())){
            return AjaxResult.success();
        }
        return AjaxResult.error("这份文件不归属于您");
    }

    @Override
    public AjaxResult transferFile(EncryptFiles encryptFiles) {
        LoginUser loginUser = SecurityUtils.getLoginUser();
        SysUser user = loginUser.getUser();
        if (!StringUtils.equals(user.getPublicKey(),encryptFiles.getBelongingPerson())){
            return AjaxResult.error("这份文件不归属于您");
        }
        if (StringUtils.equals(encryptFiles.getRecipient(),user.getPublicKey())){
            return AjaxResult.error("接受人请不要输入自己公钥");
        }
        SysUser sysUser = sysUserMapper.selectUserByPublicKey(encryptFiles.getRecipient());
        if (Objects.isNull(sysUser)){
            return AjaxResult.error("没有找到接受人公钥对应用户");
        }
        encryptFiles.setAcceptState(0);
        //encryptFiles.setEncryptionResults(this.getEncryResult(encryptFiles.getLocalAddress(),encryptFiles.getRecipient()));
        this.processFileHash(encryptFiles);
        int rows = encryptFilesMapper.updateEncryptFiles(encryptFiles);
        if (rows>0){
            // 获取文件
            EncryptFiles res = encryptFilesMapper.selectById(encryptFiles.getId());
            TabCase tabCase = new TabCase(){{
                setDigitalevidence(encryptFiles.getFileHash());
                setStatus("0");
                setOwner(encryptFiles.getBelongingPerson());
                setFilehash(res.getFileHash());
            }};
            encryptFilesMapper.updateTabCaseByHash(tabCase);
        }
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }


    @Override
    public AjaxResult upperChain(EncryptFiles encryptFiles) {
        return AjaxResult.success();
    }

    @Override
    public List<TabCase> selectCaseList(TabCase req) {
        return encryptFilesMapper.selectCaseList(req);
    }

    @Override
    public AjaxResult updateCase(TabCase req) {
        int rows = encryptFilesMapper.updateCase(req);
        return rows > 0 ? AjaxResult.success() : AjaxResult.error();
    }


    private byte[] getEncryResult(String fileAddress,String recipient) {
        File file  = new File(fileAddress);
        byte[] fileBytes = new byte[(int) file.length()];
        try(FileInputStream fileInputStream = new FileInputStream(file)){
            fileInputStream.read(fileBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
        byte[] res = null;
        try {
            res = RSAFileUtil.encrypt(fileBytes,recipient);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return res;
    }



    /**
     * 获取 文件 流
     * @param url
     * @return
     * @throws IOException
     */
    private byte[] getFile(String url) throws IOException{
        URL urlConet = new URL(url);
        HttpURLConnection con = (HttpURLConnection)urlConet.openConnection();
        con.setRequestMethod("GET");
        con.setConnectTimeout(4 * 1000);
        InputStream inStream = con .getInputStream();    //通过输入流获取图片数据
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        byte[] buffer = new byte[2048];
        int len = 0;
        while( (len=inStream.read(buffer)) != -1 ){
            outStream.write(buffer, 0, len);
        }
        inStream.close();
        byte[] data =  outStream.toByteArray();
        return data;
    }

    public static boolean equalsArr(byte[] b1,byte[] b2){
        boolean bo=false;
        for (int i = 0; i < b1.length; i++) {
            if (b1[i]==b2[i]){
                bo=true;
                continue;
            }else {
                bo=false;
                break;
            }
        }
        return bo;
    }
}
