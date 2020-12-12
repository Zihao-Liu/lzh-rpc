package com.lzh.rpc.common.constant;

/**
 * @author Liuzihao
 */
public final class CommonConstant {
    private CommonConstant() {
    }

    public static final String BANNER_INFO = "\n\n" +
            " ----------------------------------------------------------\n"+
            "|   _  _                     __ _  __   __                 |\n" +
            "|  | || |    ___    _ _     / _` | \\ \\ / /  __ _    _ _    |\n" +
            "|  | __ |   / _ \\  | ' \\    \\__, |  \\ V /  / _` |  | ' \\   |\n" +
            "|  |_||_|   \\___/  |_||_|   |___/    |_|   \\__,_|  |_||_|  |\n" +
            "| :: Lzh Rpc(Hong Yan) ::               (v0.0.1.SNAPSHOT)  |\n" +
            " -----------------------------------------------------------";

    public static final int REGISTER_INTERVAL_DURATION = 15000;
    public static final int DISCOVER_INTERVAL_DURATION = 15000;
    public static final int DISCOVER_FAILOVER_DURATION = 15000;

    public static final int DEFAULT_AUTH_DURATION = 30000;

    public static final String DEFAULT_REGISTER_SUFFIX = "/register";
    public static final String DEFAULT_AUTH_SUFFIX = "/auth";
    public static final String DEFAULT_DESTROY_SUFFIX = "/destroy";
    public static final String DEFAULT_DISCOVER_SUFFIX = "/discover?appName=%s";


    public static final String IP_AND_PORT_SPLIT = ":";
    /**
     * Provider地址格式 IP:PORT
     */
    public static final String ADDRESS_FORMAT = "%s" + IP_AND_PORT_SPLIT + "%s";

    public static final String COMMON_SPLIT = ",";

    public static final String HEART_BEAT_ID = "HEARTBEAT";
    public static final int HEART_BEAT_INTERVAL = 30;

    public static final int DEFAULT_TIMEOUT_MILLS = 10000;
    public static final int DEFAULT_SERVER_POOL_CORE = 1000;
    public static final int DEFAULT_SERVER_POOL_MAX = 2000;
    public static final int DEFAULT_SERVER_ALIVE_MILLS = 30000;
    public static final int DEFAULT_SERVER_QUEUE_SIZE = 1000;
}
