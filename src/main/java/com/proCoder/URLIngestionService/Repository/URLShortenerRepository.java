package com.proCoder.URLIngestionService.Repository;

import com.proCoder.URLIngestionService.Model.URLMappingDynamoModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.enhanced.dynamodb.DynamoDbTable;
import software.amazon.awssdk.enhanced.dynamodb.Key;
import software.amazon.awssdk.enhanced.dynamodb.TableSchema;
import software.amazon.awssdk.enhanced.dynamodb.model.PageIterable;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;
import software.amazon.awssdk.services.dynamodb.model.DynamoDbException;

import java.util.ArrayList;
import java.util.List;

@Component
public class URLShortenerRepository {
    private final DynamoDbEnhancedClient dynamoDBClient;
    DynamoDbTable<URLMappingDynamoModel> table;

    @Autowired
    URLShortenerRepository(DynamoDbEnhancedClient dynamoDBClient) {
        this.dynamoDBClient = dynamoDBClient;
        table = getTable();
    }

    public URLMappingDynamoModel findById(String shortURL) {
        List<URLMappingDynamoModel> resultList = new ArrayList<>();
        try {
            PageIterable<URLMappingDynamoModel> iterable = table.query(QueryEnhancedRequest.builder()
                    .queryConditional(QueryConditional.keyEqualTo(Key.builder()
                            .partitionValue(shortURL)
                            .build()))
                    .limit(1)
                    .build());
            iterable.items().forEach(resultList::add);

            if (!resultList.isEmpty()) {
                return resultList.get(0);
            }
        } catch (DynamoDbException ex) {
            System.out.println("Exception: " + ex.toString());
        }
        return null;
    }

    public void insertIntoDB(String longURL, String shortURL) {
        table.putItem(getItem(longURL, shortURL));
    }

    private DynamoDbTable<URLMappingDynamoModel> getTable() {
        if (dynamoDBClient != null) {
            // Create a tablescheme to scan our bean class order
            return dynamoDBClient.table("urlMapperTable",
                    TableSchema.fromBean(URLMappingDynamoModel.class));
        }

        return null;
    }

    private URLMappingDynamoModel getItem(String longURL, String shortURL) {
        URLMappingDynamoModel item = new URLMappingDynamoModel();
        item.setLongFormURL(longURL);
        item.setShortURL(shortURL);

        return item;
    }
}
