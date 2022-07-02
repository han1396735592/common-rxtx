package cn.qqhxj.rxtx.exception;

/**
 * @author han1396735592
 */
public class CommPortConnectException extends Exception {

    public CommPortConnectException(String message) {
        super("CommPort Connect Exception:" + message);
    }
}
