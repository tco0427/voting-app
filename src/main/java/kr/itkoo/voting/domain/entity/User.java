package kr.itkoo.voting.domain.entity;

import lombok.Getter;
import lombok.ToString;

import javax.persistence.*;

import static javax.persistence.GenerationType.*;

@Getter
@Entity
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = IDENTITY)
    private Long id;

    private String platformCode;

    private String name;

    private String imageUrl;

    @Column(nullable = true)
    private int updatedAt;

    private int createdAt;
}