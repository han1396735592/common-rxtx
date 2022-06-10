package cn.qqhxj.rxtx.parse;

import cn.qqhxj.rxtx.HexUtil;

/**
 * @author han1396735592
 **/
public class HexStringSerialDataParser implements SerialDataParser<String> {
    @Override
    public String parse(byte[] bytes) {
        return HexUtil.bytesToHexString(bytes);
    }
}
