package cn.qqhxj.rxtx.processor;

import cn.qqhxj.rxtx.context.AbstractSerialContext;

/**
 * @author han1396735592
 **/
@FunctionalInterface
public interface SerialByteDataProcessor {

    /**
     * 处理接收的元字节数据
     *
     * @param bytes         data
     * @param serialContext serialContext
     */
    void process(byte[] bytes, AbstractSerialContext serialContext);
}
