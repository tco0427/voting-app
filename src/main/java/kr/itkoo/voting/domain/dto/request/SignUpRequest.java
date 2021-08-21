package kr.itkoo.voting.domain.dto.request;

import kr.itkoo.voting.domain.entity.User;
import lombok.Data;

import java.time.Instant;

@Data
public class SignUpRequest {
    private String name;
    private String imageUrl;
    private String platformCode;
    private int platformId;

    public User toUserEntity(){
        long createdAt = Instant.now().getEpochSecond();
        return new User(this.platformCode, this.platformId, this.name, this.imageUrl, createdAt);
    }
}
