package main_service.card.cover.client;

import lombok.extern.slf4j.Slf4j;
import main_service.card.cover.server.service.UrlDto;
import main_service.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Service
@Slf4j
public class CoverClient extends HttpClient {
    private static final String API_PREFIX = "/";

    @Autowired
    public CoverClient(@Value("${image-server.url}") String serverUrl, RestTemplateBuilder builder) {
        super(builder.uriTemplateHandler(
                        new DefaultUriBuilderFactory(serverUrl + API_PREFIX))
                .requestFactory(HttpComponentsClientHttpRequestFactory::new) //тут возникает ошибка
                .build());
    }

    public String createCover(UrlDto urlDto, Constants.Vibe vibe, Boolean isAbstract) {
        log.debug("[COVER CLIENT] sending request to generate cover for playlist {} to Image-Service",
                urlDto.getLink());

        ResponseEntity res = this.cover(urlDto, vibe, isAbstract);

        if (res.getStatusCode().is2xxSuccessful() && res.getBody() != null) {
            return res.getBody().toString();
        } else {
            throw new RuntimeException("incorrect response from image-server. http status: "
                    + res.getStatusCode() + "\n. body: \n" + res.getBody());
            //TODO проконсультироваться и понять, как назвать кастомную ошибку
        }
    }
}