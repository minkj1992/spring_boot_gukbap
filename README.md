# JPABook
> SpringBoot + JPA + thymeLeaf + H2 DB

## **[1] 설계 원칙**
#### Loosed Coupling & High Chesion)
1. 모듈간의 연관관계가 interface로 느슨하게 연결되어야 한다.
2. 어떤 목적을 위해 연관된 기능들이 모여서 구현되고 지나치게 많은 일을 하지 않는다.

이를 구현하기 위해 DDD(Domain Driven Design)의 몇가지 원칙을 적용하겠다.

#### 역할에 따른 Domain Model
- Domain(Entity, VO)
    - Entity
        - JPA의 @Table과 매핑
        - Business Logic은 `Service`가 아닌, 상태값을 지닌 Entity가 구현하도록 한다. (`OOP에서의 객체는 상태와 행위를 가진다.`)
    - VO는 (Entity)Status와 같이 부가정보, 상태, 등급같은 Class, Enum을 활용
- Aggregate
    - CASCADE mapping을 활용하여 Aggregate 생명주기 관리한다.
    - 복잡한 Aggregate는 쪼개서 로딩 효율을 높이는 것 대신 JPA Lazy loading을 사용해 필요한 시점에 필요한 데이터들만 로딩되도록 하여 개발 효율성과 속도적인 우의를 가진다.
- Service
    - 트랜잭션 처리
    - 도메인 간의 연산 처리
    - Repository들을 field로 둔다.
    - thin layer
- Repository
    - JPA를 직접 사용하는 계층
    - Domain 모델 저장
    - EntityManager를 field로 두어, Entity, Aggregate Life Cycle을 관리
    - `JpaRepository interface`를 상속한다면 Factory를 활용해 객체 생성
- Factory
    - Entity, Aggregate 생성

- c.f) DAO/DTO 개념은 `TRANSACTION SCRIPT 패턴` 개념에서 사용하는 용어로 `DDD`개념과는 다른 개념 용어이다.

#### 레이어별 Domain Model
1. 우선 Context로 분리
2. 이후 Context안에 존재하는 Layer별 분리한다.
3. user_interface -> application -> domain -> infrastructure 순서로 layer가 나뉘며, 상위 레이어는 하위 레이어를 의존한다. (user interface는 모든 하위 레이어를 의존 가능하며, 반대로 infrastructure는 다른 레이어를 의존하면 안된다.)

- Layer 종류
    - interface
        - 역할 : User interface ( 사용자 요청을 하위 layer에 전달 )
        - @Controller
        - @Component
            - 응답 객체 Cache Service
    - application
        - @Service
            - 복잡한 비즈니스 로직
            - Domain 조합 
    - domain
        - JPA의 Entity (비즈니스 로직)
        - Factory
    - infrastructure
        - 외부 통신하는 @Component
        - 영속성 구현 Dao

## **[2] 개발과정**

#### 1. 세팅

1. 프로젝트 생성
    - start.spring.io
    - dependency 설정
