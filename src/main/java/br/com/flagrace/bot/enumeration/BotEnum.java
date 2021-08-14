package br.com.flagrace.bot.enumeration;

import java.nio.file.Paths;

public enum BotEnum {

    TOKEN(System.getenv("TOKEN")),
    PATH(Paths.get(".","temp").toString()),
    TEMPLATEIMGNAME("needle.png"),
    RED(0x7f2927),
    GREEN(0x80b461),
    BLACK(0x1FFFFFFF),
    APIKEY(System.getenv("APIKEY"));


    private String value;

    private int number;

    BotEnum(String value){
        this.value = value;
    }

    BotEnum(int number){
        this.number = number;
    }

    public String getValue(){
        return value;
    }

    public int getNumber() {
        return number;
    }

}
