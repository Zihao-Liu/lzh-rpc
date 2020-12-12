package com.lzh.rpc.common.model.center;

/**
 * @author Liuzihao
 * Created on 2020-11-11
 */
public class RegisterHeader {
    private Integer appId;
    private Long timestamp;
    private String sign;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Long timestamp) {
        this.timestamp = timestamp;
    }

    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String toString() {
        return "RegisterHeader{" +
                "appId=" + appId +
                ", timestamp=" + timestamp +
                ", sign='" + sign + '\'' +
                '}';
    }
}
