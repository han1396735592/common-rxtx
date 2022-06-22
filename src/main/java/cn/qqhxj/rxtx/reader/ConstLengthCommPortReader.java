package cn.qqhxj.rxtx.reader;


import java.io.IOException;

/**
 * @author han1396735592
 **/
public class ConstLengthCommPortReader extends BaseCommPortReader {
    private int length;

    private int index = 0;

    private byte[] bytes;

    private boolean read = true;

    @Override
    public byte[] readBytes() {
        for (; index < length; index++) {
            try {
                int read =this.getInputStream().read();
                if (read == -1) {
                    break;
                } else {
                    bytes[index] = (byte) read;
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        if (index == length) {
            index = 0;
            return bytes;
        }
        return null;
    }

    public ConstLengthCommPortReader() {
        length = 24;
        bytes = new byte[length];
    }

    ConstLengthCommPortReader(int length) {
        this.length = length;
        bytes = new byte[length];
    }

}
