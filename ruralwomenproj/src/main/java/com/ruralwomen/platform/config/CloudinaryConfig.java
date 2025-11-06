package com.ruralwomen.platform.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CloudinaryConfig {

    @Bean
    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "db5qgyyss",
                "api_key", "322583564619396",
                "api_secret", "Toq1We-u686tOFoBOkM8JPBaHoU"
        ));
    }
}
