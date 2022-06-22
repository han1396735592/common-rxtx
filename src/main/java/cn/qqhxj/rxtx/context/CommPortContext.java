package cn.qqhxj.rxtx.context;

import cn.qqhxj.rxtx.event.CommPortEventListener;
import cn.qqhxj.rxtx.parse.CommPortDataParser;
import cn.qqhxj.rxtx.processor.CommPortByteDataProcessor;
import cn.qqhxj.rxtx.processor.CommPortDataProcessor;
import cn.qqhxj.rxtx.reader.BaseCommPortReader;
import cn.qqhxj.rxtx.reader.CommPortReader;
import gnu.io.CommPort;
import gnu.io.SerialPort;

import java.io.IOException;
import java.util.*;

/**
 * @author sinok
 * @date 2022/6/22 11:53
 */

public abstract class CommPortContext<P extends CommPort> {


    /**
     * 通讯端口
     */
    protected final P commPort;

    /**
     * 通讯端口阅读器
     */
    protected CommPortReader<P> commPortReader;

    /**
     * 通讯端口字节处理器
     */
    protected CommPortByteDataProcessor<P> commPortByteDataProcessor;

    /**
     * 通讯端口数据解析器
     */
    protected final Map<String, CommPortDataParser<?, P>> commPortDataParserMap = Collections.synchronizedMap(new HashMap<>());

    /**
     * 通讯端口数据处理器
     */
    protected final Map<String, CommPortDataProcessor<?, P>> commPortDataProcessorMap = Collections.synchronizedMap(new HashMap<>());

    public CommPortContext(P commPort) {
        this.commPort = commPort;
    }


    public CommPortReader<P> getSerialReader() {
        return commPortReader;
    }

    public CommPortContext(P commPort, CommPortReader<P> commPortReader) {
        this.commPort = commPort;
        this.commPortReader = commPortReader;
    }

    public void setSerialReader(BaseCommPortReader<P> serialReader) {
        this.commPortReader = serialReader;
        serialReader.setCommPort(commPort);
    }


    public P getCommPort() {
        return commPort;
    }


    public CommPortByteDataProcessor<P> getSerialByteDataProcessor() {
        return commPortByteDataProcessor;
    }

    public void setSerialByteDataProcessor(CommPortByteDataProcessor<P> commPortByteDataProcessor) {
        this.commPortByteDataProcessor = commPortByteDataProcessor;
    }

    public byte[] readData() {
        return readData(0);
    }


    public boolean sendData(byte[] data) {
        try {
            commPort.getOutputStream().write(data);
            commPort.getOutputStream().flush();
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public final int DEFAULT_OUT_TIME = 100;

    public byte[] sendAndRead(byte[] data) {
        return sendAndRead(data, DEFAULT_OUT_TIME);
    }

    public <T> T sendAndRead(byte[] data, CommPortDataParser<T, SerialPort> parser) {
        byte[] bytes = sendAndRead(data);
        return parser.parse(bytes);
    }


    public <T> T sendAndRead(byte[] data, int outTime, CommPortDataParser<T, SerialPort> parser) {
        byte[] bytes = sendAndRead(data, outTime);
        return parser.parse(bytes);
    }


    public byte[] readData(int outTime) {
        notifyOnDataAvailable(false);
        while (outTime >= 0) {
            byte[] bytes = commPortReader.readBytes();
            if (bytes.length > 0) {
                notifyOnDataAvailable(true);
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
        notifyOnDataAvailable(true);
        return new byte[0];
    }


    protected abstract void notifyOnDataAvailable(boolean b);

    public byte[] sendAndRead(byte[] data, int outTime) {
        notifyOnDataAvailable(false);
        sendData(data);
        return readData(outTime);
    }

    /**
     * 串口监听器
     */
    protected CommPortEventListener<P> commPortEventListener;

    public CommPortEventListener<P> getCommPortEventListener() {
        return commPortEventListener;
    }

    public void setCommPortEventListener(CommPortEventListener<P> commPortEventListener) {
        this.commPortEventListener = commPortEventListener;
        autoBindCommPortEventListener(commPort);
    }

    protected abstract void autoBindCommPortEventListener(P commPort);


    public Map<String, CommPortDataParser<?, P>> getCommPortDataParserMap() {
        return commPortDataParserMap;
    }

    public Map<String, CommPortDataProcessor<?, P>> getCommPortDataProcessorMap() {
        return commPortDataProcessorMap;
    }

}
