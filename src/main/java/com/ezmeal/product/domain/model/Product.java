package com.ezmeal.product.domain.model;

import com.ezmeal.common.entity.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_product", schema = "product_service")
public class Product extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_id")
    private UUID id;

    @Column(name = "company_id", nullable = false)
    private UUID companyId;

    @Column(name = "name", length = 255, nullable = false)
    private String name;

    @Column(name = "description", length = 255)
    private String description;

    @Column(name = "price", nullable = false)
    private Integer price;

    @Column(name = "max_order_count", nullable = false)
    private Integer maxOrderCount;

    @Enumerated(EnumType.STRING)
    @Column(name = "category", nullable = false)
    private ProductCategory category;

    @Enumerated(EnumType.STRING)
    @Column(name = "meal_period", nullable = false)
    private ProductMealPeriod mealPeriod;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProductMealPlan> mealPlans = new ArrayList<>();


    // 상품 생성
    public Product(UUID companyId, String name, String description, Integer price, Integer maxOrderCount,
                   ProductCategory category, ProductMealPeriod mealPeriod) {

        String safeDescription = StringUtils.hasText(description) ? description : null;

        validate(companyId, name, price, maxOrderCount,
                category, mealPeriod, safeDescription);

        this.companyId = companyId;
        this.name = name;
        this.description = safeDescription;
        this.price = price;
        this.maxOrderCount = maxOrderCount;
        this.category = category;
        this.mealPeriod = mealPeriod;

    }

    //상품 수정
    public void update(UUID companyId, String name, String description, Integer price, Integer maxOrderCount,
                       ProductCategory category, ProductMealPeriod mealPeriod) {

        UUID nextCompanyId = companyId != null ? companyId : this.companyId;
        String nextName = name != null ? name : this.name;
        String nextDescription = description != null
                ? (StringUtils.hasText(description) ? description : null)
                : this.description;
        Integer nextPrice = price != null ? price : this.price;
        Integer nextMaxOrderCount = maxOrderCount != null ? maxOrderCount : this.maxOrderCount;
        ProductCategory nextCategory = category != null ? category : this.category;
        ProductMealPeriod nextMealPeriod = mealPeriod != null ? mealPeriod : this.mealPeriod;

        validate(nextCompanyId, nextName, nextPrice, nextMaxOrderCount, nextCategory, nextMealPeriod, nextDescription);

        this.companyId = nextCompanyId;
        this.name = nextName;
        this.description = nextDescription;
        this.price = nextPrice;
        this.maxOrderCount = nextMaxOrderCount;
        this.category = nextCategory;
        this.mealPeriod = nextMealPeriod;

    }


    //빠지면 안되는 값들 검증
    private void validate(UUID companyId, String name, Integer price, Integer maxOrderCount,
                          ProductCategory category, ProductMealPeriod mealPeriod, String description) {
        if (companyId == null) {
            throw new IllegalArgumentException("업체 id는 필수입니다.");
        }

        if (!StringUtils.hasText(name)) {
            throw new IllegalArgumentException("상품명은 필수입니다.");
        } else if (name.length() > 255) {
            throw new IllegalArgumentException("상품명은 길이가 255이하여야 합니다.");
        }

        if (price == null) {
            throw new IllegalArgumentException("상품 가격은 필수입니다.");
        }
        if (price <= 0) {
            throw new IllegalArgumentException("상품 가격은 0보다 커야 합니다.");
        }

        if (maxOrderCount == null) {
            throw new IllegalArgumentException("최대 주문 수량은 필수입니다.");
        }
        if (maxOrderCount <= 0) {
            throw new IllegalArgumentException("최대 주문 수량은 0보다 커야 합니다.");
        }

        if (category == null) {
            throw new IllegalArgumentException("상품 카테고리는 필수입니다.");
        }

        if (mealPeriod == null) {
            throw new IllegalArgumentException("식사 시간대는 필수입니다.");
        }

        if (description != null && description.length() > 255) {
            throw new IllegalArgumentException("상품 정보 길이는 255 이하여야 합니다.");
        }

    }

    public void addMealPlan(ProductMealPlan mealPlan) {

        if (mealPlan == null) {
            throw new IllegalArgumentException("식단은 필수입니다.");
        }

        validateDuplicateDay(mealPlan.getDayOfWeek());

        this.mealPlans.add(mealPlan);
        mealPlan.assignProduct(this);
    }

    private void validateDuplicateDay(DayOfWeek dayOfWeek) {
        boolean exists = mealPlans.stream()
                .anyMatch(mealPlan -> mealPlan.getDayOfWeek() == dayOfWeek);

        if (exists) {
            throw new IllegalArgumentException("같은 요일의 식단은 중복 등록할 수 없습니다.");
        }
    }
}
