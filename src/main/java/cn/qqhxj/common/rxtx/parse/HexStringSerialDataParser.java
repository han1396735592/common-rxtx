package cn.qqhxj.common.rxtx.parse;

import cn.qqhxj.common.rxtx.HexUtil;

/**
 * @author han xinjian
 **/
public class HexStringSerialDataParser implements SerialDataParser<String> {
    @Override
    public String parse(byte[] bytes) {
        return HexUtil.bytesToHexString(bytes);
    }
}
