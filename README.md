# PriorityThreadPoolExecutor

[English](https://gitee.com/wlfcolin_admin/PriorityThreadPoolExecutor/blob/master/README.en.md)

#### 介绍
具备指定和动态调整任务优先级的线程池PriorityThreadPoolExecutor

以下几个ThreadPoolExecutor的方法和PriorityThreadPoolExecutor新扩展的方法均支持指定优先级和动态调整优先级：
``` java
// ThreadPoolExecutor的方法

// 传递子类PriorityRunnable，如果要使用Runnable又需要支持优先级可用下面的扩展方法
void execute(Runnable command)
Future<?> submit(Runnable task)
Future<T> submit(Runnable task, T result)
// 传递子类PriorityCallable，如果要使用Callable又需要支持优先级可用下面的扩展方法
Future<T> submit(Callable<T> task)

// PriorityThreadPoolExecutor新扩展的方法
PriorityRunnable execute(Runnable command, int priority)
PriorityFuture<?> submit(Runnable task, int priority)
PriorityFuture<T> submit(Runnable task, T result, int priority)
PriorityFuture<T> submit(Callable<T> task, int priority)
```

已经添加到线程池后动态调整优先级操作方法（动态调整不会影响PriorityThreadPoolExecutor.corePoolSize内正在执行的任务）：
``` java
// PriorityRunnable调整优先级
PriorityRunnable.priority(int priority)
// PriorityCallable调整优先级
PriorityCallable.priority(int priority)
// PriorityFuture调整优先级
PriorityFuture.priority(int priority)
```

详细使用见：[PriorityThreadPoolExecutorTest.java](https://gitee.com/wlfcolin_admin/PriorityThreadPoolExecutor/blob/master/priority-thread-pool-executor/src/test/java/me/andy5/util/concurrent/test/PriorityThreadPoolExecutorTest.java)
