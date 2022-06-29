package cn.qqhxj.rxtx.event;


import cn.qqhxj.rxtx.SerialContext;
import cn.qqhxj.rxtx.parse.SerialDataParser;
import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.rxtx.processor.SerialDataProcessor;
import gnu.io.SerialPortEvent;

import java.util.Map;

/**
 * @author han1396735592
 **/
public class DefaultSerialDataListener extends BaseSerialDataListener {


    public DefaultSerialDataListener(SerialContext serialContext) {
        super(serialContext);
    }

    @Override
    public void serialEvent(SerialPortEvent ev) {
        if (ev.getEventType() == SerialPortEvent.DATA_AVAILABLE && this.serialContext != null) {
            byte[] bytes = serialContext.readData();
            if (bytes.length == 0) {
                return;
            }
            Map<Class, SerialDataParser> dataParserMap = serialContext.getSerialDataParserMap();
            Map<Class, SerialDataProcessor> serialDataProcessorMap = serialContext.getSerialDataProcessorMap();
            dataParserMap.forEach((clazz, parser) -> {
                Object parse = parser.parse(bytes);
                if (parse == null && serialDataProcessorMap.get(clazz) != null && clazz.equals(parse.getClass())) {
                    serialDataProcessorMap.get(clazz).process(parse);
                }
            });
            SerialByteDataProcessor processor = serialContext.getSerialByteDataProcessor();
            if (processor != null) {
                processor.process(bytes);
            }
        } else {
            System.err.println("data parse err or not find parser");
        }
    }
}
