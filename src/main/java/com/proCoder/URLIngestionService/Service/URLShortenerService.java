package com.proCoder.URLIngestionService.Service;

import com.proCoder.URLIngestionService.Repository.URLShortenerRepository;
import com.proCoder.URLIngestionService.Model.URLMappingDynamoModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;


@Service
public class URLShortenerService {
    @Autowired
    private URLShortenerRepository urlShortenerRepository;
    private Logger logger = LoggerFactory.getLogger(URLShortenerService.class);
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
        logger.info("Attempting to get longForm URL for: " + shortURL);
        URLMappingDynamoModel result =
                urlShortenerRepository.findById(shortURL);
        if (result != null) {
            return result.getLongFormURL();
        }

        return null;
    }
}
