package com.lxisoft.vegetablestore.service.impl;

import com.lxisoft.vegetablestore.domain.Vegetable;
import com.lxisoft.vegetablestore.repository.VegetableRepository;
import com.lxisoft.vegetablestore.service.VegetableService;

import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;

/**
 * Service Implementation for managing {@link Vegetable}.
 */
@Service
@Transactional
public class VegetableServiceImpl implements VegetableService {

    private final Logger log = LoggerFactory.getLogger(VegetableServiceImpl.class);

    private final VegetableRepository vegetableRepository;

    public VegetableServiceImpl(VegetableRepository vegetableRepository) {
        this.vegetableRepository = vegetableRepository;
    }

    @Override
    public Vegetable save(Vegetable vegetable) throws IOException {
        log.debug("Request to save Vegetable : {}", vegetable);

        InputStream inputStream =  new BufferedInputStream(vegetable.getImageFile().getInputStream());

        byte[]image = new byte[inputStream.available()];

        inputStream.read(image);

        vegetable.setImage(image);

        return vegetableRepository.save(vegetable);
    }

    @Override
    public Vegetable update(Vegetable vegetable) throws IOException {
        log.debug("Request to update Vegetable : {}", vegetable);

         InputStream inputStream =  new BufferedInputStream(vegetable.getImageFile().getInputStream());

        byte[]image = new byte[inputStream.available()];

        inputStream.read(image);

        vegetable.setImage(image);

        return vegetableRepository.save(vegetable);
    }

    @Override
    public Optional<Vegetable> partialUpdate(Vegetable vegetable) {
        log.debug("Request to partially update Vegetable : {}", vegetable);

        return vegetableRepository
            .findById(vegetable.getId())
            .map(existingVegetable -> {
                if (vegetable.getName() != null) {
                    existingVegetable.setName(vegetable.getName());
                }
                if (vegetable.getPrice() != null) {
                    existingVegetable.setPrice(vegetable.getPrice());
                }
                if (vegetable.getStock() != null) {
                    existingVegetable.setStock(vegetable.getStock());
                }
                if (vegetable.getMinOrderQuantity() != null) {
                    existingVegetable.setMinOrderQuantity(vegetable.getMinOrderQuantity());
                }
                if (vegetable.getBase64Image() != null) {
                    existingVegetable.setBase64Image(vegetable.getBase64Image());
                }

                return existingVegetable;
            })
            .map(vegetableRepository::save);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Vegetable> findAll() {
        log.debug("Request to get all Vegetables");

        List<Vegetable> vegetables = vegetableRepository.findAll();

        for(int i = 0; i <vegetables.size(); i++){

            vegetables.get(i).getBase64Image();
        }

        return vegetables;
    }

    @Override
    @Transactional(readOnly = true)
    public Optional<Vegetable> findOne(Long id) {
        log.debug("Request to get Vegetable : {}", id);
        return vegetableRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        log.debug("Request to delete Vegetable : {}", id);
        vegetableRepository.deleteById(id);
    }

    public List<Vegetable> search(String word) {

        return vegetableRepository.search(word);
    }

    public void image(String name, HttpServletResponse response) throws IOException {

        String path ="../../../vegetablestore/src/main/resources/picture/"+ name;

        byte [] image = getImageAsBytes(path);

        response.setContentType("image/jpeg");
        response.setContentLength(image.length);
        response.getOutputStream().write(image);
    }
    private byte[] getImageAsBytes(String name) {


        File imgPath = new File(name);

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        try{
            BufferedImage bufferedImage = ImageIO.read(imgPath);
            ImageIO.write(bufferedImage, "jpg", bos );
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return bos.toByteArray();

    }

}
