package cn.qqhxj.rxtx.util;

import cn.qqhxj.rxtx.exception.CommPortConnectException;
import gnu.io.*;

import java.util.ArrayList;
import java.util.Enumeration;
import java.util.stream.Collectors;

/**
 * @author han1396735592
 **/
public class ParallelUtils {

    /**
     * 获取并行端口名称
     *
     * @return 名称列表
     */
    public static ArrayList<String> getNameList() {
        return getIdentifierList().stream().map(CommPortIdentifier::getName).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * 获取所有通信端口标识
     *
     * @return ArrayList<CommPortIdentifier>
     */
    public static ArrayList<CommPortIdentifier> getIdentifierList() {
        Enumeration<CommPortIdentifier> portIdentifiers = CommPortIdentifier.getPortIdentifiers();
        ArrayList<CommPortIdentifier> list = new ArrayList<>();
        while (portIdentifiers.hasMoreElements()) {
            CommPortIdentifier commPortIdentifier = portIdentifiers.nextElement();
            if (commPortIdentifier.getPortType() == CommPortIdentifier.PORT_PARALLEL) {
                list.add(commPortIdentifier);
            }
        }
        return list;
    }

    /**
     * 连接通信端口
     *
     * @param portName 端口名称
     * @param mode     mode
     * @return ParallelPort
     * @throws CommPortConnectException connect error
     */
    public static ParallelPort connect(String portName, int mode) throws CommPortConnectException {
        try {
            CommPortIdentifier portIdentifier = CommPortIdentifier.getPortIdentifier(portName);
            if (portIdentifier.isCurrentlyOwned()) {
                throw new CommPortConnectException(portName + "is using");
            } else {
                if (portIdentifier.getPortType() == CommPortIdentifier.PORT_PARALLEL) {
                    CommPort commPort = null;
                    try {
                        commPort = portIdentifier.open(ParallelUtils.class.getName(), 2000);
                        if (commPort instanceof ParallelPort) {
                            ParallelPort parallelPort = (ParallelPort) commPort;
                            try {
                                parallelPort.setMode(mode);
                            } catch (UnsupportedCommOperationException e) {
                                e.printStackTrace();
                            }
                            return parallelPort;
                        } else {
                            throw new CommPortConnectException(portName + " not as expected");
                        }
                    } catch (PortInUseException e) {
                        throw new CommPortConnectException(portName + "is using");
                    }
                } else {
                    throw new CommPortConnectException(portName + " portType  is not port_parallel");
                }
            }
        } catch (NoSuchPortException e) {
            throw new CommPortConnectException("No Such Port" + portName);
        }
    }


}
