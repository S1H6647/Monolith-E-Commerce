package com.project.monolith_e_commerce.web.dto.cloudinary;

public record CloudinaryUploadResult(
        String secureUrl,
        String publicId
) {
}
