package kr.itkoo.voting.domain.entity;

import static javax.persistence.FetchType.LAZY;
import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.FetchType.*;
import static javax.persistence.GenerationType.*;
import static lombok.AccessLevel.PROTECTED;

@Entity
@Getter
@ToString(of = {"id", "user", "title"})
@NoArgsConstructor(access = PROTECTED)
public class Vote extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	@ManyToOne(fetch = LAZY)
	private User user;

	private String title;

	@OneToMany(mappedBy = "vote")
	private List<VoteItem> voteItems = new ArrayList<>();

  	public Vote(User user, String title) {
    	this.user = user;
    	this.title = title;
  	}

  public void setTitle(String title){
    this.title = title;
  }
}
