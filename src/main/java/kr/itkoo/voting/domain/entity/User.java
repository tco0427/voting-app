package kr.itkoo.voting.domain.entity;

import lombok.Getter;
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

    private String name;

    private String imageUrl;

    @Column(nullable = true)
    private Integer updatedAt;

    private Integer createdAt;

    @OneToMany(mappedBy = "user")
    private List<Vote> votes = new ArrayList<>();
}