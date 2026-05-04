package com.ezmeal.product.infrastruture.search.elasticsearch.document;

import com.ezmeal.product.application.search.ProductSearchIndexCommand;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Builder
@Document(indexName = "products")
public class ProductSearchDocument {

    @Id
    private String productId;

    private String companyId;
    private String companyName;
    private String companyDescription;

    private String name;
    private String description;

    private Integer price;
    private Integer maxOrderCount;
    private Boolean available;

    private String category;
    private String mealPeriod;

    private List<String> availableDays;
    private List<String> menuNames;
    private List<String> deliveryRegions;

    private List<ProductMealPlanSearchDocument> mealPlans;
    private List<ProductDeliveryAreaSearchDocument> deliveryAreas;

    private LocalDateTime createdAt;
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

