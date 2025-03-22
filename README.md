# MyBlog
Приложение-блог

## Технологии и инструменты:
Java 21, Spring Boot v3.3.10 (Web, Validation), JDBC, H2, Maven, Lombok, MapStruct, JUnit, Mockito, Tomcat.

## Инструкция по развёртыванию и системные требования
Min CPU/Core: 1/1, RAM:1Gb.

1. Собрать проект ```gradle bootJar```;
2. Запустить ```java -jar  build/libs/myblog-0.0.1-SNAPSHOT.jar```;
3. Открыть браузер и перейти: http://127.0.0.1:8081/posts;
