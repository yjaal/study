摘自：`https://zhuanlan.zhihu.com/p/71258711`



# 什么是Apache Kafka?
答：Apache Kafka是一个发布 - 订阅开源消息代理应用程序。这个消息传递应用程序是用“scala”编码的。基本上，这个项目是由Apache软件启动的。Kafka的设计模式主要基于事务日志设计。

# Kafka中有哪几个组件?
答：Kafka最重要的元素是：
主题：Kafka主题是一堆或一组消息。
生产者：在Kafka，生产者发布通信以及向Kafka主题发布消息。
消费者：Kafka消费者订阅了一个主题，并且还从主题中读取和处理消息。
经纪人：在管理主题中的消息存储时，我们使用Kafka Brokers。

# 解释偏移的作用。
答：给分区中的消息提供了一个顺序ID号，我们称之为偏移量。因此，为了唯一地识别分区中的每条消息，我们使用这些偏移量。

# 什么是消费者组？
答：消费者组的概念是Apache Kafka独有的。基本上，每个Kafka消费群体都由一个或多个共同消费一组订阅主题的消费者组成。

# ZooKeeper在Kafka中的作用是什么？
答：Apache Kafka是一个使用Zookeeper构建的分布式系统。虽然，Zookeeper的主要作用是在集群中的不同节点之间建立协调。但是，如果任何节点失败，我们还使用Zookeeper从先前提交的偏移量中恢复，因为它做周期性提交偏移量工作。

# 没有ZooKeeper可以使用Kafka吗？
答：绕过Zookeeper并直接连接到Kafka服务器是不可能的，所以答案是否定的。如果以某种方式，使ZooKeeper关闭，则无法为任何客户端请求提供服务。

# kafka的优点

答：Kafka有一些优点，因此使用起来很重要：
高吞吐量：我们在Kafka中不需要任何大型硬件，因为它能够处理高速和大容量数据。此外，它还可以支持每秒数千条消息的消息吞吐量。
低延迟：Kafka可以轻松处理这些消息，具有毫秒级的极低延迟，这是大多数新用例所要求的。
容错：Kafka能够抵抗集群中的节点/机器故障。
耐久性：由于Kafka支持消息复制，因此消息永远不会丢失。这是耐久性背后的原因之一。
可扩展性：卡夫卡可以扩展，而不需要通过添加额外的节点而在运行中造成任何停机。

# Kafka的主要API有哪些？
答：Apache Kafka有4个主要API：
生产者API
消费者API
流 API
连接器API

# 什么是消费者或用户？
答：Kafka消费者订阅一个主题，并读取和处理来自该主题的消息。此外，有了消费者组的名字，消费者就给自己贴上了标签。换句话说，在每个订阅使用者组中，发布到主题的每个记录都传递到一个使用者实例。确保使用者实例可能位于单独的进程或单独的计算机上。

# 解释领导者和追随者的概念。
答：在Kafka的每个分区中，都有一个服务器充当领导者，0到多个服务器充当追随者的角色。

在Kafka早期版本，对于分区和副本的状态的管理依赖于zookeeper的Watcher和队列：每一个broker都会在zookeeper注册Watcher，所以zookeeper就会出现大量的Watcher, 如果宕机的broker上的partition很多比较多，会造成多个Watcher触发，造成集群内大规模调整；每一个replica都要去再次zookeeper上注册监视器，当集群规模很大的时候，zookeeper负担很重。这种设计很容易出现脑裂和羊群效应以及zookeeper集群过载。

新的版本中该变了这种设计，使用KafkaController，只有KafkaController，Leader会向zookeeper上注册Watcher，其他broker几乎不用监听zookeeper的状态变化。

Kafka集群中多个broker，有一个会被选举为controller leader，负责管理整个集群中分区和副本的状态，比如partition的leader 副本故障，由controller 负责为该partition重新选举新的leader 副本；当检测到ISR列表发生变化，有controller通知集群中所有broker更新其MetadataCache信息；或者增加某个topic分区的时候也会由controller管理分区的重新分配工作

# Kafka集群Leader选举原理
我们知道Zookeeper集群中也有选举机制，是通过Paxos算法，通过不同节点向其他节点发送信息来投票选举出leader，但是Kafka的leader的选举就没有这么复杂了。 
Kafka的Leader选举是通过在zookeeper上创建/controller临时节点来实现leader选举，并在该节点中写入当前broker的信息 
{“version”:1,”brokerid”:1,”timestamp”:”1512018424988”} 
利用Zookeeper的强一致性特性，一个节点只能被一个客户端创建成功，创建成功的broker即为leader，即先到先得原则，leader也就是集群中的controller，负责集群中所有大小事务。 
当leader和zookeeper失去连接时，临时节点会删除，而其他broker会监听该节点的变化，当节点删除时，其他broker会收到事件通知，重新发起leader选举。



