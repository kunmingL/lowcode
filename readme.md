# 代码生成器项目说明

## 项目介绍
这是一个基于SpringBoot的代码生成器，采用DDD架构设计。本项目可以通过读取数据库表结构，自动生成相应的DTO、Domain、PO等对象，以及相关的转换器和基础CRUD操作代码。

## 特色功能
- 支持MyBatis和JPA两种持久化框架
- 根据表结构自动生成完整的DDD分层代码
- 支持文件差异比较和合并
- 自动识别主键和列属性

## 使用方法
1. 配置数据库连接信息(application.yml)
2. 访问 http://localhost:8080 打开代码生成页面
3. 选择持久化框架 (MyBatis 或 JPA)
4. 填写表名和包名信息
5. 配置各层代码生成路径
6. 点击生成代码按钮

## 生成的代码结构
- DTO: 数据传输对象，用于API层
- Domain: 领域模型对象
- PO: 持久化对象，对应数据库表
- Repository: 仓储接口，定义领域对象的存取方法
- RepositoryImpl: 仓储实现，实现领域对象与持久层的交互
- Mapper/DAO: 数据访问层，提供基础的CRUD操作
- Converter: 对象转换器，实现不同层对象之间的转换

## MyBatis与JPA区别
- MyBatis模式:
  - 使用MyBatis-Plus提供基础CRUD和分页查询
  - 生成Mapper接口和XML映射文件
  - 使用IPage对象实现分页

- JPA模式:
  - 使用Spring Data JPA提供基础CRUD和分页查询
  - 实体类使用JPA注解(@Entity, @Id等)
  - 使用Specification实现动态条件查询
  - 使用Page对象实现分页

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
