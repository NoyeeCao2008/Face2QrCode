package com.github.noyeecao2008.util;

import android.os.Handler;
import android.os.Looper;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadPool {
    private static final int CPU_COUNT = Runtime.getRuntime().availableProcessors();
    private static final int MAX_THREAD_COUNT = CPU_COUNT * 2 + 1;
    private ExecutorService mPool;
    private AtomicInteger mId = new AtomicInteger(1);
    private Handler mainHandler = new Handler(Looper.getMainLooper());

    private ThreadPool() {
        mPool = Executors.newFixedThreadPool(MAX_THREAD_COUNT,
                runnable -> new Thread(runnable, "F2QR_" + mId.getAndIncrement()));
    }

    private static class SigletionInstance {
        private static final ThreadPool INSTANCE = new ThreadPool();
    }

    public static ThreadPool getInstance() {
        return SigletionInstance.INSTANCE;
    }

    public void postToMainThread(Runnable runnable) {
        mainHandler.post(runnable);
    }

    public void postDelayedToMainThread(Runnable runnable, long delay) {
        mainHandler.postDelayed(runnable, delay);
    }

    public void postToBackground(Runnable runnable) {
        mPool.execute(runnable);
    }

    public Runnable postDelayedToBackground(Runnable runnable, long delay) {
        Runnable runnableInPool = () -> mPool.execute(runnable);
        mainHandler.postDelayed(runnable, delay);
        return runnableInPool;
    }

    public void removeRunnable(Runnable runnable) {
        mainHandler.removeCallbacks(runnable);
    }
}
