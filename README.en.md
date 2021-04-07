# PriorityThreadPoolExecutor

#### Introduction
A thread pool with assigned and dynamically adjusted task priority

The following methods of ThreadPoolExecutor and the newly extended methods of PriorityThreadPoolExecutor all support designated priority and dynamic adjustment of priority:
``` java
// Methods of ThreadPoolExecutor

// Use the subclass PriorityRunnable instead of Runnable, if it is need to use Runnable support priority you can use the following extension methods instead
void execute(Runnable command)
Future<?> submit(Runnable task)
Future<T> submit(Runnable task, T result)
// Use the subclass PriorityCallable instead of Callable, if it is need to use Callable support priority you can use the following extension methods instead
Future<T> submit(Callable<T> task)

// PriorityThreadPoolExecutor new extension method
PriorityRunnable execute(Runnable command, int priority)
PriorityFuture<?> submit(Runnable task, int priority)
PriorityFuture<T> submit(Runnable task, T result, int priority)
PriorityFuture<T> submit(Callable<T> task, int priority)
```

Use the operation method to dynamically adjusting the priority when it has been added to the thread pool (Dynamic adjustment will not affect the tasks being executed in PriorityThreadPoolExecutor.corePoolSize):
``` java
// PriorityRunnable adjust priority
PriorityRunnable.priority(int priority)
// PriorityCallable adjust priority
PriorityCallable.priority(int priority)
// PriorityFuture adjust priority
PriorityFuture.priority(int priority)
```

For detailed usage, see: [PriorityThreadPoolExecutorTest.java](https://gitee.com/wlfcolin_admin/PriorityThreadPoolExecutor/blob/master/priority-thread-pool-executor/src/test/java/me/andy5/util/concurrent/test/PriorityThreadPoolExecutorTest.java)
