摘自：`https://www.cnblogs.com/baihuitestsoftware/articles/6483690.html`

Tomcat 的缺省配置是不能稳定长期运行的，也就是不适合生产环境，它会死机，让你不断重新启动，甚至在午夜时分唤醒你。对于[操作系统](http://lib.csdn.net/base/operatingsystem)优化来说，是尽可能的增大可使用的内存容量、提高CPU 的频率，保证文件系统的读写速率等。经过压力[测试](http://lib.csdn.net/base/softwaretest)验证，在并发连接很多的情况下，CPU 的处理能力越强，系统运行速度越快。

 

Tomcat 的优化不像其它软件那样，简简单单的修改几个参数就可以了，它的优化主要有三方面，分为系统优化，Tomcat 本身的优化，[Java](http://lib.csdn.net/base/javase) 虚拟机（JVM）调优。系统优化就不在介绍了，接下来就详细的介绍一下 Tomcat 本身与 JVM 优化，以 Tomcat 7 为例。

**一、Tomcat 本身优化**

Tomcat 的自身参数的优化，这块很像 ApacheHttp Server。修改一下 xml 配置文件中的参数，调整最大连接数，超时等。此外，我们安装 Tomcat 是，优化就已经开始了。

1、工作方式选择

为了提升性能，首先就要对代码进行动静分离，让 Tomcat 只负责 jsp 文件的解析工作。如采用 Apache 和 Tomcat 的整合方式，他们之间的连接方案有三种选择，JK、http_proxy 和 ajp_proxy。相对于 JK 的连接方式，后两种在配置上比较简单的，灵活性方面也一点都不逊色。但就稳定性而言不像JK 这样久经考验，所以建议采用 JK 的连接方式。 

2、Connector 连接器的配置

之前文件介绍过的 Tomcat 连接器的三种方式： bio、nio 和 apr，三种方式性能差别很大，apr 的性能最优， bio 的性能最差。而 Tomcat 7 使用的 Connector  默认就启用的 Apr 协议，但需要系统安装 Apr 库，否则就会使用 bio 方式。

3、配置文件优化

配置文件优化其实就是对 server.xml 优化，可以提大大提高 Tomcat 的处理请求的能力，下面我们来看 Tomcat 容器内的优化。

默认配置下，Tomcat 会为每个连接器创建一个绑定的线程池（最大线程数 200），服务启动时，默认创建了 5 个空闲线程随时等待用户请求。

首先，打开 ${TOMCAT_HOME}/conf/server.xml，搜索【<Executor name="tomcatThreadPool"】，开启并调整为

```
<Executor name="tomcatThreadPool" namePrefix="catalina-exec-"
        maxThreads="500" minSpareThreads="20" maxSpareThreads="50" maxIdleTime="60000"/>
```


注意， Tomcat 7 在开启线程池前，一定要安装好 Apr 库，并可以启用，否则会有错误报出，shutdown.sh 脚本无法关闭进程。

然后，修改<Connector …>节点，增加 executor 属性，搜索【port="8080"】，调整为

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
<Connector executor="tomcatThreadPool"
               port="8080" protocol="HTTP/1.1"
               URIEncoding="UTF-8"
               connectionTimeout="30000"
               enableLookups="false"
               disableUploadTimeout="false"
               connectionUploadTimeout="150000"
               acceptCount="300"
               keepAliveTimeout="120000"
               maxKeepAliveRequests="1"
               compression="on"
               compressionMinSize="2048"
               compressableMimeType="text/html,text/xml,text/javascript,text/css,text/plain,image/gif,image/jpg,image/png" 
               redirectPort="8443" />
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)


maxThreads :Tomcat 使用线程来处理接收的每个请求，这个值表示 Tomcat 可创建的最大的线程数，默认值是 200

minSpareThreads：最小空闲线程数，Tomcat 启动时的初始化的线程数，表示即使没有人使用也开这么多空线程等待，默认值是 10。

maxSpareThreads：最大备用线程数，一旦创建的线程超过这个值，Tomcat 就会关闭不再需要的 socket 线程。

上边配置的参数，最大线程 500（一般服务器足以），要根据自己的实际情况合理设置，设置越大会耗费内存和 CPU，因为 CPU 疲于线程上下文切换，没有精力提供请求服务了，最小空闲线程数 20，线程最大空闲时间 60 秒，当然允许的最大线程连接数还受制于操作系统的内核参数设置，设置多大要根据自己的需求与环境。当然线程可以配置在“tomcatThreadPool”中，也可以直接配置在“Connector”中，但不可以重复配置。

URIEncoding：指定 Tomcat 容器的 URL 编码格式，语言编码格式这块倒不如其它 WEB 服务器软件配置方便，需要分别指定。

connnectionTimeout： 网络连接超时，单位：毫秒，设置为 0 表示永不超时，这样设置有隐患的。通常可设置为 30000 毫秒，可根据检测实际情况，适当修改。

enableLookups： 是否反查域名，以返回远程主机的主机名，取值为：true 或 false，如果设置为false，则直接返回IP地址，为了提高处理能力，应设置为 false。

disableUploadTimeout：上传时是否使用超时机制。

connectionUploadTimeout：上传超时时间，毕竟文件上传可能需要消耗更多的时间，这个根据你自己的业务需要自己调，以使Servlet有较长的时间来完成它的执行，需要与上一个参数一起配合使用才会生效。

acceptCount：指定当所有可以使用的处理请求的线程数都被使用时，可传入连接请求的最大队列长度，超过这个数的请求将不予处理，默认为100个。

keepAliveTimeout：长连接最大保持时间（毫秒），表示在下次请求过来之前，Tomcat 保持该连接多久，默认是使用 connectionTimeout 时间，-1 为不限制超时。

maxKeepAliveRequests：表示在服务器关闭之前，该连接最大支持的请求数。超过该请求数的连接也将被关闭，1表示禁用，-1表示不限制个数，默认100个，一般设置在100~200之间。

compression：是否对响应的数据进行 GZIP 压缩，off：表示禁止压缩；on：表示允许压缩（文本将被压缩）、force：表示所有情况下都进行压缩，默认值为off，压缩数据后可以有效的减少页面的大小，一般可以减小1/3左右，节省带宽。

compressionMinSize：表示压缩响应的最小值，只有当响应报文大小大于这个值的时候才会对报文进行压缩，如果开启了压缩功能，默认值就是2048。

compressableMimeType：压缩类型，指定对哪些类型的文件进行数据压缩。

noCompressionUserAgents="gozilla, traviata"： 对于以下的浏览器，不启用压缩。

如果已经对代码进行了动静分离，静态页面和图片等数据就不需要 Tomcat 处理了，那么也就不需要配置在 Tomcat 中配置压缩了。

以上是一些常用的配置参数属性，当然还有好多其它的参数设置，还可以继续深入的优化，HTTP Connector 与 AJP Connector 的参数属性值，可以参考官方文档的详细说明：

<https://tomcat.apache.org/tomcat-7.0-doc/config/http.html>

<https://tomcat.apache.org/tomcat-7.0-doc/config/ajp.html>

**二、JVM 优化**

 Tomcat 启动命令行中的优化参数，就是 JVM 的优化 。Tomcat 首先跑在 JVM 之上的，因为它的启动其实也只是一个 java 命令行，首先我们需要对这个 JAVA 的启动命令行进行调优。不管是 YGC 还是 Full GC，GC 过程中都会对导致程序运行中中断，正确的选择不同的 GC 策略，调整 JVM、GC 的参数，可以极大的减少由于 GC 工作，而导致的程序运行中断方面的问题，进而适当的提高 Java 程序的工作效率。但是调整 GC 是以个极为复杂的过程，由于各个程序具备不同的特点，如：web 和 GUI 程序就有很大区别（Web可以适当的停顿，但GUI停顿是客户无法接受的），而且由于跑在各个机器上的配置不同（主要 cup 个数，内存不同），所以使用的 GC 种类也会不同。

1、JVM 参数配置方法

Tomcat 的启动参数位于安装目录 ${JAVA_HOME}/bin目录下，[Linux](http://lib.csdn.net/base/linux) 操作系统就是 catalina.sh 文件。JAVA_OPTS，就是用来设置 JVM 相关运行参数的变量，还可以在 CATALINA_OPTS 变量中设置。关于这 2 个变量，还是多少有些区别的：

JAVA_OPTS：用于当 Java 运行时选项“start”、“stop”或“run”命令执行。

CATALINA_OPTS：用于当 Java 运行时选项“start”或“run”命令执行。

为什么有两个不同的变量？它们之间都有什么区别呢？

首先，在启动 Tomcat 时，任何指定变量的传递方式都是相同的，可以传递到执行“start”或“run”命令中，但只有设定在 JAVA_OPTS 变量里的参数被传递到“stop”命令中。对于 Tomcat 运行过程，可能没什么区别，影响的是结束程序，而不是启动程序。

第二个区别是更微妙，其他应用程序也可以使用 JAVA_OPTS 变量，但只有在 Tomcat 中使用 CATALINA_OPTS 变量。如果你设置环境变量为只使用 Tomcat，最好你会建议使用 CATALINA_OPTS 变量，而如果你设置环境变量使用其它的 Java 应用程序，例如 JBoss，你应该把你的设置放在JAVA_OPTS 变量中。

2、JVM 参数属性

32 位系统下 JVM 对内存的限制：不能突破 2GB ，那么这时你的 Tomcat 要优化，就要讲究点技巧了，而在 64 位操作系统上无论是系统内存还是 JVM 都没有受到 2GB 这样的限制。

针对于 JMX 远程监控也是在这里设置，以下为 64 位系统环境下的配置，内存加入的参数如下：**（以下内容如果报错，建议放一行，把双引号去掉可解决）**

![20](./assert/20.png)



[
![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

```
set CATALINA_OPTS="
-server 
-Xms6000M 
-Xmx6000M 
-Xss512k 
-XX:NewSize=2250M 
-XX:MaxNewSize=2250M 
-XX:PermSize=128M
-XX:MaxPermSize=256M  
-XX:+AggressiveOpts 
-XX:+UseBiasedLocking 
-XX:+DisableExplicitGC 
-XX:+UseParNewGC 
-XX:+UseConcMarkSweepGC 
-XX:MaxTenuringThreshold=31 
-XX:+CMSParallelRemarkEnabled 
-XX:+UseCMSCompactAtFullCollection 
-XX:LargePageSizeInBytes=128m 
-XX:+UseFastAccessorMethods 
-XX:+UseCMSInitiatingOccupancyOnly
-Duser.timezone=Asia/Shanghai 
-Djava.awt.headless=true"
```

[![复制代码](https://common.cnblogs.com/images/copycode.gif)](javascript:void(0);)

 

Linux：

 

> 在/usr/local/apache-tomcat-5.5.23/bin 目录下的catalina.sh添加：
>
> JAVA_OPTS='-Xms512m -Xmx1024m'要加“m”说明是MB，否则就是KB了，在启动tomcat时会 报内存不足。
>
> -Xms：初始值-Xmx：最大值-Xmn：最小值

 

Windows

 

> 在catalina.bat最前面加入set JAVA_OPTS=-Xms128m -Xmx350m 


为了看着方便，将每个参数单独写一行。上面参数好多啊，可能有人写到现在都没见过一个在 Tomcat 的启动命令里加了这么多参数，当然，这些参数只是我机器上的，不一定适合你，尤其是参数后的 value（值）是需要根据你自己的实际情况来设置的。

上述这样的配置，基本上可以达到：

系统响应时间增快；

JVM回收速度增快同时又不影响系统的响应率；

JVM内存最大化利用；

线程阻塞情况最小化。

JVM 常用参数详解：

-server：一定要作为第一个参数，在多个 CPU 时性能佳，还有一种叫 -client 的模式，特点是启动速度比较快，但运行时性能和内存管理效率不高，通常用于客户端应用程序或开发调试，在 32 位环境下直接运行 Java 程序默认启用该模式。Server 模式的特点是启动速度比较慢，但运行时性能和内存管理效率很高，适用于生产环境，在具有 64 位能力的 JDK 环境下默认启用该模式，可以不配置该参数。

-Xms：表示 Java 初始化堆的大小，-Xms 与-Xmx 设成一样的值，避免 JVM 反复重新申请内存，导致性能大起大落，默认值为物理内存的 1/64，默认（MinHeapFreeRatio参数可以调整）空余堆内存小于 40% 时，JVM 就会增大堆直到 -Xmx 的最大限制。

-Xmx：表示最大 Java 堆大小，当应用程序需要的内存超出堆的最大值时虚拟机就会提示内存溢出，并且导致应用服务崩溃，因此一般建议堆的最大值设置为可用内存的最大值的80%。如何知道我的 JVM 能够使用最大值，使用 java -Xmx512M -version 命令来进行测试，然后逐渐的增大 512 的值,如果执行正常就表示指定的内存大小可用，否则会打印错误信息，默认值为物理内存的 1/4，默认（MinHeapFreeRatio参数可以调整）空余堆内存大于 70% 时，JVM 会减少堆直到-Xms 的最小限制。

-Xss：表示每个 Java 线程堆栈大小，JDK 5.0 以后每个线程堆栈大小为 1M，以前每个线程堆栈大小为 256K。根据应用的线程所需内存大小进行调整，在相同物理内存下，减小这个值能生成更多的线程，但是操作系统对一个进程内的线程数还是有限制的，不能无限生成，经验值在 3000~5000 左右。一般小的应用， 如果栈不是很深， 应该是128k 够用的，大的应用建议使用 256k 或 512K，一般不易设置超过 1M，要不然容易出现out ofmemory。这个选项对性能影响比较大，需要严格的测试。

-XX:NewSize：设置新生代内存大小。

-XX:MaxNewSize：设置最大新生代新生代内存大小

-XX:PermSize：设置持久代内存大小

-XX:MaxPermSize：设置最大值持久代内存大小，永久代不属于堆内存，堆内存只包含新生代和老年代。

-XX:+AggressiveOpts：作用如其名（aggressive），启用这个参数，则每当 JDK 版本升级时，你的 JVM 都会使用最新加入的优化技术（如果有的话）。

-XX:+UseBiasedLocking：启用一个优化了的线程锁，我们知道在我们的appserver，每个http请求就是一个线程，有的请求短有的请求长，就会有请求排队的现象，甚至还会出现线程阻塞，这个优化了的线程锁使得你的appserver内对线程处理自动进行最优调配。

-XX:+DisableExplicitGC：在 程序代码中不允许有显示的调用“System.gc()”。每次在到操作结束时手动调用 System.gc() 一下，付出的代价就是系统响应时间严重降低，就和关于 Xms，Xmx 里的解释的原理一样，这样去调用 GC 导致系统的 JVM 大起大落。

-XX:+UseConcMarkSweepGC：设置年老代为并发收集，即 CMS gc，这一特性只有 jdk1.5
后续版本才具有的功能，它使用的是 gc 估算触发和 heap 占用触发。我们知道频频繁的 GC 会造面 JVM
的大起大落从而影响到系统的效率，因此使用了 CMS GC 后可以在 GC 次数增多的情况下，每次 GC 的响应时间却很短，比如说使用了 CMS
GC 后经过 jprofiler 的观察，GC 被触发次数非常多，而每次 GC 耗时仅为几毫秒。

-XX:+UseParNewGC：对新生代采用多线程并行回收，这样收得快，注意最新的 JVM 版本，当使用 -XX:+UseConcMarkSweepGC 时，-XX:UseParNewGC 会自动开启。因此，如果年轻代的并行 GC 不想开启，可以通过设置 -XX：-UseParNewGC 来关掉。

-XX:MaxTenuringThreshold：设置垃圾最大年龄。如果设置为0的话，则新生代对象不经过 Survivor 区，直接进入老年代。对于老年代比较多的应用（需要大量常驻内存的应用），可以提高效率。如果将此值设置为一 个较大值，则新生代对象会在 Survivor 区进行多次复制，这样可以增加对象在新生代的存活时间，增加在新生代即被回收的概率，减少Full GC的频率，这样做可以在某种程度上提高服务稳定性。该参数只有在串行 GC 时才有效，这个值的设置是根据本地的 jprofiler 监控后得到的一个理想的值，不能一概而论原搬照抄。

-XX:+CMSParallelRemarkEnabled：在使用 UseParNewGC 的情况下，尽量减少 mark 的时间。

-XX:+UseCMSCompactAtFullCollection：在使用 concurrent gc 的情况下，防止 memoryfragmention，对 live object 进行整理，使 memory 碎片减少。

-XX:LargePageSizeInBytes：指定 [Java ](http://lib.csdn.net/base/java)heap 的分页页面大小，内存页的大小不可设置过大， 会影响 Perm 的大小。

-XX:+UseFastAccessorMethods：使用 get，set 方法转成本地代码，原始类型的快速优化。

-XX:+UseCMSInitiatingOccupancyOnly：只有在 oldgeneration 在使用了初始化的比例后 concurrent collector 启动收集。

-Duser.timezone=Asia/Shanghai：设置用户所在时区。

-Djava.awt.headless=true：这个参数一般我们都是放在最后使用的，这全参数的作用是这样的，有时我们会在我们的 J2EE 工程中使用一些图表工具如：jfreechart，用于在 web 网页输出 GIF/JPG 等流，在 winodws 环境下，一般我们的 app server 在输出图形时不会碰到什么问题，但是在linux/unix 环境下经常会碰到一个 exception 导致你在 winodws 开发环境下图片显示的好好可是在 linux/unix 下却显示不出来，因此加上这个参数以免避这样的情况出现。

-Xmn：新生代的内存空间大小，注意：此处的大小是（eden+ 2 survivor space)。与 jmap -heap 中显示的 New gen 是不同的。整个堆大小 = 新生代大小 + 老生代大小 + 永久代大小。在保证堆大小不变的情况下，增大新生代后，将会减小老生代大小。此值对系统性能影响较大，Sun官方推荐配置为整个堆的 3/8。

-XX:CMSInitiatingOccupancyFraction：当堆满之后，并行收集器便开始进行垃圾收集，例如，当没有足够的空间来容纳新分配或提升的对象。对于 CMS 收集器，长时间等待是不可取的，因为在并发垃圾收集期间应用持续在运行（并且分配对象）。因此，为了在应用程序使用完内存之前完成垃圾收集周期，CMS 收集器要比并行收集器更先启动。因为不同的应用会有不同对象分配模式，JVM 会收集实际的对象分配（和释放）的运行时数据，并且分析这些数据，来决定什么时候启动一次 CMS 垃圾收集周期。这个参数设置有很大技巧，基本上满足(Xmx-Xmn)*(100-CMSInitiatingOccupancyFraction)/100 >= Xmn 就不会出现 promotion failed。例如在应用中 Xmx 是6000，Xmn 是 512，那么 Xmx-Xmn 是 5488M，也就是老年代有 5488M，CMSInitiatingOccupancyFraction=90 说明老年代到 90% 满的时候开始执行对老年代的并发垃圾回收（CMS），这时还 剩 10% 的空间是 5488*10% = 548M，所以即使 Xmn（也就是新生代共512M）里所有对象都搬到老年代里，548M 的空间也足够了，所以只要满足上面的公式，就不会出现垃圾回收时的 promotion failed，因此这个参数的设置必须与 Xmn 关联在一起。

-XX:+CMSIncrementalMode：该标志将开启 CMS 收集器的增量模式。增量模式经常暂停 CMS 过程，以便对应用程序线程作出完全的让步。因此，收集器将花更长的时间完成整个收集周期。因此，只有通过测试后发现正常 CMS 周期对应用程序线程干扰太大时，才应该使用增量模式。由于现代服务器有足够的处理器来适应并发的垃圾收集，所以这种情况发生得很少，用于但 CPU情况。

-XX:NewRatio：年轻代（包括 Eden 和两个 Survivor 区）与年老代的比值（除去持久代），-XX:NewRatio=4 表示年轻代与年老代所占比值为 1:4，年轻代占整个堆栈的 1/5，Xms=Xmx 并且设置了 Xmn 的情况下，该参数不需要进行设置。

-XX:SurvivorRatio：Eden 区与 Survivor 区的大小比值，设置为 8，表示 2 个 Survivor 区（JVM 堆内存年轻代中默认有 2 个大小相等的 Survivor 区）与 1 个 Eden 区的比值为 2:8，即 1 个 Survivor 区占整个年轻代大小的 1/10。

-XX:+UseSerialGC：设置串行收集器。

-XX:+UseParallelGC：设置为并行收集器。此配置仅对年轻代有效。即年轻代使用并行收集，而年老代仍使用串行收集。

-XX:+UseParallelOldGC：配置年老代垃圾收集方式为并行收集，JDK6.0 开始支持对年老代并行收集。

-XX:ConcGCThreads：早期 JVM 版本也叫-XX:ParallelCMSThreads，定义并发 CMS 过程运行时的线程数。比如 value=4 意味着 CMS 周期的所有阶段都以 4 个线程来执行。尽管更多的线程会加快并发 CMS 过程，但其也会带来额外的同步开销。因此，对于特定的应用程序，应该通过测试来判断增加 CMS 线程数是否真的能够带来性能的提升。如果还标志未设置，JVM 会根据并行收集器中的 -XX:ParallelGCThreads 参数的值来计算出默认的并行 CMS 线程数。

-XX:ParallelGCThreads：配置并行收集器的线程数，即：同时有多少个线程一起进行垃圾回收，此值建议配置与 CPU 数目相等。

-XX:OldSize：设置 JVM 启动分配的老年代内存大小，类似于新生代内存的初始大小 -XX:NewSize。

以上就是一些常用的配置参数，有些参数是可以被替代的，配置思路需要考虑的是 Java 提供的垃圾回收机制。虚拟机的堆大小决定了虚拟机花费在收集垃圾上的时间和频度。收集垃圾能够接受的速度和应用有关，应该通过分析实际的垃圾收集的时间和频率来调整。假如堆的大小很大，那么完全垃圾收集就会很慢，但是频度会降低。假如您把堆的大小和内存的需要一致，完全收集就很快，但是会更加频繁。调整堆大小的的目的是最小化垃圾收集的时间，以在特定的时间内最大化处理客户的请求。在基准测试的时候，为确保最好的性能，要把堆的大小设大，确保垃圾收集不在整个基准测试的过程中出现。

假如系统花费很多的时间收集垃圾，请减小堆大小。一次完全的垃圾收集应该不超过 3-5 秒。假如垃圾收集成为瓶颈，那么需要指定代的大小，检查垃圾收集的周详输出，研究垃圾收集参数对性能的影响。当增加处理器时，记得增加内存，因为分配能够并行进行，而垃圾收集不是并行的。

3、设置系统属性

之前说过，Tomcat 的语言编码，配置起来很慢，要经过多次设置才可以了，否则中文很有可能出现乱码情况。譬如汉字“中”，以 UTF-8 编码后得到的是 3 字节的值 %E4%B8%AD，然后通过 GET 或者 POST 方式把这 3 个字节提交到 Tomcat 容器，如果你不告诉 Tomcat 我的参数是用 UTF-8编码的，那么 Tomcat 就认为你是用 ISO-8859-1 来编码的，而 ISO8859-1（兼容 URI 中的标准字符集 US-ASCII）是兼容 ASCII 的单字节编码并且使用了单字节内的所有空间，因此 Tomcat 就以为你传递的用 ISO-8859-1 字符集编码过的 3 个字符，然后它就用 ISO-8859-1 来解码。

设置起来不难使用“ -D<名称>=<值> ”来设置系统属性：

-Djavax.servlet.request.encoding=UTF-8

-Djavax.servlet.response.encoding=UTF-8 

-Dfile.encoding=UTF-8 

-Duser.country=CN 

-Duser.language=zh

4、常见的 Java 内存溢出有以下三种

（1） java.lang.OutOfMemoryError: Java heap space —-JVM Heap（堆）溢出

JVM 在启动的时候会自动设置 JVM Heap 的值，其初始空间（即-Xms）是物理内存的1/64，最大空间（-Xmx）不可超过物理内存。可以利用 JVM提供的 -Xmn -Xms -Xmx 等选项可进行设置。Heap 的大小是 Young Generation 和 Tenured Generaion 之和。在 JVM 中如果 98％ 的时间是用于 GC，且可用的 Heap size 不足 2％ 的时候将抛出此异常信息。

解决方法：手动设置 JVM Heap（堆）的大小。  
（2） java.lang.OutOfMemoryError: PermGen space  —- PermGen space溢出。

PermGen space 的全称是 Permanent Generation space，是指内存的永久保存区域。为什么会内存溢出，这是由于这块内存主要是被 JVM 存放Class 和 Meta 信息的，Class 在被 Load 的时候被放入 PermGen space 区域，它和存放 Instance 的 Heap 区域不同，sun 的 GC 不会在主程序运行期对 PermGen space 进行清理，所以如果你的 APP 会载入很多 CLASS 的话，就很可能出现 PermGen space 溢出。

解决方法： 手动设置 MaxPermSize 大小

（3） java.lang.StackOverflowError   —- 栈溢出

栈溢出了，JVM 依然是采用栈式的虚拟机，这个和 C 与 Pascal 都是一样的。函数的调用过程都体现在堆栈和退栈上了。调用构造函数的 “层”太多了，以致于把栈区溢出了。通常来讲，一般栈区远远小于堆区的，因为函数调用过程往往不会多于上千层，而即便每个函数调用需要 1K 的空间（这个大约相当于在一个 C 函数内声明了 256 个 int 类型的变量），那么栈区也不过是需要 1MB 的空间。通常栈的大小是 1－2MB 的。
通常递归也不要递归的层次过多，很容易溢出。

解决方法：修改程序。

更多信息，请参考以下文章：

JVM 垃圾回收调优总结

<http://developer.51cto.com/art/201201/312639.htm>

JVM调优总结：典型配置举例

<http://developer.51cto.com/art/201201/311739.htm>

JVM基础：JVM参数设置、分析 

<http://developer.51cto.com/art/201201/312018.htm>

JVM 堆内存相关的启动参数：年轻代、老年代和永久代的内存分配

<http://www.2cto.com/kf/201409/334840.html>

Java 虚拟机–新生代与老年代GC

<http://my.oschina.net/sunnywu/blog/332870>

JVM（Java虚拟机）优化大全和案例实战

<http://blog.csdn.net/kthq/article/details/8618052>

JVM内存区域划分Eden Space、Survivor Space、Tenured Gen，Perm Gen解释 

<http://blog.chinaunix.net/xmlrpc.php?r=blog/article&uid=29632145&id=4616836>