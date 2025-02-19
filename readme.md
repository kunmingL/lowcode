# 代码生成器项目说明

## 项目介绍
这是一个基于SpringBoot的代码生成器，采用DDD架构设计。本项目可以通过读取数据库表结构，自动生成相应的DTO、Domain、PO等对象，以及相关的转换器和基础CRUD操作代码。


## 使用方法
1. 配置数据库连接信息(application.yml)
2. 访问 http://localhost:8080/generator 进入代码生成页面
3. 输入表名称和期望生成的文件路径
4. 点击生成按钮即可自动生成相关代码

## 生成的文件说明
- DTO: 数据传输对象，位于application/dto目录
- Domain: 领域模型对象，位于domain/model目录
- PO: 持久化对象，位于infrastructure/persistence/po目录
- Mapper: DTO与Domain转换器，位于infrastructure/mapper目录
- Converter: Domain与PO转换器，位于infrastructure/converter目录
- Repository: 仓储接口及实现，位于domain/repository和infrastructure/persistence/repository目录
- MyBatis映射文件: 位于resources/mapper目录
- 
- t_user
- com.changjiang.elearn
  D:\IDEA_PROJECT\elearning\elearn-api\src\main\java\com.changjiang.elearn.api\dto
  D:\IDEA_PROJECT\elearning\elearn-service\src\main\java\com\changjiang\elearn\domain\model
- D:\IDEA_PROJECT\elearning\elearn-service\src\main\java\com\changjiang\elearn\infrastructure\persistence\po
- D:\IDEA_PROJECT\elearning\elearn-service\src\main\java\com\changjiang\elearn\infrastructure\mapper
- D:\IDEA_PROJECT\elearning\elearn-service\src\main\java\com\changjiang\elearn\infrastructure\converter
- D:\IDEA_PROJECT\elearning\elearn-service\src\main\java\com\changjiang\elearn\infrastructure\repository
  D:\IDEA_PROJECT\elearning\elearn-service\src\main\resources\mapper
