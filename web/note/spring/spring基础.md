# Spring

### 1.什么是`spring`框架？主要有哪些模块？

一个`JavaEE`的轻量级开发框架。主要有

`Core Container`模块，包含`spring-beans、spring-core、spring-context、spring-expression`四个方面。

* `spring-core`和`spring-beans`：提供了框架的基础部分，包括反转控制和依赖注入功能。其中`Bean Factory`是容器核心，本质是“工厂设计模式”的实现，而且无需编程实现“单例设计模式”，单例完全由容器控制
* `spring-context`：这个模块建立在`core`和`bean`模块提供坚实的基础上，集成`Beans`模块功能并添加资源绑定、数据验证、国际化、`Java` EE支持、容器生命周期、事件传播等；核心接口是`ApplicationContext`。
* `spring-expression`：提供强大的表达式语言支持

`AOP and Instrumentation`模块，包含`spring-aop、spring-instrument`两个方面。

* `spring-aop`：`Spring AOP`模块提供了符合` AOP Alliance`规范的面向方面的编程（`aspect-oriented programming`）实现，提供比如日志记录、权限控制、性能统计等通用功能和业务逻辑分离的技术，并且能动态的把这些功能添加到需要的代码中；这样各专其职，降低业务逻辑和通用功能的耦合。
* `spring-instrument`：在特定的应用程序服务器中支持类和类加载器的实现，比如`Tomcat`。

`Messaging`，从`Spring  Framework  4`开始集成了`MessageChannel, MessageHandler`等，用于消息传递的基础。

`Data Access/Integration`，包括了`JDBC、ORM、OXM、JMS`和事务管理。

