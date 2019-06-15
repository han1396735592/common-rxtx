package cn.qqhxj.common.rxtx.processor;

/**
 * @author han xinjian
 **/
@FunctionalInterface
public interface SerialDataProcessor<T> {
    /**
     * 处理接收到的信息
     *
     * @param t object
     */
    void process(T t);
}
