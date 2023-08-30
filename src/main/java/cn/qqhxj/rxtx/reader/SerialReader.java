package cn.qqhxj.rxtx.reader;

import cn.qqhxj.rxtx.context.SerialContext;

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
     * @throws IOException error
     */
    InputStream getInputStream() throws IOException;

    /**
     * setSerialContext
     * @param serialContext serialContext
     */
    void setSerialContext(SerialContext serialContext);

    /**
     * setSerialContext
     * @return SerialContext
     */
    SerialContext setSerialContext();

}
