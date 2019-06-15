package cn.qqhxj.common.rxtx.reader;

/**
 * @author han xinjian
 **/
public interface SerialReader {

    /**
     * read a str
     *
     * @return str
     */
    String readString();

    /**
     * read a byte array
     *
     * @return data
     */
    byte[] readBytes();
}
