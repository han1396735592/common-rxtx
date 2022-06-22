package cn.qqhxj.rxtx.parse;

import cn.qqhxj.rxtx.util.HexUtil;
import gnu.io.CommPort;

/**
 * @author han1396735592
 **/
public class HexStringDataParser<P extends CommPort> implements CommPortDataParser<String, P> {
    @Override
    public String parse(byte[] bytes) {
        return HexUtil.bytesToHexString(bytes);
    }
}
