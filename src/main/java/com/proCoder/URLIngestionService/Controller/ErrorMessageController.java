package com.proCoder.URLIngestionService.Controller;

import com.proCoder.URLIngestionService.Service.URLShortenerService;
import org.apache.logging.log4j.core.util.IOUtils;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.RequestDispatcher;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Handles error scenarios and provides a custom error page
 * replacing the whileLabelError page.
 * author: jainilvp
 */
@RestController
public class ErrorMessageController implements ErrorController {
    private Logger logger = LoggerFactory.getLogger(URLShortenerService.class);

    @RequestMapping(value = "/error", produces = MediaType.TEXT_HTML_VALUE)
    public String handleError(HttpServletRequest request) {
        try {
            Object status = request.getAttribute(RequestDispatcher.ERROR_STATUS_CODE);

            if (status != null) {
                int statusCode = Integer.parseInt(status.toString());

                if (statusCode == HttpStatus.BAD_REQUEST.value()) {
                    return Files.readString(Path.of("src/main/resources/templates/error-400.html"));
                }
            }

            return Files.readString(Path.of("src/main/resources/templates/error-generic.html"));
        } catch (IOException e) {
            logger.error("IOException occurred when attempting to show error-page", e);
        }

        return null;
    }

    @Override
    public String getErrorPath() {
        return "/error";
    }
}
