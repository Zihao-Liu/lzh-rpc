package com.lzh.rpc.core.server.register;

import static com.lzh.rpc.common.util.SignUtil.getSignMd5;
import static com.lzh.rpc.core.constant.ContextConstant.DESTROY_URL_SUFFIX;
import static com.lzh.rpc.core.constant.ContextConstant.JSON_MEDIA_TYPE;
import static com.lzh.rpc.core.constant.ContextConstant.REGISTER_TIMEOUT_MILLS;
import static com.lzh.rpc.core.constant.ContextConstant.REGISTER_URL_SUFFIX;

import java.net.UnknownHostException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.lzh.rpc.common.model.center.RegisterHeader;
import com.lzh.rpc.common.model.server.ProviderInstance;
import com.lzh.rpc.common.util.HostUtil;
import com.lzh.rpc.common.util.JsonUtil;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.server.ServerProperty;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * @author Liuzihao
 * Created on 2020-11-06
 */
public class HttpServerRegister extends AbstractServerRegister {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(HttpServerRegister.class);

    private static final HttpServerRegister INSTANCE = new HttpServerRegister();

    public static RpcServerRegister getInstance() {
        return INSTANCE;
    }

    private ScheduledExecutorService executorService;

    @Override
    public void doRegister(ServerProperty property) throws UnknownHostException {
        String host = HostUtil.getHostAddress();
        Integer port = property.getPort();
        String registerUrl = property.getDomain() + REGISTER_URL_SUFFIX;
        ProviderInstance instance = new ProviderInstance(host, port);

        executorService = Executors.newScheduledThreadPool(1);
        executorService.scheduleWithFixedDelay(() ->
                doPost(instance, registerUrl, property), 0, property.getDuration(), TimeUnit.MILLISECONDS);
    }

    private void doPost(ProviderInstance instance, String url, ServerProperty property) {
        Response response = null;
        try {
            long time = System.currentTimeMillis();
            instance.setLastLiveTime(time);

            RequestBody body = RequestBody.create(JSON_MEDIA_TYPE, JsonUtil.toJson(instance));
            String sign = getSignMd5(time, property.getAppId(), property.getAppToken(), instance);

            RegisterHeader header = new RegisterHeader();
            header.setAppId(property.getAppId());
            header.setTimestamp(time);
            header.setSign(sign);

            Headers headers = new Headers.Builder()
                    .add("rpc-meta", JsonUtil.toJson(header))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .post(body)
                    .headers(headers)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(REGISTER_TIMEOUT_MILLS, TimeUnit.MILLISECONDS)
                    .build();

            Call call = client.newCall(request);
            LOGGER.debug("register do post, action: [{}]", url);
            response = call.execute();
            LOGGER.debug("register response, {}", response);
        } catch (Exception e) {
            LOGGER.error("provider do post error, ", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }

    @Override
    public void doDestroy(ServerProperty property) throws UnknownHostException {
        executorService.shutdownNow();
        String host = HostUtil.getHostAddress();
        Integer port = property.getPort();
        String registerUrl = property.getDomain() + DESTROY_URL_SUFFIX;
        ProviderInstance instance = new ProviderInstance(host, port);
        doPost(instance, registerUrl, property);
    }
}
