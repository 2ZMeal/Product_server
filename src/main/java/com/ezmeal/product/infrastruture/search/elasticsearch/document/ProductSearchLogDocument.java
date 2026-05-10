package com.ezmeal.product.infrastruture.search.elasticsearch.document;

import com.ezmeal.product.application.search.log.ProductSearchLogCommand;
import java.time.LocalDateTime;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.DateFormat;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(indexName = "product_search_logs")
public class ProductSearchLogDocument {

    @Id
    @Field(type = FieldType.Keyword)
    private String id;

    @Field(type = FieldType.Keyword)
    private String userId;
    @Field(type = FieldType.Keyword)
    private String keyword;
    @Field(type = FieldType.Keyword)
    private String category;
    @Field(type = FieldType.Keyword)
    private String mealPeriod;
    @Field(type = FieldType.Keyword)
    private String region;
    @Field(type = FieldType.Integer)
    private Integer minPrice;
    @Field(type = FieldType.Integer)
    private Integer maxPrice;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime searchedAt;

    public static ProductSearchLogDocument from(ProductSearchLogCommand command) {
        return ProductSearchLogDocument.builder()
                .id(UUID.randomUUID().toString())
                .userId(command.userId())
                .keyword(command.keyword())
                .category(command.category())
                .mealPeriod(command.mealPeriod())
                .region(command.region())
                .minPrice(command.minPrice())
                .maxPrice(command.maxPrice())
                .searchedAt(command.searchedAt())
                .build();
    }
}
