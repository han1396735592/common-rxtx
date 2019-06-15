package cn.qqhxj.common.rxtx;

/**
 * @author han xinjian
 **/
@FunctionalInterface
public interface SerialSendDataEntity {
    /**
     * 数据的反解析接口
     *
     * @return data
     */
    byte[] getBytes();
}
