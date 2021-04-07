package me.andy5.util.concurrent.test;

import org.junit.Test;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import me.andy5.util.concurrent.PriorityThreadPoolExecutor;
import me.andy5.util.concurrent.PriorityThreadPoolExecutor.Priority;
import me.andy5.util.concurrent.PriorityThreadPoolExecutor.PriorityCallable;
import me.andy5.util.concurrent.PriorityThreadPoolExecutor.PriorityFuture;
import me.andy5.util.concurrent.PriorityThreadPoolExecutor.PriorityRunnable;

/**
 * PriorityThreadPoolExecutor测试
 * PriorityThreadPoolExecutor test
 *
 * @author andy(Andy)
 * @datetime 2019-01-22 10:42 GMT+8
 * @email 411086563@qq.com
 */
public class PriorityThreadPoolExecutorTest {

    private static Log log = Log.getLog(PriorityThreadPoolExecutorTest.class);

    @Test
    public void testPriorityRunnable() {
        long start = System.currentTimeMillis();
        log.debug("--------testPriorityRunnable begin--------");

        final PriorityBlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>();
        final PriorityThreadPoolExecutor executor = new PriorityThreadPoolExecutor(2, 5, 60, TimeUnit.NANOSECONDS,
                workQueue);

        // Runnable
        final PriorityRunnable r1 = getPriorityRunnable("r1", 1);
        final PriorityRunnable r2 = getPriorityRunnable("r2", 2);
        final PriorityRunnable r3 = getPriorityRunnable("r3", 3);
        final PriorityRunnable r4 = getPriorityRunnable("r4", 4);
        final PriorityRunnable r5 = getPriorityRunnable("r5", 5);
        final PriorityRunnable r6 = getPriorityRunnable("r6", 6);
        final PriorityRunnable r7 = getPriorityRunnable("r7", 7);
        final PriorityRunnable r8 = getPriorityRunnable("r8", 8);
        final PriorityRunnable r9 = getPriorityRunnable("r9", 9);
        final PriorityRunnable r10 = getPriorityRunnable("r10", 10);
        final Runnable runnable = getRunnable("runnable");
        final Runnable _r11 = getRunnable("r11");
        final Runnable _r12 = getRunnable("r12");
        PriorityRunnable r11;
        PriorityFuture r12;

        final Runnable r13 = getTestRunnableImplPriority("r13", 13);
        final Runnable r14 = getTestRunnableImplPriority("r14", 14);

        executor.execute(r3);
        log.debug("==== add r3 ====");
        executor.execute(r4);
        log.debug("==== add r4 ====");
        executor.execute(runnable);
        log.debug("==== add runnable ====");
        executor.execute(r1);
        log.debug("==== add r1 ====");
        executor.submit(r2);
        log.debug("==== add r2 ====");
        executor.execute(r5);
        log.debug("==== add r5 ====");
        r11 = executor.execute(_r11, 11);
        log.debug("==== add r11 ====");
        r12 = executor.submit(_r12, 12);
        log.debug("==== add r12 ====");

        // 动态调整不会影响PriorityThreadPoolExecutor.corePoolSize内正在执行的任务
        // Dynamic adjustment will not affect the tasks being executed in PriorityThreadPoolExecutor.corePoolSize
        r1.priority(111);
        log.debug("-------- r1.priority(111) --------");
        r2.priority(222);
        log.debug("-------- r2.priority(222) --------");

        executor.submit(r8);
        log.debug("==== add r8 ====");
        executor.execute(r13);
        log.debug("==== add r13 ====");
        executor.submit(r14);
        log.debug("==== add r14 ====");
        executor.execute(r10);
        log.debug("==== add r10 ====");
        executor.execute(r6);
        log.debug("==== add r6 ====");
        executor.submit(r7);
        log.debug("==== add r7 ====");
        executor.execute(r9);
        log.debug("==== add r9 ====");

        // 动态调整不会影响PriorityThreadPoolExecutor.corePoolSize内正在执行的任务
        // Dynamic adjustment will not affect the tasks being executed in PriorityThreadPoolExecutor.corePoolSize
        r11.priority(1111);
        log.debug("-------- r11.priority(1111) --------");
        r12.priority(1222);
        log.debug("-------- r12.priority(1222) --------");

        executor.shutdown();
        while (true) {
            if (executor.isTerminated()) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        log.debug("--------testPriorityRunnable end, time consuming=" + (end - start) + "--------");
    }

    @Test
    public void testPriorityCallable() {
        long start = System.currentTimeMillis();
        log.debug("--------testPriorityCallable begin--------");

        final PriorityBlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>(10);
        final PriorityThreadPoolExecutor executor = new PriorityThreadPoolExecutor(2, 5, 60, TimeUnit.NANOSECONDS,
                workQueue);

        // Callable
        final PriorityCallable c1 = getPriorityCallable("c1", 1);
        final PriorityCallable c2 = getPriorityCallable("c2", 2);
        final PriorityCallable c3 = getPriorityCallable("c3", 3);
        final PriorityCallable c4 = getPriorityCallable("c4", 4);
        final PriorityCallable c5 = getPriorityCallable("c5", 5);
        final PriorityCallable c6 = getPriorityCallable("c6", 6);
        final PriorityCallable c7 = getPriorityCallable("c7", 7);
        final PriorityCallable c8 = getPriorityCallable("c8", 8);
        final PriorityCallable c9 = getPriorityCallable("c9", 9);
        final PriorityCallable c10 = getPriorityCallable("c10", 10);
        final Callable callable = getCallable("callable");
        final Callable _c11 = getCallable("c11");
        PriorityFuture c11;

        final Callable c12 = getTestCallableImplPriority("c12", 12);

        executor.submit(c3);
        log.debug("==== add c3 ====");
        executor.submit(c4);
        log.debug("==== add c4 ====");
        executor.submit(c8);
        log.debug("==== add c8 ====");
        executor.submit(c2);
        log.debug("==== add c2 ====");
        executor.submit(c1);
        log.debug("==== add c1 ====");
        executor.submit(c5);
        log.debug("==== add c5 ====");
        c11 = executor.submit(_c11, 11);
        log.debug("==== add c11 ====");

        // 动态调整不会影响PriorityThreadPoolExecutor.corePoolSize内正在执行的任务
        // Dynamic adjustment will not affect the tasks being executed in PriorityThreadPoolExecutor.corePoolSize
        c1.priority(111);
        log.debug("-------- c1.priority(111) --------");
        c2.priority(222);
        log.debug("-------- c2.priority(222) --------");

        executor.submit(c12);
        log.debug("==== add c12 ====");
        executor.submit(callable);
        log.debug("==== add callable ====");
        executor.submit(c10);
        log.debug("==== add c10 ====");
        executor.submit(c6);
        log.debug("==== add c6 ====");
        executor.submit(c7);
        log.debug("==== add c7 ====");
        executor.submit(c9);
        log.debug("==== add c9 ====");

        // 动态调整不会影响PriorityThreadPoolExecutor.corePoolSize内正在执行的任务
        // Dynamic adjustment will not affect the tasks being executed in PriorityThreadPoolExecutor.corePoolSize
        c11.priority(1111);
        log.debug("-------- c11.priority(1111) --------");

        executor.shutdown();
        while (true) {
            if (executor.isTerminated()) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        log.debug("--------testPriorityCallable end, time consuming=" + (end - start) + "--------");
    }

    @Test
    public void testPriorityThreadPoolExecutor() {
        long start = System.currentTimeMillis();
        log.debug("--------testPriorityThreadPoolExecutor begin--------");

        // 自定义排序适配器
        // Custom sort adapter
        final Comparator<Runnable> comparator = new Comparator<Runnable>() {
            @Override
            public int compare(Runnable o1, Runnable o2) {
                if (o1 instanceof Priority && o2 instanceof Priority) {
                    // 自定义优先级实现，希望优先级【低】的在前面
                    // Custom priority implementation, hope the priority [low] is in front
                    return ((Priority) o1).priority() - ((Priority) o2).priority();
                }
                return 0;
            }
        };

        // final PriorityBlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>(10);
        final PriorityBlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>(10, comparator);
        final PriorityThreadPoolExecutor executor = new PriorityThreadPoolExecutor(2, 5, 60, TimeUnit.NANOSECONDS,
                workQueue);

        // Runnable
        final PriorityRunnable r1 = getPriorityRunnable("r1", 1);
        final PriorityRunnable r2 = getPriorityRunnable("r2", 2);
        final PriorityRunnable r3 = getPriorityRunnable("r3", 3);
        final PriorityRunnable r4 = getPriorityRunnable("r4", 4);
        final PriorityRunnable r5 = getPriorityRunnable("r5", 5);
        final PriorityRunnable r6 = getPriorityRunnable("r6", 6);
        final PriorityRunnable r7 = getPriorityRunnable("r7", 7);
        final PriorityRunnable r8 = getPriorityRunnable("r8", 8);
        final PriorityRunnable r9 = getPriorityRunnable("r9", 9);
        final PriorityRunnable r10 = getPriorityRunnable("r10", 10);
        final Runnable runnable = getRunnable("runnable");
        final Runnable _r11 = getRunnable("r11");
        final Runnable _r12 = getRunnable("r12");
        PriorityRunnable r11;
        PriorityFuture r12;

        final Runnable r13 = getTestRunnableImplPriority("r13", 13);
        final Runnable r14 = getTestRunnableImplPriority("r14", 14);

        final PriorityRunnable r16 = getPriorityRunnable("r16", 16);
        final PriorityRunnable r18 = getPriorityRunnable("r18", 18);
        final PriorityRunnable r20 = getPriorityRunnable("r20", 20);

        // Callable
        final PriorityCallable c1 = getPriorityCallable("c1", 1);
        final PriorityCallable c2 = getPriorityCallable("c2", 2);
        final PriorityCallable c3 = getPriorityCallable("c3", 3);
        final PriorityCallable c4 = getPriorityCallable("c4", 4);
        final PriorityCallable c5 = getPriorityCallable("c5", 5);
        final PriorityCallable c6 = getPriorityCallable("c6", 6);
        final PriorityCallable c7 = getPriorityCallable("c7", 7);
        final PriorityCallable c8 = getPriorityCallable("c8", 8);
        final PriorityCallable c9 = getPriorityCallable("c9", 9);
        final PriorityCallable c10 = getPriorityCallable("c10", 10);
        final Callable callable = getCallable("callable");
        final Callable _c11 = getCallable("c11");
        PriorityFuture c11;

        final Callable c12 = getTestCallableImplPriority("c12", 12);

        final PriorityCallable c13 = getPriorityCallable("c13", 13);
        final PriorityCallable c15 = getPriorityCallable("c15", 15);
        final PriorityCallable c17 = getPriorityCallable("c17", 17);
        final PriorityCallable c19 = getPriorityCallable("c19", 19);

        executor.execute(r3);
        log.debug("==== add r3 ====");
        executor.submit(c9);
        log.debug("==== add c9 ====");
        executor.execute(r18);
        log.debug("==== add r18 ====");
        executor.execute(r4);
        log.debug("==== add r4 ====");
        executor.submit(c6);
        log.debug("==== add c6 ====");
        executor.submit(c7);
        log.debug("==== add c7 ====");
        executor.submit(r2);
        log.debug("==== add r2 ====");
        executor.execute(r1);
        log.debug("==== add r1 ====");
        executor.submit(c3);
        log.debug("==== add c3 ====");

        r11 = executor.execute(_r11, 11);
        log.debug("==== add r11 ====");
        r12 = executor.submit(_r12, 12);
        log.debug("==== add r12 ====");

        executor.execute(r14);
        log.debug("==== add r14 ====");
        executor.submit(c15);
        log.debug("==== add c15 ====");
        executor.submit(callable);
        log.debug("==== add callable ====");
        executor.submit(c4);
        log.debug("==== add c4 ====");
        executor.submit(r13);
        log.debug("==== add r13 ====");
        c11 = executor.submit(_c11, 11);
        log.debug("==== add c11 ====");
        executor.execute(r16);
        log.debug("==== add r16 ====");

        // 动态调整不会影响PriorityThreadPoolExecutor.corePoolSize内正在执行的任务
        // Dynamic adjustment will not affect the tasks being executed in PriorityThreadPoolExecutor.corePoolSize
        r1.priority(111);
        log.debug("-------- r1.priority(111) --------");

        final PriorityRunnable r11f = r11;
        new Thread() {
            @Override
            public void run() {
                // 在新线程中操作优先级
                // Operation priority in a new thread
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 动态调整不会影响PriorityThreadPoolExecutor.corePoolSize内正在执行的任务
                // Dynamic adjustment will not affect the tasks being executed in PriorityThreadPoolExecutor.corePoolSize
                r2.priority(222);
                log.debug("-------- r2.priority(222) --------");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                // 动态调整不会影响PriorityThreadPoolExecutor.corePoolSize内正在执行的任务
                // Dynamic adjustment will not affect the tasks being executed in PriorityThreadPoolExecutor.corePoolSize
                r11f.priority(1111);
                log.debug("-------- r11.priority(1111) --------");
            }
        }.start();

        // 动态调整不会影响PriorityThreadPoolExecutor.corePoolSize内正在执行的任务
        // Dynamic adjustment will not affect the tasks being executed in PriorityThreadPoolExecutor.corePoolSize
        r12.priority(1222);
        log.debug("-------- r12.priority(1222) --------");

        executor.submit(c2);
        log.debug("==== add c2 ====");
        executor.execute(r5);
        log.debug("==== add r5 ====");
        executor.execute(runnable);
        log.debug("==== add runnable ====");
        executor.submit(r8);
        log.debug("==== add r8 ====");
        executor.submit(c12);
        log.debug("==== add c12 ====");

        // 动态调整不会影响PriorityThreadPoolExecutor.corePoolSize内正在执行的任务
        // Dynamic adjustment will not affect the tasks being executed in PriorityThreadPoolExecutor.corePoolSize
        c11.priority(1111);
        log.debug("-------- c11.priority(1111) --------");

        executor.submit(c17);
        log.debug("==== add c17 ====");
        executor.execute(r10);
        log.debug("==== add r10 ====");
        executor.submit(c19);
        log.debug("==== add c19 ====");
        executor.submit(c1);
        log.debug("==== add c1 ====");
        executor.submit(c5);
        log.debug("==== add c5 ====");
        executor.submit(c8);
        log.debug("==== add c8 ====");
        executor.execute(r20);
        log.debug("==== add r20 ====");
        executor.submit(c13);
        log.debug("==== add c13 ====");
        executor.submit(c10);
        log.debug("==== add c10 ====");
        executor.submit(c17);
        log.debug("==== add c17 ====");
        executor.submit(r7);
        log.debug("==== add r7 ====");

        // 动态调整不会影响PriorityThreadPoolExecutor.corePoolSize内正在执行的任务
        // Dynamic adjustment will not affect the tasks being executed in PriorityThreadPoolExecutor.corePoolSize
        c5.priority(555);
        log.debug("-------- c5.priority(555) --------");

        executor.execute(r9);
        log.debug("==== add r9 ====");
        executor.execute(r6);
        log.debug("==== add r6 ====");

        executor.shutdown();
        while (true) {
            if (executor.isTerminated()) {
                break;
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        long end = System.currentTimeMillis();
        log.debug("--------testPriorityThreadPoolExecutor end, time consuming=" + (end - start) + "--------");
    }

    private static class TestRunnableImplPriority implements Runnable, Priority {

        private String name;
        private int priority;

        public TestRunnableImplPriority(String name, int priority) {
            this.name = name;
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

        @Override
        public void run() {
            try {
                // 模拟处理任务
                // Simulate processing tasks
                Thread.sleep(1456);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("execute " + name + ", priority=" + priority());
        }

        @Override
        public String toString() {
            return "PriorityThreadPoolExecutorTest.TestRunnableImplPriority{" + "name=" + name + ",priority=" + priority() + '}';
        }
    }

    private static class TestCallableImplPriority implements Callable, Priority {

        private String name;
        private int priority;

        public TestCallableImplPriority(String name, int priority) {
            this.name = name;
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

        @Override
        public String call() throws Exception {
            try {
                // 模拟处理任务
                // Simulate processing tasks
                Thread.sleep(1738);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("execute " + name + ", priority=" + priority());
            return name;
        }

        @Override
        public String toString() {
            return "PriorityThreadPoolExecutorTest.TestCallableImplPriority{" + "name=" + name + ",priority=" + priority() + '}';
        }
    }

    private final static Runnable getTestRunnableImplPriority(final String name, final int priority) {
        return new TestRunnableImplPriority(name, priority);
    }

    private final static Callable getTestCallableImplPriority(final String name, final int priority) {
        return new TestCallableImplPriority(name, priority);
    }

    private final static Runnable getRunnable(final String name) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    // 模拟处理任务
                    // Simulate processing tasks
                    Thread.sleep(1739);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer priority = getPriority(this);
                if (priority != null) {
                    log.debug("execute " + name + ", priority=" + priority);
                } else {
                    log.debug("execute " + name);
                }
            }

            @Override
            public String toString() {
                return "PriorityThreadPoolExecutorTest.Runnable{" + "name=" + name + '}';
            }
        };
    }

    private final static Callable getCallable(final String name) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    // 模拟处理任务
                    // Simulate processing tasks
                    Thread.sleep(1924);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer priority = getPriority(this);
                if (priority != null) {
                    log.debug("execute " + name + ", priority=" + priority);
                } else {
                    log.debug("execute " + name);
                }
                return name;
            }

            @Override
            public String toString() {
                return "PriorityThreadPoolExecutorTest.Callable{" + "name=" + name + '}';
            }
        };
    }

    private final static PriorityRunnable getPriorityRunnable(final String name, final int priority) {
        return new PriorityRunnable() {

            private int p = priority;

            @Override
            public void run() {
                try {
                    // 模拟处理任务
                    // Simulate processing tasks
                    Thread.sleep(1693);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("execute " + name + ", priority=" + priority());
            }

            @Override
            public int priority() {
                return p;
            }

            @Override
            public void priority(int priority) {
                p = priority;
            }

            @Override
            public String toString() {
                return "PriorityThreadPoolExecutorTest.PriorityRunnable{" + "name=" + name + ",priority=" + priority() + '}';
            }
        };
    }

    private final static PriorityCallable getPriorityCallable(final String name, final int priority) {
        return new PriorityCallable() {

            private int p = priority;

            @Override
            public String call() throws Exception {
                try {
                    // 模拟处理任务
                    // Simulate processing tasks
                    Thread.sleep(1874);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("execute " + name + ", priority=" + priority());
                return name;
            }

            @Override
            public int priority() {
                return p;
            }

            @Override
            public void priority(int priority) {
                p = priority;
            }

            @Override
            public String toString() {
                return "PriorityThreadPoolExecutorTest.PriorityCallable{" + "name=" + name + ",priority=" + priority() + '}';
            }
        };
    }

    private final static Integer getPriority(Object o) {
        if (o instanceof Priority) {
            return ((Priority) o).priority();
        }
        return null;
    }
}