* 事务模块：该模块用于`Spring`管理事务，只要是`Spring`管理对象都能得到`Spring管理事务的好处，无需在代码中进行事务控制了，而且支持编程和声明性的事物管理。
* `spring-jdbc`： 提供了一个`JBDC`的样例模板，使用这些模板能消除传统冗长的`JDBC`编码还有必须的事务控制，而且能享受到`Spring`管理事务的好处。
* `spring-orm`： 提供与流行的“对象-关系”映射框架的无缝集成，包括`Hibernate、JPA、Ibatiss`等。而且可以使用`Spring`事务管理，无需额外控制事务。
* `spring-oxm`： 提供了一个对`Object/XML`映射实现，将`java`对象映射成`XML`数据，或者将`XML`数据映射成`java`对象，`Object/XML`映射实现包括`JAXB、Castor、XMLBeans`和`XStream`。
* `spring-jms`： 用于`JMS(Java Messaging Service)`，提供一套 “消息生产者、消息消费者”模板用于更加简单的使用`JMS`，`JMS`用于用于在两个应用程序之间，或分布式系统中发送消息，进行异步通信。

`Web`，包含了`spring-web, spring-webmvc, spring-websocket, and spring-webmvc-portlet`几个模块。
* `spring-web` 提供了基础的web功能。例如多文件上传、集成`IoC`容器、远程过程访问（`RMI、Hessian、Burlap`）以及`Web Service`支持，并提供一个`RestTemplate`类来提供方便的`Restful services`访问。
* `spring-webmvc`： 提供了一个`Spring MVC Web`框架和`REST Web`服务的实现。`Spring`的`MVC`框架提供了领域模型代码和`Web`表单之间分离，并与`Spring`框架的所有其他功能集成。
* `spring-webmvc-portlet`： 提供了在`Portlet`环境中使用`MVC`实现，并且反映了`spring-webmvc`模块的功能。

### 2.使用`spring`能带来哪些好处？

* 非常轻量级的容器：以集中的、自动化的方式进行应用程序对象创建和装配，负责对象创建和装配，管理对象生命周期，能组合成复杂的应用程序。`Spring`容器是非侵入式的（不需要依赖任何`Spring`特定类），而且完全采用`POJOs`进行开发，使应用程序更容易测试、更容易管理。而且核心`JAR`包非常小，`Spring3.0.5`不到`1M`，而且不需要依赖任何应用服务器，可以部署在任何环境（`Java SE`或`Java EE`）。
* `AOP`：`AOP`是`Aspect Oriented Programming`的缩写，意思是面向切面编程，提供从另一个角度来考虑程序结构以完善面向对象编程（相对于`OOP`），即可以通过在编译期间、装载期间或运行期间实现在不修改源代码的情况下给程序动态添加功能的一种技术。通俗点说就是把可重用的功能提取出来，然后将这些通用功能在合适的时候织入到应用程序中；比如安全，日记记录，这些都是通用的功能，我们可以把它们提取出来，然后在程序执行的合适地方织入这些代码并执行它们，从而完成需要的功能并复用了这些功能。
* 简单的数据库事务管理：在使用数据库的应用程序当中，自己管理数据库事务是一项很让人头疼的事，而且很容易出现错误，`Spring`支持可插入的事务管理支持，而且无需`JEE`环境支持，通过`Spring`管理事务可以把我们从事务管理中解放出来来专注业务逻辑。
* `JDBC`抽象及`ORM`框架支持：`Spring使JDBC`更加容易使用；提供`DAO`（数据访问对象）支持，非常方便集成第三方`ORM`框架，比如`Hibernate`等；并且完全支持`Spring`事务和使用`Spring`提供的一致的异常体系。
* 灵活的`Web`层支持：`Spring`本身提供一套非常强大的`MVC`框架，而且可以非常容易的与第三方`MVC`框架集成，比如`Struts`等。
* 简化各种技术集成：提供对`Java Mail`、任务调度、`JMX、JMS、JNDI、EJB`、动态语言、远程访问、`Web Service`等的集成。



### 3.`spring bean`的各种作用域之间有什么区别？

| 作用域           | 描述                                                         |
| ---------------- | ------------------------------------------------------------ |
| `singleton`      | 该作用域将` bean` 的定义的限制在每一个 `Spring IoC `容器中的一个单一实例(默认)。 |
| `prototype`      | 该作用域将单一 `bean `的定义限制在任意数量的对象实例。       |
| `request`        | 该作用域将` bean `的定义限制为` HTTP `请求。只在` web-aware Spring ApplicationContext` 的上下文中有效。 |
| `session`        | 该作用域将` bean `的定义限制为` HTTP `会话。 只在`web-aware Spring ApplicationContext`的上下文中有效。 |
| `global-session` | 该作用域将 `bean` 的定义限制为全局` HTTP `会话。只在` web-aware Spring ApplicationContext `的上下文中有效。 |



### 4.`spring`中单例`bean`是线程安全的吗？

参考：`https://www.cnblogs.com/doit8791/p/4093808.html`

`Spring`框架里的`bean`，或者说组件，获取实例的时候都是默认的单例模式，这是在多线程开发的时候要尤其注意的地方。单例模式的意思就是只有一个实例。单例模式确保某一个类只有一个实例，而且自行实例化并向整个系统提供这个实例。这个类称为单例类。

当多用户同时请求一个服务时，容器会给每一个请求分配一个线程，这是多个线程会并发执行该请求多对应的业务逻辑（成员方法），此时就要注意了，如果该处理逻辑中有对该单列状态的修改（体现为该单列的成员属性），则必须考虑线程同步问题。

同步机制的比较:`ThreadLocal`和线程同步机制相比有什么优势呢？`ThreadLocal`和线程同步机制都是为了解决多线程中相同变量的访问冲突问题。 

* 在同步机制中，通过对象的锁机制保证同一时间只有一个线程访问变量。这时该变量是多个线程共享的，使用同步机制要求程序慎密地分析什么时候对变量进行读写，什么时候需要锁定某个对象，什么时候释放对象锁等繁杂的问题，程序设计和编写难度相对较大。 

* 而`ThreadLocal`则从另一个角度来解决多线程的并发访问。`ThreadLocal`会为每一个线程提供一个**独立的变量副本**，从而隔离了多个线程对数据的访问冲突。因为每一个线程都拥有自己的变量副本，从而也就没有必要对该变量进行同步了。`ThreadLocal`提供了线程安全的共享对象，在编写多线程代码时，可以把不安全的变量封装进`ThreadLocal`。 

