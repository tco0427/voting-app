package kr.itkoo.voting.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class VoteParticipantResponse {
    private Long id;
    private Long voteId;
}
