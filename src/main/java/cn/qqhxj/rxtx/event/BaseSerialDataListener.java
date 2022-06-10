package cn.qqhxj.rxtx.event;

import cn.qqhxj.rxtx.SerialContext;
import gnu.io.SerialPortEventListener;

/**
 * @author han1396735592
 * @date 2022/6/9 11:46
 */

public abstract class BaseSerialDataListener implements SerialPortEventListener {
    protected final SerialContext serialContext;

    public BaseSerialDataListener(SerialContext serialContext) {
        this.serialContext = serialContext;
    }


}
