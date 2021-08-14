package br.com.flagrace.bot.commands;

import br.com.flagrace.bot.embeds.Embeds;
import br.com.flagrace.bot.embeds.ErrorEmbed;
import br.com.flagrace.bot.model.Client;
import br.com.flagrace.bot.model.FlagRaceEvent;
import br.com.flagrace.bot.ocr.OCR;
import br.com.flagrace.bot.opencv.OpenCV;
import br.com.flagrace.bot.service.ClientService;
import br.com.flagrace.bot.service.FlagRaceService;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlagRaceImageRecievedCommand {

    @Autowired
    private ClientService clientService;

    @Autowired
    private FlagRaceService flagRaceService;

    @Autowired
    private OpenCV openCV;

    @SneakyThrows
    public void execute(MessageReceivedEvent event){
        Embeds embed = this.process(event.getMessage().getAttachments(), event.getMessage().getAuthor());

        if(embed instanceof ErrorEmbed){
            // TODO ERROR PROCESSING
        }
        event.getChannel().sendMessage(embed.build()).queue();
    }

    @SneakyThrows
    private Embeds process(List<Message.Attachment> attachments, User author){
        FlagRaceEvent flagRaceEvent = new FlagRaceEvent();

        Message.Attachment image = attachments.get(0);
        Client player = this.clientService.findOrCreateByAuthor(author);
        String base64Img = openCV.getPontuationImage(image, player, flagRaceEvent);

        if (base64Img.equals("")){
            // TODO
            //  FAILED TO FIND BLUE BAR WITH NAME AND SCORE!!
            //  ASK THE PERSON IF THEY WANT A JUNIOR TO CHECK
            //  ERROR EMBED!!
            return null;
        }

        String response = OCR.sendPost("data:image/png;base64,".concat(base64Img));
        JSONObject object = new JSONObject(response);

        if(object.getInt("OCRExitCode") != 1){
            // TODO
            //  THE THIRD PARTY OCR SERVICE FAILED TO PROCESS
            //  ASK THE PERSON IF THEY WANT A JUNIOR TO CHECK
            //  ERROR EMBED!!
            return null;
        }
        JSONArray lines  = object.getJSONArray("ParsedResults").getJSONObject(0).getJSONObject("TextOverlay").getJSONArray("Lines");
        String points = lines.getJSONObject(lines.length()-1).getString("LineText");

        try{
            flagRaceEvent.setPoints(Integer.parseInt(points));
            flagRaceService.update(flagRaceEvent);
        }
        catch (Exception e){
            // TODO SAME AS THE UPPER ERROR CASE!!
        }

        System.out.println(points);
        return null;
    }
}