概括起来说，对于多线程资源共享的问题，同步机制采用了“以时间换空间”的方式，而`ThreadLocal`采用了“以空间换时间”的方式。前者仅提供一份变量，让不同的线程排队访问，而后者为每一个线程都提供了一份变量，因此可以同时访问而互不影响。 

**`Spring`使用`ThreadLocal`解决线程安全问题 **

我们知道在**一般情况下，只有无状态的`Bean`才可以在多线程环境下共享**，在`Spring`中，绝大部分`Bean`都可以声明为`singleton`作用域。就是因为`Spring`对一些`Bean`（如`RequestContextHolder、TransactionSynchronizationManager、LocaleContextHolder`等）中非线程安全状态采用`ThreadLocal`进行处理，让它们也成为线程安全的状态，因为有状态的`Bean`就可以在多线程中共享了。 

一般的`Web`应用划分为展现层、服务层和持久层三个层次，在不同的层中编写对应的逻辑，下层通过接口向上层开放功能调用。**在一般情况下，从接收请求到返回响应所经过的所有程序调用都同属于一个线程**

`ThreadLocal`是解决线程安全问题一个很好的思路，它通过为每个线程提供一个独立的变量副本解决了变量并发访问的冲突问题。在很多情况下，`ThreadLocal`比直接使用`synchronized`同步机制解决线程安全问题更简单，更方便，且结果程序拥有更高的并发性。 

如果你的代码所在的进程中有多个线程在同时运行，而这些线程可能会同时运行这段代码。如果每次运行结果和单线程运行的结果是一样的，而且其他的变量的值也和预期的是一样的，就是线程安全的。 或者说:一个类或者程序所提供的接口对于线程来说是原子操作或者多个线程之间的切换不会导致该接口的执行结果存在二义性,也就是说我们不用考虑同步的问题。线程安全问题都是由全局变量及静态变量引起的。

若每个线程中对全局变量、静态变量只有读操作，而无写操作，一般来说，这个全局变量是线程安全的；若有多个线程同时执行写操作，一般都需要考虑线程同步，否则就可能影响线程安全。

* 1） 常量始终是线程安全的，因为只存在读操作。 
* 2）每次调用方法前都新建一个实例是线程安全的，因为不会访问共享的资源。
* 3）局部变量是线程安全的。因为每执行一个方法，都会在独立的空间创建局部变量，它不是共享的资源。局部变量包括方法的参数变量和方法内变量。

有状态就是有数据存储功能。有状态对象(`Stateful Bean`)，就是有实例变量的对象  ，可以保存数据，是非线程安全的。在不同方法调用间不保留任何状态。

* 无状态就是一次操作，不能保存数据。无状态对象(`Stateless Bean`)，就是没有实例变量的对象  .不能保存数据，是不变类，是线程安全的。
* 有状态对象: 无状态的`Bean`适合用不变模式，技术就是单例模式，这样可以共享实例，提高性能。有状态的`Bean`，多线程环境下不安全，那么适合用`Prototype`原型模式。`Prototype`: 每次对`bean`的请求都会创建一个新的`bean`实例。`Struts2`默认的实现是`Prototype`模式。也就是每个请求都新生成一个`Action`实例，所以不存在线程安全问题。需要注意的是，如果由`Spring`管理`action`的生命周期， `scope`要配成`prototype`作用域。



### 5.为什么需要代理模式

* 授权机制 不同级别的用户对同一对象拥有不同的访问权利,如`Jive`论坛系统中,就使用`Proxy`进行授权机制控制,访问论坛有两种人:注册用户和游客(未注册用户),`Jive`中就通过类似`ForumProxy`这样的代理来控制这两种用户对论坛的访问权限.
* 某个客户端不能直接操作到某个对象,但又必须和那个对象有所互动


### 6.怎么理解面向切面编程的切面？

切面的组成部分：

* 通知（`Advice`）: 切面应该完成的工作，定义切面何时才能使用。` Before、After、After-returning、After-throwing、Around`
* 连接点（`Join point`）: 连接点是在应用执行过程中能被插入切面的一个点。
* 切点（`Poincut`）：何处执行切面，什么类还是什么包还是什么方法时候被执行。

