package cn.qqhxj.rxtx;

import cn.qqhxj.rxtx.event.DefaultSerialDataListener;
import cn.qqhxj.rxtx.processor.SerialByteDataProcessor;
import cn.qqhxj.rxtx.reader.AnyDataReader;
import gnu.io.*;

import java.util.ArrayList;

/**
 * @author han1396735592
 * @date 2022/6/9 10:54
 */

public class Test {
    public static void main(String[] args) throws Exception {
        System.out.println(ParallelUtils.getNameList());
        ArrayList<CommPortIdentifier> commPortIdentifierList = SerialUtils.getCommPortIdentifierList();
        for (CommPortIdentifier commPortIdentifier :commPortIdentifierList){
            System.out.println(commPortIdentifier);
            commPortIdentifier.addPortOwnershipListener(new CommPortOwnershipListener() {
                @Override
                public void ownershipChange(int type) {
                    System.out.println(type);
                }
            });
        }

        ArrayList<String> commNames = SerialUtils.getCommNames();
        System.out.println(commNames);
        // 连接串口
        for (String name : commNames) {
            try {
                test(name);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        while (true) {
            Thread.sleep(1000);
            for (CommPortIdentifier commPortIdentifier :commPortIdentifierList) {
                System.out.println(commPortIdentifier);
            }
            System.out.println( SerialUtils.getCommNames());
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
