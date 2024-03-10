package com.proCoder.URLIngestionService.Model;

import lombok.Data;

@Data
public class GetShortURLRequest {
    String userName;
    String toShortenURL;

    public GetShortURLRequest(String name, String toShortenURL) {
        this.userName = name;
        this.toShortenURL = toShortenURL;
    }
}
