package cn.qqhxj.common.rxtx;

import cn.qqhxj.common.rxtx.parse.StringSerialDataParser;
import cn.qqhxj.common.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.common.rxtx.processor.SerialDataProcessor;
import cn.qqhxj.common.rxtx.reader.VariableLengthSerialReader;
import gnu.io.SerialPort;

/**
 * @author han xinjian
 * @date 2019-06-15 18:54
 **/
public class SerialUtilsTest {


    public static void main(String[] args) {

        String portName = SerialUtils.getCommNames().get(1);
        try {
            SerialPort connect = SerialUtils.connect(portName, 9600);
            SerialContext.setSerialPortEventListener(new DefaultSerialDataListener());
            SerialContext.setSerialPort(connect);
            SerialContext.getSerialDataParserSet().add(new StringSerialDataParser());
            SerialContext.setSerialReader(new VariableLengthSerialReader('{', '}'));
            SerialContext.getSerialDataProcessorSet().add(new SerialDataProcessor<String>() {
                @Override
                public void process(String s) {
                    System.out.println(s);
                }
            });

            SerialContext.setSerialByteDataProcessor(new SerialByteDataProcessor() {
                @Override
                public void process(byte[] bytes) {
                    System.out.println(bytes.length);
                    for (byte b :bytes){
                        System.out.print(((char) b)+" ");
                    }
                }
            });
            while (true) {
            }

        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}