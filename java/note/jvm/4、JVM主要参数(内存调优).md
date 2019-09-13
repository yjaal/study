摘自：`http://ifeve.com/useful-jvm-flags-part-4-heap-tuning/`

理想的情况下，一个Java程序使用JVM的默认设置也可以运行得很好，所以一般来说，没有必要设置任何JVM参数。然而，由于一些性能问题（很不幸的是，这些问题经常出现），一些相关的JVM参数知识会是我们工作中得好伙伴。在这篇文章中，我们将介绍一些关于JVM内存管理的参数。知道并理解这些参数，将对开发者和运维人员很有帮助。

所有已制定的HotSpot内存管理和垃圾回收算法都基于一个相同的堆内存划分：新生代（young generation）里存储着新分配的和较年轻的对象，老年代（old generation）里存储着长寿的对象。在此之外，永久代（permanent generation）存储着那些需要伴随整个JVM生命周期的对象，比如，已加载的对象的类定义或者String对象内部Cache。接下来，我们将假设堆内存是按照新生代、老年代和永久代这一经典策略划分的。然而，其他的一些堆内存划分策略也是可行的，一个突出的例子就是新的G1垃圾回收器，它模糊了新生代和老年代之间的区别。此外，目前的开发进程似乎表明在未来的HotSpot JVM版本中，将不会区分老年代和永久代。

#### -Xms and -Xmx (or: -XX:InitialHeapSize and -XX:MaxHeapSize)

-Xms和-Xmx可以说是最流行的JVM参数，它们可以允许我们指定JVM的初始和最大堆内存大小。一般来说，这两个参数的数值单位是Byte，但同时它们也支持使用速记符号，比如“k”或者“K”代表“kilo”，“m”或者“M”代表“mega”，“g”或者“G”代表“giga”。举个例子，下面的命令启动了一个初始化堆内存为128M，最大堆内存为2G，名叫“MyApp”的Java应用程序。

```
java -Xms128m -Xmx2g MyApp
```

在实际使用过程中，初始化堆内存的大小通常被视为堆内存大小的下界。然而JVM可以在运行时动态的调整堆内存的大小，所以理论上来说我们有可能会看到堆内存的大小小于初始化堆内存的大小。但是即使在非常低的堆内存使用下，我也从来没有遇到过这种情况。这种行为将会方便开发者和系统管理员，因为我们可以通过将“-Xms”和“-Xmx”设置为相同大小来获得一个固定大小的堆内存。 -Xms和-Xmx实际上是-XX:InitialHeapSize和-XX:MaxHeapSize的缩写。我们也可以直接使用这两个参数，它们所起得效果是一样的：

```
$ java -XX:InitialHeapSize=128m -XX:MaxHeapSize=2g MyApp
```

需要注意的是，所有JVM关于初始\最大堆内存大小的输出都是使用它们的完整名称：“InitialHeapSize”和“InitialHeapSize”。所以当你查询一个正在运行的JVM的堆内存大小时，如使用-XX:+PrintCommandLineFlags参数或者通过JMX查询，你应该寻找“InitialHeapSize”和“InitialHeapSize”标志而不是“Xms”和“Xmx”。

#### -XX:+HeapDumpOnOutOfMemoryError and -XX:HeapDumpPath

