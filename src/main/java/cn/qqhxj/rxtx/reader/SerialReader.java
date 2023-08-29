package cn.qqhxj.rxtx.reader;

import cn.qqhxj.rxtx.context.AbstractSerialContext;

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
     * setAbstractSerialContext
     * @param abstractSerialContext abstractSerialContext
     */
    void setAbstractSerialContext(AbstractSerialContext abstractSerialContext);

    /**
     * getAbstractSerialContext
     * @return AbstractSerialContext
     */
    AbstractSerialContext getAbstractSerialContext();

}
