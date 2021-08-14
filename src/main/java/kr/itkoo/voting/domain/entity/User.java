package kr.itkoo.voting.domain.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

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