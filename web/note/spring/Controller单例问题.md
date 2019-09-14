摘自：`https://blog.csdn.net/m0_37222746/article/details/56486694`

springmvc的controller是singleton的（非线程安全的），这也许就是他和struts2的区别吧！和Struts一样，Spring的Controller默认是Singleton的，这意味着每个request过来，系统都会用原有的instance去处理，这样导致了两个结果:一是我们不用每次创建Controller，二是减少了对象创建和垃圾收集的时间;由于只有一个Controller的instance，当多个线程调用它的时候，它里面的instance变量就不是线程安全的了，会发生窜数据的问题。当然大多数情况下，我们根本不需要考虑线程安全的问题，比如dao,service等，除非在bean中声明了实例变量。因此，我们在使用spring mvc 的contrller时，应避免在controller中定义实例变量。 

如果控制器是使用单例形式，且controller中有一个私有的变量a,所有请求到同一个controller时，使用的a变量是共用的，即若是某个请求中修改了这个变量a，则，在别的请求中能够读到这个修改的内容。。

有几种解决方法：
1、在Controller中使用ThreadLocal变量
2、在spring配置文件Controller中声明 scope="prototype"，每次都创建新的controller

所在在使用spring开发web 时要注意，默认Controller、Dao、Service都是单例的。



ThreadLocal 使用范例：

```java
// 定义一个ThreadLocal 变量
ThreadLocal<Long>startTime = newThreadLocal<Long>(); 
// 写入值
startTime.set(System.currentTimeMillis());   
// 读取值
startTime.get(); 
```


ThreadLocal和线程同步机制相比有什么优势呢？

ThreadLocal和线程同步机制都是为了解决多线程中相同变量的访问冲突问题。

在同步机制中，通过对象的锁机制保证同一时间只有一个线程访问变量。这时该变量是多个线程共享的，使用同步机制要求程序慎密地分析什么时候对变量进行读写，什么时候需要锁定某个对象，什么时候释放对象锁等繁杂的问题，程序设计和编写难度相对较大。

而ThreadLocal则从另一个角度来解决多线程的并发访问。ThreadLocal会为每一个线程提供一个独立的变量副本，从而隔离了多个线程对数据的访问冲突。因为每一个线程都拥有自己的变量副本，从而也就没有必要对该变量进行同步了。ThreadLocal提供了线程安全的共享对象，在编写多线程代码时，可以把不安全的变量封装进ThreadLocal。

概括起来说，对于多线程资源共享的问题，同步机制采用了“以时间换空间”的方式，而ThreadLocal采用了“以空间换时间”的方式。前者仅提供一份变量，让不同的线程排队访问，而后者为每一个线程都提供了一份变量，因此可以同时访问而互不影响。



