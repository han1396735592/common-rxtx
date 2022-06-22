package cn.qqhxj.rxtx.exception;

/**
 * @author han1396735592
 * @date 2022/6/22 11:28
 */

public class CommPortConnectException extends Exception {

    public CommPortConnectException(String message) {
        super("CommPort Connect Exception:" + message);
    }
}
