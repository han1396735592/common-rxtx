package cn.qqhxj.rxtx;

import cn.qqhxj.rxtx.event.DefaultSerialDataListener;
import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.rxtx.reader.AnyDataReader;
import gnu.io.SerialPort;

import java.util.ArrayList;

/**
 * @author han1396735592
 * @date 2022/6/9 10:54
 */

public class Test {
    public static void main(String[] args) throws Exception {
        ArrayList<String> commNames = SerialUtils.getCommNames();
        System.out.println(commNames);
        // 连接串口
        commNames.forEach((name) -> {
            try {
                test(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        while (true) {

        }
    }

    public static void test(String portName) throws Exception {
        SerialPort serialPort = SerialUtils.connect(portName, 9600);
        SerialContext serialContext = new SerialContext(serialPort);
        serialContext.setSerialReader(new AnyDataReader());
        serialContext.setSerialPortEventListener(new DefaultSerialDataListener(serialContext));
        serialContext.setSerialByteDataProcessor(new SerialByteDataProcessor() {
            @Override
            public void process(byte[] bytes) {
                System.out.println(portName + ":" + new String(bytes));
                serialContext.sendData(HexUtil.HexStringToBytes("pong " + portName));
            }
        });
    }
}
