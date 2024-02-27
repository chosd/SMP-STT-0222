# openjdk 11 alpine linux 이미지를 다운받는다.
FROM adoptopenjdk/openjdk11:alpine

# timezone을 서울로 설정한다.
ENV TZ=Asia/Seoul
RUN apk add --no-cache tzdata && \
    cp /usr/share/zoneinfo/$TZ /etc/localtime && \
    echo $TZ > /etc/timezone

### 볼륨 설정
ARG NAS_HOME
ARG BASE_DIR
ENV STT_BASE_DIR=$BASE_DIR
ENV NAS_HOME=${NAS_HOME}
ENV STT_LOG_PATH_HOME="${STT_BASE_DIR}/logs"

## 컨테이너 내부에 파일 저장 경로 생성 (기본 root로 생성)
RUN mkdir -p $NAS_HOME
RUN mkdir -p $STT_LOG_PATH_HOME

### App 실행
# JAR 파일 copy. JAR_FILE는 pom.xml에서 argument로 넣어준다.
COPY target/*.jar app.jar

# PROFILE 변수 설정. PROFILE는 pom.xml에서 argument로 넣어준다.
ARG PROFILE
ENV SPRING_PROFILE=$PROFILE

ENTRYPOINT ["java", "-Dspring.profiles.active=${SPRING_PROFILE}", "-jar","/app.jar"]
