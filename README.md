# java-web-framework
从零开始写Java Web框架


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


