package me.andy5.util.concurrent;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * 具有优先级执行的线程池
 *
 * @author andy(Andy)
 * @datetime 2019-09-19 09:01 GMT+8
 * @email 411086563@qq.com
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    //private static Log log = Log.getLog(PriorityThreadPoolExecutor.class);

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      PriorityBlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      PriorityBlockingQueue<Runnable> workQueue, ThreadFactory threadFactory) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      PriorityBlockingQueue<Runnable> workQueue, RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, handler);
    }

    public PriorityThreadPoolExecutor(int corePoolSize, int maximumPoolSize, long keepAliveTime, TimeUnit unit,
                                      PriorityBlockingQueue<Runnable> workQueue, ThreadFactory threadFactory,
                                      RejectedExecutionHandler handler) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, unit, workQueue, threadFactory, handler);
    }

    /**
     * 指定优先级执行Runnable
     *
     * @param command
     * @param priority
     * @return
     */
    public PriorityRunnable execute(Runnable command, int priority) {
        if (command instanceof PriorityRunnable) {
            this.execute(command);
            return (PriorityRunnable) command;
        }
        PriorityRunnable runnable = new PriorityRunnableAdapter(new DefaultPriority(priority), command);
        this.execute(runnable);
        return runnable;
    }

    /**
     * 指定优先级执行Runnable
     *
     * @param task
     * @param priority
     * @return
     */
    public PriorityFuture<?> submit(Runnable task, int priority) {
        if (task instanceof Priority) {
            return (PriorityFuture<?>) this.submit(task);
        }
        return (PriorityFuture<?>) this.submit(new PriorityRunnableAdapter(new DefaultPriority(priority), task));
    }

    /**
     * 指定优先级执行Runnable
     *
     * @param task
     * @param result
     * @param priority
     * @param <T>
     * @return
     */
    public <T> PriorityFuture<T> submit(Runnable task, T result, int priority) {
        if (task instanceof Priority) {
            return (PriorityFuture<T>) this.submit(task, result);
        }
        return (PriorityFuture<T>) this.submit(new PriorityRunnableAdapter(new DefaultPriority(priority), task),
                result);
    }

    /**
     * 指定优先级执行Callable
     *
     * @param task
     * @param priority
     * @param <T>
     * @return
     */
    public <T> PriorityFuture<T> submit(Callable<T> task, int priority) {
        if (task instanceof Priority) {
            return (PriorityFuture<T>) this.submit(task);
        }
        return (PriorityFuture<T>) this.submit(new PriorityCallableAdapter<T>(new DefaultPriority(priority), task));
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new PriorityFutureAdapter<T>(callable);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new PriorityFutureAdapter<T>(runnable, value);
    }

    @Override
    public void execute(Runnable command) {
        if (command instanceof PriorityComparable) { // 防止重复包装
            super.execute(command);
            return;
        }
        if (command instanceof Priority) {// 只实现了Priority接口则将priority值传递到适配器中
            super.execute(new PriorityRunnableAdapter((Priority) command, command));
            return;
        }
        super.execute(new PriorityRunnableAdapter(new DefaultPriority(), command));
    }

    @Override
    public Future<?> submit(Runnable task) {
        if (task instanceof PriorityComparable) { // 防止重复包装
            return super.submit(task);
        }
        if (task instanceof Priority) {// 只实现了Priority接口则将priority值传递到适配器中
            return super.submit(new PriorityRunnableAdapter((Priority) task, task));
        }
        return super.submit(new PriorityRunnableAdapter(new DefaultPriority(), task));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        if (task instanceof PriorityComparable) { // 防止重复包装
            return super.submit(task, result);
        }
        if (task instanceof Priority) {// 只实现了Priority接口则将Priority接口传递到适配器中
            return super.submit(new PriorityRunnableAdapter((Priority) task, task), result);
        }
        return super.submit(new PriorityRunnableAdapter(new DefaultPriority(), task), result);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (task instanceof PriorityComparable) { // 防止重复包装
            return super.submit(task);
        }
        if (task instanceof Priority) {// 只实现了Priority接口则将priority值传递到适配器中
            return super.submit(new PriorityCallableAdapter<T>((Priority) task, task));
        }
        return super.submit(new PriorityCallableAdapter<T>(new DefaultPriority(), task));
    }

    // 默认实现
    private final static class DefaultPriority implements Priority {

        private int priority;

        public DefaultPriority() {
        }

        public DefaultPriority(int priority) {
            this.priority = priority;
        }

        @Override
        public int priority() {
            return priority;
        }

        @Override
        public void priority(int priority) {
            this.priority = priority;
        }
    }

    private final static class PriorityRunnableAdapter implements PriorityRunnable, PriorityComparable<Object> {

        private Priority priority;
        private Runnable runnable;

        public PriorityRunnableAdapter(Priority priority, Runnable runnable) {
            this.priority = priority;
            this.runnable = runnable;
        }

        @Override
        public void priority(int priority) {
            if (this.priority != null) {
                this.priority.priority(priority);
            }
        }

        @Override
        public int priority() {
            if (this.priority != null) {
                return this.priority.priority();
            }
            return 0;
        }

        @Override
        public int compareTo(Object o) {
            int result = 0;
            // 空，排在最前面
            if (o == null) {
                result = -1;
            } else {
                if (o instanceof Priority) {
                    result = ((Priority) o).priority() - priority();// 优先级高的在前面
                }
            }
            //log.debug("PriorityRunnableAdapter.compareTo()-----this=" + this + ",that=" + o + ",result=" + result);
            return result;
        }

        @Override
        public void run() {
            if (runnable != null) {
                runnable.run();
            }
        }

        @Override
        public String toString() {
            return "PriorityRunnableAdapter{" + "priority=" + priority() + ", runnable=" + runnable + '}';
        }
    }

    private final static class PriorityCallableAdapter<V> implements PriorityCallable<V>, PriorityComparable<Object> {

        private Priority priority;
        private Callable<V> callable;

        public PriorityCallableAdapter(Priority priority, Callable<V> callable) {
            this.priority = priority;
            this.callable = callable;
        }

        @Override
        public void priority(int priority) {
            if (this.priority != null) {
                this.priority.priority(priority);
            }
        }

        @Override
        public int priority() {
            if (this.priority != null) {
                return this.priority.priority();
            }
            return 0;
        }

        @Override
        public int compareTo(Object o) {
            int result = 0;
            // 空，排在最前面
            if (o == null) {
                result = -1;
            } else {
                if (o instanceof Priority) {
                    result = ((Priority) o).priority() - priority();// 优先级高的在前面
                }
            }
            //log.debug("PriorityCallableAdapter.compareTo()-----this=" + this + ",that=" + o + ",result=" + result);
            return result;
        }

        @Override
        public V call() throws Exception {
            if (callable != null) {
                return callable.call();
            }
            return null;
        }

        @Override
        public String toString() {
            return "PriorityCallableAdapter{" + "priority=" + priority.priority() + ", callable=" + callable + '}';
        }
    }

    private final static class PriorityFutureAdapter<V> extends FutureTask<V> implements PriorityFuture<V>,
            PriorityComparable<Object> {

        private Priority priority;
        private Object object;

        public PriorityFutureAdapter(Callable<V> callable) {
            super(callable);
            if (callable instanceof Priority) {
                priority = (Priority) callable;
            }
            object = callable;
        }

        public PriorityFutureAdapter(Runnable runnable, V result) {
            super(runnable, result);
            if (runnable instanceof Priority) {
                priority = (Priority) runnable;
            }
            object = runnable;
        }

        @Override
        public void priority(int priority) {
            if (this.priority != null) {
                this.priority.priority(priority);
            }
        }

        @Override
        public int priority() {
            if (this.priority != null) {
                return this.priority.priority();
            }
            return 0;
        }

        @Override
        public int compareTo(Object o) {
            int result = 0;
            // 空，排在最前面
            if (o == null) {
                result = -1;
            } else {
                if (o instanceof Priority) {
                    result = ((Priority) o).priority() - priority();// 优先级高的在前面
                }
            }
            //log.debug("PriorityFutureAdapter.compareTo()-----this=" + this + ",that=" + o + ",result=" + result);
            return result;
        }

        @Override
        public String toString() {
            return "PriorityFutureAdapter{" + "priority=" + priority() + ", object=" + object + '}';
        }
    }

    /**
     * 具有优先级排序的Comparable接口
     */
    private interface PriorityComparable<T> extends Priority, Comparable<T> {
    }

    /**
     * 具有优先级排序的Callable
     */
    public interface PriorityCallable<V> extends Priority, Callable<V> {
    }

    /**
     * 具有优先级排序的Runnable
     */
    public interface PriorityRunnable extends Priority, Runnable {
    }

    /**
     * 具有优先级排序的Future
     *
     * @param <V>
     */
    public interface PriorityFuture<V> extends Priority, Runnable, Future<V> {
    }

    /**
     * 优先级排序接口
     */
    public interface Priority {

        /**
         * 优先级，越大越靠前
         *
         * @return
         */
        int priority();

        /**
         * 改变优先级，越大越靠前
         *
         * @return
         */
        void priority(int priority);
    }
}