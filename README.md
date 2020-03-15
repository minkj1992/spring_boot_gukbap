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

#### 3. Repository 개발


