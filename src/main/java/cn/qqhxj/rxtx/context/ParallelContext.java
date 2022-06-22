package cn.qqhxj.rxtx.context;

import cn.qqhxj.rxtx.reader.CommPortReader;
import gnu.io.ParallelPort;

import java.util.TooManyListenersException;

/**
 * @author han1396735592
 * @date 2022/6/22 11:13
 */

public class ParallelContext extends CommPortContext<ParallelPort> {

    public ParallelContext(ParallelPort commPort) {
        super(commPort);
    }

    public ParallelContext(ParallelPort commPort, CommPortReader<ParallelPort> commPortReader) {
        super(commPort, commPortReader);
    }


    @Override
    protected void notifyOnDataAvailable(boolean b) {
    }


    @Override
    protected void autoBindCommPortEventListener(ParallelPort commPort) {
        if (this.commPortEventListener != null && this.commPort != null) {
            commPort.removeEventListener();
            try {
                this.commPort.addEventListener(this.commPortEventListener);
            } catch (TooManyListenersException e) {
                e.printStackTrace();
            }
        }
    }
}
