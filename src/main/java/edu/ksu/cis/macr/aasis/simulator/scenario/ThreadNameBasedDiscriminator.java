package edu.ksu.cis.macr.aasis.simulator.scenario;

import ch.qos.logback.classic.spi.ILoggingEvent;
import ch.qos.logback.core.sift.Discriminator;

/**
 * A class to assist with thread-based logging. See: http://java.dzone.com/articles/siftingappender-logging.
 */
public class ThreadNameBasedDiscriminator implements Discriminator<ILoggingEvent> {
    private static final String KEY = "threadName";
    private boolean started;


    public boolean isStarted() {
        return started;
    }

    @Override
    public String getDiscriminatingValue(ILoggingEvent iLoggingEvent) {
        return Thread.currentThread().getName();
    }

    @Override
    public String getKey() {
        return KEY;
    }

    public void start() {
        started = true;
    }

    public void stop() {
        started = false;
    }
}