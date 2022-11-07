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

### Spring JDBC Classes
* MappingSqlQuery<T> : SQL 문자열과 mapRow() 메서드를 한 클래스로 감싸 준다. 
* SqlUpdate : 모든 Update SQL 문을 Wrapping할 수 있다.
* BatchSqlUpdate : 다수의 SQL문을 Batch 로 실행한다. 
* SqlFunction<T> : 데이터베이스 함수에 인자 및 반환 타입을 지정해 호출. 
* @Repository, @Resource : JDBC DAO를 설정할 때 사용

------------------------------------------------------------------
## Spring Hibernate
* ORM (Object-Relational Mapping) 라이브러리.
* 하이버네이트가 직접 생성하는 SQL을 제어할 수는 없으므로 연관관계, 즉시/지연로딩 전략을 정의할 때 주의.
* batch size, fetch size 를 조절하여 최적 수준을 찾아야 함. 

#### Dependencies
```groovy
dependencies {
    compile "javax.annotation:javax.annotation-api:1.3.2"
    compile "org.springframework:spring-context-support:5.3.22"
    compile "org.springframework:spring-orm:5.3.22"
    compile "org.hibernate:hibernate-entitymanager:5.6.5.Final"
    ...
    testCompile testing.junit
}
```

### Entity 
* @Entity 애너테이션을 적용하여 엔티티 클래스임을 명시 
* @Table 및 @Column 애너테이션으로 매핑 정의
* @Id : Primary Key를 정의. @GeneratedValue 으로 id 값 생성 방법을 함께 정의. 
* @Version : version 이 선언되면 하이버네이트는 변경감지를 통해 값을 증가시킴. 

#### Relation
##### one-to-many
* 1:N 관계로 가장 자주 사용되는 관계. 
```java
private Set<Album> albums = new HashSet<>();

@OneToMany(mappedBy = "singer", cascade = CascadeType.ALL, orphanRemoval = true)
public Set<Album> getAlbums() {
    return albums;
}
```
```java
private Singer singer;

@ManyToOne
@JoinColumn(name = "SINGER_ID")
public Singer getSinger() {
    return singer;
}
```

##### many-to-many
* JOIN TABLE 을 해 다수의 레코드간의 N:N 관계를 정의함. 
```java
private Set<Instrument> instruments = new HashSet<>();

@ManyToMany
@JoinTable(name = "singer_instrument", joinColumns = @JoinColumn(name = "SINGER_ID"), inverseJoinColumns = @JoinColumn(name = "INSTRUMENT_ID"))
public Set<Instrument> getInstruments() {
    return instruments;
}
```
```java
private Set<Singer> singers = new HashSet<>();

@ManyToMany
@JoinTable(name = "singer_instrument", joinColumns = @JoinColumn(name = "INSTRUMENT_ID"), inverseJoinColumns = @JoinColumn(name = "SINGER_ID"))
public Set<Singer> getSingers() {
    return singers;
}
```

### Hibernate Session
* 데이터베이스 조작을 위해 SessionFactory 로부터 Session 을 구한다. 
* @Repository 애너테이션을 적용해 스프링 빈으로 선언. 트랜잭션 요구사항을 정의할 때 사용한다. 
* @Resource 애너테이션을 이용해 sessionFactory 애트리뷰트를 주입. 
```java
@Repository("singerDao")
public class SingerDaoImpl implements SingerDao {
  private SessionFactory sessionFactory;
  
  @Resource(name = "sessionFactory")
  public void setSessionFactory(SessionFactory sessionFactory) {
    this.sessionFactory = sessionFactory;
  }
  ... 
}
```

#### Hibernate Entity 로 Table 생성하기
* Hibernate를 사용한 개발 시, 엔티티를 작성하고 테이블을 생성하는 것이 일반적.
* hibernate.hbm2ddl.auto 
  * create-drop 
    * 애플리케이션 구동 시 엔티티를 스캔하여 테이블을 만들고 관계 설정에 맞춰 제약조건을 구성. 
    * 매 실행 시 기존 데이터베이스는 초기화.
    * 테스트용 인메모리 데이터로 적합.
  * update 
    * 테이블 생성이 완료된 후 변경. 
    * 엔티티에 발생한 수정 사항만을 데이터베이스에 적용. 
