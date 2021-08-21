package kr.itkoo.voting.domain.dto.response;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SignUpResponse {

    private Long userId;
    private String token;

    public SignUpResponse(Long userId, String token) {
        this.userId = userId;
        this.token = token;
    }
}
