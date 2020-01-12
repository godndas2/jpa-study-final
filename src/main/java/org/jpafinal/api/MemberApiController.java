package org.jpafinal.api;

import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.jpafinal.common.CreateMemberResponse;
import org.jpafinal.domain.Member;
import org.jpafinal.dto.CreateMemberRequest;
import org.jpafinal.service.MemberService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
* @author halfdev
* @since 2020-01-13
* 1. Entity 를 외부에 노출하지말자. (Controller 에서 Param Entity 를 절대 쓰지말자)
* 2.별도의 DTO 를 생성해서 쓰자. Entity 를 바꿔도 Compile 에서 오류를 내주기도하고 API 에 영향을 주지 않음
* Entity 를 Param 으로 반환하면 해당 Entity 에 어떤 Field 를 받는지 파악하기 힘들다.
* 추가적으로, Entity 와 API Spec 을 명확하기 구분할 수 있고, Entity 가 변경되어도 API Spec 에 영향을 주지 않는다.
*/
@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    @PostMapping("/api/v1/members")
    public CreateMemberResponse saveMemberV1(@RequestBody @Valid Member member) {
        Long id = memberService.join(member);
        return new CreateMemberResponse(id);

    }

    @PostMapping("/api/v2/members")
    public CreateMemberResponse saveMemberV2(@RequestBody @Valid CreateMemberRequest request) {
        Member member = new Member();
        member.setName(request.getName());

        Long id = memberService.join(member);

        return new CreateMemberResponse(id);
    }
}
