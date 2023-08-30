package cn.qqhxj.rxtx.context;

import cn.qqhxj.rxtx.event.SerialContextEventDispatcher;
import cn.qqhxj.rxtx.event.SerialContextEventListener;
import cn.qqhxj.rxtx.parse.SerialDataParser;
import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.rxtx.processor.SerialDataProcessor;
import cn.qqhxj.rxtx.reader.SerialReader;
import gnu.io.NRSerialPort;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

/**
 * @author han1396735592
 */

public interface SerialContext {


    /**
     * 获取串口数据阅读器
     *
     * @return 串口数据阅读器
     */
    SerialReader getSerialReader();

    /**
     * 获取连接次数
     * @return connectCount
     */
    int connectCount();

    /**
     * 获取串口
     *
     * @return 获取串口
     */
    NRSerialPort getSerialPort();

    /**
     * 设置串口上下文事件监听器
     *
     * @param serialContextEventListener 串口上下文事件监听器
     */
    void setSerialContextEventListener(SerialContextEventListener serialContextEventListener);

    /**
     * 获取事件监听器
     *
     * @return 事件监听器
     */
    SerialContextEventListener getSerialContextEventListener();


    /**
     * 连接串口
     *
     * @return 是否成功
     */
    boolean connect();

    /**
     * 是否连接成功
     *
     * @return isConnected
     */
    default boolean isConnected() {
        return getSerialPort().isConnected();
    }


    /**
     * 断开连接
     */
    default void disconnect() {
        NRSerialPort serialPort = getSerialPort();
        SerialContextEventDispatcher serialContextEventDispatcher = getSerialContextEventDispatcher();
        serialPort.removeEventListener();
        serialPort.disconnect();
        serialContextEventDispatcher.disconnected();
    }

    SerialContextEventDispatcher getSerialContextEventDispatcher();

    /**
     * setSerialReader
     *
     * @param serialReader serialReader
     */
    void setSerialReader(SerialReader serialReader);

    /**
     * getSerialByteDataProcessor
     *
     * @return SerialByteDataProcessor
     */
    SerialByteDataProcessor getSerialByteDataProcessor();

    /**
     * 设置串口byte数据处理器
     *
     * @param serialByteDataProcessor serialByteDataProcessor
     */
    void setSerialByteDataProcessor(SerialByteDataProcessor serialByteDataProcessor);

    /**
     * 获取串口数据解析器
     *
     * @return 串口数据解析器
     */
    Map<Class, SerialDataParser> getSerialDataParserMap();

    /**
     * 获取串口数据处理器
     *
     * @return 串口数据处理器
     */
    Map<Class, SerialDataProcessor> getSerialDataProcessorMap();

    /**
     * 获取输入流
     *
     * @return InputStream
     */
    default InputStream getInputStream() {
        return getSerialPort().getInputStream();
    }

    /**
     * addSerialDataProcessor
     *
     * @param value SerialDataProcessor
     */
    void addSerialDataProcessor(SerialDataProcessor<?> value);

    /**
     * sendData
     *
     * @param data data
     * @return boolean
     */
    default boolean sendData(byte[] data) {
        if (isConnected()) {
            NRSerialPort serialPort = getSerialPort();
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

    /**
     * addSerialDataParser
     *
     * @param value value
     */
    void addSerialDataParser(SerialDataParser<?> value);

    /**
     * 获取串口配置
     *
     * @return 串口配置
     */
    SerialPortConfig getSerialPortConfig();

    /**
     * 读取并发送数据
     *
     * @param data 数据
     * @return 读取的数据
     */
    byte[] sendAndRead(byte[] data);

    /**
     * 读取并发送数据
     *
     * @param data    数据
     * @param timeOut 超时时间
     * @return 读取的数据
     */
    byte[] sendAndRead(byte[] data, int timeOut);

    /**
     * 读取数据
     *
     * @param timeOut 超时时间
     * @return 读取的数据
     */
    byte[] readData(int timeOut);

    /**
     * 读取并发送数据
     *
     * @param data  数据
     * @param clazz 类型
     * @param <T>   类型
     * @return 接收的数据
     */
    <T> T sendAndRead(byte[] data, Class<T> clazz);

    /**
     * 读取并发送数据
     *
     * @param data    数据
     * @param timeOut 超时时间
     * @param clazz   类型
     * @param <T>     类型
     * @return 返回的数据
     */
    <T> T sendAndRead(byte[] data, int timeOut, Class<T> clazz);

    /**
     * 读取数据
     *
     * @return 数据
     */
    byte[] readData();

}
