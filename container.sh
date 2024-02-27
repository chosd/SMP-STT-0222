# 도커 이미지 빌드
docker build -t smp/stt . --build-arg NAS_HOME=/data/stt --build-arg PROFILE=dev

# 이전 도커 컨테이너 정지 및 삭제
docker stop smp_stt || true && docker rm smp_stt

# 도커 컨테이너 실행
docker run -d -p 8081:8081 --name smp_stt smp/stt
