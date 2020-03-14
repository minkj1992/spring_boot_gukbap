# JPABook
> SpringBoot + JPA + thymeLeaf + H2 DB


## 1

- 프로젝트 생성
    - start.spring.io
    - dependency 설정
- `Lombok`, `thyme-leaf` 실행 확인
    - `IntelliJ Lombok cannot find symbol 컴파일 에러`
        - [해결법](http://blog.devenjoy.com/?p=383)
        -  `IntelliJ Preferences => Build, Excution, Deployment => Compiler => Annotation Processors-> Enable annotation processing 체크` 
- `h2`
    - 안정화 버전 1.4.199 설치
        - `ddl-auto : create`시, 테이블 Drop할때 에러가 뜨기 떄문에, 안정화된 버전을 사용한다.
    - `chmod 755 h2.sh`
        - ubuntu 실행 권한 부여
    - embedded로 `.mv`파일 먼저 생성후, spring 돌릴때는 tcp 통신
- `application.yml`
    - `.properties`삭제

- `JPA & DB` 실행 확인
    - Entity 생성
        - `Member`
            - `org.springframework.data.annotation.Id`는 non-relational DB
            - `javax.persistence.Id`는 relational data
    - Repository 생성
        - EntityManager `@PersistenceContext`
    - test
        - JAVA object가 JPA를 통해 EntityManager를 거쳐 생성해준 Repository를 거쳐 Getter를 통해 Id를 return한 값과, 제일 처음 시도했던 java object의 getID값과의 차이가 있는지 여부를 확인하는 테스트