如果我们没法为-Xmx（最大堆内存）设置一个合适的大小，那么就有可能面临内存溢出（OutOfMemoryError）的风险，这可能是我们使用JVM时面临的最可怕的猛兽之一。就同[另外一篇](http://blog.codecentric.de/en/2011/03/java-memory-configuration-and-monitoring-3rd-act/)关于这个主题的博文说的一样，导致内存溢出的根本原因需要仔细的定位。通常来说，分析堆内存快照（Heap Dump）是一个很好的定位手段，如果发生内存溢出时没有生成内存快照那就实在是太糟了，特别是对于那种JVM已经崩溃或者错误只出现在顺利运行了数小时甚至数天的生产系统上的情况。

幸运的是，我们可以通过设置-XX:+HeapDumpOnOutOfMemoryError 让JVM在发生内存溢出时自动的生成堆内存快照。有了这个参数，当我们不得不面对内存溢出异常的时候会节约大量的时间。默认情况下，堆内存快照会保存在JVM的启动目录下名为java_pid<pid>.hprof 的文件里（在这里<pid>就是JVM进程的进程号）。也可以通过设置-XX:HeapDumpPath=<path>来改变默认的堆内存快照生成路径，<path>可以是相对或者绝对路径。

虽然这一切听起来很不错，但有一点我们需要牢记。堆内存快照文件有可能很庞大，特别是当内存溢出错误发生的时候。因此，我们推荐将堆内存快照生成路径指定到一个拥有足够磁盘空间的地方。

#### -XX:OnOutOfMemoryError

当内存溢发生时，我们甚至可以可以执行一些指令，比如发个E-mail通知管理员或者执行一些清理工作。通过-XX:OnOutOfMemoryError 这个参数我们可以做到这一点，这个参数可以接受一串指令和它们的参数。在这里，我们将不会深入它的细节，但我们提供了它的一个例子。在下面的例子中，当内存溢出错误发生的时候，我们会将堆内存快照写到/tmp/heapdump.hprof 文件并且在JVM的运行目录执行脚本cleanup.sh

```
$ java -XX:+HeapDumpOnOutOfMemoryError -XX:HeapDumpPath=/tmp/heapdump.hprof -XX:OnOutOfMemoryError =``"sh ~/cleanup.sh" MyApp
```



####  -XX:PermSize and -XX:MaxPermSize

永久代在堆内存中是一块独立的区域，它包含了所有JVM加载的类的对象表示。为了成功运行应用程序，JVM会加载很多类（因为它们依赖于大量的第三方库，而这又依赖于更多的库并且需要从里面将类加载进来）这就需要增加永久代的大小。我们可以使用-XX:PermSize 和-XX:MaxPermSize 来达到这个目的。其中-XX:MaxPermSize 用于设置永久代大小的最大值，-XX:PermSize 用于设置永久代初始大小。下面是一个简单的例子：

```
$ java -XX:PermSize=128m -XX:MaxPermSize=256m MyApp
```



请注意，这里设置的永久代大小并不会被包括在使用参数-XX:MaxHeapSize 设置的堆内存大小中。也就是说，通过-XX:MaxPermSize设置的永久代内存可能会需要由参数-XX:MaxHeapSize 设置的堆内存以外的更多的一些堆内存。

#### -XX:InitialCodeCacheSize and -XX:ReservedCodeCacheSize

JVM一个有趣的，但往往被忽视的内存区域是“代码缓存”，它是用来存储已编译方法生成的本地代码。代码缓存确实很少引起性能问题，但是一旦发生其影响可能是毁灭性的。如果代码缓存被占满，JVM会打印出一条警告消息，并切换到interpreted-only 模式：JIT编译器被停用，字节码将不再会被编译成机器码。因此，应用程序将继续运行，但运行速度会降低一个数量级，直到有人注意到这个问题。就像其他内存区域一样，我们可以自定义代码缓存的大小。相关的参数是-XX:InitialCodeCacheSize 和-XX:ReservedCodeCacheSize，它们的参数和上面介绍的参数一样，都是字节值。

#### -XX:+UseCodeCacheFlushing

如果代码缓存不断增长，例如，因为热部署引起的内存泄漏，那么提高代码的缓存大小只会延缓其发生溢出。为了避免这种情况的发生，我们可以尝试一个有趣的新参数：当代码缓存被填满时让JVM放弃一些编译代码。通过使用-XX:+UseCodeCacheFlushing 这个参数，我们至少可以避免当代码缓存被填满的时候JVM切换到interpreted-only 模式。不过，我仍建议尽快解决代码缓存问题发生的根本原因，如找出内存泄漏并修复它。