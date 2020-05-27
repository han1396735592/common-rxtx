package cn.qqhxj.common.rxtx;


import cn.qqhxj.common.rxtx.parse.SerialDataParser;
import cn.qqhxj.common.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.common.rxtx.processor.SerialDataProcessor;
import cn.qqhxj.common.rxtx.reader.SerialReader;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.TooManyListenersException;

/**
 * @author han xinjian
 **/
public class SerialContext {

    private  SerialPort serialPort;

    private  SerialReader serialReader;

    private  SerialByteDataProcessor serialByteDataProcessor;

    private  SerialPortEventListener serialPortEventListener;

    public  SerialPortEventListener getSerialPortEventListener() {
        return serialPortEventListener;
    }

    public  void setSerialPortEventListener(SerialPortEventListener serialPortEventListener) {
        this.serialPortEventListener = serialPortEventListener;
        autoSerialPortAddEventListener(serialPort);
    }

    private  void autoSerialPortAddEventListener(SerialPort serialPort) {
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

    public  SerialReader getSerialReader() {
        return serialReader;
    }

    public  SerialByteDataProcessor getSerialByteDataProcessor() {
        return serialByteDataProcessor;
    }

    public  void setSerialByteDataProcessor(SerialByteDataProcessor serialByteDataProcessor) {
        this.serialByteDataProcessor = serialByteDataProcessor;
    }

    public  void setSerialDataParserSet(Set<SerialDataParser> serialDataParserSet) {
        this.serialDataParserSet = serialDataParserSet;
    }

    public  void setSerialDataProcessorSet(Set<SerialDataProcessor> serialDataProcessorSet) {
        this.serialDataProcessorSet = serialDataProcessorSet;
    }

    private  Set<SerialDataParser> serialDataParserSet =
            Collections.synchronizedSet(new HashSet<SerialDataParser>());

    public  Set<SerialDataProcessor> serialDataProcessorSet =
            Collections.synchronizedSet(new HashSet<SerialDataProcessor>());

    public  Set<SerialDataProcessor> getSerialDataProcessorSet() {
        return serialDataProcessorSet;
    }

    public  Set<SerialDataParser> getSerialDataParserSet() {
        return serialDataParserSet;
    }

    public  void setSerialReader(SerialReader serialReader) {
        this.serialReader = serialReader;
    }

    public  void setSerialPort(SerialPort serialPort) {
        this.serialPort = serialPort;
        autoSerialPortAddEventListener(serialPort);
    }

    public  final int DEFAULT_OUT_TIME = 100;

    public  byte[] sendAndRead(byte[] data) {
        return sendAndRead(data, DEFAULT_OUT_TIME);
    }

    public  <T> T sendAndRead(byte[] data, SerialDataParser<T> parser) {
        byte[] bytes = sendAndRead(data);
        return parser.parse(bytes);
    }

    public  byte[] sendAndRead(byte[] data, int outTime) {
        serialPort.notifyOnDataAvailable(false);
        sendData(data);
        return readData(outTime);
    }

    public  <T> T sendAndRead(byte[] data, int outTime, SerialDataParser<T> parser) {
        byte[] bytes = sendAndRead(data, outTime);
        return parser.parse(bytes);
    }

    public  byte[] readData() {
        return readData(DEFAULT_OUT_TIME);
    }

    public  byte[] readData(int outTime) {
        serialPort.notifyOnDataAvailable(false);
        while (outTime-- > 0) {
            byte[] bytes = serialReader.readBytes();
            if (bytes != null && bytes.length > 0) {
                serialPort.notifyOnDataAvailable(true);
                return bytes;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        serialPort.notifyOnDataAvailable(true);
        return null;
    }

    public  boolean sendData(byte[] data) {
        try {
            serialPort.getOutputStream().write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public  boolean sendData(SerialSendDataEntity obj) {
        return sendData(obj.getBytes());
    }

    public  SerialPort getSerialPort() {
        return serialPort;
    }
}
