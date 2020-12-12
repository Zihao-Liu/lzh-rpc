package com.lzh.rpc.center.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author Liuzihao
 * Created on 2020-11-11
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AppMetaInfoEntity {
    /**
     * 主键Id
     */
    private Integer id;
    /**
     * 服务Id
     */
    private Integer appId;
    /**
     * 服务名称
     */
    private String appName;
    /**
     * 服务密钥
     */
    private String appToken;
    /**
     * 服务描述
     */
    private String appDesc;
}
