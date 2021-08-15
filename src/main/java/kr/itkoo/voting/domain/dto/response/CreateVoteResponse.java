package kr.itkoo.voting.domain.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CreateVoteResponse {
    private Long id;
    private String title;
}
