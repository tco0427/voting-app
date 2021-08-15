package kr.itkoo.voting.domain.dto.request;

import lombok.Data;

@Data
public class CreateVoteRequest {
    private Long userId;
    private String title;
}
