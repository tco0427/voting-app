package kr.itkoo.voting.domain.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Getter
@Entity
@ToString
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String platformCode;

    private String name;

    private String imageUrl;

    @Column(nullable = true)
    private int updatedAt;

    private int createdAt;
}