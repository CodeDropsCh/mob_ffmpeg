package br.net.sinet.util;

import android.util.Log;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class AsynchronousTaskService {

    protected static final String TAG = "mobile-ffmpeg-android";

    protected final BlockingQueue<Runnable> queue;

    protected final Object executorServiceLock;

    protected ThreadPoolExecutor executorService;

    protected int coreThreadPoolSize;

    protected int maximumThreadPoolSize;

    protected int keepAliveTimeInSeconds;

    public AsynchronousTaskService() {
        this(1, 5, 30);
    }

    public AsynchronousTaskService(final int coreThreadPoolSize, final int maximumThreadPoolSize, final int keepAliveTimeInSeconds) {
        this.executorServiceLock = new Object();
        this.queue = new LinkedBlockingQueue<>();
        this.executorService = null;

        this.coreThreadPoolSize = coreThreadPoolSize;
        this.maximumThreadPoolSize = maximumThreadPoolSize;
        this.keepAliveTimeInSeconds = keepAliveTimeInSeconds;
    }

    public void init(final int coreThreadPoolSize, final int maximumThreadPoolSize, final int keepAliveTimeInSeconds) {
        this.coreThreadPoolSize = coreThreadPoolSize;
        this.maximumThreadPoolSize = maximumThreadPoolSize;
        this.keepAliveTimeInSeconds = keepAliveTimeInSeconds;
    }

    public void initializeExecutorService() {
        synchronized (executorServiceLock) {
            if (executorService != null) {
                executorService.shutdown();
            }

            executorService = new ThreadPoolExecutor(coreThreadPoolSize, maximumThreadPoolSize, keepAliveTimeInSeconds, TimeUnit.SECONDS, queue, new AsyncThreadFactory());
        }
    }

    public <T> Future<T> runAsynchronously(final Callable<T> callable) {

        /* INITIALIZE EXECUTOR SERVICE ONLY IF NECESSARY */
        if (executorService == null) {
            initializeExecutorService();
        }

        final String className = callable.getClass().getSimpleName();

        try {
            return executorService.submit(callable);
        } catch (Exception e) {
            Log.w(TAG, String.format("Failed to run asynchronous task %s. Running it synchronously.", className), e);
            return runSynchronously(callable);
        }
    }

    public <T> Future<T> runSynchronously(final Callable<T> callable) {
        T called = null;

        /* INITIALIZE EXECUTOR SERVICE ONLY IF NECESSARY */
        if (executorService == null) {
            initializeExecutorService();
        }

        final String className = callable.getClass().getSimpleName();

        try {
            called = callable.call();
            return new SynchronousResultFuture<>(called, true);
        } catch (final Exception e) {
            Log.d(TAG, "Failed to run asynchronous task " + className + " synchronously.", e);
            return new SynchronousResultFuture<>(called, true);
        }
    }

    public void shutdown() {
        if (executorService != null) {
            synchronized (executorServiceLock) {
                executorService.shutdown();
            }
        }
    }

}
