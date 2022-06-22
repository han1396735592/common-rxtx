package cn.qqhxj.rxtx.reader;

import gnu.io.CommPort;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author han1396735592
 * @date 2022/6/9 11:14
 */

public abstract class BaseCommPortReader<T extends CommPort> implements CommPortReader<T> {

    private T commPort;

    public void setCommPort(T commPort) {
        this.commPort = commPort;
    }

    public T getCommPort() {
        return commPort;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return commPort.getInputStream();
    }
}
