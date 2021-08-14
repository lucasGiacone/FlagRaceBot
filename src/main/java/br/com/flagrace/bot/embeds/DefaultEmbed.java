package br.com.flagrace.bot.embeds;

import br.com.flagrace.bot.enumeration.BotEnum;

public class DefaultEmbed extends Embeds{

    public DefaultEmbed(net.dv8tion.jda.api.entities.User client,String title) {
        super(title, BotEnum.BLACK.getNumber(), client);
    }

    public DefaultEmbed(net.dv8tion.jda.api.entities.User client,String title, int color) {
        super(title, color, client);
    }

}
