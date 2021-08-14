package br.com.flagrace.bot.commands;

import br.com.flagrace.bot.service.ClientService;
import br.com.flagrace.bot.service.FlagRaceService;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

@Slf4j
public class Command {

    @Autowired
    protected ClientService clientService;

    @Autowired
    protected FlagRaceService flagRaceService;

    @Getter
    private final String name = null;
    @Getter private final String description = null;
    @Getter private final List<OptionData> options = new ArrayList<>();

    public void execute(SlashCommandEvent event) throws Exception {
        log.error(String.format("O Slash Command %s da classe %s falhou ao ser executado!", StringUtils.capitalize(this.getName()), this.getClass().getSimpleName()));
    }

    public void execute(MessageReceivedEvent event) throws Exception {
        log.error(String.format("O Comando %s da classe %s falhou ao ser executado!", StringUtils.capitalize(this.getName()), this.getClass().getSimpleName()));
    }

    public void execute(ButtonClickEvent event) throws Exception{
        log.error(String.format("A Interação de botão %s da classe %s falhou ao ser executado!", StringUtils.capitalize(this.getName()), this.getClass().getSimpleName()));
    }

}
