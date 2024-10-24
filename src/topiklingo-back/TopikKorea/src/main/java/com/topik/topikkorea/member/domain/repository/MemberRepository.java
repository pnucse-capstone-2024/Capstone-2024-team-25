package com.topik.topikkorea.member.domain.repository;

import com.topik.topikkorea.member.domain.AuthType;
import com.topik.topikkorea.member.domain.Member;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("select m from Member m where m.provider = :provider and m.providerId = :providerId and m.isDeleted = false")
    Optional<Member> findMemberByProviderAndProviderId(String provider, String providerId);

    @Query("select m from Member m where m.gather.id = :gatherId and m.isDeleted = false")
    Optional<List<Member>> findByGatherId(Long gatherId);

    @Query("select m from Member m where m.email = :email and m.isDeleted = false")
    Optional<Member> findByEmail(String email);

    @Modifying
    @Query("update Member m set m.authType = :authType where m.email = :email and m.isDeleted = false")
    void updateAuthTypeByEmail(String email, AuthType authType);

    @Modifying
    @Query("update Member m set m.isDeleted = true where m.id = :id")
    void deleteById(@Param("id") Long memberId);

    @Query("select m from Member m where m.isDeleted = false and m.authType = 3")
    List<Member> findAllStudents();

    @Query("select m from Member m where m.id = :id and m.isDeleted = false")
    Optional<Member> findById(@Param("id") Long memberId);
}
