package br.com.flagrace.bot.service;

import br.com.flagrace.bot.model.Image;
import br.com.flagrace.bot.repository.ImageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

    @Autowired
    private ImageRepository imageRepository;

    public Image create(Image image){
        return this.imageRepository.save(image);
    }

    public Image update(Image image){
        return this.imageRepository.save(image);
    }

}
