package cn.qqhxj.rxtx.parse;

import gnu.io.CommPort;

/**
 * @author han1396735592
 **/
public class StringDataParser<P extends CommPort> implements CommPortDataParser<String, P> {
    @Override
    public String parse(byte[] bytes) {
        return new String(bytes);
    }
}

