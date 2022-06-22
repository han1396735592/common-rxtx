package cn.qqhxj.rxtx.parse;

import gnu.io.CommPort;

/**
 * 串口数据解析器
 *
 * @author han1396735592
 **/
@FunctionalInterface
public interface CommPortDataParser<T,P extends CommPort>  {
    /**
     * Convert To Data Based On Data
     *
     * @param bytes data
     * @return object
     */
    T parse(byte[] bytes);
}