# 是什么确保了Kafka中服务器的负载平衡？
答：由于领导者的主要角色是执行分区的所有读写请求的任务，而追随者被动地复制领导者。因此，在领导者失败时，其中一个追随者接管了领导者的角色。基本上，整个过程可确保服务器的负载平衡。其实就是kafka的分配策略，在分配策略中使用了相关的算法来保证负载平衡。

# 副本和ISR扮演什么角色？
答：基本上，复制日志的节点列表就是副本。特别是对于特定的分区。但是，无论他们是否扮演领导者的角色，他们都是如此。此外，ISR指的是同步副本。在定义ISR时，它是一组与领导者同步的消息副本。

# 为什么Kafka的复制至关重要？
答：由于复制，我们可以确保发布的消息不会丢失，并且可以在发生任何机器错误、程序错误或频繁的软件升级时使用。

# 如果副本长时间不在ISR中，这意味着什么？
答：简单地说，这意味着跟随者不能像领导者收集数据那样快速地获取数据。

# 启动Kafka服务器的过程是什么？
答：初始化ZooKeeper服务器是非常重要的一步，因为Kafka使用ZooKeeper，所以启动Kafka服务器的过程是：
要启动ZooKeeper服务器：>bin/zooKeeper-server-start.sh config/zooKeeper.properties
接下来，启动Kafka服务器：>bin/kafka-server-start.sh config/server.properties

# 在生产者中，何时发生QueueFullException？
答：每当Kafka生产者试图以代理的身份在当时无法处理的速度发送消息时，通常都会发生QueueFullException。但是，为了协作处理增加的负载，用户需要添加足够的代理，因为生产者不会阻止。



# Kafka和Flume之间的主要区别是什么？
答：Kafka和Flume之间的主要区别是：
工具类型
Apache Kafka——Kafka是面向多个生产商和消费者的通用工具。
Apache Flume——Flume被认为是特定应用程序的专用工具。

复制功能
Apache Kafka——Kafka可以复制事件。
Apache Flume——Flume不复制事件。

# Apache Kafka是分布式流处理平台吗？如果是，你能用它做什么？
答：毫无疑问，Kafka是一个流处理平台。它可以帮助：
1.轻松推送记录
2.可以存储大量记录，而不会出现任何存储问题
3.它还可以在记录进入时对其进行处理。


# 你能用Kafka做什么？
答：它可以以多种方式执行，例如：
为了在两个系统之间传输数据，我们可以用它构建实时的数据流管道。
另外，我们可以用Kafka构建一个实时流处理平台，它可以对数据快速做出反应。

# 在Kafka集群中保留期的目的是什么？
答：保留期限保留了Kafka群集中的所有已发布记录。它不会检查它们是否已被消耗。此外，可以通过使用保留期的配置设置来丢弃记录。而且，它可以释放一些空间。

# 解释Kafka可以接收的消息最大为多少？
答：Kafka可以接收的最大消息大小约为1000000字节。

# 传统的消息传递方法有哪些类型？
答：基本上，传统的消息传递方法有两种，如：
排队：这是一种消费者池可以从服务器读取消息并且每条消息转到其中一个消息的方法。
发布-订阅：在发布-订阅中，消息被广播给所有消费者。

# ISR在Kafka环境中代表什么？
答：ISR指的是同步副本。这些通常被分类为一组消息副本，它们被同步为领导者。

# 什么是Kafka中的地域复制？
答：对于我们的集群，Kafka MirrorMaker提供地理复制。基本上，消息是通过MirrorMaker跨多个数据中心或云区域复制的。因此，它可以在主动/被动场景中用于备份和恢复；也可以将数据放在离用户更近的位置，或者支持数据位置要求。

# 解释多租户是什么？
答：我们可以轻松地将Kafka部署为多租户解决方案。但是，通过配置主题可以生成或使用数据，可以启用多租户。此外，它还为配额提供操作支持。



# 解释流API的作用？
答：一种允许应用程序充当流处理器的API，它还使用一个或多个主题的输入流，并生成一个输出流到一个或多个输出主题，此外，有效地将输入流转换为输出流，我们称之为流API。

