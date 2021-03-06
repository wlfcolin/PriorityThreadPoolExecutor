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
 * 一个具有指定和动态调整任务优先级能力的Java线程池
 * A java thread pool with the ability to specify and dynamically adjust task priorities
 *
 * @author andy(Andy)
 * @datetime 2019-09-19 09:01 GMT+8
 * @email 411086563@qq.com
 */
public class PriorityThreadPoolExecutor extends ThreadPoolExecutor {

    // private static Log log = Log.getLog(PriorityThreadPoolExecutor.class);

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
     * Specify priority to execute Runnable
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
     * Specify priority to execute Runnable
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
     * Specify priority to execute Runnable
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
     * Specify priority to execute Callable
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

    /**
     * 传递子类PriorityRunnable，传递Runnable而非PriorityRunnable的话，将不支持优先级调整
     * 如果要使用Runnable又需要支持优先级可用扩展方法{{@link #execute(Runnable, int)}}并使用其返回值进行优先级调整
     *
     * Use the subclass PriorityRunnable instead of Runnable, use Runnable instead of PriorityRunnable will not support priority adjustment
     * if it is need to use Runnable support priority you can use the extension methods {{@link #execute(Runnable, int)}} and use its return value to adjust the priority
     *
     * @param command
     */
    @Override
    public void execute(Runnable command) {
        // 防止重复包装
        // Avoid duplicate packaging
        if (command instanceof PriorityComparable) {
            super.execute(command);
            return;
        }
        // 使用适配器将实现了Priority接口的Runnable进行适配
        // Use the adapter to adapt Runnable that implements the Priority interface
        if (command instanceof Priority) {
            super.execute(new PriorityRunnableAdapter((Priority) command, command));
            return;
        }
        super.execute(new PriorityRunnableAdapter(new DefaultPriority(), command));
    }

    /**
     * 传递子类PriorityRunnable，传递Runnable而非PriorityRunnable的话，将不支持优先级调整
     * 如果要使用Runnable又需要支持优先级可用扩展方法{{@link #submit(Runnable, int)}}并使用其返回值进行优先级调整
     *
     * Use the subclass PriorityRunnable instead of Runnable, use Runnable instead of PriorityRunnable will not support priority adjustment
     * if it is need to use Runnable support priority you can use the extension methods {{@link #submit(Runnable, int)}} and use its return value to adjust the priority
     *
     * @param task
     * @return
     */
    @Override
    public Future<?> submit(Runnable task) {
        // 防止重复包装
        // Avoid duplicate packaging
        if (task instanceof PriorityComparable) {
            return super.submit(task);
        }
        // 使用适配器将实现了Priority接口的Runnable进行适配
        // Use the adapter to adapt Runnable that implements the Priority interface
        if (task instanceof Priority) {
            return super.submit(new PriorityRunnableAdapter((Priority) task, task));
        }
        return super.submit(new PriorityRunnableAdapter(new DefaultPriority(), task));
    }

    /**
     * 传递子类PriorityRunnable，传递Runnable而非PriorityRunnable的话，将不支持优先级调整
     * 如果要使用Runnable又需要支持优先级可用扩展方法{{@link #submit(Runnable, Object, int)}}并使用其返回值进行优先级调整
     *
     * Use the subclass PriorityRunnable instead of Runnable, use Runnable instead of PriorityRunnable will not support priority adjustment
     * if it is need to use Runnable support priority you can use the extension methods {{@link #submit(Runnable, Object, int)}} and use its return value to adjust the priority
     *
     * @param task
     * @param result
     * @param <T>
     * @return
     */
    @Override
    public <T> Future<T> submit(Runnable task, T result) {
        // 防止重复包装
        // Avoid duplicate packaging
        if (task instanceof PriorityComparable) {
            return super.submit(task, result);
        }
        // 使用适配器将实现了Priority接口的Runnable进行适配
        // Use the adapter to adapt Runnable that implements the Priority interface
        if (task instanceof Priority) {
            return super.submit(new PriorityRunnableAdapter((Priority) task, task), result);
        }
        return super.submit(new PriorityRunnableAdapter(new DefaultPriority(), task), result);
    }

