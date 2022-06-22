package cn.qqhxj.rxtx.processor;

import gnu.io.CommPort;

/**
 * @author han1396735592
 **/
@FunctionalInterface
public interface CommPortByteDataProcessor<P extends CommPort> {

    /**
     * 处理接收的元字节数据
     *
     * @param bytes data
     */
    void process(byte[] bytes);
}