# 连接器API的作用是什么？
答：一个允许运行和构建可重用的生产者或消费者的API，将Kafka主题连接到现有的应用程序或数据系统，我们称之为连接器API。



# 比较RabbitMQ与Apache Kafka
答：Apache Kafka的另一个选择是RabbitMQ。那么，让我们比较两者：

功能
Apache Kafka– Kafka是分布式的、持久的和高度可用的，这里共享和复制数据
RabbitMQ中没有此类功能
性能速度
Apache Kafka–达到每秒100000条消息。
RabbitMQ–每秒20000条消息。

# 比较传统队列系统与Apache Kafka
答：让我们比较一下传统队列系统与Apache Kafka的功能：

消息保留
传统的队列系统 - 它通常从队列末尾处理完成后删除消息。
Apache Kafka中，消息即使在处理后仍然存在。这意味着Kafka中的消息不会因消费者收到消息而被删除。

基于逻辑的处理
传统队列系统不允许基于类似消息或事件处理逻辑。
Apache Kafka允许基于类似消息或事件处理逻辑。



# 解释术语“Log Anatomy”
答：我们将日志视为分区。基本上，数据源将消息写入日志。其优点之一是，在任何时候，都有一个或多个消费者从他们选择的日志中读取数据。下面的图表显示，数据源正在写入一个日志，而用户正在以不同的偏移量读取该日志。

# Kafka中的数据日志是什么？
答：我们知道，在Kafka中，消息会保留相当长的时间。此外，消费者还可以根据自己的方便进行阅读。尽管如此，有一种可能的情况是，如果将Kafka配置为将消息保留24小时，并且消费者可能停机超过24小时，则消费者可能会丢失这些消息。但是，我们仍然可以从上次已知的偏移中读取这些消息，但仅限于消费者的部分停机时间仅为60分钟的情况。此外，关于消费者从一个话题中读到什么，Kafka不会保持状态。

# 解释如何调整Kafka以获得最佳性能。
答：因此，调优Apache Kafka的方法是调优它的几个组件：

调整Kafka生产者
Kafka代理调优
调整Kafka消费者

# Apache Kafka的缺陷
答：Kafka的局限性是：

没有完整的监控工具集
消息调整的问题
不支持通配符主题选择
速度问题



# 优劣势总结

kafka的特点其实很明显，就是仅仅提供较少的核心功能，但是提供较高的吞吐量，ms级别的延迟，较高的可用性以及可靠性，而且是分布式的，可以任意的扩展，同时kafka也是做好的是支撑少量的topic数量即可，保证其吞吐量，而且kafka唯一的一点劣势就是可能出现就消息的重复消费，那么对数据准确性会产生影响，在大数据领域中以及日志收集中，这点轻微可以忽略。kafka的特性就是天然适合大数据实时计算以及日志的收集。

Kafka天生就是一个分布式的消息队列，它可以由多个broker组成，每个broker是一个节点；你创建一个topic，这个topic可以划分为多个partition，每个partition可以存在于不同的broker上，每个partition就放一部分数据。



# 列出所有Apache Kafka业务
答：Apache Kafka的业务包括：

添加和删除Kafka主题
如何修改Kafka主题
如何关机
在Kafka集群之间镜像数据
找到消费者的位置
扩展您的Kafka群集
自动迁移数据
退出服务器
数据中心

# 解释Apache Kafka用例？
答：Apache Kafka有很多用例，例如：

Kafka指标
可以使用Kafka进行操作监测数据。此外，为了生成操作数据的集中提要，它涉及到从分布式应用程序聚合统计信息。
Kafka日志聚合
从组织中的多个服务收集日志。

流处理
在流处理过程中，Kafka的强耐久性非常有用。



# Kafka流的特点。
答：Kafka流的一些最佳功能是

Kafka Streams具有高度可扩展性和容错性。
Kafka部署到容器，VM，裸机，云。
我们可以说，Kafka流对于小型，中型和大型用例同样可行。
此外，它完全与Kafka安全集成。
编写标准Java应用程序。
完全一次处理语义。
而且，不需要单独的处理集群。

# Kafka的流处理是什么意思？
答：连续、实时、并发和以逐记录方式处理数据的类型，我们称之为Kafka流处理。

# 系统工具有哪些类型？
答：系统工具有三种类型：

