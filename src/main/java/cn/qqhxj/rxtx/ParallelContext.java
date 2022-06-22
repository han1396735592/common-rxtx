package cn.qqhxj.rxtx;

import gnu.io.ParallelPort;

/**
 * @author han1396735592
 * @date 2022/6/22 11:13
 */

public class ParallelContext {
    /**
     * 并口对象
     */
    private final ParallelPort parallelPort;

    public ParallelContext(ParallelPort parallelPort) {
        this.parallelPort = parallelPort;
    }
}
