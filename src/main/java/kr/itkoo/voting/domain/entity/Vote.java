package kr.itkoo.voting.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.FetchType.*;

@Entity
@Getter @Setter @ToString
public class Vote {
    @Id @GeneratedValue
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user;

    private String title;

    @Column(nullable = true)
    private Integer updatedAt;

    private Integer createdAt;
}
