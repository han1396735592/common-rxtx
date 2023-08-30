package cn.qqhxj.rxtx.event;

import cn.qqhxj.rxtx.context.SerialContext;

/**
 * @author han1396735592
 */

public interface SerialContextEventListener {


    /**
     * 连接成功后触发
     * @param serialContext 串口上下文
     */
    default void connected(SerialContext serialContext) {
    }

    /**
     * 断开连接时触发
     * @param serialContext 串口上下文
     */
    default void disconnected(SerialContext serialContext) {
    }

    /**
     * 连接失败时触发
     * @param serialContext 串口上下文
     */
    default void connectError(SerialContext serialContext) {
    }

    /**
     * 连接失败时触发
     * @param serialContext 串口上下文
     */
    default void hardwareError(SerialContext serialContext) {
    }

}
