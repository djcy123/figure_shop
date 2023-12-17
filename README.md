手办商场

学号：202130440460
姓名:董俊

项目开发环境:

    操作系统:Windows7、Windows10或更高的Windows版本
    Web服务器:Tomcat8.5.96
    数据库:MySQL8.0
    开发工具:IDEA2023.3.5
    浏览器:IE


主要目录介绍

  -src
  
    -dao：存储的java类都是与数据库进行交互的类
    -filter：包中存储过滤器类，用户统一全站编码
    -listener：包中存储一个Listener类，用于监听并获取所有商品分类
    -model：包中存储java实体类
    -service：包中存储项目用到的Servlet类重载的service()方法
    -servlet:包中存储着网站用到的实现各种功能的Servlet类
    -utils：包中存储着项目用到的工具类
    
  -web

    -admin：包中存储后台管理系统的所有JSP页面文件以及CSS、JS和图片等
    -css、js、fonts、images、layer、picture包括前后台系统中用到的CSS、JS、字体样式和图片等

项目部署tips:

1) 配置c3p0连接池和自己本地的MySQL数据库连接：

   例如:
   
        <property name="jdbcUrl">jdbc:mysql://localhost:3306/figureshop?serverTimezone=UTC&amp;useUnicode=true&amp;characterEncoding=utf-8</property>
        <property name="user">root</property>
        <property name="password">114514</property>

2)配置模块依赖时

    需要将本地下载的Tomcat的lib添加到模块依赖项。
