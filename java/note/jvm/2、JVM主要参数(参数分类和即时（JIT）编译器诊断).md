摘自：`http://ifeve.com/useful-jvm-flags-part-2-flag/`

JVM 参数分类

HotSpot JVM 提供了三类参数。第一类包括了标准参数。顾名思义，标准参数中包括功能和输出的参数都是很稳定的，很可能在将来的JVM版本中不会改变。你可以用java命令（或者是用 java -help）检索出所有标准参数。我们在第一部分中已经见到过一些标准参数，例如：-server。

第二类是X参数，非标准化的参数在将来的版本中可能会改变。所有的这类参数都以-X开始，并且可以用java -X来检索。注意，不能保证所有参数都可以被检索出来，其中就没有-Xcomp。

第三类是包含XX参数（到目前为止最多的），它们同样不是标准的，甚至很长一段时间内不被列出来（最近，这种情况有改变 ，我们将在本系列的第三部分中讨论它们）。然而，在实际情况中X参数和XX参数并没有什么不同。X参数的功能是十分稳定的，然而很多XX参数仍在实验当中（主要是JVM的开发者用于debugging和调优JVM自身的实现）。值的一读的介绍非标准参数的文档 [HotSpot JVM documentation](http://www.oracle.com/technetwork/java/javase/tech/vmoptions-jsp-140102.html)，其中明确的指出XX参数不应该在不了解的情况下使用。这是真的，并且我认为这个建议同样适用于X参数（同样一些标准参数也是）。不管类别是什么，在使用参数之前应该先了解它可能产生的影响。![Read More...](data:image/gif;base64,R0lGODlhAQABAIAAAAAAAP///yH5BAEAAAAALAAAAAABAAEAAAIBRAA7)

用一句话来说明XX参数的语法。所有的XX参数都以”-XX:”开始，但是随后的语法不同，取决于参数的类型。

- 对于布尔类型的参数，我们有”+”或”-“，然后才设置JVM选项的实际名称。例如，-XX:+<name>用于激活<name>选项，而-XX:-<name>用于注销选项。
- 对于需要非布尔值的参数，如string或者integer，我们先写参数的名称，后面加上”=”，最后赋值。例如，  -XX:<name>=<value>给<name>赋值<value>。

现在让我们来看看JIT编译方面的一些XX参数。

-XX:+PrintCompilation and -XX:+CITime

当一个Java应用运行时，非常容易查看JIT编译工作。通过设置-XX:+PrintCompilation，我们可以简单的输出一些关于从字节码转化成本地代码的编译过程。我们来看一个服务端VM运行的例子：

```
$ java -server -XX:+PrintCompilation Benchmark
  1       java.lang.String::hashCode (64 bytes)
  2       java.lang.AbstractStringBuilder::stringSizeOfInt (21 bytes)
  3       java.lang.Integer::getChars (131 bytes)
  4       java.lang.Object::<init> (1 bytes)
---   n   java.lang.System::arraycopy (static)
  5       java.util.HashMap::indexFor (6 bytes)
  6       java.lang.Math::min (11 bytes)
  7       java.lang.String::getChars (66 bytes)
  8       java.lang.AbstractStringBuilder::append (60 bytes)
  9       java.lang.String::<init> (72 bytes)
 10       java.util.Arrays::copyOfRange (63 bytes)
 11       java.lang.StringBuilder::append (8 bytes)
 12       java.lang.AbstractStringBuilder::<init> (12 bytes)
 13       java.lang.StringBuilder::toString (17 bytes)
 14       java.lang.StringBuilder::<init> (18 bytes)
 15       java.lang.StringBuilder::append (8 bytes)
[...]
 29       java.util.regex.Matcher::reset (83 bytes)
```

每当一个方法被编译，就输出一行-XX:+PrintCompilation。每行都包含顺序号（唯一的编译任务ID）和已编译方法的名称和大小。因此，顺序号1，代表编译String类中的hashCode方法到原生代码的信息。根据方法的类型和编译任务打印额外的信息。例如，本地的包装方法前方会有”n”参数，像上面的System::arraycopy一样。注意这样的方法不会包含顺序号和方法占用的大小，因为它不需要编译为本地代码。同样可以看到被重复编译的方法，例如StringBuilder::append顺序号为11和15。输出在顺序号29时停止 ，这表明在这个Java应用运行时总共需要编译29个方法。

没有官方的文档关于-XX:+PrintCompilation，但是[这个](https://gist.github.com/rednaxelafx/1165804#file_notes.md)[描述](https://gist.github.com/rednaxelafx/1165804#file_notes.md)是对于此参数比较好的。我推荐更深入学习一下。

JIT编译器输出帮助我们理解客户端VM与服务端VM的一些区别。用服务端VM，我们的应用例子输出了29行，同样用客户端VM，我们会得到55行。这看起来可能很怪，因为服务端VM应该比客户端VM做了“更多”的编译。然而，由于它们各自的默认设置，服务端VM在判断方法是不是热点和需不需要编译时比客户端VM观察方法的时间更长。因此，在使用服务端VM时，一些潜在的方法会稍后编译就不奇怪了。

通过另外设置-XX:+CITime，我们可以在JVM关闭时得到各种编译的统计信息。让我们看一下一个特定部分的统计：

```
$ java -server -XX:+CITime Benchmark
[...]
Accumulated compiler times (for compiled methods only)
------------------------------------------------
  Total compilation time   :  0.178 s
    Standard compilation   :  0.129 s, Average : 0.004
    On stack replacement   :  0.049 s, Average : 0.024
[...]
```

总共用了0.178s（在29个编译任务上）。这些，”on stack replacement”占用了0.049s，即编译的方法目前在堆栈上用去的时间。这种技术并不是简单的实现性能显示，实际上它是非常重要的。没有”on stack replacement”，方法如果要执行很长时间（比如，它们包含了一个长时间运行的循环），它们运行时将不会被它们编译过的副本替换。

再一次，客户端VM与服务端VM的比较是非常有趣的。客户端VM相应的数据表明，即使有55个方法被编译了，但这些编译总共用了只有0.021s。服务端VM做的编译少但是用的时间却比客户端VM多。这个原因是，使用服务端VM在生成本地代码时执行了更多的优化。

在本系列的第一部分，我们已经学了-Xint和-Xcomp参数。结合使用-XX:+PrintCompilation和-XX:+CITime，在这两个情况下（校对者注，客户端VM与服务端VM），我们能对JIT编译器的行为有更好的了解。使用-Xint，-XX:+PrintCompilation在这两种情况下会产生0行输出。同样的，使用-XX:+CITime时，证实在编译上没有花费时间。现在换用-Xcomp，输出就完全不同了。在使用客户端VM时会产生726行输出，然后没有更多的，这是因为每个相关的方法都被编译了。使用服务端VM，我们甚至能得到993行输出，这告诉我们更积极的优化被执行了。同样，JVM 拆机(JVM teardown)时打印出的统计显示了两个VM的巨大不同。考虑服务端VM的运行：

```
$ java -server -Xcomp -XX:+CITime Benchmark
[...]
Accumulated compiler times (for compiled methods only)
------------------------------------------------
  Total compilation time   :  1.567 s
    Standard compilation   :  1.567 s, Average : 0.002
    On stack replacement   :  0.000 s, Average : -1.#IO
[...]
```

使用-Xcomp编译用了1.567s，这是使用默认设置（即，混合模式）的10倍。同样，应用程序的运行速度要比用混合模式的慢。相比较之下，客户端VM使用-Xcomp编译726个方法只用了0.208s，甚至低于使用-Xcomp的服务端VM。

补充一点，这里没有”on stack replacement”发生，因为每一个方法在第一次调用时被编译了。损坏的输出“Average: -1.#IO”（正确的是:0）再一次表明了，非标准化的输出参数不是非常可靠。

-XX:+UnlockExperimentalVMOptions

有些时候当设置一个特定的JVM参数时，JVM会在输出“Unrecognized VM option”后终止。如果发生了这种情况，你应该首先检查你是否输错了参数。然而，如果参数输入是正确的，并且JVM并不识别，你或许需要设置-XX:+UnlockExperimentalVMOptions 来解锁参数。我不是非常清楚这个安全机制的作用，但我猜想这个参数如果不正确使用可能会对JVM的稳定性有影响（例如，他们可能会过多的写入debug输出的一些日志文件）。

有一些参数只是在JVM开发时用，并不实际用于Java应用。如果一个参数不能被 -XX:+UnlockExperimentalVMOptions 开启，但是你真的需要使用它，此时你可以尝试使用debug版本的JVM。对于Java 6 HotSpot JVM你可以从[这里找到](https://jdk6.java.net/download.html)。

-XX:+LogCompilation and -XX:+PrintOptoAssembly

如果你在一个场景中发现使用 -XX:+PrintCompilation，不能够给你足够详细的信息，你可以使用 -XX:+LogCompilation把扩展的编译输出写到“hotspot.log”文件中。除了编译方法的很多细节之外，你也可以看到编译器线程启动的任务。注意-XX:+LogCompilation 需要使用-XX:+UnlockExperimentalVMOptions来解锁。

JVM甚至允许我们看到从字节码编译生成到本地代码。使用-XX:+PrintOptoAssembly，由编译器线程生成的本地代码被输出并写到“hotspot.log”文件中。使用这个参数要求运行的服务端VM是debug版本。我们可以研究-XX:+PrintOptoAssembly的输出，以至于了解JVM实际执行什么样的优化，例如，关于死代码的消除。一个非常有趣的文章提供了一个[例子](https://weblogs.java.net/blog/2008/03/30/deep-dive-assembly-code-java)。