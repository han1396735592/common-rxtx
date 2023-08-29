package cn.qqhxj.rxtx.parse;

import cn.qqhxj.rxtx.context.AbstractSerialContext;

/**
 * 串口数据解析器
 *
 * @author han1396735592
 **/
@FunctionalInterface
public interface SerialDataParser<T> {
    /**
     * Convert To Data Based On Data
     *
     * @param bytes data
     * @return object
     */
    T parse(byte[] bytes, AbstractSerialContext serialContext);
}
