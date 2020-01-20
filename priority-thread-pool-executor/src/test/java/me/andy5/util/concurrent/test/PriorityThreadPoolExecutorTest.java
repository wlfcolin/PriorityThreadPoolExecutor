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
        log.debug("--------testPriorityRunnable 开始执行--------");

        // 不提供Comparator默认PriorityThreadPoolExecutor中实现的是优先级高的在前面
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
        executor.execute(r4);
        executor.execute(runnable);
        executor.execute(r1);
        executor.submit(r2);
        executor.execute(r5);
        r11 = executor.execute(_r11, 11);
        r12 = executor.submit(_r12, 12);

        r1.priority(111);
        log.debug("-------- r1.priority(111) --------");
        r2.priority(222);
        log.debug("-------- r2.priority(222) --------");

        executor.submit(r8);
        executor.execute(r13);
        executor.submit(r14);
        executor.execute(r10);
        executor.execute(r6);
        executor.submit(r7);
        executor.execute(r9);

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
        log.debug("--------testPriorityRunnable 结束执行，耗时=" + (end - start) + "--------");
    }

    @Test
    public void testPriorityCallable() {
        long start = System.currentTimeMillis();
        log.debug("--------testPriorityCallable 开始执行--------");

        // 不提供Comparator默认PriorityThreadPoolExecutor中实现的是优先级高的在前面
        final PriorityBlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>();
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
        executor.submit(c4);
        executor.submit(c8);
        executor.submit(c2);
        executor.submit(c1);
        executor.submit(c5);
        c11 = executor.submit(_c11, 11);

        c1.priority(111);
        log.debug("-------- c1.priority(111) --------");
        c2.priority(222);
        log.debug("-------- c2.priority(222) --------");

        executor.submit(c12);
        executor.submit(callable);
        executor.submit(c10);
        executor.submit(c6);
        executor.submit(c7);
        executor.submit(c9);

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
        log.debug("--------testPriorityCallable 结束执行，耗时=" + (end - start) + "--------");
    }

    @Test
    public void testPriorityThreadPoolExecutor() {
        long start = System.currentTimeMillis();
        log.debug("--------testPriorityThreadPoolExecutor 开始执行--------");

        // 自定义排序适配器
        final Comparator<Runnable> comparator = new Comparator<Runnable>() {
            @Override
            public int compare(Runnable o1, Runnable o2) {
                if (o1 instanceof Priority && o2 instanceof Priority) {
                    return ((Priority) o1).priority() - ((Priority) o2).priority();// 自定义实现，希望优先级【低】的在前面
                }
                return 0;
            }
        };

        // 不提供Comparator默认PriorityThreadPoolExecutor中实现的是优先级高的在前面
        final PriorityBlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>();
        // 提供自定义Comparator
        // final PriorityBlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>(10, comparator);
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
        executor.submit(c9);
        executor.execute(r18);
        executor.execute(r4);
        executor.submit(c6);
        executor.submit(c7);
        executor.submit(r2);
        executor.execute(r1);
        executor.submit(c3);

        r11 = executor.execute(_r11, 11);// 如果不是为了改变优先级，返回值可以不接收
        r12 = executor.submit(_r12, 12);

        executor.execute(r14);
        executor.submit(c15);
        executor.submit(callable);
        executor.submit(c4);
        executor.submit(r13);
        c11 = executor.submit(_c11, 11);
        executor.execute(r16);

        r1.priority(111);
        log.debug("-------- r1.priority(111) --------");

        final PriorityRunnable r11f = r11;
        new Thread() {
            @Override
            public void run() {// 在新线程中操作优先级
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                r2.priority(222);
                log.debug("-------- r2.priority(222) --------");
                try {
                    Thread.sleep(1500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                r11f.priority(1111);
                log.debug("-------- r11.priority(1111) --------");
            }
        }.start();

        r12.priority(1222);
        log.debug("-------- r12.priority(1222) --------");

        executor.submit(c2);
        executor.execute(r5);
        executor.execute(runnable);
        executor.submit(r8);
        executor.submit(c12);

        c11.priority(1111);
        log.debug("-------- c11.priority(1111) --------");

        executor.submit(c17);
        executor.execute(r10);
        executor.submit(c19);
        executor.submit(c1);
        executor.submit(c5);
        executor.submit(c8);
        executor.execute(r20);
        executor.submit(c13);
        executor.submit(c10);
        executor.submit(r12);
        executor.submit(c17);
        executor.submit(r7);

        c5.priority(555);
        log.debug("-------- c5.priority(555) --------");

        executor.execute(r9);
        executor.execute(r6);

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
        log.debug("--------testPriorityThreadPoolExecutor 结束执行，耗时=" + (end - start) + "--------");
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
                Thread.sleep(1456);// 模拟实现
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("name=" + name + ",优先级=" + priority());
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
                Thread.sleep(1738);// 模拟实现
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            log.debug("name=" + name + ",优先级=" + priority());
            return name;
        }

        @Override
        public String toString() {
            return "PriorityThreadPoolExecutorTest.TestCallableImplPriority{" + "name=" + name + ",priority=" + priority() + '}';
        }
    }

    private final static Runnable getTestRunnableImplPriority(final String name, final int priority) {
        return new TestRunnableImplPriority(name, priority) {
        };
    }

    private final static Callable getTestCallableImplPriority(final String name, final int priority) {
        return new TestCallableImplPriority(name, priority) {
        };
    }

    private final static Runnable getRunnable(final String name) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1739);// 模拟实现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer priority = priority(this);
                if (priority != null) {
                    log.debug("name=" + name + ",优先级=" + priority);
                } else {
                    log.debug("name=" + name);
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
                    Thread.sleep(1924);// 模拟实现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Integer priority = priority(this);
                if (priority != null) {
                    log.debug("name=" + name + ",优先级=" + priority);
                } else {
                    log.debug("name=" + name);
                }
                return name;
            }

            @Override
            public String toString() {
                return "PriorityThreadPoolExecutorTest.Callable{" + "name=" + name + '}';
            }
        };
    }

    private final static Integer priority(Object o) {
        if (o instanceof Priority) {
            return ((Priority) o).priority();
        }
        return null;
    }

    private final static PriorityRunnable getPriorityRunnable(final String name, final int priority) {
        return new PriorityRunnable() {

            private int p = priority;

            @Override
            public void run() {
                try {
                    Thread.sleep(1693);// 模拟实现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("name=" + name + ",优先级=" + priority());
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
                    Thread.sleep(1874);// 模拟实现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("name=" + name + ",优先级=" + priority());
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
}
