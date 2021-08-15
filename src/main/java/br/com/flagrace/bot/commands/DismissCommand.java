package br.com.flagrace.bot.commands;

import br.com.flagrace.bot.embeds.DefaultEmbed;
import br.com.flagrace.bot.service.FlagRaceService;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageChannel;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DismissCommand extends Command{

    @Autowired
    private FlagRaceService flagRaceService;

    public void execute(ButtonClickEvent event){
        this.process(event.getMessage(), event.getChannel(), event.getUser());
    }

    public void process(Message message, MessageChannel channel, User author){
        String flagRaceId = message.getEmbeds().get(0).getFooter().getText().split("#")[1];
        // TODO check if it is the right user
        flagRaceService.deleteById(Long.parseLong(flagRaceId));
        channel.sendMessage(new DefaultEmbed(author, "Test").build()).queue();
        message.delete().queue();
    }

}
