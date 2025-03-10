![Kotlin](https://img.shields.io/badge/kotlin-%237F52FF.svg?style=for-the-badge&logo=kotlin&logoColor=white)
![Spring](https://img.shields.io/badge/spring-%236DB33F.svg?style=for-the-badge&logo=spring&logoColor=white)
![Postgres](https://img.shields.io/badge/postgres-%23316192.svg?style=for-the-badge&logo=postgresql&logoColor=white)
![Amazon S3](https://img.shields.io/badge/Amazon%20S3-FF9900?style=for-the-badge&logo=amazons3&logoColor=white)
![Redis](https://img.shields.io/badge/redis-%23DD0031.svg?style=for-the-badge&logo=redis&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Gradle](https://img.shields.io/badge/Gradle-02303A.svg?style=for-the-badge&logo=Gradle&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)
![Swagger](https://img.shields.io/badge/-Swagger-%23Clojure?style=for-the-badge&logo=swagger&logoColor=white)

# Cloud file storage
Реализация проекта №6 из [роадмапа](https://zhukovsd.github.io/java-backend-learning-course/projects/cloud-file-storage/) на Kotlin.

## Использованные технологии:
- **Spring Boot**
- **Spring Security, Spring Sessions**
- **Spring Data Jpa**
- **MapStruct**
- **Gradle**
- **Postgres**
- **Minio**
- **Redis**
- **Swagger**
- **Testcontainers**
- **Docker**

## Frontend
Фрронт взят из [репозитория](https://github.com/zhukovsd/cloud-storage-frontend)

## Запуск
1) Собрать Docker image через [jib](https://github.com/GoogleContainerTools/jib) с помощью команды
```
gradle jibDockerBuild
```
2) В корне проекта создать .env файл по следующему шаблону
```
POSTGRES_USER=
POSTGRES_PASSWORD=
POSTGRES_DB=
POSTGRES_URL=

MINIO_URL=
MINIO_ROOT_USER=
MINIO_ROOT_PASSWORD=
MINIO_DEFAULT_BUCKETS=

REDIS_HOST=
REDIS_PASSWORD=
REDIS_PORT=
REDIS_DATABASES=
```
3) Запустить docker-compose
```
docker-compose -f docker-compose-prod.yml up -d
```
