package com.ezmeal.product.infrastructure.search.elasticsearch.document;

import com.ezmeal.product.application.search.ProductSearchIndexCommand;
import java.time.LocalDateTime;
import java.util.List;
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
@Document(indexName = "products")
public class ProductSearchDocument {

    @Id
    @Field(type = FieldType.Keyword)
    private String productId;

    @Field(type = FieldType.Keyword)
    private String companyId;
    @Field(type = FieldType.Text)
    private String companyName;
    @Field(type = FieldType.Text)
    private String companyDescription;

    @Field(type = FieldType.Text)
    private String name;
    @Field(type = FieldType.Text)
    private String description;

    @Field(type = FieldType.Integer)
    private Integer price;
    @Field(type = FieldType.Integer)
    private Integer maxOrderCount;
    @Field(type = FieldType.Boolean)
    private Boolean available;

    @Field(type = FieldType.Keyword)
    private String category;
    @Field(type = FieldType.Keyword)
    private String mealPeriod;

    @Field(type = FieldType.Keyword)
    private List<String> availableDays;
    @Field(type = FieldType.Text)
    private List<String> menuNames;
    @Field(type = FieldType.Keyword)
    private List<String> deliveryRegions;

    @Field(type = FieldType.Object)
    private List<ProductMealPlanSearchDocument> mealPlans;
    @Field(type = FieldType.Nested)
    private List<ProductDeliveryAreaSearchDocument> deliveryAreas;

    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime createdAt;
    @Field(type = FieldType.Date, format = DateFormat.date_hour_minute_second_millis)
    private LocalDateTime modifiedAt;

    public static ProductSearchDocument from(ProductSearchIndexCommand command) {
        return ProductSearchDocument.builder()
                .productId(command.productId())
                .companyId(command.companyId())
                .companyName(command.companyName())
                .companyDescription(command.companyDescription())
                .name(command.name())
                .description(command.description())
                .price(command.price())
                .maxOrderCount(command.maxOrderCount())
                .available(command.available())
                .category(command.category())
                .mealPeriod(command.mealPeriod())
                .availableDays(command.availableDays())
                .menuNames(command.menuNames())
                .deliveryRegions(command.deliveryRegions())
                .mealPlans(
                        command.mealPlans() == null ? List.of() :
                                command.mealPlans().stream()
                                        .map(ProductMealPlanSearchDocument::from)
                                        .toList()
                )
                .deliveryAreas(
                        command.deliveryAreas() == null ? List.of() :
                                command.deliveryAreas().stream()
                                        .map(ProductDeliveryAreaSearchDocument::from)
                                        .toList()
                )
                .createdAt(command.createdAt())
                .modifiedAt(command.modifiedAt())
                .build();
    }
}

