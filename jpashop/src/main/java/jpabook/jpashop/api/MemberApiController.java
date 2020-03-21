package jpabook.jpashop.api;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.dto.CreateMemberRequest;
import jpabook.jpashop.dto.CreateMemberResponse;
import jpabook.jpashop.service.MemberService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
     * 등록 V1: 요청 값으로 Member Entity를 직접 받는다.
     */
    @PostMapping("api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);
    }

    /**
     * 등록 V2: 요청 값으로 Member Entity 대신에 DTO를 받는다.
     */
    @PostMapping("api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) { //생성자를 spring이 찾아주어야 하니, 디폴트 생성자 필요
        Member member = new Member();
        member.setName(request.getName());
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);    // new를 직접 호출해주니 디폴트 생성자 필요없다.
    }

}