* 切面（`Aspect`）:通知和切点的结合，定义了他是什么，在何时和何处完成其功能。

* 织入（`Weaving`）：是吧切面应用到目标对象并且创建新的代理对象的过程，切面在指定的连接点被注入到目标对象中。
* 时间：编译期、类加载期（`JVM`）、运行期（在织入切面时）



### 7.讲解 `OOP` 与 `AOP`（面向切面编程）的简单对比？

`AOP、OOP`在字面上虽然非常类似，但却是面向不同领域的两种设计思想。`OOP`（面向对象编程）**针对业务处理过程的实体及其属性和行为进行抽象封装**，以获得更加清晰高效的逻辑单元划分。

而`AOP`则是针对业务处理过程中的切面进行提取，它所面对的是**处理过程中的某个步骤或阶段**，以获得逻辑过程中各部分之间低耦合性的隔离效果。这两种设计思想在目标上有着本质的差异。

举个简单的例子，对于“雇员”这样一个业务实体进行封装，自然是`OOP/OOD`的任务，我们可以为其建立一个“`Employee`”类，并将“雇员”相关的属性和行为封装其中。而用`AOP`设计思想对“雇员”进行封装将无从谈起。同样，对于“权限检查”这一动作片断进行划分，则是`AOP`的目标领域。而通过`OOD/OOP`对一个动作进行封装，则有点不伦不类。



### 8.讲解`Spring` 框架中` AOP `实现原理？

参考：`https://blog.csdn.net/hello_worldee/article/details/78136616`

其实本质就是代理。



### 9.讲解`Spring `框架中如何基于` AOP `实现的事务管理？

参考：`https://blog.csdn.net/csdn_huzeliang/article/details/78995795`

**`Spring`支持编程式事务管理和声明式事务管理两种方式**
* 编程式事务管理使用`TransactionTemplate`或者直接使用底层的`PlatformTransactionManager`。对于编程式事务管理，`spring`推荐使用`TransactionTemplate`。
* 声明式事务管理建立在`AOP`之上的。其本质是对方法前后进行拦截，然后在目标方法开始之前创建或者加入一个事务，在执行完目标方法之后根据执行情况提交或者回滚事务。声明式事务最大的优点就是不需要通过编程的方式管理事务，这样就不需要在业务逻辑代码中掺杂事务管理的代码，只需在配置文件中做相关的事务规则声明(或通过基于`@Transactional`注解的方式)，便可以将事务规则应用到业务逻辑中。
显然声明式事务管理要优于编程式事务管理，这正是`spring`倡导的非侵入式的开发方式。声明式事务管理使业务代码不受污染，一个普通的`POJO`对象，只要加上注解就可以获得完全的事务支持。和编程式事务相比，声明式事务唯一不足地方是，后者的最细粒度只能作用到方法级别，无法做到像编程式事务那样可以作用到代码块级别。但是即便有这样的需求，也存在很多变通的方法，比如，可以将需要进行事务管理的代码块独立为方法等等。声明式事务管理也有两种常用的方式，一种是基于`tx`和`aop`名字空间的`xml`配置文件，另一种就是基于`@Transactional`注解。显然基于注解的方式更简单易用，更清爽。

**`Spring`事务特性**
`spring`所有的事务管理策略类都继承自`org.springframework.transaction.PlatformTransactionManager`接口

其中`TransactionDefinition`接口定义以下特性：

