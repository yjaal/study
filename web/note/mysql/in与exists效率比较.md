摘自：`https://blog.csdn.net/lilun517735159/article/details/78826521`

这条语句适用于a表比b表大的情况

```sql
select * from ecs_goods a where cat_id in(select cat_id from ecs_category);
```

这条语句适用于b表比a表大的情况

```sql
select * from ecs_goods a where EXISTS(select cat_id from ecs_category b where a.cat_id = b.cat_id);
```



原因:(转发)

```sql
select * from A
where id in(select id from B)
```

以上查询使用了in语句,in()只执行一次,它查出B表中的所有id字段并缓存起来.之后,检查A表的id是否与B表中的id相等,如果相等则将A表的记录加入结果集中,直到遍历完A表的所有记录.
它的查询过程类似于以下过程

```java
List resultSet=[];
Array A=(select * from A);
Array B=(select id from B);

for(int i=0;i<A.length;i++) {
   for(int j=0;j<B.length;j++) {
      if(A[i].id==B[j].id) {
         resultSet.add(A[i]);
         break;
      }
   }
}
return resultSet;
```



可以看出,当B表数据较大时不适合使用in(),因为它会B表数据全部遍历一次.
如:A表有10000条记录,B表有1000000条记录,那么最多有可能遍历10000*1000000次,效率很差.
再如:A表有10000条记录,B表有100条记录,那么最多有可能遍历10000*100次,遍历次数大大减少,效率大大提升.

结论:in()适合B表比A表数据小的情况

```sql
select a.* from A a 
where exists(select 1 from B b where a.id=b.id)
```



以上查询使用了exists语句,exists()会执行A.length次,它并不缓存exists()结果集,因为exists()结果集的内容并不重要,重要的是结果集中是否有记录,如果有则返回true,没有则返回false.
它的查询过程类似于以下过程

```java
List resultSet=[];
Array A=(select * from A)

for(int i=0;i<A.length;i++) {
   if(exists(A[i].id) {    //执行select 1 from B b where b.id=a.id是否有记录返回
       resultSet.add(A[i]);
   }
}
return resultSet;
```



当B表比A表数据大时适合使用exists(),因为它没有那么遍历操作,只需要再执行一次查询就行.
如:A表有10000条记录,B表有1000000条记录,那么exists()会执行10000次去判断A表中的id是否与B表中的id相等.
如:A表有10000条记录,B表有100000000条记录,那么exists()还是执行10000次,因为它只执行A.length次,可见B表数据越多,越适合exists()发挥效果.
再如:A表有10000条记录,B表有100条记录,那么exists()还是执行10000次,还不如使用in()遍历10000*100次,因为in()是在内存里遍历比较,而exists()需要查询数据库,我们都知道查询数据库所消耗的性能更高,而内存比较很快.

结论:exists()适合B表比A表数据大的情况

当A表数据与B表数据一样大时,in与exists效率差不多,可任选一个使用.

在查询的两个表大小相当的情况下，3种查询方式的执行时间通常是：
EXISTS <= IN <= JOIN
NOT EXISTS <= NOT IN <= LEFT JOIN
只有当表中字段允许NULL时，NOT IN的方式最慢：
NOT EXISTS <= LEFT JOIN <= NOT IN

但是如果两个表中一个较小，一个较大，则子查询表大的用exists，子查询表小的用in，因为in 是把外表和内表作hash 连接，而exists是对外表作loop循环，每次loop循环再对内表进行查询。



下面再看not exists 和 not in

```sql
1. select * from A where not exists (select * from B where B.id = A.id);
2. select * from A where A.id not in (select id from B);
```

看查询1，还是和上面一样，用了B的索引

而对于查询2，可以转化成如下语句

```sql
select * from A where A.id != 1 and A.id != 2 and A.id != 3;
```

可以知道not in是个范围查询，这种!=的范围查询无法使用任何索引,等于说A表的每条记录，都要在B表里遍历一次，查看B表里是否存在这条记录，故not exists比not in效率高

mysql中的in语句是把外表和内表作hash 连接，而exists语句是对外表作loop循环，每次loop循环再对内表进行查询。一直大家都认为exists比in语句的效率要高，这种说法其实是不准确的。这个是要区分环境的。


如果查询的两个表大小相当，那么用in和exists差别不大。 
如果两个表中一个较小，一个是大表，则子查询表大的用exists，子查询表小的用in： 
例如：表A（小表），表B（大表）

```sql
1：
select * from A where cc in (select cc from B) 效率低，用到了A表上cc列的索引；
select * from A where exists(select cc from B where cc=A.cc) 效率高，用到了B表上cc列的索引。 
相反的

2：
select * from B where cc in (select cc from A) 效率高，用到了B表上cc列的索引；
select * from B where exists(select cc from A where cc=B.cc) 效率低，用到了A表上cc列的索引。
```

not in 和not exists如果查询语句使用了not in 那么内外表都进行全表扫描，没有用到索引；而not extsts 的子查询依然能用到表上的索引。所以无论那个表大，用not exists都比not in要快。 
in 与 =的区别 

```sql
select name from student where name in ('zhang','wang','li','zhao'); 
```

与 

```sql
select name from student where name='zhang' or name='li' or name='wang' or name='zhao' 
```

的结果是相同的。
