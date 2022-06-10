package cn.qqhxj.rxtx.reader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author han1396735592
 **/
public class AnyDataReader extends BaseSerialReader {

    @Override
    public byte[] readBytes() {
        try {
            InputStream inputStream = this.getInputStream();
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }

}