* 事务隔离级别
    隔离级别是指若干个并发的事务之间的隔离程度。`TransactionDefinition `接口中定义了五个表示隔离级别的常量：
    * `TransactionDefinition.ISOLATION_DEFAULT`：这是默认值，表示使用底层数据库的默认隔离级别。对大部分数据库而言，通常这值就是`TransactionDefinition.ISOLATION_READ_COMMITTED`。
    * `TransactionDefinition.ISOLATION_READ_UNCOMMITTED`：该隔离级别表示一个事务可以读取另一个事务修改但还没有提交的数据。该级别不能防止脏读，不可重复读和幻读，因此很少使用该隔离级别。比如`PostgreSQL`实际上并没有此级别。
    * `TransactionDefinition.ISOLATION_READ_COMMITTED`：该隔离级别表示一个事务只能读取另一个事务已经提交的数据。该级别可以防止脏读，这也是大多数情况下的推荐值。
    * `TransactionDefinition.ISOLATION_REPEATABLE_READ`：该隔离级别表示一个事务在整个过程中可以多次重复执行某个查询，并且每次返回的记录都相同。该级别可以防止脏读和不可重复读。
    * `TransactionDefinition.ISOLATION_SERIALIZABLE`：所有的事务依次逐个执行，这样事务之间就完全不可能产生干扰，也就是说，该级别可以防止脏读、不可重复读以及幻读。但是这将严重影响程序的性能。通常情况下也不会用到该级别。

* 事务传播行为
    所谓事务的传播行为是指，如果在开始当前事务之前，一个事务上下文已经存在，此时有若干选项可以指定一个事务性方法的执行行为。在`TransactionDefinition`定义中包括了如下几个表示传播行为的常量：
    * `TransactionDefinition.PROPAGATION_REQUIRED`：如果当前存在事务，则加入该事务；如果当前没有事务，则创建一个新的事务。这是默认值。
    * `TransactionDefinition.PROPAGATION_REQUIRES_NEW`：创建一个新的事务，如果当前存在事务，则把当前事务挂起。
    * `TransactionDefinition.PROPAGATION_SUPPORTS`：如果当前存在事务，则加入该事务；如果当前没有事务，则以非事务的方式继续运行。
    * `TransactionDefinition.PROPAGATION_NOT_SUPPORTED`：以非事务方式运行，如果当前存在事务，则把当前事务挂起。
    * `TransactionDefinition.PROPAGATION_NEVER`：以非事务方式运行，如果当前存在事务，则抛出异常。
    * `TransactionDefinition.PROPAGATION_MANDATORY`：如果当前存在事务，则加入该事务；如果当前没有事务，则抛出异常。
    * `TransactionDefinition.PROPAGATION_NESTED`：如果当前存在事务，则创建一个事务作为当前事务的嵌套事务来运行；如果当前没有事务，则该取值等价于`TransactionDefinition.PROPAGATION_REQUIRED`。

* 事务超时
    所谓事务超时，就是指一个事务所允许执行的最长时间，如果超过该时间限制但事务还没有完成，则自动回滚事务。在` TransactionDefinition `中以` int` 的值来表示超时时间，其单位是秒。默认设置为底层事务系统的超时值，如果底层数据库事务系统没有设置超时值，那么就是`none`，没有超时限制。

* 事务只读属性
    只读事务用于客户代码只读但不修改数据的情形，只读事务用于特定情景下的优化，比如使用`Hibernate`的时候。默认为读写事务。
    “只读事务”并不是一个强制选项，它只是一个“暗示”，提示数据库驱动程序和数据库系统，这个事务并不包含更改数据的操作，那么`JDBC`驱动程序和数据库就有可能根据这种情况对该事务进行一些特定的优化，比方说不安排相应的数据库锁，以减轻事务对数据库的压力，毕竟事务也是要消耗数据库的资源的。 
    但是你非要在“只读事务”里面修改数据，也并非不可以，只不过对于数据一致性的保护不像“读写事务”那样保险而已。 因此，“只读事务”仅仅是一个性能优化的推荐配置而已，并非强制你要这样做不可

* `spring`事务回滚规则
    指示`spring`事务管理器回滚一个事务的推荐方法是在当前事务的上下文内抛出异常。`spring`事务管理器会捕捉任何未处理的异常，然后依据规则决定是否回滚抛出异常的事务。
    默认配置下，`spring`只有在抛出的异常为运行时`unchecked`异常时才回滚该事务，也就是抛出的异常为`RuntimeException`的子类(`Errors`也会导致事务回滚)，而抛出`checked`异常则不会导致事务回滚。可以明确的配置在抛出那些异常时回滚事务，包括`checked`异常。也可以明确定义那些异常抛出时不回滚事务。还可以编程性的通过`setRollbackOnly()`方法来指示一个事务必须回滚，在调用完`setRollbackOnly()`后你所能执行的唯一操作就是回滚。



