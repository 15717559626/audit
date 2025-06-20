FROM openjdk:8-jdk-slim

# 设置工作目录
WORKDIR /app

# 复制 jar 文件到容器中
COPY target/audit-0.0.1-SNAPSHOT.jar app.jar

# 暴露端口（根据你的应用端口调整，默认8080）
EXPOSE 8080

# 设置 JVM 参数和启动命令
ENTRYPOINT ["java", "-jar", "/app/app.jar"]