package cn.qqhxj.rxtx.reader;

import cn.qqhxj.rxtx.context.SerialContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author han1396735592
 */

public abstract class BaseSerialReader implements SerialReader {

    private SerialContext serialContext;

    @Override
    public void setSerialContext(SerialContext serialContext) {
        this.serialContext = serialContext;
    }

    @Override
    public SerialContext setSerialContext() {
        return serialContext;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return serialContext.getInputStream();
    }
    public byte[] readAvailableBytes() {
        try {
            InputStream inputStream = this.getInputStream();
            byte[] bytes = new byte[inputStream.available()];
            inputStream.read(bytes);
            return bytes;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new byte[0];
    }
}
