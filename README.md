## 目的

使用统一的方式来管理kafka

包括:

* 统一的配置方式, 通过config server
* 统一的监控管理
* 自动分配kafka id


## TODO

1. 通过zookeeper来自动分配 broker.id (为啥kafka不直接那么做? )
2. 把kafka的metrics统一的管理起来
3. kafka使用的log4j, 怎么整合起来?

    Done, 直接把log4j的依赖从kafka中去掉就可以了, slf4j中的log4j-over-slf4j已经提供了这个支持

4. web 界面来创建topic, 删除topic, 调整paratation等基本的操作
5. 作为测试组件在依赖kafka的项目中使用
