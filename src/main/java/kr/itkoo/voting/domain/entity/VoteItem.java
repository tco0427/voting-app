package kr.itkoo.voting.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;

@Entity
@Getter
@AllArgsConstructor
@ToString
public class VoteItem {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long voteId;

    private String name;

    private Long updatedAt;

    private Long createdAt;

    //JPA 프록시 객체 관련해서 명세상만 필요하므로 protected로 제한
    protected VoteItem() {}
}
