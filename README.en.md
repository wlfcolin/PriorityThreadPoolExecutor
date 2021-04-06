# PriorityThreadPoolExecutor

#### Introduction
A thread pool with assigned and dynamically adjusted task priority

The following methods of ThreadPoolExecutor and the newly extended methods of PriorityThreadPoolExecutor all support designated priority and dynamic adjustment of priority:
``` java
// Methods of ThreadPoolExecutor

// Pass the subclass PriorityRunnable, if you cannot pass PriorityRunnable, you can use the following extension methods
void execute(Runnable command)
Future<?> submit(Runnable task)
Future<T> submit(Runnable task, T result)
// Pass the subclass PriorityCallable, if you cannot pass PriorityCallable, you can use the following extension methods
Future<T> submit(Callable<T> task)

// PriorityThreadPoolExecutor new extension method
PriorityRunnable execute(Runnable command, int priority)
PriorityFuture<?> submit(Runnable task, int priority)
PriorityFuture<T> submit(Runnable task, T result, int priority)
PriorityFuture<T> submit(Callable<T> task, int priority)
```

After it has been added to the thread pool, the operation method of dynamically adjusting the priority (the effective data of dynamically adjusting the priority will be affected by PriorityThreadPoolExecutor.corePoolSize):
``` java
// PriorityRunnable adjust priority
PriorityRunnable.priority(int priority)
// PriorityCallable adjust priority
PriorityCallable.priority(int priority)
// PriorityFuture adjust priority
PriorityFuture.priority(int priority)
```

For detailed usage, see: [PriorityThreadPoolExecutorTest.java](https://gitee.com/wlfcolin_admin/PriorityThreadPoolExecutor/blob/master/priority-thread-pool-executor/src/test/java/me/andy5/util/concurrent/test/PriorityThreadPoolExecutorTest.java)
