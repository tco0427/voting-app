package kr.itkoo.voting.domain.entity;

import java.util.ArrayList;
import java.util.List;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;

@Entity
@Getter
@Setter
@ToString
public class Vote {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    @ManyToOne(fetch = LAZY)
    private User user;

    private String title;

    @Column(nullable = true)
    private Integer updatedAt;

    private Integer createdAt;

    @OneToMany
    private List<VoteItem> voteItems = new ArrayList<>();
}
