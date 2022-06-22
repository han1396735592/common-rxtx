package cn.qqhxj.rxtx.reader;

import java.nio.ByteBuffer;
import java.util.Arrays;

/**
 * @author han1396735592
 **/
public class LiveControlCommPortReader extends BaseCommPortReader {


    private byte[] startChat;

    private int flagIndex;

    private int dataLengthIndex;

    private int allLength = 0;

    private int length;

    private ByteBuffer byteBuffer = ByteBuffer.allocate(1024);

    private boolean notOver = false;

    public LiveControlCommPortReader(int flagIndex, int dataLengthIndex, byte... startChat) {
        this.startChat = startChat;
        this.flagIndex = flagIndex;
        this.dataLengthIndex = dataLengthIndex;
    }

    @Override
    public byte[] readBytes() {
        try {
            byte read = ((byte) this.getInputStream().read());
            int index = Arrays.binarySearch(startChat, read);
            if (index >= 0) {
                byteBuffer.put(read);
                allLength = 1;
                notOver = true;
            } else {
                if (notOver) {
                    if (allLength == dataLengthIndex) {
                        length = read;
                    }
                    allLength += 1;
                    byteBuffer.put(read);
                    if (allLength == flagIndex + length) {
                        notOver = false;
                        byte[] array = Arrays.copyOf(byteBuffer.array(), byteBuffer.position());
                        byteBuffer = ByteBuffer.allocate(1024);
                        allLength = 0;
                        return array;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