2. `Lombok`, `thyme-leaf` 실행 확인
    - `IntelliJ Lombok cannot find symbol 컴파일 에러`
        - [해결법](http://blog.devenjoy.com/?p=383)
        -  `IntelliJ Preferences => Build, Excution, Deployment => Compiler => Annotation Processors-> Enable annotation processing 체크` 
3. `h2`
    - 안정화 버전 1.4.199 설치
        - `ddl-auto : create`시, 테이블 Drop할때 에러가 뜨기 떄문에, 안정화된 버전을 사용한다.
    - `chmod 755 h2.sh`
        - ubuntu 실행 권한 부여
    - embedded로 `.mv`파일 먼저 생성후, spring 돌릴때는 tcp 통신
4. `application.yml`
    - `.properties`삭제

5. `JPA & DB` 실행 확인
    - Entity 생성
        - `Member`
            - `org.springframework.data.annotation.Id`는 non-relational DB
            - `javax.persistence.Id`는 relational data
    - Repository 생성
        - EntityManager `@PersistenceContext`
    - test
        - JAVA object가 JPA를 통해 EntityManager를 거쳐 생성해준 Repository를 거쳐 Getter를 통해 Id를 return한 값과, 제일 처음 시도했던 java object의 getID값과의 차이가 있는지 여부를 확인하는 테스트

#### 2. Domain 개발
> Entity Constructor는 Protected with Lombok Annotation
1. Member Entity 생성
    - 회원
2. Order ENtity 생성
    - 주문
    - OrderStatus VO 생성
3. OrderItem Entity생성
    - 상품과 주문서의 관계를 가진 Entity
4. Item Entity 생성
    - Child Entity 생성
        - Album 
        - Book
        - Movie
5. Delivery Entity 생성
    - DeliveryStatus VO 생성
6. Address VO생성
    - `@Embeddable`, `Enum`

**컬렉션은 필드에서 초기화 하자.**

#### 3. Repository / Service 개발
1. MemberRepository 생성
    - @Repository: SQL_Exception 투명하게 에러 핸들링 가능
    - @PersistenceContext
        - JPA로부터 EntityManager를 DI받을 수 있다. (어떤 DB 의존성가질 것인가 등의 정보를 자동으로 처리, 생명주기)
        - Repository는 EntityManager를 Autowired시켜준다 (@PersistenceContext)
    - em.createQuery는 javax.persistence.Query (JPQL)

    - 참고로 Repository의 EntityManager도 주입 가능
    ```java
    @PersistenceContext //JPA로부터 EntityManager를DI받을 수 있다. (어떤 DB 의존성가질 것인가 등의 정보를자동으로 처리, 생명주기)
    private EntityManager em;
    ```
    ```java
    @Repository
    @RequiredArgsConstructor
    public class MemberRepository {
        private final EntityManager em;
        ...
    }
    ```
2. MemberService 생성
    - Entity값 Validation Code ( `validateDuplicateMember()` )
    - Repository에 CRUD 요청위임 method()들
        - SRP (Single Resposibility Principle) 하나의 class는 하나의 역할만 한다.
            - **Service는 다양한 Repository들을 모아서 logic들을 처리해주어야 한다. 그러므로 여러 Repository에게 위임한다.**
    - `@Transactional` 처리
        - readOnly를 통해서 최적화 시켜주고 Write가 필요한 작업만 Transactional 붙여준다.
    - 서비스는 트랜잭션의 단위이다.

3. MemberServiceTest()
- 테스트
    - 회원가입
        - 문제가 `@NoArgsConstructor(access = AccessLevel.PROTECTED)`로 `Member`를 보호해주었는데, test하기 위해서는 풀어주어야 하나? @Builder를 사용하면 되지 않을까? 
        - Member Entity 근본부터 잘못되었다. 근본은 Entity의 Setter는 닫아두고 Builder Pattern을 사용해서 생성자를 만들어준다. (ID를 제외한)
        - 이후 DTO를 사용하여 UPDATE와 관련된 Method들을 처리해준다.  -> 아니면 setter 열어두고 repository를 통해서 WAS단에서 수정된 데이터들을 proxy객체를 통해서 수정해준다.
        - **절대로 테이블과 매핑되는 Entity 클래스를 Request / Response 클래스로 사용해선 안됨.**

        - update 코드에 대한 Stack Overflow 글
            - 
            ```java
                Customer customerToUpdate = customerRepository.getOne(id);
                customerToUpdate.setName(customerDto.getName);
                customerRepository.save(customerToUpdate);
            ```
                - SQL상에서도 Update Query만 생성된다.
    
        - `Caused by: org.hibernate.AnnotationException: Illegal attempt to map a non collection as a @OneToMany, @ManyToMany or @CollectionOfElements: jpabook.jpashop.domain.Category.parent` 에러 발생
            - `@ManyToOne(fetch = FetchType.LAZY)`로 Parent를 바꿔주었다.
        - ` Invocation of init method failed; nested exception is org.hibernate.AnnotationException: No identifier specified for entity: jpabook.jpashop.domain.item.Movie`
            - `public class Movie extends Item` extend를 빼먹었었다.

        - `java.lang.Exception: Test class should have exactly one public zero-argument constructor`, 테스트 환경에서는 `@RequiredArgsConstructor`를 통한 injection이 통하지 않는다. 즉 @Autowired 써주어야 한다.
    
    - 중복회원 예외
    
    - @TODO: **Entity에서 @Setter를 없애줄 수 있는 방법은 무엇일까?**
        - builder pattern

4. Item Entity
- Business Logic ( add , remove )
- Exception 생성
    - `NotEnoughStockException`

5. Item Repository
- Save()
    - persist()
    - @TODO: update용 코드를 위한 `.merge()`는 사용하지 않는다, 추후 준영속성 엔티티는 영속성을 부여해주어 처리한다.
- findOne()
- findAll()

6. Item Service
- Service는 SRP 원칙에 의해 Repository에게 위임한다. Service의 기능은 Transactional과 validate이고 validate 코드는 Entity에게 위임한다.

- Save()
    - 트랜잭션 처리
- findOne()
- findAll()


7. Order Entity
- 비즈니스 로직
    - 주문 취소
        - Order가 가진 주문 상태가 배송이 아니라면, 모든 OrderItem들을 Cancel 시켜준다.
        - `orderItem.cancel();` -> `deligate`
- 복잡한 생성 로직
    - setter는 내부에서만 사용하게 하기 위해 생성 메서드를 둔다.
    - 좋은 방법은 아니지만, BaseBean을 상속받는 것보다 비즈니스 로직 먼저 생성하는 것이 옳다고 생각한다.
- 조회 로직
    - **`int totalPrice` 필드를 두고 add, cancel 때마다 상태를 변경해준다.**
    - 주문을 한다면 : item의 잔고는 줄여주고, Order의 가격은 늘려주고
    - 주문을 취소한다면: item 잔고는 늘려주고, Order의 가격은 줄여주고

8. OrderItem Entity
- 주문취소
    - 취소될 때 Order에게 update되어야할 가격을 전달해준다.
- 복잡한 생성자







