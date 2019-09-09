参考：`https://juejin.im/entry/5b3af5fd518825621d57791a`

## 默认使用的是什么通信框架，还有别的选择吗

答：默认也推荐使用netty框架，还有mina。

## 服务调用是阻塞的吗

答：默认是阻塞的，可以异步调用，没有返回值的可以这么做。

## 一般使用什么注册中心？还有别的选择吗

答：推荐使用zookeeper注册中心，还有redis等不推荐。

## 默认使用什么序列化框架，你知道的还有哪些

答：默认使用Hessian序列化，还有Duddo、FastJson、Java自带序列化。

## 服务提供者能实现失效踢出是什么原理

答：服务失效踢出基于zookeeper的临时节点原理。

## 服务上线怎么不影响旧版本

答：采用多版本开发，不影响旧版本。

## 如何解决服务调用链过长的问题

答：可以结合zipkin实现分布式服务追踪。

## 说说核心的配置有哪些

答：

核心配置有

- dubbo:service/
- dubbo:reference/
- dubbo:protocol/
- dubbo:registry/
- dubbo:application/
- dubbo:provider/
- dubbo:consumer/
- dubbo:method/

## dubbo推荐用什么协议

dubbo://（推荐）
rmi://
hessian://
http://
webservice://
thrift://
memcached://
redis://
rest://


## 同一个服务多个注册的情况下可以直连某一个服务吗

答：可以直连，修改配置即可，也可以通过telnet直接某个服务。

## dubbo在安全机制方面如何解决的

dubbo通过token令牌防止用户绕过注册中心直连，然后在注册中心管理授权，dubbo提供了黑白名单，控制服务所允许的调用方。

## 集群容错怎么做

答：读操作建议使用Failover失败自动切换，默认重试两次其他服务器。写操作建议使用Failfast快速失败，发一次调用失败就立即报错。

## 在使用过程中都遇到了些什么问题？ 如何解决

1.同时配置了XML和properties文件，则properties中的配置无效

只有XML没有配置时，properties才生效。

2.dubbo缺省会在启动时检查依赖是否可用，不可用就抛出异常，阻止spring初始化完成，check属性默认为true。

测试时有些服务不关心或者出现了循环依赖，将check设置为false

3.为了方便开发测试，线下有一个所有服务可用的注册中心，这时，如果有一个正在开发中的服务提供者注册，可能会影响消费者不能正常运行。

解决：让服务提供者开发方，只订阅服务，而不注册正在开发的服务，通过直连测试正在开发的服务。设置dubbo:registry标签的register属性为false。

4.spring 2.x初始化死锁问题。

在spring解析到dubbo:service时，就已经向外暴露了服务，而spring还在接着初始化其他bean，如果这时有请求进来，并且服务的实现类里有调用applicationContext.getBean()的用法。getBean线程和spring初始化线程的锁的顺序不一样，导致了线程死锁，不能提供服务，启动不了。

解决：不要在服务的实现类中使用applicationContext.getBean();如果不想依赖配置顺序，可以将dubbo:provider的deplay属性设置为-1，使dubbo在容器初始化完成后再暴露服务。

5.服务注册不上

检查dubbo的jar包有没有在classpath中，以及有没有重复的jar包

检查暴露服务的spring配置有没有加载

在服务提供者机器上测试与注册中心的网络是否通

6.出现RpcException: No provider available for remote service异常

表示没有可用的服务提供者，

1). 检查连接的注册中心是否正确

2). 到注册中心查看相应的服务提供者是否存在

3). 检查服务提供者是否正常运行

7.出现”消息发送失败”异常

通常是接口方法的传入传出参数未实现Serializable接口。

## dubbo和dubbox之间的区别

答：dubbox是当当网基于dubbo上做了一些扩展，如加了服务可restful调用，更新了开源组件等。

## 你还了解别的分布式框架吗

答：别的还有spring的spring cloud，facebook的thrift，twitter的finagle等。

## Dubbo支持哪些协议，每种协议的应用场景，优缺点

1. dubbo： 单一长连接和NIO异步通讯，适合大并发小数据量的服务调用，以及消费者远大于提供者。传输协议TCP，异步，Hessian序列化；
2. rmi： 采用JDK标准的rmi协议实现，传输参数和返回参数对象需要实现Serializable接口，使用java标准序列化机制，使用阻塞式短连接，传输数据包大小混合，消费者和提供者个数差不多，可传文件，传输协议TCP。 多个短连接，TCP协议传输，同步传输，适用常规的远程服务调用和rmi互操作。在依赖低版本的Common-Collections包，java序列化存在安全漏洞；
3. webservice： 基于WebService的远程调用协议，集成CXF实现，提供和原生WebService的互操作。多个短连接，基于HTTP传输，同步传输，适用系统集成和跨语言调用；http： 基于Http表单提交的远程调用协议，使用Spring的HttpInvoke实现。多个短连接，传输协议HTTP，传入参数大小混合，提供者个数多于消费者，需要给应用程序和浏览器JS调用；
4. hessian： 集成Hessian服务，基于HTTP通讯，采用Servlet暴露服务，Dubbo内嵌Jetty作为服务器时默认实现，提供与Hession服务互操作。多个短连接，同步HTTP传输，Hessian序列化，传入参数较大，提供者大于消费者，提供者压力较大，可传文件；
5. memcache： 基于memcached实现的RPC协议
6. redis： 基于redis实现的RPC协议

## Dubbo集群的负载均衡有哪些策略　　

1. Dubbo提供了常见的集群策略实现，并预扩展点予以自行实现。
2. Random LoadBalance: 随机选取提供者策略，有利于动态调整提供者权重。截面碰撞率高，调用次数越多，分布越均匀；
3. RoundRobin LoadBalance: 轮循选取提供者策略，平均分布，但是存在请求累积的问题；
4. LeastActive LoadBalance: 最少活跃调用策略，解决慢提供者接收更少的请求；
5. ConstantHash LoadBalance: 一致性Hash策略，使相同参数请求总是发到同一提供者，一台机器宕机，可以基于虚拟节点，分摊至其他提供者，避免引起提供者的剧烈变动；