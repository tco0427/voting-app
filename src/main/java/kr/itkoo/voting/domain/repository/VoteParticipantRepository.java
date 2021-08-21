package kr.itkoo.voting.domain.repository;

import kr.itkoo.voting.domain.entity.VoteParticipant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface VoteParticipantRepository extends JpaRepository<VoteParticipant, Long> {

}
