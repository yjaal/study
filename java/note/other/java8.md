摘自：`https://my.oschina.net/benhaile/blog/177148`

## 关于JSR335
JSR是Java Specification Requests的缩写，意思是Java 规范请求,Java 8 版本的主要改进是 Lambda 项目（JSR 335），其目的是使 Java 更易于为多核处理器编写代码。JSR 335=lambda表达式+接口改进（默认方法）+批量数据操作。加上前面两篇，我们已是完整的学习了JSR335的相关内容了。

## 外部 VS 内部迭代
以前Java集合是不能够表达内部迭代的，而只提供了一种外部迭代的方式，也就是for或者while循环。

```java
List persons = asList(new Person("Joe"), new Person("Jim"), new Person("John"));
for (Person p :  persons) {
   p.setLastName("Doe");
}
```



上面的例子是我们以前的做法，也就是所谓的外部迭代，循环是固定的顺序循环。在现在多核的时代，如果我们想并行循环，不得不修改以上代码。效率能有多大提升还说定，且会带来一定的风险（线程安全问题等等）。 要描述内部迭代，我们需要用到Lambda这样的类库,下面利用lambda和Collection.forEach重写上面的循环 

`persons.forEach(p->p.setLastName("Doe"));`

现在是由jdk 库来控制循环了，我们不需要关心last name是怎么被设置到每一个person对象里面去的，库可以根据运行环境来决定怎么做，并行，乱序或者懒加载方式。这就是内部迭代，客户端将行为p.setLastName当做数据传入api里面。 

内部迭代其实和集合的批量操作并没有密切的联系，借助它我们感受到语法表达上的变化。真正有意思的和批量操作相关的是新的流（stream）API。新的java.util.stream包已经添加进JDK 8了。

## Stream API
流（Stream）仅仅代表着数据流，并没有数据结构，所以他遍历完一次之后便再也无法遍历（这点在编程时候需要注意，不像Collection，遍历多少次里面都还有数据），它的来源可以是Collection、array、io等等。



## 并行流

```java
List <Person> people = list.getStream.parallel().collect(Collectors.toList());
```



顾名思义，当使用顺序方式去遍历时，每个item读完后再读下一个item。而使用并行去遍历时，数组会被分成多个段，其中每一个都在不同的线程中处理，然后将结果一起输出。

### 并行流原理

```java
List originalList = someData;
split1 = originalList(0, mid);//将数据分小部分
split2 = originalList(mid,end);
new Runnable(split1.process());//小部分执行操作
new Runnable(split2.process());
List revisedList = split1 + split2;//将结果合并
```



大家对hadoop有稍微了解就知道，里面的 MapReduce  本身就是用于并行处理大数据集的软件框架，其 处理大数据的核心思想就是大而化小，分配到不同机器去运行map，最终通过reduce将所有机器的结果结合起来得到一个最终结果，与MapReduce不同，Stream则是利用多核技术可将大数据通过多核并行处理，而MapReduce则可以分布式的。























