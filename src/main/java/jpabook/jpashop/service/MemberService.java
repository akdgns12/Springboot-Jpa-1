package jpabook.jpashop.service;

import jpabook.jpashop.domain.Member;
import jpabook.jpashop.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
// Transactional - 트랜잭션, 영속성 컨텍스트
// readOnly=true - 데이터의 변경이 없는 읽기 전용 메서드에 사용, 영속성 컨텍스트를 플러시 하지 않으므로 약간의 성능 향상(읽기전용에는 다 적용), 데이터베이스 드라이버가 지원하면 DB에서 성능 향상
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MemberService {


    private final MemberRepository memberRepository;

//    아래 방법으로 생성자 Injection하는게 좋은 방법 -> @RequiredArgsConstructor라는 lombok어노테이션을 쓰면 final이 붙은 생성자를 밑과 같이 생성해줌
//    @Autowired // 생성자 Injection 많이 사용, 생성자가 하나면 생략 가능
//    public MemberService(MemberRepository memberRepository) {
//        this.memberRepository = memberRepository;
//    }

    /**
     * 회원 가입
     */
    @Transactional // 쓰기에는 @Transactional의 default값인 readonly = false가 적용
                    // class level에 @Transactional이 걸려있지만 이렇게 클래스 내부에 따로 걸려있다면 이곳이 우선권을 가짐
    public Long join(Member member) {
        validateDuplicateMember(member); // 중복 회원 검증
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicateMember(Member member) {
        List<Member> findMembers = memberRepository.findByName(member.getName());
        if(!findMembers.isEmpty()) {
            throw new IllegalStateException("이미 존재하는 회원입니다.");
        }
    }


    //회원 전체 조회
    public List<Member> findMembers() {
        return memberRepository.findAll();
    }

    //회원 단건 조회(하나만 조회)
    public Member findOne(Long memberId) {
        return memberRepository.findOne(memberId);
    }

}
