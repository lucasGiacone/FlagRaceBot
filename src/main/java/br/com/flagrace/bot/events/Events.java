package br.com.flagrace.bot.events;

import br.com.flagrace.bot.model.Client;
import br.com.flagrace.bot.model.FlagRaceEvent;
import br.com.flagrace.bot.ocr.OCR;
import br.com.flagrace.bot.opencv.OpenCV;
import br.com.flagrace.bot.service.ClientService;
import br.com.flagrace.bot.service.FlagRaceService;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.ReadyEvent;
import net.dv8tion.jda.api.events.interaction.SlashCommandEvent;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.hooks.ListenerAdapter;
import org.jetbrains.annotations.NotNull;
import org.json.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;

@Component
public class Events extends ListenerAdapter {
    @Autowired
    FlagRaceService flagRaceService;

    @Autowired
    ClientService clientService;

    @Autowired
    OpenCV openCV;

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
            FlagRaceEvent flagRaceEvent = new FlagRaceEvent();

            User author = event.getMessage().getAuthor();
            Message.Attachment image = attachments.get(0);
            Client player = this.clientService.findOrCreateByAuthor(author);
            String base64Img = openCV.getPontuationImage(image, player, flagRaceEvent);

            if (base64Img.equals("")){
                // TODO Something failed implement it later
                return;
            }


            String response = OCR.sendPost("data:image/png;base64,".concat(base64Img));
            JSONObject object = new JSONObject(response);

            if(object.getInt("OCRExitCode") != 1){
                //FAILED CALL AN ADMIN
                return;
            }
            JSONArray lines  = object.getJSONArray("ParsedResults").getJSONObject(0).getJSONObject("TextOverlay").getJSONArray("Lines");
            String points = lines.getJSONObject(lines.length()-1).getString("LineText");

            try{
                flagRaceEvent.setPoints(Integer.parseInt(points));
                flagRaceService.update(flagRaceEvent);
            }
            catch (Exception e){
                //OCR FAILED PARSING WE SCREWED
            }

            System.out.println(points);

        }
    }
}
