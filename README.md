
# KJSky 餐饮点餐系统
苍穹外卖 Kotlin Jimmer Sa-Token 虫豸版 
## 项目简介
KJSky 是一个基于 Spring Boot 和 Kotlin 构建的餐饮点餐系统，旨在为餐饮企业提供高效的数字化解决方案。该项目集成了微信小程序、WebSocket 实时通信、MinIO 文件存储、微信支付等功能，支持用户在线点餐、订单管理、菜品管理、数据统计等核心业务流程。

## 技术栈
- **后端框架**：Spring Boot 3.5.3
- **编程语言**：Kotlin 2.1.0
- **ORM 框架**：Jimmer 0.9.96
- **数据库**：MySQL 8+
- **缓存系统**：Redis
- **安全框架**：Sa-Token 1.44.0
- **支付集成**：微信支付 SDK
- **对象存储**：Minio
- **实时通信**：WebSocket
- **工具库**：Hutool 5.8.26, Apache POI 5.4.1

## 核心功能
- 用户注册/登录（微信小程序）
- 菜品浏览与搜索
- 购物车管理
- 订单创建与状态跟踪
- 微信支付集成
- 数据统计与报表导出
- WebSocket 实时消息推送

## 项目结构
```
KJSky/
├── src/
│   ├── main/
│   │   ├── kotlin/               # Kotlin 源代码目录
│   │   ├── resources/            # 资源文件目录
│   ├── test/                     # 测试代码目录
├── build.gradle.kts              # Gradle 构建脚本
├── gradlew                       # Gradle 包装器脚本 (Unix)
├── gradlew.bat                   # Gradle 包装器脚本 (Windows)
├── HELP.md                       # 项目帮助文档
├── settings.gradle.kts             # Gradle 设置文件
└── sky.sql                       # 数据库初始化脚本
```
