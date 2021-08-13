package br.com.flagrace.bot.opencv;

import br.com.flagrace.bot.enumeration.BotEnum;
import br.com.flagrace.bot.model.Client;
import br.com.flagrace.bot.model.FlagRaceEvent;
import br.com.flagrace.bot.service.FlagRaceService;
import lombok.SneakyThrows;
import net.dv8tion.jda.api.entities.Message;
import org.opencv.core.*;
import org.opencv.core.Point;
import org.opencv.imgproc.Imgproc;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgproc.Imgproc.TM_SQDIFF_NORMED;

@Component
public class OpenCV {

    @Autowired
    private FlagRaceService flagRaceService;

    public String getPontuationImage(Message.Attachment image, Client player, FlagRaceEvent flagRaceEvent){
        Mat template = imread(BotEnum.TEMPLATEIMGNAME.getValue());
        flagRaceEvent.setPlayer(player);
        flagRaceEvent.setDate(LocalDateTime.now());
        flagRaceService.create(flagRaceEvent);

        File recievedImage = getFlagRaceImageFile(image, flagRaceEvent);
        Mat incomingMatImage = imread(recievedImage.getPath());

        // TODO Proper deletion logic

        return getMatchingImage(incomingMatImage, template);
    }

    @SneakyThrows
    private File getFlagRaceImageFile(Message.Attachment image, FlagRaceEvent flagRaceEvent){


        String fileExt = Objects.requireNonNull(image.getFileExtension());
        String fileName = flagRaceEvent.getId().toString().concat(".").concat(fileExt);
        String filePath = Paths.get(BotEnum.PATH.getValue(), fileName).toString();

        CompletableFuture<Void> downloadFuture = image.downloadToFile(new File(filePath))
                .thenAccept(file -> {
                    System.out.println("Saved attachment to " + file.getName());
                    flagRaceEvent.setFileName(file.getName());
                    flagRaceService.update(flagRaceEvent);
                });

        downloadFuture.get();
        return new File(filePath);
    }



    private static BufferedImage mat2BufferedImage(Mat m) {

        int type = BufferedImage.TYPE_BYTE_GRAY;
        if ( m.channels() > 1 ) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }
        int bufferSize = m.channels()*m.cols()*m.rows();
        byte [] b = new byte[bufferSize];
        m.get(0,0,b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(),m.rows(), type);
        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);
        return image;
    }

    @SneakyThrows
    private static String getMatchingImage(Mat img, Mat template){
        //Create result image properties
        Mat result = new Mat();
        int result_cols = img.cols() - template.cols() + 1;
        int result_rows = img.rows() - template.rows() + 1;
        result.create(result_rows, result_cols, CvType.CV_32FC1);

        // Process both images
        Imgproc.matchTemplate(img, template, result, TM_SQDIFF_NORMED);
        Core.normalize(result, result, 0, 1, Core.NORM_MINMAX, -1, new Mat());
        Core.MinMaxLocResult mmr = Core.minMaxLoc(result);

        // Returns empty string if not confident enough
        if(mmr.maxVal < 0.8) return "";

        Point matchLoc = mmr.minLoc;
        Rect rectCrop = new Rect( (int) matchLoc.x, (int) matchLoc.y,template.cols(),template.rows());
        Mat imgROI = new Mat(img, rectCrop);

        BufferedImage bufferedROI = resize(mat2BufferedImage(imgROI), 3);

        final ByteArrayOutputStream os = new ByteArrayOutputStream();


        ImageIO.write(bufferedROI, "png", os);

        return Base64.getEncoder().encodeToString(os.toByteArray());
    }

    private static BufferedImage resize(BufferedImage inputImage, int scaledWidth, int scaledHeight) {

        // creates output image
        BufferedImage outputImage = new BufferedImage(scaledWidth,
                scaledHeight, inputImage.getType());

        // scales the input image to the output image
        Graphics2D g2d = outputImage.createGraphics();
        g2d.drawImage(inputImage, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        return outputImage;
    }

    private static BufferedImage resize(BufferedImage inputImage, double percent){
        int scaledWidth = (int) (inputImage.getWidth() * percent);
        int scaledHeight = (int) (inputImage.getHeight() * percent);
        return resize(inputImage, scaledWidth, scaledHeight);
    }

}
