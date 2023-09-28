FROM openjdk:8
# 环境变量
# 内存参数
ENV JAVA_JVM_OPTS1=-Xms1024m
ENV JAVA_JVM_OPTS2=-Xmx1024m

#日志输出路径
ENV LOG_PATH=/var/log/Spring-websocket/
ENV LOG_FILE=/var/log/Spring-weboskcet/Spring-weboskcet.log


WORKDIR /opt/app

# 拷贝代码和配置文件
COPY *.jar /opt/app/app.jar
COPY ./config  /opt/app/config
# 拷贝脚本到可执行目录
COPY docker-entrypoint.sh /usr/local/bin/

EXPOSE 8888
# 授予权限
RUN chmod +x /usr/local/bin/docker-entrypoint.sh

ENTRYPOINT ["docker-entrypoint.sh"]
VOLUME /tmp
ADD weixing_java.jar /weixing_java.jar
EXPOSE 9898
ENV JAVA_HOME /usr/local/soft/jdk/jdk-17.0.1
ENV PATH $PATH:$JAVA_HOME/bin
CMD ["java","-jar","weixing_java.jar"]

