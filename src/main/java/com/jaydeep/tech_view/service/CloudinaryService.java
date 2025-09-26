package com.jaydeep.tech_view.service;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudinaryService {

    private final Cloudinary cloudinary;

    private void validateImageFile(MultipartFile file) {
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Only image files are allowed");
        }
        if (file.getSize() > 5 * 1024 * 1024) { // 5MB
            throw new IllegalArgumentException("File size must be less than 5MB");
        }
    }


    // Upload a cover image for a post
    public Map uploadPostCoverImage(MultipartFile file, Long userId, Long postId) throws IOException {
        validateImageFile(file);

        String folderPath = String.format("tech-view/users/%d/posts/%d", userId, postId);

        return cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "folder", folderPath,
                "public_id", "coverImage", // file name inside the folder
                "resource_type", "image"
        ));
    }

    // Delete an image by public_id
    public Map deleteFile(String publicId) throws IOException {
        return cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
    }
}
