package kr.itkoo.voting.domain.entity;

import lombok.*;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Setter
@Getter
@AllArgsConstructor
@ToString(of = {"id", "name"})
@NoArgsConstructor(access = PROTECTED)
public class VoteItem extends BaseEntity{

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    @ManyToOne(fetch = LAZY)
    private Vote vote;

    public VoteItem(Vote vote, String name){
        this.vote = vote;
        this.name = name;
    }
}
