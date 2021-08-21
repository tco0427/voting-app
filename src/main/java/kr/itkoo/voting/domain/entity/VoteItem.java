package kr.itkoo.voting.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Setter
@Getter
@AllArgsConstructor
@ToString
public class VoteItem {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String name;

    private Integer updatedAt;

    private Integer createdAt;

    @JsonIgnore
    @ManyToOne(fetch = LAZY)
    private Vote vote;

    //JPA 프록시 객체 관련해서 명세상만 필요하므로 protected로 제한
    protected VoteItem() {
    }

    public VoteItem(Vote vote, String name){
        this.vote = vote;
        this.name = name;
    }
}
