package kr.itkoo.voting.domain.dto.response;

import lombok.Data;

@Data
public class SignUpResponse {

    private Long userId;
    private String token;

    public SignUpResponse(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
