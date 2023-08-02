package com.ruoyi.travel.domain;

import lombok.Data;

/**
 * 加密文件domin
 *
 * @author
 */
@Data
public class EncryptFiles {

    private Long id;

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件地址
     */
    private String fileAddress;

    /**
     * 本地地址
     */
    private String localAddress;

    /**
     * 上传人
     */
    private String uploadedBy;

    /**
     * 归属人
     */
    private String belongingPerson;

    /**
     * 接收人
     */
    private String recipient;

    /**
     * 接受状态(0:待接受、1:已接受)
     */
    private Integer acceptState;

    /**
     * 文件状态(0:有效、1:无效)
     */
    private Integer fileStatus;

    /**
     * 加密结果
     */
    private byte[] encryptionResults;

    /**
     * 文件hash
     */
    private String fileHash;

    /**
     * 文件hash加密值
     */
    private String fileHashEncry;

    /**
     * 私钥
     */
    private String privateKey;


    /**
     * 区块数据
     */
    private String blockData;

    /**
     * 区块数据hash加密值
     */
    private String blockDataEncry;
}
