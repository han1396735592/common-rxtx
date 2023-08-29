package cn.qqhxj.rxtx.reader;


import java.io.IOException;

/**
 * @author han1396735592
 **/
public class ConstLengthSerialReader extends BaseSerialReader {
    private final int length;

    private final byte[] bytes;

    private int readLength = 0;

    @Override
    public byte[] readBytes() {
        try {
            while (readLength != length) {
                int read = this.getInputStream().read();
                if (read != -1) {
                    bytes[readLength] = (byte) read;
                    readLength++;
                } else {
                    return new byte[0];
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        readLength = 0;
        return bytes;
    }

    public ConstLengthSerialReader() {
        length = 24;
        bytes = new byte[length];
    }

    public ConstLengthSerialReader(int length) {
        this.length = length;
        bytes = new byte[length];
    }

}
