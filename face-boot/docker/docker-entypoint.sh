#!/bin/bash

# JVM参数
JAVA_OPTS=
#  当JAVA_JVM_OPTS1长度大于0时为真
if [ -n "${JAVA_JVM_OPTS1}" ] ; then
  JAVA_OPTS="${JAVA_OPTS} ${JAVA_JVM_OPTS1}"
fi

if [ -n "${JAVA_JVM_OPTS2}" ] ; then
  JAVA_OPTS="${JAVA_OPTS} ${JAVA_JVM_OPTS2}"
fi
#  当LOG_OPTS长度==0时为真
LOG_OPTS=
if [ -z "${LOG_OPTS}" ] ; then
  LOG_OPTS="-Dlogging.file.name=${LOG_FILE} -Dlogging.file.path=${LOG_PATH}"
fi

# 打印配置参数
echo "JVM启动参数："${JAVA_OPTS}
echo "日志参数："${LOG_OPTS}
java -server ${JAVA_OPTS} ${LOG_OPTS} -jar  /opt/app/app.jar

