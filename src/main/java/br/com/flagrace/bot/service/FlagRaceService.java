package br.com.flagrace.bot.service;

import br.com.flagrace.bot.model.FlagRaceEvent;
import br.com.flagrace.bot.repository.FlagRaceEventRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class FlagRaceService {

    @Autowired
    private FlagRaceEventRepository flagRaceEventRepository;

    public FlagRaceEvent create(FlagRaceEvent event){
        return this.flagRaceEventRepository.save(event);
    }

    public FlagRaceEvent update(FlagRaceEvent event){
        return this.flagRaceEventRepository.save(event);
    }

}
