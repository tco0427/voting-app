package kr.itkoo.voting.service;

import java.util.List;
import java.util.Optional;
import kr.itkoo.voting.domain.entity.VoteItem;
import kr.itkoo.voting.domain.repository.VoteItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VoteItemService {

    private final VoteItemRepository voteItemRepository;

    public Optional<VoteItem> findById(Long id) {
        return voteItemRepository.findById(id);
    }

    public List<VoteItem> findAll() {
        return voteItemRepository.findAll();
    }

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
    public void update(Long id, String name, Integer updatedAt) {
        VoteItem voteItem = voteItemRepository.findById(id).get();
        voteItem.setName(name);
        voteItem.setUpdatedAt(updatedAt);
    }

    public void deleteById(Long id) {
        voteItemRepository.deleteById(id);
    }
}
