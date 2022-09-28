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

------------------------------------------------------------------
## Spring JDBC
### JDBC Infrastructure
* 각 데이터베이스용 JDBC Driver 가 핵심. 
* JDBC Driver 로딩 시 자신을 java.sql.DriverManager 클래스에 등록하여 java.sql.Connection 인터페이스를 반환해 SQL을 실행

### DataSource in DAO Class 
* DAO 패턴은 저수준 데이터 엑세스 API 및 데이터 조작을 고수준 비즈니스 서비스와 분리할 때 사용.
* DAO 패턴의 구성
  * DAO interface : 모델 객체에서 수행될 표준 조작을 정의 
  * DAO 구현체 : JDBC Connection 또는 DataSource 를 사용하는 interface 구현체. 
  * Entity : 테이블 레코드에 매핑되는 간단한 POJO 객체. 
* 각 DAO 구현체는 Bean 초기화 시 dataSource 를 DI로 주입받는다. 
```xml
<bean id="jdbcSingerDao" class="com.sik.study.spring5.ch6.dao.JdbcSingerDao"
      c:dataSource-ref="dataSource" />
```

### JdbcTemplate Class
#### JdbcTemplate
* Spring JDBC 지원 기능의 핵심. 
* 모든 유형의 SQL문을 실행할 수 있으며 실행결과를 타입에 상관없이 반환할 수 있다.
* SQL Parameter 로 일반 위치 지정자(? 문자)를 사용, Object 배열로 전달.
```java
jdbcTemplate = new JdbcTemplate();
jdbcTemplate.setDataSource(dataSource);
```

#### NamedParameterJdbcTemplate
* 일반 위치 지정자가 아닌, Parameter Name 기반으로 바인딩. 

#### RowMapper<T>
* 데이터 여러 Row를 질의하여 엔티티로 변환하여 사용한다. 
* RowMapper<Entity Class> 를 이용해 JDBC ResultSet 을 POJO 객체로 매핑할 수 있다.

#### ResultSetExtractor
* RowMapper<T>는 단일 도메인 객체에만 매핑할 수 있다. 
* ResultSetExtractor 구현체를 이용해 JDBC ResultSet 을 보다 복잡한 객체 컬렉션으로 반환받을 수 있다.

