package cn.qqhxj.rxtx.event;

import cn.qqhxj.rxtx.context.SerialContext;

/**
 * @author han1396735592
 * @date 2023/8/29 11:43
 */

public interface SerialContextEventListener {


    /**
     * 连接成功后触发
     */
    default void connected(SerialContext serialContext) {
    }

    /**
     * 断开连接时触发
     */
    default void disconnected(SerialContext serialContext) {
    }

    /**
     * 连接失败时触发
     */
    default void connectError(SerialContext serialContext) {
    }

    /**
     * 连接失败时触发
     */
    default void hardwareError(SerialContext serialContext) {
    }

}
