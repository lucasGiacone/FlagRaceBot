package br.com.flagrace.bot.enumeration;

public enum BotEnum {

    PREFIX("$"),
    TOKEN(System.getenv("TOKEN"));


    private String value;

    BotEnum(String value){
        this.value = value;
    }

    public String getValue(){
        return value;
    }

}
