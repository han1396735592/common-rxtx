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

    private static SerialPort serialPort;

    private static SerialReader serialReader;

    private static SerialByteDataProcessor serialByteDataProcessor;

    private static SerialPortEventListener serialPortEventListener;

    public static SerialPortEventListener getSerialPortEventListener() {
        return serialPortEventListener;
    }

    public static void setSerialPortEventListener(SerialPortEventListener serialPortEventListener) {
        SerialContext.serialPortEventListener = serialPortEventListener;
        autoSerialPortAddEventListener(serialPort);
    }

    private static void autoSerialPortAddEventListener(SerialPort serialPort) {
        if (SerialContext.serialPortEventListener != null && SerialContext.serialPort != null) {
            serialPort.removeEventListener();
            try {
                SerialContext.serialPort.addEventListener(SerialContext.serialPortEventListener);
                SerialContext.serialPort.notifyOnDataAvailable(true);
            } catch (TooManyListenersException e) {
                e.printStackTrace();
            }
        }
    }

    public static SerialReader getSerialReader() {
        return serialReader;
    }

    public static SerialByteDataProcessor getSerialByteDataProcessor() {
        return serialByteDataProcessor;
    }

    public static void setSerialByteDataProcessor(SerialByteDataProcessor serialByteDataProcessor) {
        SerialContext.serialByteDataProcessor = serialByteDataProcessor;
    }

    public static void setSerialDataParserSet(Set<SerialDataParser> serialDataParserSet) {
        SerialContext.serialDataParserSet = serialDataParserSet;
    }

    public static void setSerialDataProcessorSet(Set<SerialDataProcessor> serialDataProcessorSet) {
        SerialContext.serialDataProcessorSet = serialDataProcessorSet;
    }

    private static Set<SerialDataParser> serialDataParserSet =
            Collections.synchronizedSet(new HashSet<SerialDataParser>());

    public static Set<SerialDataProcessor> serialDataProcessorSet =
            Collections.synchronizedSet(new HashSet<SerialDataProcessor>());

    public static Set<SerialDataProcessor> getSerialDataProcessorSet() {
        return serialDataProcessorSet;
    }

    public static Set<SerialDataParser> getSerialDataParserSet() {
        return serialDataParserSet;
    }

    public static void setSerialReader(SerialReader serialReader) {
        SerialContext.serialReader = serialReader;
    }

    public static void setSerialPort(SerialPort serialPort) {
        SerialContext.serialPort = serialPort;
        autoSerialPortAddEventListener(serialPort);
    }

    public static final int DEFAULT_OUT_TIME = 100;

    public static byte[] sendAndRead(byte[] data) {
        return sendAndRead(data, DEFAULT_OUT_TIME);
    }

    public static <T> T sendAndRead(byte[] data, SerialDataParser<T> parser) {
        byte[] bytes = sendAndRead(data);
        return parser.parse(data);
    }

    public static byte[] sendAndRead(byte[] data, int outTime) {
        serialPort.notifyOnDataAvailable(false);
        sendData(data);
        return readData(outTime);
    }

    public static <T> T sendAndRead(byte[] data, int outTime, SerialDataParser<T> parser) {
        byte[] bytes = sendAndRead(data, outTime);
        return parser.parse(bytes);
    }

    public static byte[] readData() {
        return readData(DEFAULT_OUT_TIME);
    }

    public static byte[] readData(int outTime) {
        serialPort.notifyOnDataAvailable(false);
        while (outTime-- > 0) {
            byte[] bytes = serialReader.readBytes();
            if (bytes != null && bytes.length > 0) {
                serialPort.notifyOnDataAvailable(true);
                return bytes;
            }
        }
        serialPort.notifyOnDataAvailable(true);
        return null;
    }

    public static boolean sendData(byte[] data) {
        try {
            serialPort.getOutputStream().write(data);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean sendData(SerialSendDataEntity obj) {
        return sendData(obj.getBytes());
    }

    public static SerialPort getSerialPort() {
        return serialPort;
    }
}
