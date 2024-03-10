package com.proCoder.URLIngestionService.Controller;

import com.proCoder.URLIngestionService.Model.GetShortURLResponse;
import com.proCoder.URLIngestionService.Model.GetShortURLRequest;
import com.proCoder.URLIngestionService.Model.RedirectShortURLRequest;
import com.proCoder.URLIngestionService.Model.RedirectShortURLResponse;
import com.proCoder.URLIngestionService.Service.URLShortenerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("")
public class URLController {
    @Autowired
    private URLShortenerService shortenerService;

    @RequestMapping(value = "/HealthCheck", method = RequestMethod.GET)
    ResponseEntity HealthCheckControlled() {
        return ResponseEntity.status(HttpStatus.FOUND)
                .body("Healthy!");
    }

    @RequestMapping(value = "/getShortenedURL", method = RequestMethod.POST)
    ResponseEntity<GetShortURLResponse>
    getUserData(@RequestBody GetShortURLRequest getShortURLRequest) {
        try {
            GetShortURLResponse response = new GetShortURLResponse();
            String shortURL = shortenerService.generateShortenedURL(getShortURLRequest.getToShortenURL());
            response.setShortenedURL("http://localhost:8080?=" + shortURL);

            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @RequestMapping(value = "/redirect", method = RequestMethod.POST)
    ResponseEntity
    redirect(@RequestBody RedirectShortURLRequest request) {
        String longFormURL = shortenerService.getLongFormURL(request.getShortURL());

        if (longFormURL != null) {
            RedirectShortURLResponse response = new RedirectShortURLResponse();
            response.setRedirectURL(longFormURL);
            return ResponseEntity.status(HttpStatus.ACCEPTED).body(response);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }
}
