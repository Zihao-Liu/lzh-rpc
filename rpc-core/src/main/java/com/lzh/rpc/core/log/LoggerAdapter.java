package com.lzh.rpc.core.log;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 统一日志包装类
 *
 * @author Liuzihao
 */
public class LoggerAdapter {

    private static final String LOGGER_PREFIX = "[lzh-rpc] ";

    private Logger logger;

    private LoggerAdapter() {
    }

    private LoggerAdapter(Logger logger) {
        this.logger = logger;
    }

    public static LoggerAdapter getLogger(Class<?> clazz) {
        return new LoggerAdapter(LoggerFactory.getLogger(clazz));
    }

    public void info(String msg) {
        String info = LOGGER_PREFIX + msg;
        logger.info(info);
    }

    public void info(String format, Object... args) {
        String info = LOGGER_PREFIX + format;
        logger.info(info, args);
    }

    public void debug(String format, Object... args) {
        if (logger.isDebugEnabled()) {
            String info = LOGGER_PREFIX + format;
            logger.debug(info, args);
        }
    }

    public void error(String msg) {
        String info = LOGGER_PREFIX + msg;
        logger.error(info);
    }

    public void error(String format, Object... args) {
        String info = LOGGER_PREFIX + format;
        logger.error(info, args);
    }

    public void error(String msg, Throwable t) {
        String info = LOGGER_PREFIX + msg;
        logger.error(info, t);
    }
}
