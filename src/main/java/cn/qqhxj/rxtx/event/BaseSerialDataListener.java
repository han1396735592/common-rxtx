package cn.qqhxj.rxtx.event;

import cn.qqhxj.rxtx.SerialContext;
import gnu.io.SerialPortEventListener;

/**
 * @author han1396735592
 */
public abstract class BaseSerialDataListener implements SerialPortEventListener {
    protected final SerialContext serialContext;

    public BaseSerialDataListener(SerialContext serialContext) {
        this.serialContext = serialContext;
    }


}
