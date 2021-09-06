package kr.itkoo.voting.domain.dto.request;

import kr.itkoo.voting.data.code.PlatformCode;
import kr.itkoo.voting.domain.entity.User;
import lombok.Data;

@Data
public class SignUpRequest {

    private PlatformCode platformCode;
    private int platformId;
    private String name;
    private String imageUrl;

    public User toUserEntity() {
        return new User(this.platformCode, this.platformId, this.name, this.imageUrl);
    }
}
