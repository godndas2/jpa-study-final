package org.jpafinal.api;

import lombok.RequiredArgsConstructor;
import org.jpafinal.common.CreateMemberResponse;
import org.jpafinal.common.Result;
import org.jpafinal.common.UpdateMemberResponse;
import org.jpafinal.domain.Member;
import org.jpafinal.dto.CreateMemberRequest;
import org.jpafinal.dto.MemberDto;
import org.jpafinal.dto.UpdateMemberRequest;
import org.jpafinal.service.MemberService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.stream.Collectors;


@RestController
@RequiredArgsConstructor
public class MemberApiController {

    private final MemberService memberService;

    /**
    * @author halfdev
    * @since 2020-01-14
    * 목적 : 회원 정보 목록 API
    * Issue : List<Member> ref #1 과 같이 Entity 를 반환하면 해당 Entity 의 정보들이 외부에 전부 노출된다,
    * List 로 반환하면 JSON 으로 받을 때 Array 로 넘어온다. 즉, API 유연성이 떨어져버린다.
    * Resolve : 노출하지 않고자 하는 정보에 @JsonIgnore 를 붙여준다.
    * 문제점 : 다른 API 에서는 @JsonIgnore 하지 않은 정보가 필요하다면?
    */
    @GetMapping("/api/v1/members")
    public List<Member> membersV1() {
        return memberService.findMembers();
    }

    /**
    * @author halfdev
    * @since 2020-01-14
    * MemberDto 로 바꿔쳐준다.
    * Result.java 로 Array 가 아닌 Object 형태로 감싸준다.
    * API 와 DTO 가 1대1 로 FIT 하게 해주자
    */
    @GetMapping("/api/v2/members")
    public Result MemberV2() {
        List<Member> findMembers = memberService.findMembers();
        List<MemberDto> findMembersCollect = findMembers.stream()
                .map(member -> new MemberDto(member.getName()))
                .collect(Collectors.toList());

        return new Result(findMembersCollect, findMembersCollect.size());
    }

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

    @PutMapping("/api/v2/members/{id}")
    public UpdateMemberResponse updateMemberV2(@PathVariable("id") Long id,
                                               @RequestBody @Valid UpdateMemberRequest request) {

        memberService.update(id,request.getName());
        Member findMember = memberService.findOne(id);
        return new UpdateMemberResponse(findMember.getId(), findMember.getName());

    }
}
