package br.com.flagrace.bot.events;

import br.com.flagrace.bot.commands.DismissCommand;
import br.com.flagrace.bot.commands.FlagRaceImageReceivedCommand;
import br.com.flagrace.bot.commands.RetryCommand;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.ButtonClickEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Component
@Slf4j
public class Events extends ListenerAdapter {

    //TODO BUTTON REPLY EVENT
    //TODO SLASH COMMAND EVENT WITH ADMIN PERMISSION -- ADMIN USER NEED TO BE CREATED
    //TODO PING LEON ON ERROR LMAO

    @Autowired
    private FlagRaceImageReceivedCommand flagRaceImageRecievedCommand;

    @Autowired
    private DismissCommand dismissCommand;

    @Autowired
    private RetryCommand retryCommand;


    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.printf("[%s] Bot Online!%n", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event) {
        System.out.println("SLASH");
        switch (event.getName()) {
            case "ping" -> {
                long time = System.currentTimeMillis();
                event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                        .flatMap(v ->
                                event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                        ).queue(); // Queue both reply and edit
            }
            case "say" -> event.reply(Objects.requireNonNull(event.getOption("content")).getAsString()) // reply or acknowledge
                    .queue(); // Queue both reply and edit
        }
    }

    @SneakyThrows
    @Override
    public void onMessageReceived(@NotNull MessageReceivedEvent event) {
        if(event.getMessage().getAuthor().isBot()) return;

        List<Message.Attachment> attachments = event.getMessage().getAttachments();
        if ( attachments.size() == 0 ){
            System.out.println("TextOnly");
        }
        else if( attachments.size() == 1 && attachments.get(0).isImage()){
            this.flagRaceImageRecievedCommand.execute(event);
        }
    }

    @SneakyThrows
    @Override
    public void onButtonClick(@NotNull ButtonClickEvent event) {
        String id = Objects.requireNonNull(Objects.requireNonNull(event.getButton()).getId());
        switch (id) {
            case "Retry" -> retryCommand.execute(event);
            case "DismissAndHumanFallback" -> dismissCommand.execute(event);
        }
    }
}
