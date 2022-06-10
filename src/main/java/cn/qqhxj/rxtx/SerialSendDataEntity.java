package cn.qqhxj.rxtx;

/**
 * @author han1396735592
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
