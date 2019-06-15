package cn.qqhxj.common.rxtx.processor;

/**
 * @author han xinjian
 **/
@FunctionalInterface
public interface SerialByteDataProcesser {

    /**
     * 处理接收的元字节数据
     *
     * @param bytes data
     */
    void processer(byte[] bytes);
}
