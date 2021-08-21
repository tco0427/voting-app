package kr.itkoo.voting.domain.repository;

import java.util.List;
import java.util.Optional;

import kr.itkoo.voting.domain.entity.Vote;
import kr.itkoo.voting.domain.entity.VoteItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VoteItemRepository extends JpaRepository<VoteItem, Long> {
    public Optional<VoteItem> findByName(String name);

    public List<VoteItem> findByVote(Vote vote);
}