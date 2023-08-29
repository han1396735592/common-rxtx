package cn.qqhxj.rxtx.context;

import cn.qqhxj.rxtx.event.AbstractSerialContextListener;
import cn.qqhxj.rxtx.parse.SerialDataParser;
import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.rxtx.processor.SerialDataProcessor;
import cn.qqhxj.rxtx.reader.BaseSerialReader;
import gnu.io.NRSerialPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.TooManyListenersException;

/**
 * @author han1396735592
 */

public abstract class AbstractSerialContext {
    private static final Logger log = LoggerFactory.getLogger(AbstractSerialContext.class);
    public final int DEFAULT_OUT_TIME = 100;
    /**
     * 串口对象
     */
    protected final NRSerialPort serialPort;
    protected SerialPortConfig serialPortConfig;
    /**
     * 串口监听器
     */
    protected AbstractSerialContextListener serialContextListener;

    /**
     * 串口数据阅读器
     */
    protected BaseSerialReader serialReader;

    /**
     * 获取串口数据阅读器
     *
     * @return 串口数据阅读器
     */
    public BaseSerialReader getSerialReader() {
        return serialReader;
    }

    /**
     * 获取串口
     *
     * @return 获取串口
     */
    public NRSerialPort getSerialPort() {
        return serialPort;
    }

    /**
     * 连接串口
     *
     * @return 是否成功
     */
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
                if (serialContextListener != null) {
                    serialContextListener.connected();
                }
            } else {
                if (serialContextListener != null) {
                    serialContextListener.connectError();
                }
            }
        }
        return connect;
    }

    /**
     * 是否连接成功
     *
     * @return isConnected
     */
    public boolean isConnected() {
        return serialPort.isConnected();
    }

    /**
     * 断开连接
     */
    public void disconnect() {
        serialPort.removeEventListener();
        serialPort.disconnect();
        if (serialContextListener != null) {
            serialContextListener.disconnected();
        }
    }


    private void autoSerialPortAddEventListener() {
        if (this.serialContextListener != null && this.serialPort.getSerialPortInstance() != null) {
            serialPort.removeEventListener();
            try {
                this.serialPort.addEventListener(this.serialContextListener);
                this.serialPort.getSerialPortInstance().notifyOnDataAvailable(true);
                this.serialPort.getSerialPortInstance().notifyOnBreakInterrupt(true);
            } catch (TooManyListenersException e) {
                e.printStackTrace();
            }
        }
    }

    public AbstractSerialContextListener getSerialContextListener() {
        return serialContextListener;
    }

    public void setSerialContextListener(AbstractSerialContextListener serialContextListener) {
        log.info("[{}({})] setSerialContextListener {}", serialPortConfig.getAlias(), serialPortConfig.getPort(), serialContextListener);
        this.serialContextListener = serialContextListener;
        autoSerialPortAddEventListener();
        if (serialPortConfig.isAutoConnect()) {
            this.connect();
        }
    }

    public void setSerialReader(BaseSerialReader serialReader) {
        this.serialReader = serialReader;
        serialReader.setAbstractSerialContext(this);
        log.info("[{}({})] serialReader {}", serialPortConfig.getAlias(), serialPortConfig.getPort(), serialReader);
    }

    public SerialByteDataProcessor getSerialByteDataProcessor() {
        return serialByteDataProcessor;
    }

    public void setSerialByteDataProcessor(SerialByteDataProcessor serialByteDataProcessor) {
        this.serialByteDataProcessor = serialByteDataProcessor;
        log.info("[{}({})] setSerialByteDataProcessor {}", serialPortConfig.getAlias(), serialPortConfig.getPort(), serialByteDataProcessor);
    }

    public Map<Class, SerialDataParser> getSerialDataParserMap() {
        return serialDataParserMap;
    }

    public Map<Class, SerialDataProcessor> getSerialDataProcessorMap() {
        return serialDataProcessorMap;
    }

    protected SerialByteDataProcessor serialByteDataProcessor;

    protected final Map<Class, SerialDataParser> serialDataParserMap = Collections.synchronizedMap(new HashMap<>());

    protected final Map<Class, SerialDataProcessor> serialDataProcessorMap = Collections.synchronizedMap(new HashMap<>());

    public AbstractSerialContext(SerialPortConfig serialPortConfig) {
        this.serialPort = new NRSerialPort(serialPortConfig.getPort(),
                serialPortConfig.getBaud(),
                serialPortConfig.getParity(),
                serialPortConfig.getDataBits(),
                serialPortConfig.getStopBits());
        this.serialPortConfig = serialPortConfig;
    }


    public AbstractSerialContext(SerialPortConfig serialPortConfig, BaseSerialReader serialReader) {
        this(serialPortConfig);
        this.serialReader = serialReader;
    }

    /**
     * 获取输入流
     *
     * @return InputStream
     */
    public InputStream getInputStream() {
        return serialPort.getInputStream();
    }

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

    public boolean sendData(byte[] data) {
        if (isConnected()) {
            try {
                serialPort.getOutputStream().write(data);
                serialPort.getOutputStream().flush();
                return true;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public void addSerialDataParser(SerialDataParser<?> value) {
        if (value != null) {
            Method method = null;
            try {
                method = value.getClass().getMethod("parse", byte[].class, AbstractSerialContext.class);
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

    public SerialPortConfig getSerialPortConfig() {
        return serialPortConfig;
    }


    public abstract byte[] readData();

    public byte[] sendAndRead(byte[] data) {
        return sendAndRead(data, DEFAULT_OUT_TIME);
    }

    public byte[] sendAndRead(byte[] data, int outTime) {
        if (serialPort.isConnected()) {
            serialPort.notifyOnDataAvailable(false);
        }
        if (sendData(data)) {
            return readData(outTime);
        }
        return new byte[0];
    }

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


    public <T> T sendAndRead(byte[] data, Class<T> clazz) {
        byte[] bytes = sendAndRead(data);
        Map<Class, SerialDataParser> dataParserMap = this.getSerialDataParserMap();
        SerialDataParser<T> serialDataParser = dataParserMap.get(clazz);
        return serialDataParser.parse(bytes, this);
    }


    public <T> T sendAndRead(byte[] data, int outTime, Class<T> clazz) {
        byte[] bytes = sendAndRead(data, outTime);
        Map<Class, SerialDataParser> dataParserMap = this.getSerialDataParserMap();
        SerialDataParser<T> serialDataParser = dataParserMap.get(clazz);
        return serialDataParser.parse(bytes, this);
    }
}
