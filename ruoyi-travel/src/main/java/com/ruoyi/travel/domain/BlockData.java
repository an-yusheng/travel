package com.ruoyi.travel.domain;

import lombok.Data;

@Data
public class BlockData extends EncryptFiles{


    /**
     * 区块索引号(区块高度)
     */
    private int index;
    /**
     * 当前区块的hash值,区块唯一标识
     */
    private String hash;
    /**
     * 前一个区块的hash值
     */
    private String previousHash;
    /**
     * 生成区块的时间戳
     */
    private long timestamp;
    /**
     * 工作量证明，计算正确hash值的次数
     */
    private int nonce;
}
