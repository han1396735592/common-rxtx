package cn.qqhxj.rxtx.event;

import cn.qqhxj.rxtx.context.AbstractSerialContext;
import cn.qqhxj.rxtx.context.SerialContext;
import cn.qqhxj.rxtx.parse.SerialDataParser;
import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.rxtx.processor.SerialDataProcessor;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.TimeUnit;



/**
 * @author han1396735592
 */
public abstract class AbstractSerialContextListener implements SerialPortEventListener {
    protected final AbstractSerialContext serialContext;

    private static final Logger log = LoggerFactory.getLogger(AbstractSerialContextListener.class);

    public AbstractSerialContextListener(AbstractSerialContext serialContext) {
        this.serialContext = serialContext;
    }

    @Override
    public void serialEvent(SerialPortEvent ev) {
        if (ev.getEventType() == SerialPortEvent.DATA_AVAILABLE) {
            byte[] bytes = serialContext.readData();
            if (bytes.length > 0) {
                Map<Class, SerialDataParser> dataParserMap = serialContext.getSerialDataParserMap();
                Map<Class, SerialDataProcessor> serialDataProcessorMap = serialContext.getSerialDataProcessorMap();
                dataParserMap.forEach((clazz, parser) -> {
                    Object parse = parser.parse(bytes, serialContext);
                    if (parse != null && serialDataProcessorMap.get(clazz) != null && clazz.equals(parse.getClass())) {
                        serialDataProcessorMap.get(clazz).process(parse, serialContext);
                    }
                });
                SerialByteDataProcessor processor = serialContext.getSerialByteDataProcessor();
                if (processor != null) {
                    processor.process(bytes, serialContext);
                }
            }
        } else if (ev.getEventType() == SerialPortEvent.HARDWARE_ERROR) {
            this.hardwareError();
            serialContext.disconnect();
        }
    }

    /**
     * 连接成功后触发
     */
    public void connected() {
        log.info("[{}({})] connected", serialContext.getSerialPortConfig().getAlias(), serialContext.getSerialPortConfig().getPort());
    }

    /**
     * 断开连接时触发
     */
    public void disconnected() {
        log.info("[{}({})] disconnected", serialContext.getSerialPortConfig().getAlias(), serialContext.getSerialPortConfig().getPort());
    }

    /**
     * 连接失败是触发
     */
    public void connectError() {
        log.error("[{}({})] connectError", serialContext.getSerialPortConfig().getAlias(), serialContext.getSerialPortConfig().getPort());
    }

    /**
     * 连接失败是触发
     */
    public void hardwareError() {
        log.error("[{}({})] hardwareError", serialContext.getSerialPortConfig().getAlias(), serialContext.getSerialPortConfig().getPort());
    }

    public void tryAutoConnect(long timeout) {
        if (serialContext.getSerialPortConfig().isAutoConnect()) {
            if (timeout >= 0) {
                SerialContext.EVENT_EXECUTOR.schedule(() -> {
                    if (!serialContext.isConnected()) {
                        log.info("[{}({})] try auto connect", serialContext.getSerialPortConfig().getAlias(), serialContext.getSerialPortConfig().getPort());
                        serialContext.connect();
                    }
                }, timeout, TimeUnit.MILLISECONDS);
            }

        }
    }

}
