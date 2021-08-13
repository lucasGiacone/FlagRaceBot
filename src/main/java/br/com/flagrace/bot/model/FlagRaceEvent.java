package br.com.flagrace.bot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
@Table(name = "flagraceevent")
public class FlagRaceEvent {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    private String fileName;
    private Boolean isDeleted = false;
    private LocalDateTime date;
    private int points;

    @OneToOne
    @JoinColumn(name = "playerId")
    private Client player;
}
