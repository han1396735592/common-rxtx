package cn.qqhxj.rxtx.parse;

/**
 * @author han1396735592
 **/
public class StringSerialDataParser implements SerialDataParser<String> {
    @Override
    public String parse(byte[] bytes) {
        return new String(bytes);
    }
}

