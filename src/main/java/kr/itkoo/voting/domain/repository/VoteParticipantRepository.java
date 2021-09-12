package kr.itkoo.voting.domain.repository;

import kr.itkoo.voting.domain.entity.VoteParticipant;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VoteParticipantRepository extends JpaRepository<VoteParticipant, Long> {
    Slice<VoteParticipant> findByUserId(Long userId, Pageable pageable);
}
