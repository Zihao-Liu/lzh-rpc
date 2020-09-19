package com.lzh.rpc.common.model.request;

import java.io.Serializable;

/**
 * @author Liuzihao
 */
public class RpcRequestHeader implements Serializable {

    private Integer appId;

    private Long timeStamp;

    private String msgToken;

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Long getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Long timeStamp) {
        this.timeStamp = timeStamp;
    }

    public String getMsgToken() {
        return msgToken;
    }

    public void setMsgToken(String msgToken) {
        this.msgToken = msgToken;
    }

    @Override
    public String toString() {
        return "RpcRequestHeader{" +
                "appId=" + appId +
                ", timeStamp=" + timeStamp +
                ", msgToken='" + msgToken + '\'' +
                '}';
    }
}
