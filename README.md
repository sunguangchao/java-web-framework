# java-web-framework

## smart-frameword
参考《架构探险-从零开始写Java Web框架》实现

## nettyRpc
### 简单介绍
1. 服务发布与订阅：服务端使用Zookeeper注册服务地址，客户端从Zookeeper获取可用的服务地址。
2. 通信：使用Netty作为通信框架。
3. Spring：使用Spring配置服务，加载Bean，扫描注解。
4. 动态代理：客户端使用代理模式透明化服务调用。
5. 消息编解码：使用Protostuff序列化和反序列化消息。

### reference
* [一个轻量级分布式RPC框架](https://www.cnblogs.com/luxiaoxun/p/5272384.html)
* [轻量级分布式RPC框架](https://my.oschina.net/huangyong/blog/361751)

第四章
=================
事务隔离级别(Transaction Isolation Level)
* READ_UNCOMMITTED
* READ_COMMITTED-MySQL数据库的默认级别
* REPEATABLE_READ
* SERIALIZABLE

从上往下，级别越来越高，并发性越来越差，安全性越来越高

* 脏读-事务A读取了事务B未提交的数据，并在这个基础上有做了其他操作
* 不可重复读——事务A读取了事务B已经提交的数据
* 幻读——事务A读取了事务B已提交的数据


