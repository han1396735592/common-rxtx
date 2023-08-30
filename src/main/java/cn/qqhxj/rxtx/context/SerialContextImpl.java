package cn.qqhxj.rxtx.context;


import cn.qqhxj.rxtx.EventExecutor;
import cn.qqhxj.rxtx.event.SerialContextEventDispatcher;
import cn.qqhxj.rxtx.event.SerialContextEventListener;
import cn.qqhxj.rxtx.parse.SerialDataParser;
import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.rxtx.processor.SerialDataProcessor;
import cn.qqhxj.rxtx.reader.SerialReader;
import gnu.io.NRSerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;


/**
 * @author han1396735592
 **/
public final class SerialContextImpl implements SerialContext {

    private static final Logger log = LoggerFactory.getLogger(SerialContextImpl.class);
    public static int DEFAULT_OUT_TIME = 100;
    public static final EventExecutor EVENT_EXECUTOR = new EventExecutor(2);

    /**
     * 串口对象
     */
    private final NRSerialPort serialPort;

    /**
     * 串口配置
     */
    private final SerialPortConfig serialPortConfig;

    /**
     * 串口数据阅读器
     */
    private SerialReader serialReader;

    /**
     * 串口byte数据处理器
     */
    private SerialByteDataProcessor serialByteDataProcessor;

    /**
     * 串口上下文事件监听器
     */
    private SerialContextEventListener serialContextEventListener;

    /**
     * 串口数据解析器
     */
    private final Map<Class, SerialDataParser> serialDataParserMap = Collections.synchronizedMap(new HashMap<>());

    /**
     * 串口数据处理器
     */
    private final Map<Class, SerialDataProcessor> serialDataProcessorMap = Collections.synchronizedMap(new HashMap<>());
    /**
     * 串口上下文事件分发器
     */
    private final SerialContextEventDispatcher serialContextEventDispatcher;

    public SerialContextImpl(SerialPortConfig serialPortConfig) {
        this.serialPort = new NRSerialPort(serialPortConfig.getPort(),
                serialPortConfig.getBaud(),
                serialPortConfig.getParity(),
                serialPortConfig.getDataBits(),
                serialPortConfig.getStopBits());
        this.serialPortConfig = serialPortConfig;
        this.serialContextEventDispatcher = new SerialContextEventDispatcher(this);
    }

    public SerialContextImpl(SerialPortConfig serialPortConfig, SerialReader serialReader) {
        this(serialPortConfig);
        this.serialReader = serialReader;

    }

    @Override
    public SerialReader getSerialReader() {
        return serialReader;
    }


    @Override
    public NRSerialPort getSerialPort() {
        return serialPort;
    }

    @Override
    public void setSerialContextEventListener(SerialContextEventListener serialContextEventListener) {
        this.serialContextEventListener = serialContextEventListener;
    }

    @Override
    public SerialContextEventListener getSerialContextEventListener() {
        return serialContextEventListener;
    }

