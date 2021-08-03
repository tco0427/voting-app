package kr.itkoo.voting.service;

import kr.itkoo.voting.domain.entity.Vote;
import kr.itkoo.voting.domain.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VoteService {

    private final VoteRepository voteRepository;

    public Optional<Vote> findById(Long id){
        return voteRepository.findById(id);
    }

    public List<Vote> findAll(){
        return voteRepository.findAll();
    }

    public Long join(Vote vote){
        Vote save = voteRepository.save(vote);
        return save.getId();
    }

    @Transactional
    public void update(Long id, String title, Integer updatedAt){
        Vote vote = voteRepository.findById(id).get();
        vote.setTitle(title);
        vote.setUpdatedAt(updatedAt);
    }
}
