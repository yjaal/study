摘自：`https://zhuanlan.zhihu.com/p/34440779`

## 定义

**REST代表表现层状态转移(representational state transfer)**，由Roy Fielding在他的[论文](https://link.zhihu.com/?target=http%3A//www.ics.uci.edu/~fielding/pubs/dissertation/top.htm)中提出。REST用来描述客户端通过某种形式获取服务器的数据，这些数据资源的格式通常是JSON或XML。同时，这些资源的表现或资源的集合是可以修改的，伴随着行为和关系可以通过多媒体来发现。在我看来，一种简单的理解就是：**在设计API时，使用路径定位资源，方法定义操作，通过Content-Type和Accept来协商资源的类型**。REST也有一些限制：

1. REST是无状态的，请求之间没有持久的会话信息
2. 响应需要声明成可缓存的
3. REST关注一致性，如果使用HTTP，需要尽可能使用HTTP的特性，而不是去发明新的公约

这些限制允许REST架构的API更加稳定。

在REST流行之前，大多数API使用XML-RPC或SOAP来设计。XML-RPC最大的问题是将所有的数据转换成用XML来描述，是一件很费劲的事。以前在写JAVA的时候，最烦的就是写XML配置文件(在这里黑一下JAVA)，SOAP也是一样。

**RPC代表远程过程调用(remote procedure call)**，其中最著名的便是由Facebook开发，现在由Apache维护的[Thrift](https://link.zhihu.com/?target=http%3A//thrift.apache.org/)(感谢Facebook为开源社区做出的伟大贡献)，其包含多种语言的实现。由于我们痛恨XML的数据格式繁杂，所以大多数RPC协议都是基于JSON。我的理解是，RPC是跨语言跨平台的服务调用，不仅是C-S或者前后端之间的通信。一个完善的RPC框架还包含代码生成，通信的规范等等，在这里我们只谈API的设计。在实际使用中，以前后端开发为例，**前端传递一个方法名和参数给后端，后端执行对应的方法，并给该方法传递对应的参数，最后将执行的结果传递给前端**。一个简单的例子：

```http
OST /sayHello HTTP/1.1
HOST: api.example.com
Content-Type: application/json

{"name": "I am happy"}
```

其中sayHello就是方法名，{"name": "I am happy"}就是对应的参数，所以之后后端可能会执行sayHello("I am happy")。基于HTTP API，**RPC通常使用URL来表示方法名，而HTTP query string或body 来表示方法调用需要的参数**。

看一个用Google搜索出来的SOAP的例子，这个例子的方法名叫*getAdUnitsByStatement：*

```xml
<?xml version="1.0" encoding="UTF-8"?>
<soapenv:Envelope
        xmlns:soapenv="http://schemas.xmlsoap.org/soap/envelope/"
        xmlns:xsd="http://www.w3.org/2001/XMLSchema"
        xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance">
  <soapenv:Header>
    <ns1:RequestHeader
         soapenv:actor="http://schemas.xmlsoap.org/soap/actor/next"
         soapenv:mustUnderstand="0"
         xmlns:ns1="https://www.google.com/apis/ads/publisher/v201605">
      <ns1:networkCode>123456</ns1:networkCode>
      <ns1:applicationName>DfpApi-Java-2.1.0-dfp_test</ns1:applicationName>
    </ns1:RequestHeader>
  </soapenv:Header>
  <soapenv:Body>
    <getAdUnitsByStatement xmlns="https://www.google.com/apis/ads/publisher/v201605">
      <filterStatement>
        <query>WHERE parentId IS NULL LIMIT 500</query>
      </filterStatement>
    </getAdUnitsByStatement>
  </soapenv:Body>
</soapenv:Envelope>
```

虽然payload非常庞大，但是参数只有一行：

```xml
<query>WHERE parentId IS NULL LIMIT 500</query>
```

用JS来描述，就是：

```javascript
/* 方法定义 */
function getAdUnitsByStatement(filterStatement) {
  // ...
};

/* 调用 */
getAdUnitsByStatement('WHERE parentId IS NULL LIMIT 500');
```

而换成JSON描述的API 就非常的简洁了：

```json
POST /getAdUnitsByStatement HTTP/1.1
HOST: api.example.com
Content-Type: application/json

{"filter": "WHERE parentId IS NULL LIMIT 500"}
```

上面的getAdUnitsByStatement相对来说扩展性差一些，从语义上来说，“通过语句来获取广告单元”，如果我们要通过其他方式来获取广告单元，就需要另外定义一个API：getAdUnitsBySomethingElse。而通过REST，会更加简洁一些，我们可以通过定义参数来实现：

```http
GET /ads?statement={foo}
GET /ads?something={bar})
```

## 适用场景

**基于RPC的API更加适用行为(也就是命令和过程)，基于REST的API更加适用于构建模型(也就是资源和实体)，处理CRUD。**

REST不仅是CRUD(CRUD Boy哈哈)，不过REST也比较适合做CRUD。

1. REST使用HTTP的方法，例如：GET,POST,PUT,DELETE,OPTIONS还有比较不常用的PATCH方法。
2. RPC通常只会使用GET和POST方法，GET方法通常用来获取信息，POST方法可以用来进行所有的行为。

举个简单的例子，如果有REST的API: DELETE /user/1 ，那么通过RPC来实现就是 /deleteUser, HTTP的body是{"id": 1}。这两个实现没有太多的不同，而最大的不同是**如何处理行为**，在RPC中，可以比较清晰的设计API POST /doWhatevevThingNow，仅仅通过URL就能理解这个API的含义，而REST会让你觉得其只适合进行CRUD的操作。

但是情况并非完全如此，触发的行为可以通过这两种方法来完成。但在REST中，触发器更像是"事后"的。例如，如果需要发送消息给用户，RPC会是这样的：

```http
POST /SendUserMessage HTTP/1.1
Host: api.example.com
Content-Type: application/json

{"userId": 501, "message": "Hello!"}
```

而在REST中：

```http
POST /users/501/messages HTTP/1.1
Host: api.example.com
Content-Type: application/json

{"message": "Hello!"}
```

1. RPC：发送一个消息，然后消息会存储到数据库中来保存历史，有可能会有其他的RPC调用，但这个操作对我们不可见
2. REST：在用户的消息集合中创建一条消息资源，我们能够通过GET方法来通过相同的URL获取这个历史

这种"事后"的行为可以在REST中处理很多事情，例如：有一个旅行的APP，这些旅行需要开始，完成，取消的动作，不然用户不知道该在什么时候开始或结束。如果在REST API中，已经有了GET /trips 和 POST /trips，大多数人会尝试使用基于子资源的操作：

```http
POST /trips/123/start
POST /trips/123/finish
POST /trips/123/cancel
```

这种设计就将RPC的风格引入到了REST中，这是一种流行的解决方案，但这就不是经典的REST 风格了。这种交叉也说明了把行为引入REST中非常困难，一种解决的方法就是状态机。

所以很多时候，会不仅仅选择一种方法或者API，应用可以轻易的有多个API或附加服务，可以根据情况选择REST和RPC，这两种风格的API 很好的共存。通常，对于实体资源的增删改查(CRUD)，REST就能很好的胜任，但是对于增删改查以为的一些行为，例如上面的对于旅行的开始，结束，取消这些行为，REST无法描述，就要使用RPC来设计API了。

