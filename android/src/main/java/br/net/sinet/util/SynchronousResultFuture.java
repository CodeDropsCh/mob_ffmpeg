

package br.net.sinet.util;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class SynchronousResultFuture<V> implements Future<V> {

    protected final V v;

    protected final boolean completed;

    public SynchronousResultFuture(final V v, final boolean completed) {
        this.v = v;
        this.completed = completed;
    }

    @Override
    public boolean cancel(final boolean mayInterruptIfRunning) {
        return false;
    }

    @Override
    public boolean isCancelled() {
        return !completed;
    }

    @Override
    public boolean isDone() {
        return completed;
    }

    @Override
    public V get() throws InterruptedException, ExecutionException {
        return v;
    }

    @Override
    public V get(final long timeout, final TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        return v;
    }

}
