package cn.qqhxj.rxtx.reader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author han1396735592
 **/
public class AnyDataReader extends BaseSerialReader {

    @Override
    public byte[] readBytes() {
        return readAvailableBytes();
    }

}
