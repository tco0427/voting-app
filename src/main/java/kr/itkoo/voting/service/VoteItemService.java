package kr.itkoo.voting.service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import kr.itkoo.voting.domain.entity.Vote;
import kr.itkoo.voting.domain.entity.VoteItem;
import kr.itkoo.voting.domain.repository.VoteItemRepository;
import kr.itkoo.voting.domain.repository.VoteRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class VoteItemService {

    private final VoteItemRepository voteItemRepository;
    private final VoteRepository voteRepository;

    public VoteItem findById(Long id) {
        return voteItemRepository.findById(id)
                .orElseThrow(NoSuchElementException::new);
    }

    public List<VoteItem> findAll() {
        return voteItemRepository.findAll();
    }

    public List<VoteItem> findAllByVoteId(Long voteId) throws Exception {
        Optional<Vote> voteOptional = voteRepository.findById(voteId);
        if(voteOptional.isEmpty()){
            throw new Exception();
        }
        return voteItemRepository.findByVote(voteOptional.get());
    }

    @Transactional
    public Long save(VoteItem voteItem) {
        validateDuplicateItem(voteItem);
        VoteItem save = voteItemRepository.save(voteItem);
        return save.getId();
    }

    private void validateDuplicateItem(VoteItem voteItem) {
        voteItemRepository.findByName(voteItem.getName())
            .ifPresent(v -> {
                throw new IllegalStateException("이미 존재하는 아이템입니다.");
            });
    }

    @Transactional
    public void update(Long id, String name) {
        VoteItem voteItem = voteItemRepository.findById(id).get();
        voteItem.setName(name);
    }

    public void deleteById(Long id) {
        voteItemRepository.deleteById(id);
    }
}
