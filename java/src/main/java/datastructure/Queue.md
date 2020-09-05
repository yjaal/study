常用的几种`BlockingQueue`：

* `ArrayBlockingQueue(int i)`:规定大小的`BlockingQueue`，其构造必须指定大小。其所含的对象是`FIFO`顺序排序的。

* `LinkedBlockingQueue()或者(int i)`:大小不固定的`BlockingQueue`，若其构造时指定大小，生成的`BlockingQueue`有大小限制，不指定大小，其大小有`Integer.MAX_VALUE`来决定。其所含的对象是`FIFO`顺序排序的。`fixedThreadPool`使用此队列，在使用此线程池的时候一定要指定大小。`singleThreadExecutor`也使用此队列。

* `PriorityBlockingQueue()或者(int i)`:类似于`LinkedBlockingQueue`，但是其所含对象的排序不是`FIFO`，而是依据对象的自然顺序或者构造函数的`Comparator`决定。

* `SynchronizedQueue()`:特殊的`BlockingQueue`，对其的操作必须是放和取交替完成。`cachedThreadPool`使用此队列，此队列只能存放一个线程

* `DelayQueue`延迟处理队列，可以设置处理的延迟时间，比如延迟一秒再处理，当延迟未到的时候无法取到数据

* `LinkedBlockingDeque`是一个支持`FIFO`和`FILO`的双向的阻塞队列。

* `LinkedTransferQueue`是 `SynchronousQueue` 和 `LinkedBlockingQueue` 的合体，性能比 `LinkedBlockingQueue` 更高（没有锁操作），比 `SynchronousQueue`能存储更多的元素。

* 使用优先级队列`DelayedWorkQueue`，保证添加到队列中的任务，会按照任务的延时时间进行排序，延时时间少的任务首先被获取。

