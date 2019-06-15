package cn.qqhxj.common.rxtx.parse;

/**
 * @author han xinjian
 **/
public class StringSerialDataParser implements SerialDataParser<String> {
    @Override
    public String parse(byte[] bytes) {
        return new String(bytes);
    }
}

