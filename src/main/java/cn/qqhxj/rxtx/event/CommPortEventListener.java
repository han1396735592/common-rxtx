package cn.qqhxj.rxtx.event;

import cn.qqhxj.rxtx.context.CommPortContext;
import cn.qqhxj.rxtx.parse.CommPortDataParser;
import cn.qqhxj.rxtx.processor.CommPortByteDataProcessor;
import cn.qqhxj.rxtx.processor.CommPortDataProcessor;
import gnu.io.CommPort;
import gnu.io.ParallelPortEventListener;
import gnu.io.SerialPortEventListener;

import java.lang.reflect.Method;
import java.util.Map;

/**
 * @author han1396735592
 * @date 2022/6/9 11:46
 */

public abstract class CommPortEventListener<T extends CommPort> implements SerialPortEventListener, ParallelPortEventListener {
    protected final CommPortContext<T> commPortContext;

    public CommPortEventListener(CommPortContext<T> commPortContext) {
        this.commPortContext = commPortContext;
    }

    protected void processorsDataAvailable() {
        byte[] bytes = commPortContext.readData();
        if (bytes.length == 0) {
            return;
        }
        Map<String, CommPortDataParser<?, T>> commPortDataParserMap = commPortContext.getCommPortDataParserMap();
        Map<String, CommPortDataProcessor<?, T>> commPortDataProcessorMap = commPortContext.getCommPortDataProcessorMap();
        commPortDataParserMap.forEach((k, v) -> {
            Object parse = v.parse(bytes);
            if (parse.getClass().getName().equals(k)) {
                CommPortDataProcessor<?, T> commPortDataProcessor = commPortDataProcessorMap.get(k);
                if (commPortDataProcessor != null) {
                    try {
                        Class portDataProcessorClass = commPortDataProcessor.getClass();
                        Method method = portDataProcessorClass.getMethod("process", parse.getClass());
                        method.setAccessible(true);
                        method.invoke(commPortDataProcessor, parse);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        CommPortByteDataProcessor<T> processor = commPortContext.getSerialByteDataProcessor();
        if (processor != null) {
            processor.process(bytes);
        }
    }


}
