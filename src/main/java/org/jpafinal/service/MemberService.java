package org.jpafinal.service;

import lombok.RequiredArgsConstructor;
import org.jpafinal.domain.Member;
import org.jpafinal.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    /**
     * 회원가입
     */
    @Transactional
    public Long join(Member member) {
        validateDuplicateMember(member);
        memberRepository.save(member);
        return member.getId();
    }

    /**
     * 중복회원 검증
     */
    private void validateDuplicateMember(Member member) {

        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }

    /**
     * 회원전체조회
     */
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    /**
     * 회원단건조회
     */
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

    /**
    * @author halfdev
    * @since 2020-01-13
    * @Transactional 있는 상태에서 findOne 을 하면 영속성 컨텍스트에서 가져온다.
    * 그 후, 값을 parameter 의 name 으로 넘어오는 것을  member.setName(name) 으로 바꾸면
    * Commit 되는 시점에서 JPA 가 변경감지를 하게되고 Update 쿼리가 실행된다.
    */
    @Transactional
    public void update(Long id, String name) {
        Member member = memberRepository.findOne(id);
        member.setName(name);
    }
}
