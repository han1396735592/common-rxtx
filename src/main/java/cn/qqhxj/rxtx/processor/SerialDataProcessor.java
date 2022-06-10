package cn.qqhxj.rxtx.processor;

/**
 * @author han1396735592
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
