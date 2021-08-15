package br.com.flagrace.bot.embeds;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.MessageEmbed;

import java.util.ArrayList;
import java.util.List;

public abstract class Embeds {

    String title;
    int color;
    List<MessageEmbed.Field> fields = new ArrayList<>();
    net.dv8tion.jda.api.entities.User client;
    String footer;

    public Embeds(String title, int color, net.dv8tion.jda.api.entities.User client){
        this.title = title;
        this.color = color;
        this.client = client;
        this.footer = "Sent to " + client.getName();
    }

    public Embeds(String title, int color, net.dv8tion.jda.api.entities.User client, MessageEmbed.Field field){
        this.title = title;
        this.color = color;
        this.client = client;
        this.fields.add(field);
        this.footer = "Sent to " + client.getName();
    }

    public Embeds(String title, int color, net.dv8tion.jda.api.entities.User client, List<MessageEmbed.Field> fields){
        this.title = title;
        this.color = color;
        this.client = client;
        this.fields = fields;
        this.footer = "Sent to " + client.getName();
    }

    public void setFooter(String text){
        this.footer = text;
    }


    public EmbedBuilder embedBuilder(){
        EmbedBuilder embed = new EmbedBuilder();
        embed.setTitle(title);
        embed.setColor(color);

        for (MessageEmbed.Field field: fields) {
            embed.addField(field);
        }

        embed.setFooter(footer, client.getAvatarUrl());
        return embed;
    }

    public MessageEmbed build(){
        return embedBuilder().build();
    }

    public void addField(String title, String text){
        fields.add(new MessageEmbed.Field(title, text, false));
    }

    public void addField(MessageEmbed.Field field){
        fields.add(field);
    }

    public static MessageEmbed.Field makeField(String title, String text){
        return new MessageEmbed.Field(title, text, false);
    }

}
