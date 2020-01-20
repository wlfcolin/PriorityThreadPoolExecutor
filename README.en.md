# PriorityThreadPoolExecutor

#### 介绍
具备指定和动态调整优先级的线程池PriorityThreadPoolExecutor

以下几个ThreadPoolExecutor的方法和新扩展的方法均支持指定优先级和动态调整优先级：
```
// ThreadPoolExecutor的方法
void execute(Runnable command) // 传递子类PriorityRunnable
Future<?> submit(Runnable task) // 传递子类PriorityRunnable
Future<T> submit(Runnable task, T result) // 传递子类PriorityRunnable
Future<T> submit(Callable<T> task) // 传递子类PriorityCallable
// 新扩展的方法
PriorityRunnable execute(Runnable command, int priority)
PriorityFutureTask<?> submit(Runnable task, int priority)
PriorityFutureTask<T> submit(Runnable task, T result, int priority)
PriorityFutureTask<T> submit(Callable<T> task, int priority)
```

详细使用见：[PriorityThreadPoolExecutorTest.java](https://gitee.com/wlfcolin/PriorityThreadPoolExecutor/blob/master/priority-thread-pool-executor/src/test/java/me/andy5/util/concurrent/test/PriorityThreadPoolExecutorTest.java)