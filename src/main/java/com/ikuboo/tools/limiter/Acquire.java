package com.ikuboo.tools.limiter;

/**
 * @author yuanchunsen@jd.com
 * 2018/5/18.
 */
public class Acquire {
    private boolean aquire;
    private Long currentRate;

    public Acquire(boolean aquire, Long currentRate) {
        this.aquire = aquire;
        this.currentRate = currentRate;
    }

    public boolean isAquire() {
        return aquire;
    }
    public Long getCurrentRate() {
        return currentRate;
    }
}
