package cn.qqhxj.rxtx;

import java.util.concurrent.*;

/**
 * @author han1396735592
 * @date 2023/7/4 10:30
 */

public class EventExecutor extends ScheduledThreadPoolExecutor {

    public EventExecutor(int corePoolSize) {
        super(corePoolSize);
    }
}
