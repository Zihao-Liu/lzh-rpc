package com.lzh.rpc.core.client.discover;

import static com.lzh.rpc.common.constant.CommonConstant.DEFAULT_DISCOVER_SUFFIX;
import static com.lzh.rpc.common.util.SignUtil.getSignMd5;
import static com.lzh.rpc.core.constant.ContextConstant.REGISTER_TIMEOUT_MILLS;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import org.apache.commons.compress.utils.Lists;

import com.google.gson.reflect.TypeToken;
import com.lzh.rpc.common.model.center.RegisterHeader;
import com.lzh.rpc.common.model.server.ProviderInstance;
import com.lzh.rpc.common.util.JsonUtil;
import com.lzh.rpc.core.client.factory.BaseClientFactory;
import com.lzh.rpc.core.log.LoggerAdapter;
import com.lzh.rpc.core.model.client.ClientProperty;

import okhttp3.Call;
import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * HTTP: 注册中心，使用注册中心进行服务发现。server端必须在注册中心注册
 *
 * @author Liuzihao
 * @since 0.0.1
 */
public class HttpClientDiscover extends AbstractClientDiscover {

    private static final LoggerAdapter LOGGER = LoggerAdapter.getLogger(HttpClientDiscover.class);
    private static final TypeToken<List<ProviderInstance>> TYPE_TOKEN = new TypeToken<List<ProviderInstance>>() {
    };

    private static volatile HttpClientDiscover INSTANCE = new HttpClientDiscover();

    public static RpcClientDiscover getInstance() {
        return INSTANCE;
    }

    private ScheduledExecutorService executorService;

    @Override
    public void doDestroy() {
        executorService.shutdownNow();
    }

    @Override
    public void doDiscover(List<ClientProperty> clientPropertyList) {
        executorService = Executors.newScheduledThreadPool(clientPropertyList.size());

        clientPropertyList.forEach(property -> {
            // 先同步消费一次
            doPost(property);
            executorService.scheduleWithFixedDelay(() -> doPost(property), 0, property.getDuration(),
                    TimeUnit.MILLISECONDS);
        });
    }

    private void doPost(ClientProperty property) {
        Response response = null;
        try {
            long time = System.currentTimeMillis();
            String url = property.getDomain() + String.format(DEFAULT_DISCOVER_SUFFIX, property.getService());

            String sign = getSignMd5(time, property.getAppId(), property.getAppToken(), property.getService());

            RegisterHeader header = new RegisterHeader();
            header.setAppId(property.getAppId());
            header.setTimestamp(time);
            header.setSign(sign);

            Headers headers = new Headers.Builder()
                    .add("rpc-meta", JsonUtil.toJson(header))
                    .build();

            Request request = new Request.Builder()
                    .url(url)
                    .get()
                    .headers(headers)
                    .build();

            OkHttpClient client = new OkHttpClient.Builder()
                    .callTimeout(REGISTER_TIMEOUT_MILLS, TimeUnit.MILLISECONDS)
                    .build();

            Call call = client.newCall(request);
            LOGGER.debug("register do post, action: [{}]", url);
            response = call.execute();
            ResponseBody responseBody = response.body();
            if (!response.isSuccessful() || Objects.isNull(responseBody)) {
                return;
            }
            List<ProviderInstance> instanceList = JsonUtil.fromJson(responseBody.string(), TYPE_TOKEN);
            if (Objects.isNull(instanceList)) {
                instanceList = Lists.newArrayList();
            }
            BaseClientFactory.updateProvider(property.getReference(), instanceList);
            LOGGER.debug("register response, {}", JsonUtil.toJson(instanceList));
        } catch (Exception e) {
            LOGGER.error("provider do post error, ", e);
        } finally {
            if (response != null) {
                response.close();
            }
        }
    }
}
