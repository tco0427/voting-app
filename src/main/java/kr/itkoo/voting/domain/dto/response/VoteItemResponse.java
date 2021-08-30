package kr.itkoo.voting.domain.dto.response;

import kr.itkoo.voting.domain.entity.VoteItem;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteItemResponse {
    private Long id;
    private String name;

    public VoteItemResponse(VoteItem voteItem){
        this.id = voteItem.getId();
        this.name = voteItem.getName();
    }
}
