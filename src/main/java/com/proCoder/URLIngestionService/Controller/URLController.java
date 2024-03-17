package com.proCoder.URLIngestionService.Controller;

import com.proCoder.URLIngestionService.Model.GetShortURLResponse;
import com.proCoder.URLIngestionService.Model.GetShortURLRequest;
import com.proCoder.URLIngestionService.Model.RedirectShortURLRequest;
import com.proCoder.URLIngestionService.Model.RedirectShortURLResponse;
import com.proCoder.URLIngestionService.Service.URLShortenerService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import static com.proCoder.URLIngestionService.Utils.Constants.DOMAIN_NAME;

@RestController
@RequestMapping("")
public class URLController {
    Logger logger = LoggerFactory.getLogger(URLController.class);
    @Autowired
    private URLShortenerService shortenerService;

    /**
     * Health check used by network load balancer.
     * @return: 200 success
     */
    @RequestMapping(value = "/HealthCheck", method = RequestMethod.GET)
    ResponseEntity HealthCheckControlled() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body("Healthy!");
    }

    /**
     * Publicly-exposed API which on providing a long-form URL attempts
     * to return a short 18 bit URL (10 for domain 8 for uniquely identifying the URL)
     * that redirects to the long-form URL.
     *
     * @param getShortURLRequest: long-form URL and user information
     * @return: 200 if success, else 5XX
     */
    @RequestMapping(value = "/getShortenedURL", method = RequestMethod.POST)
    ResponseEntity<GetShortURLResponse>
    getUserData(@RequestBody GetShortURLRequest getShortURLRequest) {
        logger.info("Received request with URL: " + getShortURLRequest.getToShortenURL());
        try {
            GetShortURLResponse response = new GetShortURLResponse();
            String shortURL = shortenerService
                    .generateShortenedURL(getShortURLRequest.getToShortenURL());
            response.setShortenedURL(DOMAIN_NAME + shortURL);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (RuntimeException e) {
            logger.error("getShortenedURL: Error occurred when attempting to create short URL: "
                    + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * Publicly-exposed API which on receipt of a short URL attempts
     * to redirect it to it's long-form counterpart.
     * @param request: short-form URL
     * @return: 307 if success, 400 if no long-form mapping exists and 500 otherwise
     */
    @RequestMapping(value = "/redirect", method = RequestMethod.POST)
    ResponseEntity redirect(@RequestBody RedirectShortURLRequest request) {
        logger.info("Received redirect request with URL: " + request.getShortURL());

        try {
            String longFormURL = shortenerService.getLongFormURL(request.getShortURL());

            if (longFormURL != null) {
                RedirectShortURLResponse response = new RedirectShortURLResponse();
                response.setRedirectURL(longFormURL);
                return ResponseEntity.status(HttpStatus.TEMPORARY_REDIRECT).body(response);
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
            }
        } catch (RuntimeException e) {
            logger.error("Redirect: Error occurred when attempting to redirect URL:"
                    + e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}
