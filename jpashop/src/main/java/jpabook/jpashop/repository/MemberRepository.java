package jpabook.jpashop.repository;

import jpabook.jpashop.domain.Member;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Repository //SQL_Exception 투명하게 에러 핸들링 가능
public class MemberRepository {

    @PersistenceContext //JPA로부터 EntityManager를 DI받을 수 있다. (어떤 DB 의존성가질 것인가 등의 정보를 자동으로 처리, 생명주기)
    private EntityManager em;

    public void save(Member member) {
        em.persist(member);
    }

    public Member findOne(Long id) {
        return em.find(Member.class, id);
    }

    public List<Member> findAll() {
        //데이터베이스에서 필터해서 조회해올 무언가가 필요하고, 그게 객체지향 쿼리 언어(JPQL)이다.
        return em.createQuery("select m from Member m", Member.class)
                .getResultList();
    }

    public List<Member> findByName(String name) {
        return em.createQuery("select m from Member m where m.name = :name", Member.class)
                .setParameter("name", name)
                .getResultList();
    }
}
