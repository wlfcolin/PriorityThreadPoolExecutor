package me.andy5.util.concurrent.test;

import org.junit.Test;

import java.util.Comparator;
import java.util.concurrent.Callable;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;

import me.andy5.util.concurrent.PriorityThreadPoolExecutor;
import me.andy5.util.concurrent.PriorityThreadPoolExecutor.Priority;
import me.andy5.util.concurrent.PriorityThreadPoolExecutor.PriorityCallable;
import me.andy5.util.concurrent.PriorityThreadPoolExecutor.PriorityFutureTask;
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

        final PriorityBlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>();// 不提供Comparator
        // 默认在PriorityThreadPoolExecutor中实现的是优先级高的在前面
        final PriorityThreadPoolExecutor executor = new PriorityThreadPoolExecutor(2, 5, 60, TimeUnit.NANOSECONDS,
                workQueue);

        // Runnable
        final PriorityRunnable r1 = getPriorityRunnable(1);
        final PriorityRunnable r2 = getPriorityRunnable(2);
        final PriorityRunnable r3 = getPriorityRunnable(3);
        final PriorityRunnable r4 = getPriorityRunnable(4);
        final PriorityRunnable r5 = getPriorityRunnable(5);
        final PriorityRunnable r6 = getPriorityRunnable(6);
        final PriorityRunnable r7 = getPriorityRunnable(7);
        final PriorityRunnable r8 = getPriorityRunnable(8);
        final PriorityRunnable r9 = getPriorityRunnable(9);
        final PriorityRunnable r10 = getPriorityRunnable(10);
        final Runnable runnable = getRunnable();
        final Runnable _r11 = getRunnable(11);
        final Runnable _r12 = getRunnable(12);
        PriorityRunnable r11;
        PriorityFutureTask r12;

        executor.execute(r3);
        executor.execute(r4);
        executor.execute(runnable);
        executor.execute(r1);
        executor.submit(r2);
        executor.execute(r5);
        r11 = executor.execute(_r11, 11);
        r12 = executor.submit(_r12, 11);

        r1.priority(111);
        r2.priority(222);

        executor.submit(r8);

        r11.priority(1111);
        r12.priority(1222);

        executor.execute(r10);
        executor.execute(r6);
        executor.submit(r7);
        executor.execute(r9);

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

        final PriorityBlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>();// 不提供Comparator
        // 默认在PriorityThreadPoolExecutor中实现的是优先级高的在前面
        final PriorityThreadPoolExecutor executor = new PriorityThreadPoolExecutor(2, 5, 60, TimeUnit.NANOSECONDS,
                workQueue);

        // Callable
        final PriorityCallable c1 = getPriorityCallable(1);
        final PriorityCallable c2 = getPriorityCallable(2);
        final PriorityCallable c3 = getPriorityCallable(3);
        final PriorityCallable c4 = getPriorityCallable(4);
        final PriorityCallable c5 = getPriorityCallable(5);
        final PriorityCallable c6 = getPriorityCallable(6);
        final PriorityCallable c7 = getPriorityCallable(7);
        final PriorityCallable c8 = getPriorityCallable(8);
        final PriorityCallable c9 = getPriorityCallable(9);
        final PriorityCallable c10 = getPriorityCallable(10);
        final Callable callable = getCallable();
        final Callable _c11 = getCallable(11);
        PriorityFutureTask c11;

        executor.submit(c3);
        executor.submit(c4);
        executor.submit(c8);
        executor.submit(c2);
        executor.submit(c1);
        executor.submit(c5);
        c11 = executor.submit(_c11, 11);

        c1.priority(111);
        c2.priority(222);
        c11.priority(1111);

        executor.submit(callable);
        executor.submit(c10);
        executor.submit(c6);
        executor.submit(c7);
        executor.submit(c9);

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

        final PriorityBlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>();// 不提供Comparator
        // 默认在PriorityThreadPoolExecutor中实现的是优先级高的在前面
        // final PriorityBlockingQueue<Runnable> workQueue = new PriorityBlockingQueue<>(10, comparator);
        final PriorityThreadPoolExecutor executor = new PriorityThreadPoolExecutor(2, 5, 60, TimeUnit.NANOSECONDS,
                workQueue);

        // Runnable
        final PriorityRunnable r1 = getPriorityRunnable(1);
        final PriorityRunnable r2 = getPriorityRunnable(2);
        final PriorityRunnable r3 = getPriorityRunnable(3);
        final PriorityRunnable r4 = getPriorityRunnable(4);
        final PriorityRunnable r5 = getPriorityRunnable(5);
        final PriorityRunnable r6 = getPriorityRunnable(6);
        final PriorityRunnable r7 = getPriorityRunnable(7);
        final PriorityRunnable r8 = getPriorityRunnable(8);
        final PriorityRunnable r9 = getPriorityRunnable(9);
        final PriorityRunnable r10 = getPriorityRunnable(10);
        final Runnable runnable = getRunnable();
        final Runnable _r11 = getRunnable(11);
        final Runnable _r12 = getRunnable(12);
        PriorityRunnable r11;
        PriorityFutureTask r12;

        final PriorityRunnable r14 = getPriorityRunnable(14);
        final PriorityRunnable r16 = getPriorityRunnable(16);
        final PriorityRunnable r18 = getPriorityRunnable(18);
        final PriorityRunnable r20 = getPriorityRunnable(20);

        // Callable
        final PriorityCallable c1 = getPriorityCallable(1);
        final PriorityCallable c2 = getPriorityCallable(2);
        final PriorityCallable c3 = getPriorityCallable(3);
        final PriorityCallable c4 = getPriorityCallable(4);
        final PriorityCallable c5 = getPriorityCallable(5);
        final PriorityCallable c6 = getPriorityCallable(6);
        final PriorityCallable c7 = getPriorityCallable(7);
        final PriorityCallable c8 = getPriorityCallable(8);
        final PriorityCallable c9 = getPriorityCallable(9);
        final PriorityCallable c10 = getPriorityCallable(10);
        final Callable callable = getCallable();
        final Callable _c11 = getCallable(11);
        PriorityFutureTask c11;

        final PriorityCallable c13 = getPriorityCallable(13);
        final PriorityCallable c15 = getPriorityCallable(15);
        final PriorityCallable c17 = getPriorityCallable(17);
        final PriorityCallable c19 = getPriorityCallable(19);

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
        r12 = executor.submit(_r12, 11);

        executor.execute(r14);
        executor.submit(c15);
        executor.submit(callable);
        executor.submit(c4);
        c11 = executor.submit(_c11, 11);
        executor.execute(r16);


        r1.priority(111);
        final PriorityRunnable r11f = r11;
        new Thread() {
            @Override
            public void run() {// 在新线程中操作优先级
                r2.priority(222);
                r11f.priority(1111);
            }
        }.start();

        r12.priority(1222);

        executor.submit(c2);
        executor.execute(r5);
        executor.execute(runnable);
        executor.submit(r8);

        c11.priority(1111);

        executor.submit(c17);
        executor.execute(r10);
        executor.submit(c19);
        executor.submit(c1);
        executor.submit(c5);
        executor.submit(c8);
        executor.execute(r20);

        c5.priority(555);
        c8.priority(888);

        executor.submit(c13);
        executor.submit(c10);
        executor.submit(r12);
        executor.submit(c17);
        executor.submit(r7);

        c13.priority(1300);

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

    private static Runnable getRunnable(final int priority) {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1873);// 模拟实现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("r" + priority);
            }
        };
    }

    private static Runnable getRunnable() {
        return new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(2134);// 模拟实现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("r");
            }
        };
    }

    private static PriorityRunnable getPriorityRunnable(final int priority) {
        return new PriorityRunnable(priority) {
            @Override
            public void run() {
                try {
                    Thread.sleep(2369);// 模拟实现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("r" + priority + ",优先级=" + priority());
            }
        };
    }

    private static Callable getCallable() {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    Thread.sleep(1630);// 模拟实现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("c");
                return this.toString();
            }
        };
    }

    private static Callable getCallable(final int priority) {
        return new Callable<String>() {
            @Override
            public String call() throws Exception {
                try {
                    Thread.sleep(1924);// 模拟实现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("c" + priority);
                return this.toString();
            }
        };
    }

    private static PriorityCallable getPriorityCallable(final int priority) {
        return new PriorityCallable<Integer>(priority) {
            @Override
            public Integer call() throws Exception {
                try {
                    Thread.sleep(1874);// 模拟实现
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                log.debug("c" + priority + ",优先级=" + priority());
                return priority();
            }
        };
    }
}
