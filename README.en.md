# PriorityThreadPoolExecutor

#### 介绍
具备指定和动态调整任务优先级线程池PriorityThreadPoolExecutor

以下几个ThreadPoolExecutor的方法和新扩展的方法均支持指定优先级和动态调整优先级：
```
// ThreadPoolExecutor的方法
void execute(Runnable command) // 传递子类PriorityRunnable，如果无法传递PriorityRunnable可用下面的扩展方法
Future<?> submit(Runnable task) // 传递子类PriorityRunnable，如果无法传递PriorityRunnable可用下面的扩展方法
Future<T> submit(Runnable task, T result) // 传递子类PriorityRunnable，如果无法传递PriorityRunnable可用下面的扩展方法
Future<T> submit(Callable<T> task) // 传递子类PriorityCallable，如果无法传递PriorityCallable可用下面的扩展方法
// 新扩展的方法
PriorityRunnable execute(Runnable command, int priority)
PriorityFutureTask<?> submit(Runnable task, int priority)
PriorityFutureTask<T> submit(Runnable task, T result, int priority)
PriorityFutureTask<T> submit(Callable<T> task, int priority)
```

已经添加到线程池后动态更改优先级操作方法：
```
PriorityRunnable.priority(int priority)
PriorityCallable.priority(int priority)
PriorityFutureTask.priority(int priority)
```

详细使用见：[PriorityThreadPoolExecutorTest.java](https://gitee.com/wlfcolin/PriorityThreadPoolExecutor/blob/master/priority-thread-pool-executor/src/test/java/me/andy5/util/concurrent/test/PriorityThreadPoolExecutorTest.java)