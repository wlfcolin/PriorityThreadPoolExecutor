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
        PriorityRunnable runnable;
        if (command instanceof PriorityRunnable) {
            runnable = (PriorityRunnable) command;
        } else {
            runnable = new RunnableAdapter(command, priority);
        }
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
    public PriorityFutureTask<?> submit(Runnable task, int priority) {
        PriorityRunnable runnable;
        if (task instanceof PriorityRunnable) {
            runnable = (PriorityRunnable) task;
        } else {
            runnable = new RunnableAdapter(task, priority);
        }
        return (PriorityFutureTask<?>) this.submit(runnable);
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
    public <T> PriorityFutureTask<T> submit(Runnable task, T result, int priority) {
        PriorityRunnable runnable;
        if (task instanceof PriorityRunnable) {
            runnable = (PriorityRunnable) task;
        } else {
            runnable = new RunnableAdapter(task, priority);
        }
        return (PriorityFutureTask<T>) this.submit(runnable, result);
    }

    /**
     * 指定优先级执行Callable
     *
     * @param task
     * @param priority
     * @param <T>
     * @return
     */
    public <T> PriorityFutureTask<T> submit(Callable<T> task, int priority) {
        PriorityCallable callable;
        if (task instanceof PriorityCallable) {
            callable = (PriorityCallable) task;
        } else {
            callable = new CallableAdapter<>(task, priority);
        }
        return (PriorityFutureTask<T>) this.submit(callable);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Callable<T> callable) {
        return new PriorityFutureTask<>(callable);
    }

    @Override
    protected <T> RunnableFuture<T> newTaskFor(Runnable runnable, T value) {
        return new PriorityFutureTask<>(runnable, value);
    }

    @Override
    public void execute(Runnable command) {
        if (command instanceof PriorityComparable) { // 防止重复包装
            super.execute(command);
            return;
        }
        int priority = 0;
        if (command instanceof Priority) {// 只实现了Priority接口则将priority值传递到适配器中
            priority = ((Priority) command).priority();
        }
        super.execute(new RunnableAdapter(command, priority));
    }

    @Override
    public Future<?> submit(Runnable task) {
        if (task instanceof PriorityComparable) { // 防止重复包装
            return super.submit(task);
        }
        int priority = 0;
        if (task instanceof Priority) {// 只实现了Priority接口则将priority值传递到适配器中
            priority = ((Priority) task).priority();
        }
        return super.submit(new RunnableAdapter(task, priority));
    }

    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        if (task instanceof PriorityComparable) { // 防止重复包装
            return super.submit(task, result);
        }
        int priority = 0;
        if (task instanceof Priority) {// 只实现了Priority接口则将priority值传递到适配器中
            priority = ((Priority) task).priority();
        }
        return super.submit(new RunnableAdapter(task, priority), result);
    }

    @Override
    public <T> Future<T> submit(Callable<T> task) {
        if (task instanceof PriorityComparable) { // 防止重复包装
            return super.submit(task);
        }
        int priority = 0;
        if (task instanceof Priority) {// 只实现了Priority接口则将priority值传递到适配器中
            priority = ((Priority) task).priority();
        }
        return super.submit(new CallableAdapter<T>(task, priority));
    }

    // Runnable 适配器
    private static class RunnableAdapter extends PriorityRunnable {

        private Runnable runnable;

        public RunnableAdapter(Runnable runnable, int priority) {
            super(priority);
            this.runnable = runnable;
        }

        @Override
        public void run() {
            if (runnable != null) {
                runnable.run();
            }
        }

        @Override
        public String toString() {
            return "RunnableAdapter{" + "runnable=" + runnable + ", priority=" + priority() + '}';
        }
    }

    // Callable 适配器
    private static class CallableAdapter<V> extends PriorityCallable<V> {

        private Callable<V> callable;

        public CallableAdapter(Callable<V> callable, int priority) {
            super(priority);
            this.callable = callable;
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
            return "CallableAdapter{" + "callable=" + callable + ", priority=" + priority() + '}';
        }
    }

    /**
     * 具有优先级执行的Runnable
     */
    public static abstract class PriorityRunnable implements Runnable, PriorityComparable {

        private int priority;

        public PriorityRunnable(int priority) {
            this.priority = priority;
        }

        @Override
        public void priority(int priority) {
            this.priority = priority;
        }

        @Override
        public int priority() {
            return priority;
        }

        @Override
        public int compareTo(Priority o) {
            int result = 0;
            // 空，排在最前面
            if (o == null) {
                result = -1;
            } else {
                result = o.priority() - priority();// 优先级高的在前面
            }
            //log.debug("PriorityRunnable.compareTo()-----this=" + this + ",that=" + o + ",result=" + result);
            return result;
        }

        @Override
        public String toString() {
            return "PriorityRunnable{" + "priority=" + priority + '}';
        }
    }

    /**
     * 具有优先级执行的Callable
     *
     * @param <V>
     */
    public static abstract class PriorityCallable<V> implements Callable<V>, PriorityComparable {

        private int priority;

        public PriorityCallable(int priority) {
            this.priority = priority;
        }

        @Override
        public void priority(int priority) {
            this.priority = priority;
        }

        @Override
        public int priority() {
            return priority;
        }

        @Override
        public int compareTo(Priority o) {
            int result = 0;
            // 空，排在最前面
            if (o == null) {
                result = -1;
            } else {
                result = o.priority() - priority();// 优先级高的在前面
            }
            //log.debug("PriorityCallable.compareTo)-----this=" + this + ",that=" + o + ",result=" + result);
            return result;
        }

        @Override
        public String toString() {
            return "PriorityCallable{" + "priority=" + priority + '}';
        }
    }

    /**
     * 具有优先级执行的FutureTask
     *
     * @param <V>
     */
    public class PriorityFutureTask<V> extends FutureTask<V> implements PriorityComparable {

        private Object object;
        private Priority priority;

        public PriorityFutureTask(Callable<V> callable) {
            super(callable);
            object = callable;
            if (callable instanceof Priority) {
                priority = (Priority) callable;
            }
        }

        public PriorityFutureTask(Runnable runnable, V result) {
            super(runnable, result);
            object = runnable;
            if (runnable instanceof Priority) {
                priority = (Priority) runnable;
            }
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
        public int compareTo(Priority o) {
            int result = 0;
            // 空，排在最前面
            if (o == null) {
                result = -1;
            } else {
                result = o.priority() - priority();// 优先级高的在前面
            }
            //log.debug("PriorityFutureTask.compareTo()-----this=" + this + ",that=" + o + ",result=" + result);
            return result;
        }

        @Override
        public String toString() {
            return "PriorityFutureTask{" + "object=" + object + ", priority=" + priority() + '}';
        }
    }

    /**
     * 具有Priority优先排序的Comparable接口
     */
    public interface PriorityComparable extends Priority, Comparable<Priority> {
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