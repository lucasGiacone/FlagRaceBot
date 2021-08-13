package br.com.flagrace.bot.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "client")
public class Client {

    @Id
    private Long id;
    private String name;

    @OneToMany(mappedBy = "id", cascade = CascadeType.ALL)
    private List<FlagRaceEvent> flagRaceEventList;

    public Client() { }

    public Client(Long id, String name){
        this.id = id;
        this.name = name;
    }


}
