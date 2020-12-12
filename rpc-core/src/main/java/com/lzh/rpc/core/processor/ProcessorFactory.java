package com.lzh.rpc.core.processor;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.utils.Lists;

import com.lzh.rpc.common.constant.RpcErrorEnum;
import com.lzh.rpc.common.exception.RpcException;
import com.lzh.rpc.core.model.processor.ProcessorMeta;

/**
 * @author Liuzihao
 * Created on 2020-11-17
 */
public class ProcessorFactory {

    private ProcessorFactory() {
    }

    public static void doPreRequest(ProcessorMeta meta) {
        List<BaseProcessor> processorList = Lists.newArrayList();
        processorList.add(new TraceProcessor());
        processorList.forEach(processor -> processor.doPreRequest(meta));
    }

    public static void doPostRequest(ProcessorMeta meta) {
        List<BaseProcessor> processorList = Lists.newArrayList();
        processorList.add(new TraceProcessor());
        processorList.forEach(processor -> processor.doPostRequest(meta));
    }

    public static void doPreResponse(ProcessorMeta meta) {
        List<BaseProcessor> processorList = buildServerProcessorList(meta);
        processorList.forEach(processor -> processor.doPreResponse(meta));
    }

    public static void doPostResponse(ProcessorMeta meta) {
        List<BaseProcessor> processorList = buildServerProcessorList(meta);
        processorList.forEach(processor -> processor.doPostResponse(meta));
    }

    private static List<BaseProcessor> buildClientProcessorList(ProcessorMeta meta) {
        List<BaseProcessor> processorList = Lists.newArrayList();
        processorList.add(new TraceProcessor());
        processorList.add(new AuthProcessor());
        List<Class<? extends BaseProcessor>> customProcessors = meta.getClientProperty().getProcessors();
        processorList.addAll(listCustomProcessor(customProcessors));
        return processorList;
    }

    private static List<BaseProcessor> buildServerProcessorList(ProcessorMeta meta) {
        List<BaseProcessor> processorList = Lists.newArrayList();
        processorList.add(new TraceProcessor());
        if (meta.getServerProperty().getEnableAuth()) {
            processorList.add(new AuthProcessor());
        }
        List<Class<? extends BaseProcessor>> customProcessors = meta.getServerProperty().getProcessors();
        processorList.addAll(listCustomProcessor(customProcessors));
        return processorList;
    }

    private static List<BaseProcessor> listCustomProcessor(List<Class<? extends BaseProcessor>> clazzList) {
        if (CollectionUtils.isNotEmpty(clazzList)) {
            List<BaseProcessor> processors = new ArrayList<>();
            try {
                for (Class<? extends BaseProcessor> clazz : clazzList) {
                    BaseProcessor newInstance = clazz.newInstance();
                    processors.add(newInstance);
                }
            } catch (Exception e) {
                throw RpcException.error(RpcErrorEnum.PROCESSOR_INIT_ERROR);
            }
            return processors;
        }
        return Lists.newArrayList();
    }
}
