package kr.itkoo.voting.domain.dto.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CreateVoteItemRequest {
    private Long voteId;
    private String name;
}
