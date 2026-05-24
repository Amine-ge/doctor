# 当前项目：RuoYi 多模块 Spring Boot 项目
# 后端端口：8080
# Java 版本：17

FROM maven:3.8.8-eclipse-temurin-17 AS build

WORKDIR /app

COPY . /app

RUN mvn -s /app/settings.xml clean package -DskipTests


FROM eclipse-temurin:17-jre-jammy

RUN apt-get update \
    && apt-get install -y --no-install-recommends ca-certificates ca-certificates-java \
    && update-ca-certificates \
    && rm -rf /var/lib/apt/lists/*

ENV TZ=Asia/Shanghai

WORKDIR /app

COPY --from=build /app/ruoyi-admin/target/*.jar /app/app.jar

EXPOSE 8080

CMD ["java", "-jar", "/app/app.jar"]