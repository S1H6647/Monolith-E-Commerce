package com.project.monolith_e_commerce.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.project.monolith_e_commerce.web.dto.cloudinary.CloudinaryUploadResult;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {

    private final Cloudinary cloudinary;

    public CloudinaryUploadResult uploadImage(MultipartFile file) {
        try {
            Map uploadResult = cloudinary.uploader().upload(
                    file.getBytes(),
                    ObjectUtils.asMap(
                            "folder", "monolith-ecom/products",
                            "resource_type", "image",
                            "public_id", UUID.randomUUID().toString(),
                            "allowed_formats", new String[]{"jpg", "jpeg", "png", "webp"}
                    )
            );
            String secureUrl = (String) uploadResult.get("secure_url");
            String publicId = (String) uploadResult.get("public_id");

            return new CloudinaryUploadResult(secureUrl, publicId);

        } catch (IOException e) {
            throw new RuntimeException("Failed to upload image", e);
        }
    }

    public void delete(String imagePublicId) {
        try {
            cloudinary.uploader().destroy(imagePublicId,
                    ObjectUtils.asMap("resource_type", "image")
            );
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }
}
