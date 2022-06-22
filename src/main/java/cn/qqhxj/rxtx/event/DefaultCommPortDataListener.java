package cn.qqhxj.rxtx.event;


import cn.qqhxj.rxtx.context.CommPortContext;
import gnu.io.*;

/**
 * @author han1396735592
 **/
public class DefaultCommPortDataListener extends CommPortEventListener<SerialPort> {


    public DefaultCommPortDataListener(CommPortContext<SerialPort> commPortContext) {
        super(commPortContext);
    }

    @Override
    public void serialEvent(SerialPortEvent ev) {
        if (ev.getEventType() == SerialPortEvent.DATA_AVAILABLE && this.commPortContext != null) {
            processorsDataAvailable();
        } else {
            System.err.println("data parse err or not find parser");
        }
    }

    @Override
    public void parallelEvent(ParallelPortEvent ev) {

    }
}
