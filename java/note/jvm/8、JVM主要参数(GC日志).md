摘自：`http://ifeve.com/useful-jvm-flags-part-8-gc-logging/`

本系列的最后一部分是有关垃圾收集（GC）日志的JVM参数。GC日志是一个很重要的工具，它准确记录了每一次的GC的执行时间和执行结果，通过分析GC日志可以优化堆设置和GC设置，或者改进应用程序的对象分配模式。

### -XX:+PrintGC

参数-XX:+PrintGC（或者-verbose:gc）开启了简单GC日志模式，为每一次新生代（young generation）的GC和每一次的Full GC打印一行信息。下面举例说明：

```
[GC 246656K->243120K(376320K), 0.0929090 secs]
[Full GC 243120K->241951K(629760K), 1.5589690 secs]
```



每行开始首先是GC的类型（可以是“GC”或者“Full GC”），然后是在GC之前和GC之后已使用的堆空间，再然后是当前的堆容量，最后是GC持续的时间（以秒计）。

第一行的意思就是GC将已使用的堆空间从246656K减少到243120K，当前的堆容量（译者注：GC发生时）是376320K，GC持续的时间是0.0929090秒。

简单模式的GC日志格式是与GC算法无关的，日志也没有提供太多的信息。在上面的例子中，我们甚至无法从日志中判断是否GC将一些对象从young generation移到了old generation。所以详细模式的GC日志更有用一些。

### -XX:PrintGCDetails

如果不是使用-XX:+PrintGC，而是-XX:PrintGCDetails，就开启了详细GC日志模式。在这种模式下，日志格式和所使用的GC算法有关。我们首先看一下使用Throughput垃圾收集器在young generation中生成的日志。为了便于阅读这里将一行日志分为多行并使用缩进。

```
[GC
	[PSYoungGen: 142816K->10752K(142848K)] 246648K->243136K(375296K), 0.0935090 secs
]

[Times: user=0.55 sys=0.10, real=0.09 secs]
```



我们可以很容易发现：这是一次在young generation中的GC，它将已使用的堆空间从246648K减少到了243136K，用时0.0935090秒。此外我们还可以得到更多的信息：所使用的垃圾收集器（即PSYoungGen）、young generation的大小和使用情况（在这个例子中“PSYoungGen”垃圾收集器将young generation所使用的堆空间从142816K减少到10752K）。

既然我们已经知道了young generation的大小，所以很容易判定发生了GC，因为young generation无法分配更多的对象空间：已经使用了142848K中的142816K。我们可以进一步得出结论，多数从young generation移除的对象仍然在堆空间中，只是被移到了old generation：通过对比绿色的和蓝色的部分可以发现即使young generation几乎被完全清空（从142816K减少到10752K），但是所占用的堆空间仍然基本相同（从246648K到243136K）。

详细日志的“Times”部分包含了GC所使用的CPU时间信息，分别为操作系统的用户空间和系统空间所使用的时间。同时，它显示了GC运行的“真实”时间（0.09秒是0.0929090秒的近似值）。如果CPU时间（译者注：0.55秒+0.10秒）明显多于”真实“时间（译者注：0.09秒），我们可以得出结论：GC使用了多线程运行。这样的话CPU时间就是所有GC线程所花费的CPU时间的总和。实际上我们的例子中的垃圾收集器使用了8个线程。

接下来看一下Full GC的输出日志

```
[Full GC
[PSYoungGen: 10752K->9707K(142848K)]
[ParOldGen: 232384K->232244K(485888K)] 243136K->241951K(628736K)
[PSPermGen: 3162K->3161K(21504K)], 1.5265450 secs
]
```



除了关于young generation的详细信息，日志也提供了old generation和permanent generation的详细信息。对于这三个generations，一样也可以看到所使用的垃圾收集器、堆空间的大小、GC前后的堆使用情况。需要注意的是显示堆空间的大小等于young generation和old generation各自堆空间的和。以上面为例，堆空间总共占用了241951K，其中9707K在young generation，232244K在old generation。Full GC持续了大约1.53秒，用户空间的CPU执行时间为10.96秒，说明GC使用了多线程（和之前一样8个线程）。

