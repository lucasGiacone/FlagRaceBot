package br.com.flagrace.bot.configuration;

import br.com.flagrace.bot.enumeration.BotEnum;
import br.com.flagrace.bot.events.Events;
import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.entities.Activity;
import net.dv8tion.jda.api.interactions.commands.build.CommandData;
import net.dv8tion.jda.api.interactions.commands.build.OptionData;
import net.dv8tion.jda.api.requests.restaction.CommandListUpdateAction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.security.auth.login.LoginException;

import static net.dv8tion.jda.api.interactions.commands.OptionType.STRING;

@Configuration
public class Bot {

    @Autowired
    private Events events;

    public static JDA jda;

    @Bean
    public void initialize() throws LoginException {
        jda = JDABuilder.createLight(BotEnum.TOKEN.getValue())
                .setRawEventsEnabled(true)
                .addEventListeners(events)
                .setActivity(Activity.playing("Flag Race"))
                .build();

        jda.upsertCommand("ping", "Calculate ping of the bot").queue();

        CommandListUpdateAction commands = jda.updateCommands();

        commands.addCommands(
                new CommandData("say", "Faça o bot falar o que você disse")
                        .addOptions(new OptionData(STRING, "conteúdo", "O que o bot deve dizer")
                                .setRequired(true))
        );

        commands.addCommands(
                new CommandData("ping", "Ping-Pong com o bot")
        );

        commands.queue();
    }

}
