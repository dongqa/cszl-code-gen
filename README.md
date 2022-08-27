

# MyBatis-Plus 代码生成器

## 概述

基于springboot，mybatis-plus，vue-admin构建的一套代码生成器，支持mysql，postgresql，clickhouse数据库

### 功能：

- 自动生成entity类

- 自动生成mapper接口

- 自动生成xml文件

- 自动生成service接口

- 自动生成service实现类

- 自动生成contoller层接口方法

- 支持swagger2

- 支持可选字段查询，更新功能

- 支持生成on duplicate key update语句

- 支持文本驼峰互转

- 支持json对象转javabean功能

  

## 使用说明

在线地址：http://code-gen.cszl.wudongqiang.top:81/#/code/gen

或

git clone https://github.com/dongqa/cszl-code-gen.git

1. 前端运行

   确保系统环境安装了nodejs

   ```
   cd front
   
   npm install
   
   npm run dev
   ```

2. 后端运行

​		进入项目根目录，确保系统环境安装了Maven		

​		`mvn clean install -DskipTests`

​		运行jar文件

