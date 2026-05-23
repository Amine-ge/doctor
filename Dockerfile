# 二开推荐阅读：如何提高项目构建效率
# https://developers.weixin.qq.com/miniprogram/dev/wxcloudrun/src/scene/build/speed.html

# =========================================================
# 第一阶段：构建阶段
# 使用 Maven + JDK8 编译 RuoYi 多模块项目
# =========================================================
FROM maven:3.8.8-eclipse-temurin-17 AS build

# 指定构建过程中的工作目录
WORKDIR /app

# 复制整个项目到容器中
# RuoYi 是多模块项目，不能只复制 src
# 必须把 ruoyi-admin、ruoyi-common、ruoyi-framework 等模块全部复制进去
COPY . /app

# 执行 Maven 打包
# -s /app/settings.xml：使用项目中的 Maven 镜像配置
# -f /app/pom.xml：指定父工程 pom.xml
# clean package：清理并打包
# -DskipTests：跳过测试，避免云端构建时因为测试类失败导致部署失败
RUN mvn -s /app/settings.xml -f /app/pom.xml clean package -DskipTests


# =========================================================
# 第二阶段：运行阶段
# 使用 Alpine + OpenJDK8 运行打包后的 jar
# =========================================================
FROM eclipse-temurin:17-jre-alpine

# 替换 Alpine 软件源为腾讯云镜像源，提高下载速度
# 安装 OpenJDK8 运行环境、HTTPS 证书、时区数据
RUN sed -i 's/dl-cdn.alpinelinux.org/mirrors.tencent.com/g' /etc/apk/repositories \
    && apk add --update --no-cache ca-certificates tzdata \
    && cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime \
    && echo Asia/Shanghai > /etc/timezone \
    && rm -f /var/cache/apk/*

# 指定运行时工作目录
WORKDIR /app

# 复制 ruoyi-admin 模块打包后的 jar 到运行目录
# RuoYi 多模块项目最终启动 jar 一般在 ruoyi-admin/target 目录下
# 这里统一改名为 app.jar，避免 jar 包名字不一致导致启动失败
COPY --from=build /app/ruoyi-admin/target/*.jar /app/app.jar

# 暴露端口
# 这里必须和：
# 1. ruoyi-admin/src/main/resources/application.yml 里的 server.port
# 2. 微信云托管服务配置里的端口
# 保持一致
EXPOSE 8080

# 启动 Spring Boot 项目
CMD ["java", "-jar", "/app/app.jar"]
