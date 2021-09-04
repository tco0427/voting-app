package kr.itkoo.voting.domain.entity;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.PROTECTED;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import kr.itkoo.voting.domain.dto.response.FAQResponse;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class FAQ extends BaseEntity {

	@Id
	@GeneratedValue(strategy = IDENTITY)
	private Long id;

	private String title;

	@Column(columnDefinition = "boolean default true")
	private Boolean isVisible;

	private String content;

	public FAQ(String title, String content) {
		this.title = title;
		this.content = content;
	}

	public FAQ(Long id, String title, String content) {
		this.id = id;
		this.title = title;
		this.content = content;
	}

	public FAQResponse toFAQResponse() {
		return new FAQResponse(this.title, this.content);
	}
}