### 10.谈谈对控制反转(`IOC`)与依赖注入(`DI`)

`IoC`（`Inversion of Control`，控制反转）。这是`spring`的核心，贯穿始终。所谓`IoC`，对于`spring`框架来说，就是由`spring`来负责控制对象的生命周期和对象间的关系。如果由编程者来控制生命周期的所有事情，那么就是正向控制。而`Spring`所倡导的开发方式就是如此，所有的类都会在`spring`容器中登记，告诉`spring`你是个什么东西，你需要什么东西，然后`spring`会在系统运行到适当的时候，把你要的东西主动给你，同时也把你交给其他需要你的东西。所有的类的创建、销毁都由 spring来控制，也就是说控制对象生存周期的不再是引用它的对象，而是`spring`。对于某个具体的对象而言，以前是它控制其他对象，现在是所有对象都被`spring`控制，所以这叫控制反转。

`IoC`的一个重点是在系统运行中，动态的向某个对象提供它所需要的其他对象。这一点是通过`DI`（`Dependency Injection`，依赖注入）来实现的。比如对象`A`需要操作数据库，以前我们总是要在A中自己编写代码来获得一个`Connection`对象，有了` spring`我们就只需要告诉`spring`，`A`中需要一个`Connection`，至于这个`Connection`怎么构造，何时构造，`A`不需要知道。在系统运行时，`spring`会在适当的时候制造一个`Connection`，然后像打针一样，注射到`A`当中，这样就完成了对各个对象之间关系的控制。



### 11.怎么理解` Spring IOC` 容器？

`Spring IOC` 负责创建对象，管理对象（通过依赖注入（`DI`），装配对象，配置对象，并且管理这些对象的整个生命周期。`Spring IOC `负责创建对象，管理对象（通过依赖注入（`DI`），装配对象，配置对象，并且管理这些对象的整个生命周期。



### 12.`Spring IOC `怎么管理 `Bean` 之间的依赖关系，怎么避免循环依赖？

参考：`https://www.jianshu.com/p/6c359768b1dc`

`Spring`通过三级缓存加上“提前曝光”机制，配合`Java`的对象引用原理，比较完美地解决了某些情况下的循环依赖问题。但是如果两个对象是构造函数相互依赖则直接抛出异常。


### 13.`BeanFactory` 和 `FactoryBean `有什么区别？

前者是`Bean`的容器，后者是用于产生`Bean`的工厂`Bean`。

### 14.`BeanFactory `和` ApplicationContext `有什么不同？

如果说`BeanFactory`是`Spring`的心脏，那么`ApplicationContext`就是完整的躯体了，`ApplicationContext`由`BeanFactory`派生而来,其不仅仅具备了`BeanFactory`的功能，还可以提供一些其他功能，即提供了更多面向实际应用的功能。在`BeanFactory`中，很多功能需要以编程的方式实现，而在`ApplicationContext`中则可以通过配置实现。

`BeanFactorty`接口提供了配置框架及基本功能，但是无法支持`spring`的`aop`功能和`web`应用。而`ApplicationContext`接口作为`BeanFactory`的派生，因而提供`BeanFactory``所有的功能。而且ApplicationContext`还在功能上做了扩展，相较于`BeanFactorty`，`ApplicationContext`还提供了以下的功能： 

* `MessageSource`, 提供国际化的消息访问  
* 资源访问，如`URL`和文件  
* 事件传播特性，即支持`aop`特性
* 载入多个（有继承关系）上下文 ，使得每一个上下文都专注于一个特定的层次，比如应用的`web`层 

### 15.谈谈`Spring Bean` 创建过程中的设计模式？

参考：`https://www.cnblogs.com/yuefan/p/3763898.html`

`spring`中使用到的设计模式：工厂、单例、适配器、包装器、代理、观察、策略、模板。







