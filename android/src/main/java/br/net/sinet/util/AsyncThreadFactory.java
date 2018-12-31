

package br.net.sinet.util;

import android.support.annotation.NonNull;

import java.text.MessageFormat;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class AsyncThreadFactory implements ThreadFactory {

    protected static final String DEFAULT_THREAD_NAME_PREFIX = "AsyncTaskThread";

    protected static AtomicInteger threadCounter;

    static {
        threadCounter = new AtomicInteger(1);
    }

    private final String namePrefix;

    public AsyncThreadFactory() {
        this(DEFAULT_THREAD_NAME_PREFIX);
    }

    public AsyncThreadFactory(final String namePrefix) {
        this.namePrefix = namePrefix;
    }

    @Override
    public Thread newThread(@NonNull final Runnable runnable) {
        return new Thread(runnable, MessageFormat.format("{0}-{1,number,#}", namePrefix, threadCounter.getAndIncrement()));
    }

}
