package coverit.image_service.storage.config;

import com.cloudinary.Cloudinary;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class CloudinaryConfig {
    private final String cloudName;
    private final String apiKey;
    private final String apiSecret;

    public CloudinaryConfig(@Value(value = "${cloud_name}") String cloudName,
                            @Value(value = "${cloudinary_api_key}") String apiKey,
                            @Value(value = "${cloudinary_api_secret}") String apiSecret) {
        this.cloudName = cloudName;
        this.apiKey = apiKey;
        this.apiSecret = apiSecret;
    }

    public Cloudinary cloudinaryConfig() {
        Cloudinary cloudinary = null;
        Map config = new HashMap();
        config.put("cloud_name", cloudName);
        config.put("api_key", apiKey);
        config.put("api_secret", apiSecret);
        cloudinary = new Cloudinary(config);
        return cloudinary;
    }
}
