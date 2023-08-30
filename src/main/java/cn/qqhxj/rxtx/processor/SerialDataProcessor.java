package cn.qqhxj.rxtx.processor;

import cn.qqhxj.rxtx.context.SerialContext;

/**
 * @author han1396735592
 **/
@FunctionalInterface
public interface SerialDataProcessor<T> {
    /**
     * 处理接收到的信息
     *
     * @param t object
     * @param serialContext serialContext
     */
    void process(T t, SerialContext serialContext);
}
