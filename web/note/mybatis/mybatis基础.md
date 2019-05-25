## 1、`#{}`和`${}`的区别是什么

`#{}`是预编译处理，`${}`是字符串替换。
`Mybatis`在处理`#{}`时，会将`sql`中的`#{}`替换为`?`号，调用`PreparedStatement`的`set`方法来赋值；
`Mybatis`在处理`${}`时，就是把`${}`替换成变量的值。
使用`#{}`可以有效的防止`SQL`注入，提高系统安全性。



## 2、`Dao`接口的工作原理是什么？`Dao`接口里的方法，参数不同时，方法能重载吗？

`Dao`接口，就是人们常说的`Mapper`接口，接口的全限名，就是映射文件中的`namespace`的值，接口的方法名，就是映射文件中`MappedStatement`的`id`值，接口方法内的参数，就是传递给`sql`的参数。`Mapper`接口是没有实现类的，当调用接口方法时，接口`全限名+方法名`拼接字符串作为key值，可唯一定位一个`MappedStatement`，举例：`com.mybatis3.mappers.StudentDao.findStudentById`，可以唯一找到`namespace`为`com.mybatis3.mappers.StudentDao`下面`id = findStudentById`的`MappedStatement`。在`Mybatis`中，每一个`<select>、<insert>、<update>、<delete>`标签，都会被解析为一个`MappedStatement`对象。`Dao`接口里的方法，是不能重载的，因为是`全限名+方法名`的保存和寻找策略。`Dao`接口的工作原理是`JDK`动态代理，`Mybatis`运行时会使用`JDK`动态代理为`Dao`接口生成代理`proxy`对象，代理对象`proxy`会拦截接口方法，转而执行`MappedStatement`所代表的`sql`，然后将`sql`执行结果返回。



## 3、`Mybatis`是如何进行分页的？分页插件的原理是什么？

`Mybatis`使用`RowBounds`对象进行分页，它是针对`ResultSet`结果集执行的内存分页，而非物理分页，可以在`sql`内直接书写带有物理分页的参数来完成物理分页功能，也可以使用分页插件来完成物理分页。分页插件的基本原理是使用`Mybatis`提供的插件接口，实现自定义插件，在插件的拦截方法内拦截待执行的`sql`，然后重写`sql`，根据`dialect`方言，添加对应的物理分页语句和物理分页参数。



## 4、`Mybatis`是如何将`sql`执行结果封装为目标对象并返回的？都有哪些映射形式？

第一种是使用`<resultMap>`标签，逐一定义列名和对象属性名之间的映射关系。第二种是使用`sql`列的别名功能，将列别名书写为对象属性名，比如`T_NAME AS NAME`，对象属性名一般是`name`，小写，但是列名不区分大小写，`Mybatis`会忽略列名大小写。有了列名与属性名的映射关系后，`Mybatis`通过反射创建对象，同时使用反射给对象的属性逐一赋值并返回，那些找不到映射关系的属性，是无法完成赋值的。



## 5、在`mapper`中如何传递多个参数

方式一

```java
//DAO层的函数
Public UserselectUser(String name,String area);  
```

```xml
//对应的xml,#{0}代表接收的是dao层中的第一个参数，#{1}代表dao层中第二参数，更多参数一致往后加即可。
<select id="selectUser"resultMap="BaseResultMap">  
    select *  fromuser_user_t   whereuser_name = #{0} anduser_area=#{1}  
</select>  
```

方式二

```java
import org.apache.ibatis.annotations.param; 
public interface usermapper { 
 User selectUser(@param(“username”) string username, 
 @param(“hashedpassword”) string hashedpassword); 
}
```

```xml
<select id=”selectuser” resulttype=”user”> 
     select id, username, hashedpassword 
     from some_table 
     where username = #{username} 
     and hashedpassword = #{hashedpassword} 
</select>
```



## 6、`MyBatis`的缓存

`MyBatis`的缓存分为一级缓存和二级缓存,一级缓存放在`session`里面,默认就有,二级缓存放在它的命名空间里,默认是不打开的,使用二级缓存属性类需要实现`Serializable`序列化接口(可用来保存对象的状态),可在它的映射文件中配置`<cache/>`

1）一级缓存` Mybatis`的一级缓存是指`SQLSession`，一级缓存的作用域是`SQlSession`, `Mybatis`默认开启一级缓存。 在同一个`SqlSession`中，执行相同的`SQL`查询时；第一次会去查询数据库，并写在缓存中，第二次会直接从缓存中取。 当执行`SQL`时候两次查询中间发生了增删改的操作，则`SQLSession`的缓存会被清空。 每次查询会先去缓存中找，如果找不到，再去数据库查询，然后把结果写到缓存中。 `Mybatis`的内部缓存使用一个`HashMap`，`key`为`hashcode+statementId+sql`语句。`Value`为查询出来的结果集映射成的`java`对象。 `SqlSession`执行`insert、update、delete`等操作`commit`后会清空该`SQLSession`缓存。

