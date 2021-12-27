package kr.itkoo.voting.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VoteByUserResponse {
    private Long userId;
    private List<VoteWithItemResponse> voteResponses;
}
