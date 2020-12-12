package com.lzh.rpc.core.processor;

import java.lang.reflect.Method;
import java.util.Objects;

import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import com.lzh.rpc.common.constant.RpcErrorEnum;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.common.model.request.RpcRequest;
import com.lzh.rpc.common.model.request.RpcRequestHeader;
import com.lzh.rpc.common.util.SignUtil;
import com.lzh.rpc.core.annotation.RpcAuthControl;
import com.lzh.rpc.core.annotation.RpcAuthMeta;
import com.lzh.rpc.core.annotation.RpcAuthRule;
import com.lzh.rpc.core.model.processor.ProcessorMeta;

/**
 * @author Liuzihao
 * Created on 2020-12-05
 */
public class AuthProcessor implements BaseProcessor {
    @Override
    public void doPreRequest(ProcessorMeta meta) {
        RpcRequestHeader header = meta.getRequest().getHeader();
        String accessToken = meta.getClientProperty().getAccessToken();
        header.setMsgToken(getMsgToken(meta, accessToken));
        meta.getRequest().setHeader(header);
    }

    @Override
    public void doPostRequest(ProcessorMeta meta) {

    }

    @Override
    public void doPreResponse(ProcessorMeta meta) {
        RpcRequest request = meta.getRequest();
        try {
            String className = request.getClassName();
            Class<?> objClz = Class.forName(className);
            // 获取调用的方法名称。
            String methodName = request.getMethodName();
            // 参数类型
            Class<?>[] paramsTypes = request.getParamTypes();
            // 调用实现类的指定的方法并返回结果。
            Method method = objClz.getMethod(methodName, paramsTypes);

            RpcAuthControl authControl = method.getAnnotation(RpcAuthControl.class);

            if (Objects.nonNull(authControl)) {
                boolean methodAllow = checkAuth(authControl, meta);
                if (methodAllow) {
                    return;
                }
            }
            authControl = objClz.getAnnotation(RpcAuthControl.class);
            if (Objects.nonNull(authControl)) {
                boolean classAllow = checkAuth(authControl, meta);
                if (!classAllow) {
                    throw RpcException.error(RpcErrorEnum.PERMISSION_DENIED);
                }
            }
        } catch (NoSuchMethodException | ClassNotFoundException e) {
            throw RpcException.error(RpcErrorEnum.METHOD_NOT_FOUND);
        }
    }

    private boolean checkAuth(RpcAuthControl authControl, ProcessorMeta processorMeta) {
        if (Objects.isNull(authControl)) {
            return true;
        }
        Integer appId = processorMeta.getRequest().getHeader().getAppId();
        RpcAuthRule[] authRules = authControl.rules();
        boolean forbidAll = false;
        for (RpcAuthRule authRule : authRules) {
            // 优先级 1 不允许特定的appId通过
            if (!authRule.allowed() && authRule.metas().length > 0) {
                for (RpcAuthMeta meta : authRule.metas()) {
                    if (appId == null || meta.appId() == appId) {
                        return false;
                    }
                }
            }

            // 优先级 2 允许特定的appId通过
            if (authRule.allowed() && authRule.metas().length > 0 && appId != null) {
                for (RpcAuthMeta meta : authRule.metas()) {
                    if (appId.equals(meta.appId())) {
                        String signToken = getMsgToken(processorMeta, meta.token());
                        return signToken.equals(processorMeta.getRequest().getHeader().getMsgToken());
                    }
                }
            }

            // 优先级 3 不允许所有appId通过
            if (!authRule.allowed() && authRule.metas().length == 0) {
                forbidAll = true;
            }
        }
        return !forbidAll;
    }


    @Override
    public void doPostResponse(ProcessorMeta meta) {

    }

    private String getMsgToken(ProcessorMeta meta, String accessToken) {
        RpcRequestHeader header = meta.getRequest().getHeader();
        if (ObjectUtils.allNotNull(header.getTimeStamp(), header.getAppId(), accessToken)) {
            return SignUtil.getSignMd5(header.getTimeStamp(), header.getAppId(), accessToken,
                    meta.getRequest().getParams());
        }
        return StringUtils.EMPTY;
    }
}
