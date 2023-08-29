package cn.qqhxj.rxtx.event;


import cn.qqhxj.rxtx.context.AbstractSerialContext;


/**
 * @author han1396735592
 **/
public class SerialContextListener extends AbstractSerialContextListener {
    /**
     * 自动重连间隔
     */
    private Long autoReconnectInterval;

    public SerialContextListener(AbstractSerialContext serialContext, Long autoReconnectInterval) {
        super(serialContext);
        this.autoReconnectInterval = autoReconnectInterval;
    }

    public SerialContextListener(AbstractSerialContext serialContext) {
        this(serialContext, 10000L);
    }

    @Override
    public void connectError() {
        super.connectError();
        this.tryAutoConnect(autoReconnectInterval);
    }

    @Override
    public void hardwareError() {
        super.hardwareError();
        this.tryAutoConnect(autoReconnectInterval);
    }

    public Long getAutoReconnectInterval() {
        return autoReconnectInterval;
    }

    public void setAutoReconnectInterval(Long autoReconnectInterval) {
        this.autoReconnectInterval = autoReconnectInterval;
    }
}