对不同generation详细的日志可以让我们分析GC的原因，如果某个generation的日志显示在GC之前，堆空间几乎被占满，那么很有可能就是这个generation触发了GC。但是在上面的例子中，三个generation中的任何一个都不是这样的，在这种情况下是什么原因触发了GC呢。对于Throughput垃圾收集器，在某一个generation被过度使用之前，GC ergonomics（参考本系列第6节）决定要启动GC。

Full GC也可以通过显式的请求而触发，可以是通过应用程序，或者是一个外部的JVM接口。这样触发的GC可以很容易在日志里分辨出来，因为输出的日志是以“Full GC(System)”开头的，而不是“Full GC”。

对于Serial垃圾收集器，详细的GC日志和Throughput垃圾收集器是非常相似的。唯一的区别是不同的generation日志可能使用了不同的GC算法（例如：old generation的日志可能以Tenured开头，而不是ParOldGen）。使用垃圾收集器作为一行日志的开头可以方便我们从日志就判断出JVM的GC设置。

对于CMS垃圾收集器，young generation的详细日志也和Throughput垃圾收集器非常相似，但是old generation的日志却不是这样。对于CMS垃圾收集器，在old generation中的GC是在不同的时间片内与应用程序同时运行的。GC日志自然也和Full GC的日志不同。而且在不同时间片的日志夹杂着在此期间young generation的GC日志。但是了解了上面介绍的GC日志的基本元素，也不难理解在不同时间片内的日志。只是在解释GC运行时间时要特别注意，由于大多数时间片内的GC都是和应用程序同时运行的，所以和那种独占式的GC相比，GC的持续时间更长一些并不说明一定有问题。

正如我们在第7节中所了解的，即使CMS垃圾收集器没有完成一个CMS周期，Full GC也可能会发生。如果发生了GC，在日志中会包含触发Full GC的原因，例如众所周知的”concurrent mode failure“。

为了避免过于冗长，我这里就不详细说明CMS垃圾收集器的日志了。另外，CMS垃圾收集器的作者做了详细的说明（在这里），强烈建议阅读。

### -XX:+PrintGCTimeStamps和-XX:+PrintGCDateStamps

使用-XX:+PrintGCTimeStamps可以将时间和日期也加到GC日志中。表示自JVM启动至今的时间戳会被添加到每一行中。例子如下：

```
0.185: [GC 66048K->53077K(251392K), 0.0977580 secs]
0.323: [GC 119125K->114661K(317440K), 0.1448850 secs]
0.603: [GC 246757K->243133K(375296K), 0.2860800 secs]
```



如果指定了-XX:+PrintGCDateStamps，每一行就添加上了绝对的日期和时间。

```
2014-01-03T12:08:38.102-0100: [GC 66048K->53077K(251392K), 0.0959470 secs]
2014-01-03T12:08:38.239-0100: [GC 119125K->114661K(317440K), 0.1421720 secs]
2014-01-03T12:08:38.513-0100: [GC 246757K->243133K(375296K), 0.2761000 secs]
```



如果需要也可以同时使用两个参数。推荐同时使用这两个参数，因为这样在关联不同来源的GC日志时很有帮助。

### -Xloggc

缺省的GC日志时输出到终端的，使用-Xloggc:也可以输出到指定的文件。需要注意这个参数隐式的设置了参数-XX:+PrintGC和-XX:+PrintGCTimeStamps，但为了以防在新版本的JVM中有任何变化，我仍建议显示的设置这些参数。

### 可管理的JVM参数

一个常常被讨论的问题是在生产环境中GC日志是否应该开启。因为它所产生的开销通常都非常有限，因此我的答案是需要开启。但并不一定在启动JVM时就必须指定GC日志参数。

HotSpot JVM有一类特别的参数叫做可管理的参数。对于这些参数，可以在运行时修改他们的值。我们这里所讨论的所有参数以及以“PrintGC”开头的参数都是可管理的参数。这样在任何时候我们都可以开启或是关闭GC日志。比如我们可以使用JDK自带的jinfo工具来设置这些参数，或者是通过JMX客户端调用`HotSpotDiagnostic MXBean的`setVMOption方法来设置这些参数。