```java
hibernateProp.put("hibernate.hbm2ddl.auto", "create-drop");
```


------------------------------------------------------------------
## Spring JPA
* Java Persistence API. 
* 하이버네이트의 영향을 받은 자바 데이터 액세스 기술 표준. 
* JPQL (Java Persistence Query Language) 를 사용, DBMS에 무관하게 적용 가능함. 

#### Dependencies
```groovy
dependencies {
  compile "org.hibernate:hibernate-entitymanager:5.6.5.Final"
  compile "org.hibernate.javax.persistence:hibernate-jpa-2.1-api:1.0.2.Final"
  ...
  testCompile testing.junit
}
```

* JPA 설정에 정의한 내용과 같이 EntityManagerFactory 를 통해 EntityManager가 서비스 클래스에서 주입된다.
```java
@PersistenceContext
private EntityManager em;
```

### JPA2 Criteria API Query
* 사용자가 입력할 가능성이 있는 조건의 조합 경우의 수만큼 쿼리를 사전에 준비하기는 어려움. 
* JPA2 에서 강타입(Strong Typed) Criteria API Query를 제공함. 
  * 엔티티 클래스의 메타 모델을 기반으로 검색 조건 전달. 
  * 엔티티 클래스 메타 모델은 클래스명 뒤에 언더스코어(_)를 붙여 생성. 
* 하이버네이트는 메타 모델 생성기라는 도구를 제공함.
```groovy
dependencies {
  annotationProcessor "org.hibernate:hibernate-jpamodelgen:5.6.5.Final"
  compile "com.mysema.querydsl:querydsl-apt:2.7.1"
  ...
}

task generateQueryDSL(type: JavaCompile, group: 'build', description: 'QueryDSL Query Type Generating.') {
  source = sourceSets.main.java
  classpath = configurations.compile + configurations.querydslapt
  options.compilerArgs = [
          "-proc:only",
          "-processor", "org.hibernate.jpamodelgen.JPAMetaModelEntityProcessor"
  ]
  destinationDir = sourceSets.generated.java.srcDirs.iterator().next()
}
compileJava.dependsOn generateQueryDSL
```

## Spring Data JPA
* Spring JPA 의 하위 프로젝트로, JPA 애플리케이션 개발의 단순화 목적.
* Repository Class 를 추상화하며 Entity Listener 를 이용해 Entity Class 의 기본적인 감사 정보를 추적.


### Spring Data JPA Repository
* Repository 인터페이스를 확장한 다양한 인터페이스를 제공. 
* CrudRepository 인터페이스에 기본적인 CRUD 동작이 추상화되어 있어 일일히 구현할 필요가 없다. 
  * 인터페이스 상속 시 제네릭 타입으로 엔티티와 ID타입을 전달한다. 
* JpaRepository 는 CrudRepository 보다 진보된 형태로, 배치, 페이징, 정렬 기능을 제공. 

#### Dependencies & Config
```groovy
dependencies {
  compile "org.springframework.data:spring-data-jpa:2.0.11.RELEASE"
  ...
  testCompile testing.junit
}
```
* Data JPA Repository 구현체를 빈으로 등록하기 위한 정의 필요.
```java
@Configuration
...
@EnableJpaRepositories(basePackages = {"com.sik.study.spring5.ch8"})
public class DataJpaConfig {
  ...
}
```


### 데이터의 감사 (Audit)
* 데이터의 생성자, 생성시각, 수정자, 수정시각.
* Auditable Interface 상속을 통해 JPA Entity Listener 로서 감사 정보를 자동 추적하는 기능을 제공함. 

