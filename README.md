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
- `application.yml`
    - `.properties`삭제
