package cn.qqhxj.rxtx.event;


import cn.qqhxj.rxtx.SerialContext;
import cn.qqhxj.rxtx.parse.SerialDataParser;
import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.rxtx.processor.SerialDataProcessor;
import gnu.io.SerialPortEvent;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Set;

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
            Set<SerialDataParser> parserSet = serialContext.getSerialDataParserSet();
            byte[] bytes = serialContext.readData();
            if (bytes.length == 0) {
                return;
            }
            Object parse;
            for (SerialDataParser serialDataParser : parserSet) {
                parse = serialDataParser.parse(bytes);
                if (parse != null) {
                    dataProcessors(parse);
                }
            }
            if (bytes.length > 0) {
                SerialByteDataProcessor processor = serialContext.getSerialByteDataProcessor();
                if (processor != null) {
                    processor.process(bytes);
                }
            }
        } else {
            System.err.println("data parse err or not find parser");
        }
    }

    private void dataProcessors(Object obj) {
        Set<SerialDataProcessor> dataProcessors = serialContext.getSerialDataProcessorSet();
        for (SerialDataProcessor serialDataProcessor : dataProcessors) {
            Class cl = serialDataProcessor.getClass();
            Class c2 = cl.getSuperclass();
            while (!c2.equals(Object.class)) {
                cl = cl.getSuperclass();
                c2 = cl.getSuperclass();
            }
            Type[] types = cl.getGenericInterfaces();
            for (Type type : types) {
                if (type instanceof ParameterizedType) {
                    ParameterizedType parameterizedType = (ParameterizedType) type;
                    Type rawType = parameterizedType.getRawType();
                    if (rawType instanceof Class) {
                        boolean equals = rawType.equals(SerialDataProcessor.class);
                        if (equals) {
                            String typeName = ((ParameterizedType) type).getActualTypeArguments()[0].getTypeName();
                            try {
                                Class<?> forName = Class.forName(typeName);
                                if (forName == obj.getClass()) {
                                    serialDataProcessor.process(obj);
                                }
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
            }
        }
    }
}