    /**
     * 传递子类PriorityCallable，传递Callable而非PriorityCallable的话，将不支持优先级调整
     * 如果要使用Callable又需要支持优先级可用扩展方法{{@link #submit(Callable, int)}}并使用其返回值进行优先级调整
     *
     * Use the subclass PriorityCallable instead of Callable, use Callable instead of PriorityCallable will not support priority adjustment
     * if it is need to use Callable support priority you can use the extension methods {{@link #submit(Callable, int)}} and use its return value to adjust the priority
     *
     * @param task
     * @param <T>
     * @return
     */
    @Override
    public <T> Future<T> submit(Callable<T> task) {
        // 防止重复包装
        // Avoid duplicate packaging
        if (task instanceof PriorityComparable) {
            return super.submit(task);
        }
        // 使用适配器将实现了Priority接口的Runnable进行适配
        // Use the adapter to adapt Runnable that implements the Priority interface
        if (task instanceof Priority) {
            return super.submit(new PriorityCallableAdapter<T>((Priority) task, task));
        }
        return super.submit(new PriorityCallableAdapter<T>(new DefaultPriority(), task));
    }

    // Priority的默认实现
    // default implements of Priority
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

    // Priority与Runnable的适配器
    // Priority and Runnable adapter
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
            // Empty, at the top
            if (o == null) {
                result = -1;
            } else {
                if (o instanceof Priority) {
                    // 优先级高的在前面
                    // The higher priority is first
                    result = ((Priority) o).priority() - priority();
                }
            }
            // log.debug("PriorityRunnableAdapter.compareTo()-----this=" + this + ",that=" + o + ",result=" + result);
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

    // Priority与Callable的适配器
    // Priority and Callable adapter
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
            // Empty, at the top
            if (o == null) {
                result = -1;
            } else {
                if (o instanceof Priority) {
                    // 优先级高的在前面
                    // The higher priority is first
                    result = ((Priority) o).priority() - priority();
                }
            }
            // log.debug("PriorityCallableAdapter.compareTo()-----this=" + this + ",that=" + o + ",result=" + result);
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

    // Priority与Future的适配器
    // Priority and Future adapter
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
            // Empty, at the top
            if (o == null) {
                result = -1;
            } else {
                if (o instanceof Priority) {
                    // 优先级高的在前面
                    // The higher priority is first
                    result = ((Priority) o).priority() - priority();
                }
            }
            // log.debug("PriorityFutureAdapter.compareTo()-----this=" + this + ",that=" + o + ",result=" + result);
            return result;
        }

        @Override
        public String toString() {
            return "PriorityFutureAdapter{" + "priority=" + priority() + ", object=" + object + '}';
        }
    }

    /**
     * 具有优先级排序的Comparable接口
     * Comparable interface with priority ordering
     *
     * @param <T>
     */
    private interface PriorityComparable<T> extends Priority, Comparable<T> {
    }

    /**
     * 具有优先级排序的Callable接口
     * Callable interface with priority ordering
     *
     * @param <V>
     */
    public interface PriorityCallable<V> extends Priority, Callable<V> {
    }

    /**
     * 具有优先级排序的Runnable接口
     * Runnable interface with priority ordering
     */
    public interface PriorityRunnable extends Priority, Runnable {
    }

    /**
     * 具有优先级排序的Future接口
     * Future interface with priority ordering
     *
     * @param <V>
     */
    public interface PriorityFuture<V> extends Priority, Future<V> {
    }

    /**
     * 优先级排序接口
     * Priority interface
     *
     */
    public interface Priority {

        /**
         * 优先级，越大越靠前
         * Priority, the bigger the higher
         *
         * @return
         */
        int priority();

        /**
         * 改变优先级，越大越靠前
         * Change the priority, the bigger the higher
         *
         * @param priority
         */
        void priority(int priority);
    }
}