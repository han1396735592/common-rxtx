package cn.qqhxj.rxtx.reader;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author han1396735592
 **/
public class VariableLengthSerialReader extends BaseSerialReader {

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);


    private char startChar = '{';

    private char endChar = '}';

    public VariableLengthSerialReader() {
    }

    public VariableLengthSerialReader(char startChar, char endChar) {
        this.startChar = startChar;
        this.endChar = endChar;
    }

    @Override
    public byte[] readBytes() {
        int ch = 0;
        while (ch != -1) {
            try {
                ch = this.getInputStream().read();
                if (ch == startChar) {
                    byteBuffer = ByteBuffer.allocate(1024);
                    byteBuffer.put((byte) ch);
                    continue;
                }
                if (ch == endChar) {
                    if (byteBuffer.position() > 0) {
                        if (((char) byteBuffer.get(0)) == startChar) {
                            byteBuffer.put((byte) ch);
                            byte[] array = Arrays.copyOf(byteBuffer.array(), byteBuffer.position());
                            byteBuffer = ByteBuffer.allocate(1024);
                            return array;
                        } else {
                            byteBuffer = ByteBuffer.allocate(1024);
                        }
                    }
                }
                byteBuffer.put((byte) ch);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
}
