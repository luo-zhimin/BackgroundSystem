
<p align="center">
    <a target="_blank" href="https://www.oracle.com/technetwork/java/javase/downloads/index.html">
    <img src="https://img.shields.io/badge/JDK-1.8+-green.svg"  alt="java"/></a>
</p>

[特性](#特性) |[注意](#注意) | [快速开始](#快速开始) 

# 简介

**Background-System**是一个基于SpringBoot的后台管理系统，前端使用Vue，后端使用SpringBoot，数据库使用MySQL，缓存使用Redis，使用了MybatisPlus，Shiro，JWT，Swagger，Quartz，Jasypt等技术，实现了用户管理，角色管理，权限管理，菜单管理，接口管理，定时任务，文件上传，邮件发送等功能。

已经上线小程序，铃星印品-（原代码）

# 特性
- 配置文件加密，（java）打包混淆
- 支持MySQL数据库
- 支持非关系型数据库Redis
- 支持分页查询
- 支持SQL缓存
- 支持对接口权限配置、拦截器等功能（登陆拦截）
- 支持Swagger接口文档生成
- 支持数据库事务、SQL支持拼接，占位符，判断等语法
- 支持文件上传、下载、输出图片
- 支持邮件发送（默认发送当天订单销售量-需要配置send-email参数）
- 支持定时任务（处理订单状态，优惠卷状态）
- 支持excel复杂导入导出（多sheet等，列宽自动调整）
- 待支持启动时自动创建数据库表（待续）

# 注意
- **配置文件全部使用 jasypt 进行配置文件加密，解密是在启动时进行的，所以需要在启动时配置解密密码（vm选项）**
- **Djasypt.encryptor.password=xx**
- **如果不配置解密密码，启动时会报错，无法启动**
- **xx是你配置的key，根据你的key进行解密**
- **key默认5de88f71a509010ba5a0491c751b2d77**
- **项目也进行了加密，若要防止暴露，推荐使用 encrypted.jar，密码在[pom.xml](pom.xml)中设置，默认是项目名称**
- 启动脚本[background.sh](src%2Fmain%2Fresources%2Fbackground.sh)，linux启动脚本，需要配置加密的key，配置默认key
- 启动mysql脚本[backgroundSystem.sql](src%2Fmain%2Fresources%2Fstatic%2FbackgroundSystem.sql)

# 快速开始

## 下载

[下载地址](https://github.com/luo-zhimin/BackgroundSystem/releases)  

## 修改[application.yml](src%2Fmain%2Fresources%2Fapplication.yml)配置文件

```yaml
# 邮件配置/数据库配置 涉及到密码的配置都需要加密，加密方式为：jasypt.encryptor.password=xx，xx为你的key，然后使用jasypt加密
spring:
  mail:
    host: smtp.qq.com
    protocol: smtp
#    中文编码
    default-encoding: utf-8
#  发送的邮箱账号
    username: ENC(yOcA+pTrPhZyuJKlBQ52O5MedEIQe7Dl1SFzBTu0R1nqZdiwjz3gMg==)
#    发送的邮箱授权码
    password: ENC(nNjoKm63Ltqm2/LnKb3p9eitKY0lj8xaxlKalCfNfExtMN9Drz5I9Q==)
# 临时生产zip包的路径 linux配置为 /zip window 配置为 /opt/project/zip 具体路径
zip:
  path: zip
# 文件上传路径，如果使用本地上传，需要配置此路径，默认是阿里云oss，上传脚本放在resources/fileServer
file:
#  布置的fileServer的地址
  address: http://localhost:8000
#  fileServer的config里面配置的token
  token: ENC(a0A4+mHJ3y68MbYC2vvPGBXa8SbrPyPgZb+depGkHsxj5KjNhDWJOg==)
```