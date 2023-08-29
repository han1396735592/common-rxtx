package cn.qqhxj.rxtx.context;


import cn.qqhxj.rxtx.EventExecutor;
import cn.qqhxj.rxtx.reader.BaseSerialReader;


/**
 * @author han1396735592
 **/
public class SerialContext extends AbstractSerialContext {
    public static final EventExecutor EVENT_EXECUTOR = new EventExecutor(2);

    public SerialContext(SerialPortConfig serialPortConfig) {
        super(serialPortConfig);
    }

    public SerialContext(SerialPortConfig serialPortConfig, BaseSerialReader serialReader) {
        super(serialPortConfig, serialReader);
    }

    @Override
    public byte[] readData() {
        return readData(0);
    }

}
