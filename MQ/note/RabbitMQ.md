摘自：`https://www.cnblogs.com/nzxj/p/10470453.html`



# 使用场景有哪些？
单反单收，单发多收，发布订阅，按路由发送，按主题发送

# 有哪些重要的角色？

Server,Consumer,Producer

# 有哪些重要的组件？


1.Server(broker): 接受客户端连接，实现AMQP消息队列和路由功能的进程。

2.Virtual Host:其实是一个虚拟概念，类似于权限控制组，一个Virtual Host里面可以有若干个Exchange和Queue，但是权限控制的最小粒度是Virtual Host

3.Exchange:接受生产者发送的消息，并根据Binding规则将消息路由给服务器中的队列。ExchangeType决定了Exchange路由消息的行为，例如，在RabbitMQ中，ExchangeType有direct、Fanout和Topic三种，不同类型的Exchange路由的行为是不一样的。

4.Message Queue：消息队列，用于存储还未被消费者消费的消息。

5.Message: 由Header和Body组成，Header是由生产者添加的各种属性的集合，包括Message是否被持久化、由哪个Message Queue接受、优先级是多少等。而Body是真正需要传输的APP数据。

6.Binding:Binding联系了Exchange与Message Queue。Exchange在与多个Message Queue发生Binding后会生成一张路由表，路由表中存储着Message Queue所需消息的限制条件即Binding Key。当Exchange收到Message时会解析其Header得到Routing Key，Exchange根据Routing Key与Exchange Type将Message路由到Message Queue。Binding Key由Consumer在Binding Exchange与Message Queue时指定，而Routing Key由Producer发送Message时指定，两者的匹配方式由Exchange Type决定。 

7.Connection:连接，对于RabbitMQ而言，其实就是一个位于客户端和Broker之间的TCP连接。

8.Channel:信道，仅仅创建了客户端到Broker之间的连接后，客户端还是不能发送消息的。需要为每一个Connection创建Channel，AMQP协议规定只有通过Channel才能执行AMQP的命令。一个Connection可以包含多个Channel。之所以需要Channel，是因为TCP连接的建立和释放都是十分昂贵的，如果一个客户端每一个线程都需要与Broker交互，如果每一个线程都建立一个TCP连接，暂且不考虑TCP连接是否浪费，就算操作系统也无法承受每秒建立如此多的TCP连接。RabbitMQ建议客户端线程之间不要共用Channel，至少要保证共用Channel的线程发送消息必须是串行的，但是建议尽量共用Connection。

9.Command:AMQP的命令，客户端通过Command完成与AMQP服务器的交互来实现自身的逻辑。例如在RabbitMQ中，客户端可以通过publish命令发送消息，txSelect开启一个事务，txCommit提交一个事务。
复制代码

# vhost 的作用是什么？

虚拟主机，一个broker里可以开设多个vhost，用作不同用户的权限分离。



# 消息是怎么发送的？

（1）客户端连接到消息队列服务器，打开一个channel。
（2）客户端声明一个exchange，并设置相关属性。
（3）客户端声明一个queue，并设置相关属性。
（4）客户端使用routing key，在exchange和queue之间建立好绑定关系。
（5）客户端投递消息到exchange。

# 怎么保证消息的稳定性？

费者在消费完消息后发送一个回执给RabbitMQ，RabbitMQ收到消息回执（Message acknowledgment）后才将该消息从Queue中移除；如果RabbitMQ没有收到回执并检测到消费者的RabbitMQ连接断开，则RabbitMQ会将该消息发送给其他消费者（如果存在多个消费者）进行处理。这里不存在timeout概念，一个消费者处理消息时间再长也不会导致该消息被发送给其他消费者，除非它的RabbitMQ连接断开。



# 怎么避免消息丢失？

将Queue与Message都设置为可持久化的（durable），这样可以保证绝大部分情况下我们的RabbitMQ消息不会丢失。但依然解决不了小概率丢失事件的发生（比如RabbitMQ服务器已经接收到生产者的消息，但还没来得及持久化该消息时RabbitMQ服务器就断电了），如果我们需要对这种小概率事件也要管理起来，那么我们要用到事务。



# 要保证消息持久化成功的条件有哪些？

queue，exchange和Message都持久化；

引入RabbitMQ的mirrored-queue即镜像队列，这个相当于配置了副本，当master在此特殊时间内crash掉，可以自动切换到slave，这样有效的保障了HA,；

要在producer引入事务机制或者Confirm机制来确保消息已经正确的发送至broker端

# 持久化有什么缺点？

一、如果消息的自动确认为true，那么在消息被接收以后，RabbitMQ就会删除该消息，假如消费端此时宕机，那么消息就会丢失。因此需要将消息设置为手动确认。
二、设置手动确认会出现另一个问题，如果消息已被成功处理，但在消息确认过程中出现问题，那么在消费端重启后，消息会重新被消费。
三、发送端为了保证消息会成功投递，一般会设定重试。如果消息发送至RabbitMQ之后，在RabbitMQ回复已成功接收消息过程中出现异常，那么发送端会重新发送该消息，从而造成消息重复发送。
四、RabbitMQ的消息磁盘写入，如果出现问题，也会造成消息丢失。

# 几种广播类型

fanout: 所有bind到此exchange的queue都可以接收消息（纯广播，绑定到RabbitMQ的接受者都能收到消息）；
direct: 通过routingKey和exchange决定的那个唯一的queue可以接收消息；
topic:所有符合routingKey(此时可以是一个表达式)的routingKey所bind的queue可以接收消息；

# 怎么实现延迟消息队列？

第一步：给队列或者指定消息设置过期时间（TTL），过期后变成 死信

第二部：设置死信的转发规则（如果没有任何规则，则直接丢弃死信） ，从新消费

# 集群有什么用？　

允许消费者和生产者在Rabbit节点崩溃的情况下继续运行；
通过增加节点来扩展Rabbit处理更多的消息，承载更多的业务量；

# 节点的类型有哪些？

内存节点、磁盘节点。顾名思义内存节点就是将所有数据放在内存，磁盘节点将数据放在磁盘



# 集群搭建需要注意哪些问题？

每个节点Cookie的同步；主机之间 必须可以相互识别并可达，/etc/hosts文件配置必须准确



# 每个节点是其他节点的完整拷贝吗？为什么？

不是，队列的完整信息只放在一个节点，其他节点存放的是该队列的指针



# 集群中唯一一个磁盘节点崩溃了会发生什么情况？

如果唯一的磁盘节点崩溃，集群是可以保持运行的，但不能更改任何东西。

不能创建队列
不能创建交换器
不能创建绑定
不能添加用户
不能更改权限
不能添加和删除集群几点



# rabbitmq 对集群节点停止顺序有要求吗？

启动顺序：磁盘节点 => 内存节点
关闭顺序：内存节点 => 磁盘节点