摘自：`http://ifeve.com/useful-jvm-flags-part-1-jvm-types-and-compiler-modes-2/`

现在的JVM运行Java程序（和其它的兼容性语言）时在高效性和稳定性方面做的非常出色。自适应内存管理、垃圾收集、及时编译、动态类加载、锁优化——这里仅仅列举了某些场景下会发生的神奇的事情，但他们几乎不会直接与普通的程序员相关。在运行时，JVM会不断的计算并优化应用或者应用的某些部分。

虽然有了这种程度的自动化（或者说有这么多自动化），但是JVM仍然提供了足够多的外部监控和手动调优工具。在有错误或低性能的情况下，JVM必须能够让专家调试。顺便说一句，除了这些隐藏在引擎中的神奇功能，允许大范围的手动调优也是现代JVM的优势之一。有趣的是，一些命令行参数可以在JVM启动时传入到JVM中。一些JVM提供了几百个这样的参数，所以如果没有这方面的知识很容易迷失。这系列博客的目标是着重讲解日常相关的一些参数以及他们的适用场合。我们将专注于Java6的Sun/Oracle HotSpot JVM，大多数情况下，这些参数也会适用于其他一些流行的JVM里。



**-server and -client**

有两种类型的 HotSpot JVM，即”server”和”client”。服务端的VM中的默认为堆提供了一个更大的空间以及一个并行的垃圾收集器，并且在运行时可以更大程度地优化代码。客户端的VM更加保守一些（校对注：这里作者指客户端虚拟机有较小的默认堆大小），这样可以缩短JVM的启动时间和占用更少的内存。有一个叫”JVM功效学”的概念，它会在JVM启动的时候根据可用的硬件和操作系统来自动的选择JVM的类型。[具体](http://docs.oracle.com/javase/6/docs/technotes/guides/vm/server-class.html)[的标准可以在这里找到](http://docs.oracle.com/javase/6/docs/technotes/guides/vm/server-class.html)。从标准表中，我们可以看到客户端的VM只在32位系统中可用。

如果我们不喜欢预选（校对注：指JVM自动选择的JVM类型）的JVM，我们可以使用-server和-client参数来设置使用服务端或客户端的VM。虽然当初服务端VM的目标是长时间运行的服务进程，但是现在看来，在运行独立应用程序时它比客户端VM有更出色的性能。当应用的性能非常重要时，我推荐使用-server参数来选择服务端VM。一个常见的问题：在一个32位的系统上，HotSpot JDK可以运行服务端VM，但是32位的JRE只能运行客户端VM。

**-version and -showversion**

当我们调用“java”命令时，我们如何才能知道我们安装的是哪个版本的Java和JVM类型呢？在同一个系统中安装多个Java，如果不注意的话有运行错误JVM的风险。在不同的Linux版本上预装JVM这方面，我承认现在已经变的比以前好很多了。幸运的是，我们现在可以使用-version参数，它可以打印出正在使用的JVM的信息。例如：

```
$ java -version
java version "1.6.0_24"
Java(TM) SE Runtime Environment (build 1.6.0_24-b07)
Java HotSpot(TM) Client VM (build 19.1-b02, mixed mode, sharing)
```

输出显示的是Java版本号(1.6.0_24)和JRE确切的build号(1.6.0_24-b07)。我们也可以看到JVM的名字(HotSpot)、类型(client)和build ID（19.1-b02) ）。除此之外，我们还知道JVM以混合模式(mixed mode)在运行，这是HotSpot默认的运行模式，意味着JVM在运行时可以动态的把字节码编译为本地代码。我们也可以看到类数据共享（class data sharing）是开启的，类数据共享（class data sharing）是一种在只读缓存（在jsa文件中，”Java Shared Archive”）中存储JRE的系统类，被所有Java进程的类加载器用来当做共享资源。类数据共享(Class data sharing)可能在经常从jar文档中读所有的类数据的情况下显示出性能优势。

-version参数在打印完上述信息后立即终止JVM。还有一个类似的参数-showversion可以用来输出相同的信息，但是-showversion紧接着会处理并执行Java程序。因此，-showversion对几乎所有Java应用的命令行都是一个有效的补充。你永远不知道你什么时候，突然需要了解一个特定的Java应用（崩溃时）使用的JVM的一些信息。在启动时添加-showversion，我们就能保证当我们需要时可以得到这些信息。

**-Xint, -Xcomp, 和 -Xmixed**

-Xint和-Xcomp参数和我们的日常工作不是很相关，但是我非常有兴趣通过它来了解下JVM。在解释模式(interpreted mode)下，-Xint标记会强制JVM执行所有的字节码，当然这会降低运行速度，通常低10倍或更多。-Xcomp参数与它（-Xint）正好相反，JVM在第一次使用时会把所有的字节码编译成本地代码，从而带来最大程度的优化。这听起来不错，因为这完全绕开了缓慢的解释器。然而，很多应用在使用-Xcomp也会有一些性能损失，当然这比使用-Xint损失的少，原因是-xcomp没有让JVM启用JIT编译器的全部功能。JIT编译器在运行时创建方法使用文件，然后一步一步的优化每一个方法，有时候会主动的优化应用的行为。这些优化技术，比如，积极的分支预测（optimistic branch prediction），如果不先分析应用就不能有效的使用。另一方面方法只有证明它们与此相关时才会被编译，也就是，在应用中构建某种热点。被调用很少（甚至只有一次）的方法在解释模式下会继续执行，从而减少编译和优化成本。

注意混合模式也有他自己的参数，-Xmixed。最新版本的HotSpot的默认模式是混合模式，所以我们不需要特别指定这个标记。我们来用对象填充HashMap然后检索它的结果做一个简单的用例。每一个例子，它的运行时间都是很多次运行的平均时间。

```
$ java -server -showversion Benchmark
java version "1.6.0_24"
Java(TM) SE Runtime Environment (build 1.6.0_24-b07)
Java HotSpot(TM) Server VM (build 19.1-b02, mixed mode)
 
Average time: 0.856449 seconds
```



```
$ java -server -showversion -Xcomp Benchmark
java version "1.6.0_24"
Java(TM) SE Runtime Environment (build 1.6.0_24-b07)
Java HotSpot(TM) Server VM (build 19.1-b02, compiled mode)
 
Average time: 0.950892 seconds
```



```
$ java -server -showversion -Xint Benchmark
java version "1.6.0_24"
Java(TM) SE Runtime Environment (build 1.6.0_24-b07)
Java HotSpot(TM) Server VM (build 19.1-b02, interpreted mode)
 
Average time: 7.622285 seconds
```

当然也有很多使-Xcomp表现很好的例子。特别是运行时间长的应用，我强烈建议大家使用JVM的默认设置,让JIT编译器充分发挥其动态潜力，毕竟JIT编译器是组成JVM最重要的组件之一。事实上，正是因为JVM在这方面的进展才让Java不再那么慢。



