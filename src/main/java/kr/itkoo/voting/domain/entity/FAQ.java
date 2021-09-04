package kr.itkoo.voting.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;

import static javax.persistence.GenerationType.IDENTITY;
import static lombok.AccessLevel.*;

@Entity
@Getter
@ToString
@NoArgsConstructor(access = PROTECTED)
public class FAQ extends BaseEntity{
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
}
