package kr.itkoo.voting.domain.entity;

import kr.itkoo.voting.data.code.PlatformCode;
import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Getter
@Entity
@ToString
public class User extends BaseEntity{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PlatformCode platformCode;

    private Integer platformId;

    private String name;

    private String imageUrl;

    @OneToMany(mappedBy = "user")
    private List<Vote> votes = new ArrayList<>();

    protected User() {}

    public User(PlatformCode platformCode, int platformId, String name, String imageUrl) {
        this.platformCode = platformCode;
        this.platformId = platformId;
        this.name = name;
        this.imageUrl = imageUrl;
    }
}