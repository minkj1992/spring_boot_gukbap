package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;


    //회원가입
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();      //Create시 생성한 Entity의 식별자를 return
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if (!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    //회원 조회
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    public List<Member> findMembers() {
        return memberRepository.findAll();
    }


    /**
     * 회원 수정
     */
    @Transactional  // commit 타임에 dirty checking (준영속성 -- transactional 시작, 영속성컨텍스트 생성 -- > 영속성 ---dirty checking--> update Query ---영속성 컨텍스트 종료, 트랜잭션 종료 ---->)
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);   // 준 영속성
        member.setName(name);
    }
}
