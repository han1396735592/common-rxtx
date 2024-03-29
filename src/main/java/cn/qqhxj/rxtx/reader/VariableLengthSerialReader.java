package cn.qqhxj.rxtx.reader;


import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author han1396735592
 **/
public class VariableLengthSerialReader extends BaseSerialReader {

    private final ByteBuffer byteBuffer;


    private final char startChar;

    private final char endChar;

    public VariableLengthSerialReader() {
        this('{', '}');
    }

    public VariableLengthSerialReader(char startChar, char endChar) {
        this(startChar, endChar, 1024);
    }

    public VariableLengthSerialReader(char startChar, char endChar, int length) {
        this.startChar = startChar;
        this.endChar = endChar;
        this.byteBuffer = ByteBuffer.allocate(length);
    }

    @Override
    public byte[] readBytes() {
        int ch = 0;
        while (ch != -1) {
            try {
                ch = this.getInputStream().read();
                if (ch == startChar) {
                    byteBuffer.position(0);
                    byteBuffer.put((byte) ch);
                    continue;
                }
                if (ch == endChar) {
                    if (byteBuffer.position() > 0) {
                        if (((char) byteBuffer.get(0)) == startChar) {
                            byteBuffer.put((byte) ch);
                            byte[] array = Arrays.copyOf(byteBuffer.array(), byteBuffer.position());
                            byteBuffer.position(0);
                            return array;
                        } else {
                            byteBuffer.position(0);
                        }
                    }
                }
                if (ch != -1) {
                    byteBuffer.put((byte) ch);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return new byte[0];
    }
}
