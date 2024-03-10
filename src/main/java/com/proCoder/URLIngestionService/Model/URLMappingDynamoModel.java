package com.proCoder.URLIngestionService.Model;

import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBAttribute;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBHashKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBRangeKey;
import com.amazonaws.services.dynamodbv2.datamodeling.DynamoDBTable;
import lombok.Data;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbAttribute;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbBean;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbPartitionKey;
import software.amazon.awssdk.enhanced.dynamodb.mapper.annotations.DynamoDbSortKey;

@Data
@DynamoDbBean
public class URLMappingDynamoModel {

    private String longFormURL;

    private String shortURL;

    @DynamoDbPartitionKey
    @DynamoDbAttribute("shortURL")
    public String getShortURL() {
        return shortURL;
    }
    @DynamoDbSortKey
    @DynamoDbAttribute("longFormURL")
    public String getLongFormURL() {
        return longFormURL;
    }
    public void setLongFormURL(String longFormURL) {
        this.longFormURL = longFormURL;
    }
    public void setShortURL(String shortURL) {
        this.shortURL = shortURL;
    }
}
