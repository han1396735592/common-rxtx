package cn.qqhxj.rxtx;


import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.rxtx.processor.SerialDataProcessor;
import cn.qqhxj.rxtx.parse.SerialDataParser;
import cn.qqhxj.rxtx.reader.BaseSerialReader;
import cn.qqhxj.rxtx.reader.SerialReader;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TooManyListenersException;

/**
 * @author han1396735592
 **/
public class SerialContext {

    /**
     * 串口对象
     */
    private final SerialPort serialPort;

    /**
     * 串口监听器
     */
    private SerialPortEventListener serialPortEventListener;


    /**
     * 串口数据阅读器
     */
    private SerialReader serialReader;

    private SerialByteDataProcessor serialByteDataProcessor;

    private final Set<SerialDataParser> serialDataParserSet = Collections.synchronizedSet(new HashSet<SerialDataParser>());

    public final Set<SerialDataProcessor> serialDataProcessorSet = Collections.synchronizedSet(new HashSet<SerialDataProcessor>());


    public SerialContext(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public SerialContext(SerialPort serialPort, SerialReader serialReader) {
        this.serialPort = serialPort;
        this.serialReader = serialReader;
    }


    public SerialPortEventListener getSerialPortEventListener() {
        return serialPortEventListener;
    }

    public void setSerialPortEventListener(SerialPortEventListener serialPortEventListener) {
        this.serialPortEventListener = serialPortEventListener;
        autoSerialPortAddEventListener(serialPort);
    }

    private void autoSerialPortAddEventListener(SerialPort serialPort) {
        if (this.serialPortEventListener != null && this.serialPort != null) {
            serialPort.removeEventListener();
            try {
                this.serialPort.addEventListener(this.serialPortEventListener);
                this.serialPort.notifyOnDataAvailable(true);
            } catch (TooManyListenersException e) {
                e.printStackTrace();
            }
        }
    }

    public SerialReader getSerialReader() {
        return serialReader;
    }

    public void setSerialReader(BaseSerialReader serialReader) {
        this.serialReader = serialReader;
        serialReader.setSerialPort(serialPort);
    }

    public SerialByteDataProcessor getSerialByteDataProcessor() {
        return serialByteDataProcessor;
    }

    public void setSerialByteDataProcessor(SerialByteDataProcessor serialByteDataProcessor) {
        this.serialByteDataProcessor = serialByteDataProcessor;
    }


    public Set<SerialDataProcessor> getSerialDataProcessorSet() {
        return serialDataProcessorSet;
    }

    public Set<SerialDataParser> getSerialDataParserSet() {
        return serialDataParserSet;
    }


    public final int DEFAULT_OUT_TIME = 100;

    public byte[] sendAndRead(byte[] data) {
        return sendAndRead(data, DEFAULT_OUT_TIME);
    }

    public <T> T sendAndRead(byte[] data, SerialDataParser<T> parser) {
        byte[] bytes = sendAndRead(data);
        return parser.parse(bytes);
    }

    public byte[] sendAndRead(byte[] data, int outTime) {
        serialPort.notifyOnDataAvailable(false);
        sendData(data);
        return readData(outTime);
    }

    public <T> T sendAndRead(byte[] data, int outTime, SerialDataParser<T> parser) {
        byte[] bytes = sendAndRead(data, outTime);
        return parser.parse(bytes);
    }

    public byte[] readData() {
        return readData(0);
    }

    public byte[] readData(int outTime) {
        serialPort.notifyOnDataAvailable(false);
        while (outTime >= 0) {
            byte[] bytes = serialReader.readBytes();
            if (bytes.length > 0) {
                serialPort.notifyOnDataAvailable(true);
                return bytes;
            }
            outTime--;
            if (outTime > 0) {
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
        serialPort.notifyOnDataAvailable(true);
        return new byte[0];
    }

    public boolean sendData(byte[] data) {
        try {
            serialPort.getOutputStream().write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean sendData(SerialSendDataEntity obj) {
        return sendData(obj.getBytes());
    }

    public SerialPort getSerialPort() {
        return serialPort;
    }
}
