package cn.qqhxj.common.rxtx;


import cn.qqhxj.common.rxtx.parse.SerialDataParser;
import cn.qqhxj.common.rxtx.processor.SerialByteDataProcesser;
import cn.qqhxj.common.rxtx.processor.SerialDataProcessor;
import cn.qqhxj.common.rxtx.reader.SerialReader;
import gnu.io.SerialPort;

import java.io.IOException;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

/**
 * @author han xinjian
 **/
public class SerialContext {

    private static SerialPort serialPort;

    private static SerialReader serialReader;

    private static SerialByteDataProcesser serialByteDataProcesser;

    public static void setSerialByteDataProcesser(SerialByteDataProcesser serialByteDataProcesser) {
        SerialContext.serialByteDataProcesser = serialByteDataProcesser;
    }

    public static SerialByteDataProcesser getSerialByteDataProcesser() {
        return serialByteDataProcesser;
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
    }

    public static byte[] readData() {
        return serialReader.readBytes();
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
        try {
            serialPort.getOutputStream().write(obj.getBytes());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static SerialPort getSerialPort() {
        return serialPort;
    }
}
