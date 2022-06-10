package cn.qqhxj.rxtx.processor;

/**
 * @author han1396735592
 **/
@FunctionalInterface
public interface SerialByteDataProcessor {

    /**
     * 处理接收的元字节数据
     *
     * @param bytes data
     */
    void process(byte[] bytes);
}
