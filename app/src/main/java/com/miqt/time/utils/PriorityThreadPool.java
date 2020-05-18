package com.miqt.time.utils;


import java.util.concurrent.*;

/**
 * 支持任务优先级的线程池
 */
public class PriorityThreadPool {
    private final ThreadPoolExecutor mExecutor;
    private final Sync mSync;
    private final Async mAsync;
    private static volatile PriorityThreadPool instance = null;

    private static final int CORE_POOL_SIZE = 1;
    private static final int MAXIMUM_POOL_SIZE = 15;
    private static final long KEEP_ALIVE_TIME = 3;

    private PriorityThreadPool() {
        mExecutor = new ThreadPoolExecutor(CORE_POOL_SIZE,
                MAXIMUM_POOL_SIZE,
                KEEP_ALIVE_TIME,
                TimeUnit.SECONDS,
                new PriorityBlockingQueue<Runnable>(1),
                getThreadFactory(),
                new ThreadPoolExecutor.AbortPolicy()
        );
        mSync = new Sync();
        mAsync = new Async();
    }

    private ThreadFactory getThreadFactory() {
        return new ThreadFactory() {
            short i = 0;

            @Override
            public Thread newThread(Runnable r) {
                return new Thread(r, "PriorityThreadPool_" + (i++ % MAXIMUM_POOL_SIZE));
            }
        };
    }

    public void shutdown() {
        if (mExecutor == null) {
            return;
        }
        if (mExecutor.isShutdown()) {
            return;
        }
        mExecutor.shutdown();
    }

    public static PriorityThreadPool getInstance() {
        if (instance == null) {
            synchronized (PriorityThreadPool.class) {
                if (instance == null) {
                    instance = new PriorityThreadPool();
                }
            }
        }
        return instance;
    }

    public Async asyn() {
        return mAsync;
    }

    public Sync sync() {
        return mSync;
    }

    public class Async {
        public <T> Future<T> submit(AbstractPrioritizedCallable<T> callable) {
            return mExecutor.submit(callable);
        }

        public void execute(AbstractPrioritizedRunnable runnable) {
            mExecutor.execute(runnable);
        }

        public <T> Future<T> submit(final Callable<T> callable, long priority) {
            AbstractPrioritizedFutureTask<T> task = new AbstractPrioritizedFutureTask<>(new AbstractPrioritizedCallable<T>(priority) {
                @Override
                public T call() throws Exception {
                    return callable.call();
                }
            });
            mExecutor.execute(task);
            return task;
        }

        public <T> Future<T> submit(final Callable<T> callable) {
            return submit(callable, 0);
        }

        public void execute(final Runnable runnable, long priority) {
            mExecutor.execute(new AbstractPrioritizedRunnable(priority) {
                @Override
                public void run() {
                    runnable.run();
                }
            });
        }

        public void execute(final Runnable runnable) {
            execute(runnable, 0);
        }
    }

    public class Sync {
        public <T> T submit(AbstractPrioritizedCallable<T> callable, long timeout, TimeUnit unit) {
            try {
                AbstractPrioritizedFutureTask<T> task = new AbstractPrioritizedFutureTask<>(callable);
                mExecutor.execute(task);
                if (timeout > 0 && unit != null) {
                    return task.get(timeout, unit);
                } else {
                    return task.get();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public void execute(final Runnable runnable, long prioriy, long timeout, TimeUnit unit) {
            execute(new AbstractPrioritizedRunnable(prioriy) {
                @Override
                public void run() {
                    runnable.run();
                }
            }, timeout, unit);
        }

        public void execute(final Runnable runnable, long prioriy) {
            execute(runnable, prioriy, 0, null);
        }

        public void execute(final Runnable runnable) {
            execute(runnable, 0);
        }

        public <T> T submit(final Callable<T> callable, long prioriy, long timeout, TimeUnit unit) {
            return submit(new AbstractPrioritizedCallable<T>(prioriy) {
                @Override
                public T call() throws Exception {
                    return callable.call();
                }
            }, timeout, unit);
        }

        public <T> T submit(final Callable<T> callable, long prioriy) {
            return submit(callable, prioriy, 0, null);
        }

        public <T> T submit(final Callable<T> callable) {
            return submit(callable, 0, 0, null);
        }

        public void execute(AbstractPrioritizedRunnable runnable, long timeout, TimeUnit unit) {
            try {
                AbstractPrioritizedFutureTask<Object> task = new AbstractPrioritizedFutureTask<>(runnable, null);
                mExecutor.execute(task);
                if (timeout > 0 && unit != null) {
                    task.get(timeout, unit);
                } else {
                    task.get();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private static class PrioritizedComparable implements
            Comparable<Object> {
        private final long priority;

        public PrioritizedComparable(long priority) {
            this.priority = priority;
        }

        public long getPriority() {
            return priority;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof PrioritizedComparable) {
                // 优先级越大越优先
                if (this.getPriority() < ((PrioritizedComparable) o).getPriority()) {
                    return 1;
                } else if (this.getPriority() > ((PrioritizedComparable) o).getPriority()) {
                    return -1;
                } else {
                    return 0;
                }
            } else {
                return this.compareTo(((AbstractPrioritizedFutureTask) o).comparable);
            }

        }
    }

    public static abstract class AbstractPrioritizedRunnable extends PrioritizedComparable implements Runnable {
        public AbstractPrioritizedRunnable(long priority) {
            super(priority);
        }
    }

    public static abstract class AbstractPrioritizedCallable<T> extends PrioritizedComparable implements Callable<T> {
        public AbstractPrioritizedCallable(long priority) {
            super(priority);
        }
    }

    public static class AbstractPrioritizedFutureTask<T> extends FutureTask<T> implements Comparable<Object> {

        private PrioritizedComparable comparable;

        public AbstractPrioritizedFutureTask(AbstractPrioritizedCallable<T> callable) {
            super(callable);
            comparable = callable;
        }

        public AbstractPrioritizedFutureTask(AbstractPrioritizedRunnable runnable, T result) {
            super(runnable, result);
            comparable = runnable;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof AbstractPrioritizedFutureTask) {
                return comparable.compareTo(((AbstractPrioritizedFutureTask) o).comparable);
            } else {
                return comparable.compareTo(o);
            }
        }
    }
}
