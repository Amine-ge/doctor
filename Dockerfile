# 二开推荐阅读：https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/scene/build/speed.html
# 当前项目：RuoYi 多模块 Spring Boot 项目
# 后端端口：8080
# Java 版本：17

# =========================
# 第一阶段：Maven 构建阶段
# =========================
FROM maven:3.8.8-eclipse-temurin-17 AS build

# 指定构建工作目录
WORKDIR /app

# 拷贝整个项目到容器中
# RuoYi 是多模块项目，不能只 COPY src 和 pom.xml
COPY . /app

# 使用项目中的 settings.xml 构建，如果你的项目没有 settings.xml，把这一行改成：
# RUN mvn clean package -DskipTests
RUN mvn -s /app/settings.xml clean package -DskipTests


# =========================
# 第二阶段：运行阶段
# =========================
FROM eclipse-temurin:17-jre

# 安装 HTTPS CA 证书，解决调用微信 HTTPS 接口证书链校验失败问题
RUN apt-get update \
    && apt-get install -y ca-certificates \
    && update-ca-certificates \
    && rm -rf /var/lib/apt/lists/*

# 设置时区为上海时间，可选
ENV TZ=Asia/Shanghai

# 指定运行目录
WORKDIR /app

# 拷贝 RuoYi admin 模块构建出的 jar 包
COPY --from=build /app/ruoyi-admin/target/*.jar /app/app.jar

# 暴露端口
# 必须和 application.yml 的 server.port、微信云托管服务端口一致
EXPOSE 8080

# 启动命令
CMD ["java", "-jar", "/app/app.jar"]