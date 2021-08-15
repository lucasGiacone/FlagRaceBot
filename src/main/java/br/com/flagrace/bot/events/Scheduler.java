package br.com.flagrace.bot.events;


import br.com.flagrace.bot.configuration.Bot;
import br.com.flagrace.bot.enumeration.BotEnum;
import net.dv8tion.jda.api.entities.TextChannel;
import org.jetbrains.annotations.NotNull;

import java.time.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

import static java.util.concurrent.TimeUnit.*;

public class Scheduler {

    @NotNull
    private static final TextChannel channel = Objects.requireNonNull(Bot.jda.getTextChannelById(BotEnum.CHANNELID.getNumberLong()));

    private static final ScheduledExecutorService scheduler =
            Executors.newScheduledThreadPool(5);


    public static void sendFlagRaceReminder(){
        LocalTime now = LocalTime.parse(Instant.now().toString().split("T")[1].split("\\.")[0]);

        List<String> timeStrings = Arrays.asList("12:00","19:00","21:00","22:00","23:00");
        List<String> timeStringsPst = Arrays.asList("05 am pst","12 pm pst","02 pm pst","03 pm pst","04 pm pst");
        List<Long> secondsToFlags = new ArrayList<>();

        for (String flagTime: timeStrings) {
            secondsToFlags.add(getRemainingTime(Duration.between(now, LocalTime.parse(flagTime)).toSeconds()));
            System.out.println(secondsToFlags.get(secondsToFlags.size()-1));
        }

        for (int i = 0; i < 5; i++){
            scheduler.scheduleAtFixedRate(createRunnable(timeStringsPst.get(i)), secondsToFlags.get(i), DAYS.toSeconds(1), SECONDS);
        }

        secondsToFlags.clear();
        timeStrings.clear();
        timeStringsPst.clear();
    }

    private static Runnable createRunnable(String time){
        return () -> {
            String message = time.concat(" flag race in 5 minuter <@"+BotEnum.TAGID.getValue()+">");
            channel.sendMessage(message).queue();
            System.out.println("FLAG RACE");
        };
    }

    private static long getRemainingTime(long seconds){
        return (seconds - MINUTES.toSeconds(5) + DAYS.toSeconds(1))%DAYS.toSeconds(1);
    }
}
