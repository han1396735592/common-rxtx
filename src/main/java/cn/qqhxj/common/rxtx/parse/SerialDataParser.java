package cn.qqhxj.common.rxtx.parse;

/**
 * @author han xinjian
 **/
@FunctionalInterface
public interface SerialDataParser<T> {
    /**
     *
     * Convert To Data Based On Data
     *
     * @param bytes data
     * @return object
     */
    T parse(byte[] bytes);
}
