package br.com.flagrace.bot.embeds;

import br.com.flagrace.bot.enumeration.BotEnum;

public class ErrorEmbed extends Embeds{

    public ErrorEmbed(net.dv8tion.jda.api.entities.User client){
        super("Error", BotEnum.RED.getNumber(),client);
    }

    public ErrorEmbed(net.dv8tion.jda.api.entities.User client,String title){
        super(title,BotEnum.RED.getNumber(),client);
    }

}
