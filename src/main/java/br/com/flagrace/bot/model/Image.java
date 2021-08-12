package br.com.flagrace.bot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "images")
public class Image {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String originalFileName;
    private String extractedFileName;
    private Boolean isDeleted = false;
}
