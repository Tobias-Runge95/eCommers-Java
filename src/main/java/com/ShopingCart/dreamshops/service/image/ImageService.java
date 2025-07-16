package com.ShopingCart.dreamshops.service.image;

import com.ShopingCart.dreamshops.DTO.Image.ImageDto;
import com.ShopingCart.dreamshops.exceptions.ResourceNotFoundException;
import com.ShopingCart.dreamshops.model.Image;
import com.ShopingCart.dreamshops.model.Product;
import com.ShopingCart.dreamshops.repository.ImageRepository;
import com.ShopingCart.dreamshops.service.product.IProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.sql.rowset.serial.SerialBlob;
import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ImageService implements IImageService{

    private final ImageRepository imageRepository;
    private final IProductService productService;

    @Override
    public Image getImageById(Long id) {
        return imageRepository
                .findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Image not found"));
    }

    @Override
    public void deleteImageById(Long id) {
        imageRepository
                .findById(id)
                .ifPresentOrElse(imageRepository::delete, () -> new ResourceNotFoundException("Image not found"));
    }

    @Override
    public List<ImageDto> saveImages(List<MultipartFile> files, Long productId) {
        Product product = productService.getProductById(productId);
        List<ImageDto> savedImageDto = new ArrayList<>();
        for (MultipartFile file : files) {
            try{
                Image image = new Image();
                image.setFileName(file.getOriginalFilename());
                image.setFileType(file.getContentType());
                image.setImage(new SerialBlob(file.getBytes()));
                image.setProduct(product);
                image.setDownloadUrl("/api/v1/images/image/download" + image.getId());
                Image savedImage = imageRepository.save(image);
                savedImage.setDownloadUrl("/api/v1/images/image/download" + savedImage.getId());

                ImageDto imageDto = new ImageDto();
                imageDto.setImageName(savedImage.getName());
                imageDto.setImageId(savedImage.getId());
                imageDto.setDownloadUrl(savedImage.getDownloadUrl());
                savedImageDto.add(imageDto);
            }catch(IOException | SQLException e){
                throw new RuntimeException(e.getMessage());
            }
        }
        return savedImageDto;
    }

    @Override
    public void updateImage(MultipartFile file, Long imageId) {
        Image image = getImageById(imageId);
        try{
            image.setFileName(file.getOriginalFilename());
            image.setFileType(file.getContentType());
            image.setImage(new SerialBlob(file.getBytes()));
            imageRepository.save(image);
        }catch (IOException | SQLException e){
            throw new RuntimeException(e.getMessage());
        }

    }
}
