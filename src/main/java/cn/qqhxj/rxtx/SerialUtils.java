package cn.qqhxj.rxtx;

import cn.qqhxj.rxtx.exception.CommPortConnectException;
import gnu.io.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.stream.Collectors;

/**
 * @author han1396735592
 **/
public class SerialUtils {

    /**
     * 获取串口名称列表
     * 请使用 getNameList 代替
     *
     * @return 名称列表
     */
    @Deprecated
    public static ArrayList<String> getCommNames() {
        return getCommPortIdentifierList().stream().map(CommPortIdentifier::getName).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 获取串口名称列表
     *
     * @return 名称列表
     */
    public static ArrayList<String> getNameList() {
        return getCommPortIdentifierList().stream().map(CommPortIdentifier::getName).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 获取所有通信端口标识
     *
     * @return 通信端口标识列表
     */
    public static ArrayList<CommPortIdentifier> getCommPortIdentifierList() {
        Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        ArrayList<CommPortIdentifier> list = new ArrayList<>();
        while (portIdentifiers.hasMoreElements()) {
            CommPortIdentifier commPortIdentifier = portIdentifiers.nextElement();
            if (commPortIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                list.add(commPortIdentifier);
            }
        }
        return list;
    }

    /**
     * connect SerialPort
     *
     * @param portName portName
     * @param baudRate baudRate
     * @param dataBits dataBits
     * @param stopBits stopBits
     * @param parity   parity
     * @return SerialPort
     * @throws CommPortConnectException connect error
     */
    public static SerialPort connect(String portName, int baudRate, int dataBits, int stopBits, int parity) throws CommPortConnectException {
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            if (portIdentifier.isCurrentlyOwned()) {
                throw new CommPortConnectException(portName + " is using");
            } else {
                if (portIdentifier.getPortType() == CommPortIdentifier.PORT_SERIAL) {
                    CommPort commPort = null;
                    try {
                        commPort = portIdentifier.open(SerialUtils.class.getName(), 2000);
                        if (commPort instanceof SerialPort) {
                            SerialPort serialPort = (SerialPort) commPort;
                            try {
                                serialPort.setSerialPortParams(baudRate, dataBits, stopBits, parity);
                            } catch (UnsupportedCommOperationException e) {
                                e.printStackTrace();
                            }
                            return (SerialPort) commPort;
                        } else {
                            throw new CommPortConnectException(portName + " not as expected");
                        }
                    } catch (PortInUseException e) {
                        throw new CommPortConnectException(portName + " is using");
                    }
                } else {
                    throw new CommPortConnectException(portName + " portType  is not port_serial");
                }
            }
        } catch (NoSuchPortException e) {
            throw new CommPortConnectException("No Such Port " + portName);
        }

    }


    /**
     * connect SerialPort
     *
     * @param portName 串口名称
     * @param baudRate 波特率
     * @return SerialPort
     * @throws CommPortConnectException SerialPort connect err
     */
    public static SerialPort connect(String portName, int baudRate) throws CommPortConnectException {
        return connect(portName, baudRate, SerialPort.DATABITS_8, SerialPort.STOPBITS_1, SerialPort.PARITY_NONE);
    }

}
