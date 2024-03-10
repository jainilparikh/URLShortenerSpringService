package com.proCoder.URLIngestionService.Service;

import com.proCoder.URLIngestionService.Repository.URLShortenerRepository;
import com.proCoder.URLIngestionService.Model.URLMappingDynamoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import static com.proCoder.URLIngestionService.Utils.Constants.DOMAIN_NAME;

@Service
public class URLShortenerService {
    @Autowired
    private URLShortenerRepository urlShortenerRepository;
    private String base64chars = "0123456789abcdefghijklmnopqrstuvwxyz_ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";

    public String generateShortenedURL(String longFormURL) {
        String shortURL;
        try {
            do {
                SecureRandom secureRandom = SecureRandom.getInstanceStrong();
                shortURL = secureRandom.ints(8, 0,
                        base64chars.length()).mapToObj(index -> base64chars.charAt(index))
                        .collect(StringBuilder::new, StringBuilder::append, StringBuilder::append)
                        .toString();
            } while (isDuplicate(shortURL));

            insertElemToDb(shortURL, longFormURL);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return shortURL;
    }

    private void insertElemToDb(String shortURL, String longFormURL) {
        urlShortenerRepository.insertIntoDB(longFormURL, shortURL);
    }

    private boolean isDuplicate(String shortURL) {
        URLMappingDynamoModel result =
                urlShortenerRepository.findById(shortURL);

        return result != null && "".equals(result.getShortURL());
    }

    public String getLongFormURL(String shortURL) {
        System.out.println(shortURL.substring(DOMAIN_NAME.length()));
        URLMappingDynamoModel result = urlShortenerRepository.findById(
                shortURL.substring(DOMAIN_NAME.length()));
        if (result != null) {
            return result.getLongFormURL();
        }

        return null;
    }
}