2）二级缓存是`mapper`级别的，`Mybatis`默认是没有开启二级缓存的。 第一次调用`mapper`下的`SQL`去查询用户的信息，查询到的信息会存放代该`mapper`对应的二级缓存区域。 第二次调用`namespace`下的`mapper`映射文件中，相同的`sql`去查询用户信息，会去对应的二级缓存内取结果。 如果调用相同`namespace`下的`mapepr`映射文件中增删改`sql`，并执行了`commit`操作，此时会重新执行查询。



## 7、`Mybatis`是否支持延迟加载？如果支持，它的实现原理是什么？

1）`Mybatis`仅支持`association`关联对象和`collection`关联集合对象的延迟加载，`association`指的就是一对一，`collection`指的就是一对多查询。在`Mybatis`配置文件中，可以配置是否启用延迟加载`lazyLoadingEnabled=true|false`。

2）它的原理是，使用`CGLIB`创建目标对象的代理对象，当调用目标方法时，进入拦截器方法，比如调用`a.getB().getName()`，拦截器`invoke()`方法发现`a.getB()`是`null`值，那么就会单独发送事先保存好的查询关联`B`对象的`sql`，把`B`查询上来，然后调用`a.setB(b)`，于是`a`的对象`b`属性就有值了，接着完成`a.getB().getName()`方法的调用。这就是延迟加载的基本原理。



## 8、什么是`MyBatis`的接口绑定,有什么好处？

接口映射就是在`MyBatis`中任意定义接口,然后把接口里面的方法和`SQL`语句绑定,我们直接调用接口方法就可以,这样比起原来了`SqlSession`提供的方法我们可以有更加灵活的选择和设置。



## 9、接口绑定有几种实现方式,分别是怎么实现的?

接口绑定有两种实现方式,一种是通过注解绑定,就是在接口的方法上面加上`@Select、@Update`等注解里面包含`Sql`语句来绑定,另外一种就是通过`xml`里面写`SQL`来绑定,在这种情况下,要指定`xml`映射文件里面的`namespace`必须为接口的全路径名。当`Sql`语句比较简单时候,用注解绑定；当`SQL`语句比较复杂时候,用`xml`绑定,一般用`xml`绑定的比较多。



## 10、`Mybatis`映射文件中，如果`A`标签通过`include`引用了`B`标签的内容，请问，`B`标签能否定义在`A`标签的后面，还是说必须定义在`A`标签的前面？

虽然`Mybatis`解析`Xml`映射文件是按照顺序解析的，但是，被引用的`B`标签依然可以定义在任何地方，`Mybatis`都可以正确识别。原理是，`Mybatis`解析`A`标签，发现`A`标签引用了`B`标签，但是`B`标签尚未解析到，尚不存在，此时，`Mybatis`会将A标签标记为未解析状态，然后继续解析余下的标签，包含`B`标签，待所有标签解析完毕，`Mybatis`会重新解析那些被标记为未解析的标签，此时再解析`A`标签时，`B`标签已经存在，`A`标签也就可以正常解析完成了。



## 11、`Mybatis`的`Xml`映射文件中，不同的`Xml`映射文件，`id`是否可以重复？

不同的`Xml`映射文件，如果配置了`namespace`，那么`id`可以重复；如果没有配置`namespace`，那么`id`不能重复；毕竟`namespace`不是必须的，只是最佳实践而已。原因就是`namespace+id`是作为`Map<String, MappedStatement>`的`key`使用的，如果没有`namespace`，就剩下`id`，那么，`id`重复会导致数据互相覆盖。



## 12、`Mybatis`都有哪些`Executor`执行器？它们之间的区别是什么？

`Mybatis`有三种基本的`Executor`执行器，`SimpleExecutor、ReuseExecutor、BatchExecutor`。

1）`SimpleExecutor`：每执行一次`update`或`select`，就开启一个`Statement`对象，用完立刻关闭`Statement`对象。

2）`ReuseExecutor`：执行`update`或`select`，以`sql`作为`key`查找`Statement`对象，存在就使用，不存在就创建，用完后，不关闭`Statement`对象，而是放置于`Map`。

3）`BatchExecutor`：完成批处理。



## 13、`MyBatis`编程步骤是什么样的？

① 创建`SqlSessionFactory `
② 通过`SqlSessionFactory`创建`SqlSession `
③ 通过`sqlsession`执行数据库操作 
④ 调用`session.commit()`提交事务 
⑤ 调用`session.close()`关闭会话















