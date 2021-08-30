package kr.itkoo.voting.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class VoteWithItemResponse {
    private Long id;
    private String title;
    private List<VoteItemResponse> voteItems;
}
