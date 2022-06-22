package cn.qqhxj.rxtx.processor;

import gnu.io.CommPort;

/**
 * @author han1396735592
 **/
@FunctionalInterface
public interface CommPortDataProcessor<T,P extends CommPort> {
    /**
     * 处理接收到的信息
     *
     * @param t object
     */
    void process(T t);
}
