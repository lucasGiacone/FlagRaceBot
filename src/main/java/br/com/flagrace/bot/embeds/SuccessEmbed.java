package br.com.flagrace.bot.embeds;

import br.com.flagrace.bot.enumeration.BotEnum;

public class SuccessEmbed extends Embeds {

    public SuccessEmbed(net.dv8tion.jda.api.entities.User client){
        super("Sucesso!", BotEnum.GREEN.getNumber(), client);
    }

    public SuccessEmbed(net.dv8tion.jda.api.entities.User client, String title){
        super(title,BotEnum.GREEN.getNumber(), client);
    }

}