#### Config
* Jpa의 감사기능 활성화를 위한 선언 필요.
* AuditorAwareBean 클래스는 작업을 수행하는 사용자 이름을 반환. 
  * 서비스에서는 보안 인프라스트럭쳐로부터 사용자 정보를 반환할것.
```java
@Configuration
...
@EnableJpaAuditing(auditorAwareRef = "auditorAwareBean")
public class DataJpaConfig {
  ...
}

@Component
public class AuditorAwareBean implements AuditorAware<String> {
    public Optional<String> getCurrentAuditor() {
        return Optional.of("prospring5") // User Name
    }
}
```



#### AuditableEntity
* @EntityListener 애너테이션을 부여하고, 추적 대상 컬럼에 속성별 애너테이션을 부여한다.
```java
@Entity
@EntityListener(AuditingEntityListener.class)
@Table(name = "singer_audit")
public class SingerAudit implements Serializable {
  ...
  @CreatedDate
  @Column(name = "CREATED_DATE")
  @Temporal(TemporalType.TIMESTAMP)
  private Date createdDate;
  ...
}
```
* 아래와 같이 상속을 통해 간소화할 수 있다. 
```java
@Entity
@Table(name = "singer_audit")
public class SingerAudit implements AuditableEntity<SingerAudit> {
  ...
}
```


### Entity Version Control
* 엔티티의 테이블마다 이력 테이블을 구성. 
* 기본 구성은 엔티티와 동일하며 아래의 컬럼을 추가해 버전을 관리. 
  * AUDIT_REVISION : 이력 레코드의 시작 개정 번호
  * ACTION_TYPE : 조작 유형. 추가(0) 수정(1) 삭제(2)
  * AUDIT_REVISION_END : 이력 레코드의 마지막 개정 번호 
  * AUDIT_REVISION_END_TS : 마지막 개정이 수정된 시점의 타임스탬프

* AuditEventListener 는 다양한 퍼시스턴스 이벤트를 가로채 엔티티 클래스의 수정 전 스냅샷을 이력 테이블에 저장.
* 엔티티 클래스에 @Audited 애너테이션을 부여하여 버전관리 활성화.
* 특정 필드를 감사 대상에서 제외하고자 하는 경우 @NotAudited 를 적용.


#### Config
```java
@EnableJpaAuditing(auditorAwareRef = "auditorAwareBean")
public class EnversConfig {
  ...

  public static Properties hibernateProperties() {
    Properties hibernateProp = new Properties();
    
    ...

    //Properties for Hibernate Envers
    hibernateProp.put("org.hibernate.envers.audit_table_suffix", "_H");                 // 이력 테이블 접미어
    hibernateProp.put("org.hibernate.envers.revision_field_name", "AUDIT_REVISION");    // 개정 번호 컬럼 지정
    hibernateProp.put("org.hibernate.envers.revision_type_field_name", "ACTION_TYPE");  // 조작 유형 컬럼 지정
    hibernateProp.put("org.hibernate.envers.audit_strategy", "org.hibernate.envers.strategy.ValidityAuditStrategy"); // 엔티티 버전 관리 감사 전략 
    hibernateProp.put("org.hibernate.envers.audit_strategy_validity_end_rev_field_name", "AUDIT_REVISION_END"); // 이력 레코드 마지막 개정 번호 컬럼 지정
    hibernateProp.put("org.hibernate.envers.audit_strategy_validity_store_revend_timestamp", "True"); // 마지막 개정번호의 타임스탬프 저장여부
    hibernateProp.put("org.hibernate.envers.audit_strategy_validity_revend_timestamp_field_name",
            "AUDIT_REVISION_END_TS");
    return hibernateProp;
  }
}

```

## Spring Boot JPA
* 엔티티, 데이터베이스, 저장소, 서비스를 포함한 모든 항목을 기본 제공.
* 별도의 Configuration Class 가 필요치 않음. 

```groovy
dependencies {
  compile "org.springframework.boot:spring-boot-starter-data-jpa:2.0.6.RELEASE"
  compile "com.h2database:h2:2.1.214"
  ...
}
```