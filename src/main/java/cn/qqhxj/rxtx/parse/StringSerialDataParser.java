package cn.qqhxj.rxtx.parse;

import cn.qqhxj.rxtx.context.AbstractSerialContext;

/**
 * @author han1396735592
 **/
public class StringSerialDataParser implements SerialDataParser<String> {
    @Override
    public String parse(byte[] bytes, AbstractSerialContext serialContext) {
        return new String(bytes);
    }
}

