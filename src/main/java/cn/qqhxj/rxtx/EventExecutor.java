package cn.qqhxj.rxtx;

import java.util.concurrent.*;

/**
 * @author han1396735592
 */

public class EventExecutor extends ScheduledThreadPoolExecutor {

    public EventExecutor(int corePoolSize) {
        super(corePoolSize);
    }
}