    @Override
    public boolean connect() {
        boolean connect = serialPort.isConnected();
        //之前未连接
        if (!connect) {
            try {
                Field port = serialPort.getClass().getDeclaredField("port");
                port.setAccessible(true);
                try {
                    port.set(serialPort, serialPortConfig.getPort());
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            } catch (NoSuchFieldException e) {
                e.printStackTrace();
            }
            serialPort.setStopBits(serialPortConfig.getStopBits());
            serialPort.setDataBits(serialPortConfig.getDataBits());
            serialPort.setParity(serialPortConfig.getParity());
            serialPort.setBaud(serialPortConfig.getBaud());
            connect = serialPort.connect();
            if (connect) {
                autoSerialPortAddEventListener();
                serialContextEventDispatcher.connected();
            } else {
                serialContextEventDispatcher.connectError();
            }
        }
        return connect;
    }


    private void autoSerialPortAddEventListener() {
        if (this.serialPort.getSerialPortInstance() != null) {
            serialPort.removeEventListener();
            try {
                this.serialPort.addEventListener(this.serialContextEventDispatcher);
                this.serialPort.getSerialPortInstance().notifyOnDataAvailable(true);
                this.serialPort.getSerialPortInstance().notifyOnBreakInterrupt(true);
            } catch (TooManyListenersException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setSerialReader(SerialReader serialReader) {
        this.serialReader = serialReader;
        serialReader.setSerialContext(this);
        log.info("[{}({})] serialReader {}", serialPortConfig.getAlias(), serialPortConfig.getPort(), serialReader);
    }

    @Override
    public SerialByteDataProcessor getSerialByteDataProcessor() {
        return serialByteDataProcessor;
    }

    @Override
    public void setSerialByteDataProcessor(SerialByteDataProcessor serialByteDataProcessor) {
        this.serialByteDataProcessor = serialByteDataProcessor;
        log.info("[{}({})] setSerialByteDataProcessor {}", serialPortConfig.getAlias(), serialPortConfig.getPort(), serialByteDataProcessor);
    }


    @Override
    public Map<Class, SerialDataParser> getSerialDataParserMap() {
        return serialDataParserMap;
    }

    @Override
    public Map<Class, SerialDataProcessor> getSerialDataProcessorMap() {
        return serialDataProcessorMap;
    }


    @Override
    public SerialContextEventDispatcher getSerialContextEventDispatcher() {
        return serialContextEventDispatcher;
    }

    @Override
    public void addSerialDataProcessor(SerialDataProcessor<?> value) {
        if (value != null) {
            Method[] methods = value.getClass().getMethods();
            for (Method method : methods) {
                Class<?>[] parameterTypes = method.getParameterTypes();
                if ("process".equals(method.getName()) && parameterTypes.length == 2) {
                    Class<?> clazz = parameterTypes[0];
                    if (!clazz.equals(Object.class)) {
                        log.info("[{}({})] addSerialDataProcessor {}={}", serialPortConfig.getAlias(), serialPortConfig.getPort(), clazz.getName(), value);
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


    @Override
    public void addSerialDataParser(SerialDataParser<?> value) {
        if (value != null) {
            Method method = null;
            try {
                method = value.getClass().getMethod("parse", byte[].class, SerialContext.class);
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
            if (method != null) {
                Class<?> returnType = method.getReturnType();
                log.info("[{}({})] addSerialDataParser {}={}", serialPortConfig.getAlias(), serialPortConfig.getPort(), returnType.getName(), value);
                if (serialDataParserMap.containsKey(returnType)) {
                    serialDataParserMap.replace(returnType, value);
                } else {
                    serialDataParserMap.put(returnType, value);
                }
            }
        }
    }

    @Override
    public SerialPortConfig getSerialPortConfig() {
        return serialPortConfig;
    }

    @Override
    public byte[] sendAndRead(byte[] data) {
        return sendAndRead(data, DEFAULT_OUT_TIME);
    }

    @Override
    public byte[] sendAndRead(byte[] data, int outTime) {
        if (serialPort.isConnected()) {
            serialPort.notifyOnDataAvailable(false);
        }
        if (sendData(data)) {
            return readData(outTime);
        }
        return new byte[0];
    }

    @Override
    public byte[] readData(int outTime) {
        serialPort.notifyOnDataAvailable(false);
        long startTime = System.currentTimeMillis();
        do {
            byte[] bytes = serialReader.readBytes();
            if (bytes.length > 0) {
                serialPort.notifyOnDataAvailable(true);
                return bytes;
            }
            try {
                Thread.sleep(1);
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        } while (System.currentTimeMillis() - startTime <= outTime);
        serialPort.notifyOnDataAvailable(true);
        return new byte[0];
    }

    @Override
    public <T> T sendAndRead(byte[] data, Class<T> clazz) {
        byte[] bytes = sendAndRead(data);
        Map<Class, SerialDataParser> dataParserMap = this.getSerialDataParserMap();
        SerialDataParser<T> serialDataParser = dataParserMap.get(clazz);
        return serialDataParser.parse(bytes, this);
    }

    @Override
    public <T> T sendAndRead(byte[] data, int outTime, Class<T> clazz) {
        byte[] bytes = sendAndRead(data, outTime);
        Map<Class, SerialDataParser> dataParserMap = this.getSerialDataParserMap();
        SerialDataParser<T> serialDataParser = dataParserMap.get(clazz);
        return serialDataParser.parse(bytes, this);
    }

    @Override
    public byte[] readData() {
        return readData(DEFAULT_OUT_TIME);
    }

}
