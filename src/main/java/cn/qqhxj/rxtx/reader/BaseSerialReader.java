package cn.qqhxj.rxtx.reader;

import cn.qqhxj.rxtx.context.AbstractSerialContext;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author han1396735592
 */

public abstract class BaseSerialReader implements SerialReader {

    private AbstractSerialContext abstractSerialContext;

    @Override
    public void setAbstractSerialContext(AbstractSerialContext abstractSerialContext) {
        this.abstractSerialContext = abstractSerialContext;
    }

    @Override
    public AbstractSerialContext getAbstractSerialContext() {
        return abstractSerialContext;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return abstractSerialContext.getInputStream();
    }
}
