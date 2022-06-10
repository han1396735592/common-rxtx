package cn.qqhxj.rxtx.reader;

import gnu.io.SerialPort;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author han1396735592
 * @date 2022/6/9 11:14
 */

public abstract class BaseSerialReader implements SerialReader {

    private SerialPort serialPort;

    public void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return serialPort.getInputStream();
    }
}
