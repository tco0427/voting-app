package kr.itkoo.voting.service;

import kr.itkoo.voting.domain.entity.VoteParticipant;
import kr.itkoo.voting.domain.repository.VoteParticipantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VoteParticipantService {

    private final VoteParticipantRepository voteParticipantRepository;

    public Long save(VoteParticipant voteParticipant) {
        VoteParticipant savedVoteParticipant = voteParticipantRepository.save(voteParticipant);

        return savedVoteParticipant.getId();
    }

    public List<VoteParticipant> getVoteParticipantByUserId(Long userId, PageRequest pageRequest){
        return voteParticipantRepository.findByUserId(userId, pageRequest).getContent();
    }
}
