package cn.qqhxj.rxtx;


import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.rxtx.processor.SerialDataProcessor;
import cn.qqhxj.rxtx.parse.SerialDataParser;
import cn.qqhxj.rxtx.reader.BaseSerialReader;
import cn.qqhxj.rxtx.reader.SerialReader;
import gnu.io.SerialPort;
import gnu.io.SerialPortEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author han1396735592
 **/
public class SerialContext {
    private static final Logger log = LoggerFactory.getLogger(SerialContext.class);
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

    private final Map<Class, SerialDataParser> serialDataParserMap = Collections.synchronizedMap(new HashMap<>());

    public final Map<Class, SerialDataProcessor> serialDataProcessorMap = Collections.synchronizedMap(new HashMap<>());


    public SerialContext(SerialPort serialPort) {
        this.serialPort = serialPort;
    }

    public SerialContext(SerialPort serialPort, BaseSerialReader serialReader) {
        this.serialPort = serialPort;
        setSerialReader(serialReader);
    }


    public SerialPortEventListener getSerialPortEventListener() {
        return serialPortEventListener;
    }

    public void setSerialPortEventListener(SerialPortEventListener serialPortEventListener) {
        this.serialPortEventListener = serialPortEventListener;
        autoSerialPortAddEventListener(serialPort);
        log.debug("[{}] setSerialPortEventListener {}", serialPort.getName(), serialPortEventListener);
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
        log.debug("[{}] setSerialReader {}", serialPort.getName(), serialReader);
    }

    public SerialByteDataProcessor getSerialByteDataProcessor() {
        return serialByteDataProcessor;
    }

    public void setSerialByteDataProcessor(SerialByteDataProcessor serialByteDataProcessor) {
        this.serialByteDataProcessor = serialByteDataProcessor;
        log.debug("[{}] setSerialByteDataProcessor {}", serialPort.getName(), serialByteDataProcessor);
    }

    public Map<Class, SerialDataParser> getSerialDataParserMap() {
        return serialDataParserMap;
    }

    public Map<Class, SerialDataProcessor> getSerialDataProcessorMap() {
        return serialDataProcessorMap;
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

    public SerialPort getSerialPort() {
        return serialPort;
    }

    public void addSerialDataParser(SerialDataParser<?> value) {
        if (value != null) {
            Method method = null;
            try {
                method = value.getClass().getMethod("parse", byte[].class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (method != null) {
                Class<?> returnType = method.getReturnType();
                log.debug("[{}] addSerialDataParser {}={}", serialPort.getName(), returnType, value);
                if (serialDataParserMap.containsKey(returnType)) {
                    serialDataParserMap.replace(returnType, value);
                } else {
                    serialDataParserMap.put(returnType, value);
                }
            }
        }
    }

    public void addSerialDataProcessor(SerialDataProcessor<?> value) {
        if (value != null) {
            Method[] methods = value.getClass().getMethods();
            for (Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if ("process".equals(method.getName()) && parameterTypes.length == 1) {
                    Class<?> clazz = parameterTypes[0];
                    if (!clazz.equals(Object.class)) {
                        log.debug("[{}] addSerialDataProcessor {}={}", serialPort.getName(), clazz, value);
                        if (serialDataProcessorMap.containsKey(clazz)) {
                            serialDataProcessorMap.replace(clazz, value);
                        } else {
                            serialDataProcessorMap.put(clazz, value);
                        }
                    }
                }
            }
        }
    }
}
