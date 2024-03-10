package com.proCoder.URLIngestionService.Model;

import lombok.Data;
import org.springframework.web.bind.annotation.ResponseBody;

@Data
@ResponseBody
public class GetShortURLResponse {
    String shortenedURL;
}