Kafka迁移工具：它有助于将代理从一个版本迁移到另一个版本。
Mirror Maker：Mirror Maker工具有助于将一个Kafka集群的镜像提供给另一个。
消费者检查:对于指定的主题集和消费者组，它显示主题，分区，所有者。

# 什么是复制工具及其类型？
答：为了增强持久性和更高的可用性，这里提供了复制工具。其类型为

创建主题工具
列表主题工具
添加分区工具



# 说明Kafka的一个最佳特征。
答：Kafka的最佳特性是“各种各样的用例”。
这意味着Kafka能够管理各种各样的用例，这些用例对于数据湖来说非常常见。例如日志聚合、Web活动跟踪等。

# 副本因子replication-factor

答：假设我们有3个kafka broker分别brokerA、brokerB、brokerC.

* 当我们创建的topic有3个分区partition时并且replication-factor为1，基本上一个broker上一个分区。当一个broker宕机了，该topic就无法使用了，因为三个分区只有两个能用，
* 当我们创建的topic有3个分区partition时并且replication-factor为2时，可能分区数据分布情况是
    brokerA， partiton0，partiton1，
    brokerB， partiton1，partiton2
    brokerC， partiton2，partiton0，
    每个分区有一个副本，当其中一个broker宕机了，kafka集群还能完整凑出该topic的三个分区，例如当brokerA宕机了，可以通过brokerB和brokerC组合出topic的三个分区。



# Kafka提供的保证是什么？
答：他们是

生产者向特定主题分区发送的消息的顺序相同。
此外，消费者实例按照它们存储在日志中的顺序查看记录。
此外，即使不丢失任何提交给日志的记录，我们也可以容忍最多N-1个服务器故障。



# Kafka 消息保留机制

* log.retention.ms 消息时间
    Kafka通常根据时间决定数据可以保留多久。默认使用log.retention.hours参数配置时间，默认值是168小时，也就是一周。除此之外，还有其他两个参数，log.retention.minutes和log.retention.ms，这三个参数作用是一样的，都是决定消息多久以会被删除，不过还是推荐使用log.retention.ms，如果指定了不止一个参数，Kafka会优先使用最小值的那个参数。
* log.retention.bytes 消息大小
    通过保留的消息字节数来判断小是否过期，它的值通过参数log.retention.bytes来指定，作用在每一个分区上，也就是说如果一个包含8个分区的主题，并且log.retention.bytes被设置为1GB，那么这个主题最多可以保留8GB的数据，所以，当主题的分区个数增加时，整个主题可以保留的数据也随之增加。
    如果同时指定了两个参数没只要任意一个参数得到满足，消息就会被删除。例如，假设log.retention.ms为86400000（也就是一天），log.retention.bytes的值设置为1GB，如果消息字节总数在不到一天的时间就超过了1GB，那么堆出来的部分就会被删除，相反，如果消息字节总数小与1GB，那么一天之后这些消息也会被删除，尽管分区的数据总量小于1GB

* log.segment.bytes 日志片段大小
    当消息来到broker是，它们就会被追加到分区的当前日志片段上，当日志片段大小到达log.segment.bytes指定的上限（默认是1GB）时，当前日志片段就会被关闭，一个新的日志片段就会被打开。如果一个日志之片段被关闭，就开始等待过期时间。这个参数的值越小们就会越频繁的关闭和分配新文件，从而降低了磁盘写入的整体效率。
* log.segment.ms 日志片段时间
    指定了多长时间之后日志片段会被关闭，就像log.retention.bytes和log.retention.ms这两个参数一样。log.segment.bytes和log.segment.ms这两个参数之间也不存在互斥问题。日志片段会在大小或时间达到上限时被关闭，就看哪个条件晓得到满足。默认情况下log.segment.ms没有设定值，所以只根据大小来关闭日志片段
* message.max.bytes 单条消息大小
* broker通过设置message.max.bytes参数来限制单个消息的大小，默认值时1000000，也就是1MB。如果生产者尝试发送的消息超过1MB，不仅消息不会被接受，还会受到broker返回的错误信息。跟其他与字节相关的配置参数一样，该参数指的是压缩后的消息大小，也就是说，只要压缩后的消息小于message.max.bytes指定的值，消息的实际大小可以远大于这个值。



# zk扮演的角色

1、存储元数据信息：包括consumerGroup/consumer、broker、Topic等；

2、0.8版本kafka支partition级别的replication，所以Kafka负责选出一个Broker节点作为controller来在一个partiiton内副本间进行Leader选举，维护出一个ISR；

3、目前，没了zk，kafka启动都启不起来。

