package cn.qqhxj.rxtx.context;

import gnu.io.SerialPort;

/**
 * @author han1396735592
 */
public class SerialPortConfig {
    /**
     * 自动重连
     */
    private boolean autoConnect = true;

    /**
     * 重连间隔
     */
    private int autoReconnectInterval = 10000;
    /**
     * 串口
     */
    private String port;
    /**
     * 别名
     */
    private String alias;

    /**
     * 波特率
     */
    private int baud = 115200;

    private int dataBits = SerialPort.DATABITS_8;

    private int stopBits = SerialPort.STOPBITS_1;

    private int parity = SerialPort.PARITY_NONE;

    public boolean isAutoConnect() {
        return autoConnect;
    }

    public void setAutoConnect(boolean autoConnect) {
        this.autoConnect = autoConnect;
    }


    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }


    public int getDataBits() {
        return dataBits;
    }

    public String getPort() {
        return port;
    }

    public void setPort(String port) {
        this.port = port;
        if (this.alias == null) {
            this.alias = port;
        }
    }

    public int getBaud() {
        return baud;
    }

    public void setBaud(int baud) {
        this.baud = baud;
    }

    public void setDataBits(int dataBits) {
        this.dataBits = dataBits;
    }

    public int getStopBits() {
        return stopBits;
    }

    public void setStopBits(int stopBits) {
        this.stopBits = stopBits;
    }

    public int getParity() {
        return parity;
    }

    public void setParity(int parity) {
        this.parity = parity;
    }

    public int getAutoReconnectInterval() {
        return autoReconnectInterval;
    }

    public void setAutoReconnectInterval(int autoReconnectInterval) {
        this.autoReconnectInterval = autoReconnectInterval;
    }

    @Override
    public String toString() {
        return "{port:" + port + ",alias:" + alias + ",baud:" + baud + ",dataBits:" + dataBits + ",parity:" + parity + ",stopBits:" + stopBits + "}";
    }
}
