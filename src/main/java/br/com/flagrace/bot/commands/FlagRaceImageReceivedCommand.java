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
import net.dv8tion.jda.api.entities.Emoji;
import net.dv8tion.jda.api.entities.Message;
import net.dv8tion.jda.api.entities.MessageEmbed;
import net.dv8tion.jda.api.entities.User;
import net.dv8tion.jda.api.events.message.MessageReceivedEvent;
import net.dv8tion.jda.api.interactions.components.Button;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class FlagRaceImageReceivedCommand extends Command {

    @Autowired
    private ClientService clientService;

    @Autowired
    private FlagRaceService flagRaceService;

    @Autowired
    private OpenCV openCV;

    @SneakyThrows
    public void execute(MessageReceivedEvent event){
        ErrorEmbed embed = this.process(event.getMessage().getAttachments(), event.getMessage().getAuthor());

        if (embed != null){
            MessageEmbed message = embed.build();

            // Error button
            if(embed instanceof ErrorEmbed){
                // TODO ERROR PROCESSING
                // Error message action row
                ////// X BUTTON IS TO DISMISS IMAGE AND CONTACT LEON
                ////// O BUTTON IS THAT THE PERSON WILL TRY TO RESEND THE IMAGE!

                event.getChannel().sendMessage(message)
                        .setActionRow(
                                Button.primary("Retry", Emoji.fromUnicode("U+2714")),
                                Button.danger("Dismiss", Emoji.fromUnicode("U+2716"))
                        ).queue();
                return;
            }
            event.getChannel().sendMessage(message).queue();
        }
        else{
            event.getMessage().addReaction("U+2705");
        }
    }



    @SneakyThrows
    private ErrorEmbed process(List<Message.Attachment> attachments, User author){
        FlagRaceEvent flagRaceEvent = new FlagRaceEvent();

        Message.Attachment image = attachments.get(0);
        Client player = this.clientService.findOrCreateByAuthor(author);
        String base64Img = openCV.getPontuationImage(image, player, flagRaceEvent);

        //we don't have a matching image
        if (base64Img.equals("")) {
            ErrorEmbed embed = new ErrorEmbed(author, "Failed to process image");
            String text = "• Verify if the blue rectangle that contains your name is not covered or cut" +
                      " \n • Try to resend the image with a higher resolution" +
                      " \n • Ask a junior to manually verify your image(X button)";
            embed.addField("Your image failed to process you can try:",text);
            return embed;
        }

        String response = OCR.sendPost("data:image/png;base64,".concat(base64Img));
        JSONObject object = new JSONObject(response);

        if(object.getInt("OCRExitCode") != 1){
            ErrorEmbed embed = new ErrorEmbed(author, "Text recognition failed");
            String text = "• Ask a junior to manually verify your image(X button)";
            embed.addField("Your image failed to process you can try:",text);
            return embed;
        }
        String points = "0";
        try{
            JSONArray lines  = object.getJSONArray("ParsedResults").getJSONObject(0).getJSONObject("TextOverlay").getJSONArray("Lines");
            points = lines.getJSONObject(lines.length()-1).getString("LineText");
            flagRaceEvent.setPoints(Integer.parseInt(points));
            flagRaceService.update(flagRaceEvent);
        }
        catch (Exception e){
            ErrorEmbed embed = new ErrorEmbed(author, "Text recognition failed");
            String text = "• Ask a junior to manually verify your image(X button)";
            embed.addField("Your image failed to process you can try:",text);
            return embed;
        }

        System.out.println(points);

        return null;
    }
}
