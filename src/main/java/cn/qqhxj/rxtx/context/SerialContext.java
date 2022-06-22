package cn.qqhxj.rxtx.context;


import cn.qqhxj.rxtx.reader.CommPortReader;
import gnu.io.SerialPort;

import java.util.TooManyListenersException;

/**
 * @author han1396735592
 **/
public class SerialContext extends CommPortContext<SerialPort> {


    public SerialContext(SerialPort serialPort) {
        super(serialPort);
    }

    public SerialContext(SerialPort serialPort, CommPortReader<SerialPort> commPortReader) {
        super(serialPort, commPortReader);
    }

    @Override
    protected void autoBindCommPortEventListener(SerialPort commPort) {
        if (this.commPortEventListener != null && this.commPort != null) {
            commPort.removeEventListener();
            try {
                this.commPort.addEventListener(this.commPortEventListener);
                this.commPort.notifyOnDataAvailable(true);
            } catch (TooManyListenersException e) {
                e.printStackTrace();
            }
        }
    }


    @Override
    protected void notifyOnDataAvailable(boolean b) {
        commPort.notifyOnDataAvailable(b);
    }
}
