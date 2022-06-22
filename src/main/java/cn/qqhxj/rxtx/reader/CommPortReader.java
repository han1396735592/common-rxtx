package cn.qqhxj.rxtx.reader;

import gnu.io.CommPort;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author han1396735592
 **/
public interface CommPortReader<T extends CommPort> {

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
