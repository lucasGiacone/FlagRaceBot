package br.com.flagrace.bot.enumeration;

import java.nio.file.Paths;

public enum BotEnum {

    TOKEN(System.getenv("TOKEN")),
    PATH(Paths.get(".","temp").toString()),
    TEMPLATEIMGNAME("needle.png"),
    APIKEY(System.getenv("APIKEY"));


    private String value;

    BotEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
