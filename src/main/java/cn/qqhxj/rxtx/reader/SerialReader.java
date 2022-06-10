package cn.qqhxj.rxtx.reader;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author han1396735592
 **/
public interface SerialReader {

    /**
     * read a byte array
     *
     * @return data
     */
    byte[] readBytes();


    /**
     * getInputStream
     *
     * @return InputStream
     */
    InputStream getInputStream() throws IOException;
}
