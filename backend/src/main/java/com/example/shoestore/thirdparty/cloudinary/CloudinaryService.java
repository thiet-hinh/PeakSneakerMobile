package com.example.shoestore.thirdparty.cloudinary;

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
    private final CloudinaryConfig cloudinaryConfig;

    public String upload(MultipartFile file) throws IOException {
        String originalName = file.getOriginalFilename();
        if(originalName == null){
            throw new NullPointerException("file name empty cannot upload!");
        }
        String publicId = originalName.substring(0, originalName.lastIndexOf("."));

        cloudinary.uploader().upload(file.getBytes(), ObjectUtils.asMap(
                "public_id", publicId, "overwrite", true, "unique_filename", false, "resource_type", "image"
        ));
        return originalName;
    }

    public boolean delete(String imageName) throws IOException {
        String publicId = imageName.substring(0, imageName.lastIndexOf("."));
        Map<?, ?> result = cloudinary.uploader().destroy(publicId, ObjectUtils.asMap("resource_type", "image"));
        return "ok".equals(result.get("result"));
    }

    public String createImageUrl(String imageName){
        return cloudinaryConfig.getBaseUrl()+imageName;
    }



}
