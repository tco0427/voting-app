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
public class VoteParticipant {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long userId;

    private Long voteId;

    private Long voteItemId;

    public VoteParticipant(Long userId, Long voteId, Long voteItemId) {
        this.userId = userId;
        this.voteId = voteId;
        this.voteItemId = voteItemId;
    }

    public VoteParticipant() {}
}
