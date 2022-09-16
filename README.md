# study-spring5-data

=============

## 참고문헌
* 전문가를 위한 Spring5 개정판, 길벗출판사

## Environments
* Framework : Spring 5.0
* SDK : Azul zulu version 13.0.12
* Gradle : 6.2

------------------------------------------------------------------
## Embedded Database Settings
* Spring 3.0 부터 지원 
* 구동 시 임베디드 데이터베이스를 자동으로 시작하고 DataSource로 노출.
* Embedded Database 를 Spring 구성 파일에 정의
* Spring 4.0 기본 HSQL 지원, 이외에 H2, DERBY 가능 
```xml
    <jdbc:embedded-database id="dataSource" type="H2">
        <jdbc:script location="classpath:db/h2/schema.sql"/>
        <jdbc:script location="classpath:db/h2/test-data.sql"/>
    </jdbc:embedded-database>
```
* 지정한 id dataSource 를 참조로 jdbcTemplate Bean 을 정의한다. 
```xml
    <bean id="jdbcTemplate" class="org.springframework.jdbc.core.JdbcTemplate"
          p:dataSource-ref="dataSource" />
```
* 종료 시 실행할 메서드를 Bean 으로 정의해 DB 초기화할 수 있다. 
```xml
<bean class="com.sik.study.spring5.ch6.CleanUp" c:jdbcTemplate-ref="jdbcTemplate"
      destroy-method="destroy"/>
```



## Spring JDBC