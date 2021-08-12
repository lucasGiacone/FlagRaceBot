package br.com.flagrace.bot.events;

import br.com.flagrace.bot.enumeration.BotEnum;
import br.com.flagrace.bot.model.Image;
import br.com.flagrace.bot.service.ImageService;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.naming.directory.InvalidAttributesException;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Component
public class Events extends ListenerAdapter {


    @Autowired
    ImageService imageService;

    @Override
    public void onReady(@NotNull ReadyEvent event) {
        System.out.printf("[%s] Bot Online!%n", LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm:ss")));
    }

    @Override
    public void onSlashCommand(SlashCommandEvent event)
    {
        System.out.println("SLASH");
        switch (event.getName()) {
            case "ping" -> {
                long time = System.currentTimeMillis();
                event.reply("Pong!").setEphemeral(true) // reply or acknowledge
                        .flatMap(v ->
                                event.getHook().editOriginalFormat("Pong: %d ms", System.currentTimeMillis() - time) // then edit original
                        ).queue(); // Queue both reply and edit
            }
            case "say" -> event.reply(Objects.requireNonNull(event.getOption("conteúdo")).getAsString()) // reply or acknowledge
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

//            String[] args = event.getMessage().getContentRaw().split(" ");
//            String firstWord = args[0].substring(1);
//
//            if(args[0].startsWith(BotEnum.PREFIX.getValue())){
//
//                if(firstWord.equalsIgnoreCase("test")){
//                    System.out.println("TEST");
//                }
//            }

        }
        else if( attachments.size() == 1 && attachments.get(0).isImage()){
            Message.Attachment imageAtt = attachments.get(0);
            Image image = new Image();
            imageService.create(image);

            String fileExt = Objects.requireNonNull(imageAtt.getFileExtension());

            String fileName = image.getId().toString().concat(".").concat(fileExt);

            imageAtt.downloadToFile(new File(Paths.get(BotEnum.PATH.getValue(), fileName).toString()))
                    .thenAccept(file -> {
                        System.out.println("Saved attachment to " + file.getName());

                        image.setOriginalFileName(file.getName());
                        imageService.update(image);

                        // Double imageCompatibility = getImageCompatibility(image)

                        // if(imageCompatibility > Threshold) {
                        //     // TODO Ocr Detection and value check
                        //     // TODO Main logic will be here probably the creation of new classes will be necessary.
                        // }
                        // else {
                        //     // TODO ask if the person is sure the image is valid -> set a 1 min timer if no response failed
                        //     // TODO if positive responde ask a junir to check (Leon...)
                        //     // TODO if negative delete the image and continue
                    })
                    .exceptionally(t -> { // handle failure
                        //TODO delete image if created successfully

                        t.printStackTrace();
                        return null;
                    });


            System.out.println("Image");
        }
    }
}
