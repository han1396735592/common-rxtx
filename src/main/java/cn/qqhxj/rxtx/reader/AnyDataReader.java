package cn.qqhxj.rxtx.reader;


/**
 * @author han1396735592
 **/
public class AnyDataReader extends BaseSerialReader {

    @Override
    public byte[] readBytes() {
        return readAvailableBytes();
    }

}
