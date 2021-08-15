package br.com.flagrace.bot.enumeration;

import java.nio.file.Paths;

public enum BotEnum {

    TOKEN(System.getenv("FLAGTOKEN")),
    PATH(Paths.get(".","temp").toString()),
    TEMPLATEIMGNAME("needle.png"),
    RED(0x7f2927),
    GREEN(0x80b461),
    YELLOW(0xd0a843),
    BLACK(0x1FFFFFFF),
    CHANNELID(860540873044656165L),
    TAGID("872270290955173948"),
    APIKEY(System.getenv("APIKEY"));


    private String value;

    private int number;

    private Long numberLong;

    BotEnum(String value){
        this.value = value;
    }

    BotEnum(int number){
        this.number = number;
    }

    BotEnum(Long numberLong){
        this.numberLong = numberLong;
    }

    public String getValue(){
        return value;
    }

    public int getNumber() {
        return number;
    }

    public Long getNumberLong(){
        return numberLong;
    }

}
