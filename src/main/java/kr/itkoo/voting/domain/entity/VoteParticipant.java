package kr.itkoo.voting.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@AllArgsConstructor
@ToString
@NoArgsConstructor(access = PROTECTED)
public class VoteParticipant {

    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private Long voteId;

    private Long voteItemId;

    @ManyToOne(fetch = LAZY)
    private User user;

    public VoteParticipant(User user, Long voteId, Long voteItemId) {
        this.user = user;
        this.voteId = voteId;
        this.voteItemId = voteItemId;
    }
}
