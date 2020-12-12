package com.lzh.rpc.center.model.param;

import lombok.Data;

/**
 * @author Liuzihao
 * Created on 2020-11-11
 */
@Data
public class UpdateAppParam {
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
