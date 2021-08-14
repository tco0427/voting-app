package kr.itkoo.voting.domain.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

import static javax.persistence.GenerationType.*;

@Getter
@Entity
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String platformCode;

    private int platformId;

    private String name;

    private String imageUrl;

    @Column(nullable = true)
    private long updatedAt;

    private long createdAt;

    @OneToMany(mappedBy = "user")
    private List<Vote> votes = new ArrayList<>();

    public User(String platformCode, int platformId, String name, String imageUrl, long updatedAt, long createdAt) {
        this.platformCode = platformCode;
        this.platformId = platformId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.updatedAt = updatedAt;
        this.createdAt = createdAt;
    }

    public User(String platformCode, int platformId, String name, String imageUrl, long createdAt) {
        this.platformCode = platformCode;
        this.platformId = platformId;
        this.name = name;
        this.imageUrl = imageUrl;
        this.createdAt = createdAt;
    }
}