package com.ezmeal.product.domain.model;

import com.ezmeal.common.entity.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.DayOfWeek;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.util.StringUtils;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "p_product_meal_plan", schema = "product_service")
public class ProductMealPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_meal_plan_id")
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Enumerated(EnumType.STRING)
    @Column(name = "day_of_week", nullable = false)
    private DayOfWeek dayOfWeek;

    @Column(name = "menu_name", nullable = false, length = 255)
    private String menuName;

    @Column(name = "allergy_info", length = 255)
    private String allergyInfo;

    @Column(name = "nutrition_info", length = 255)
    private String nutritionInfo;

    public ProductMealPlan(DayOfWeek dayOfWeek, String menuName, String allergyInfo,
                           String nutritionInfo) {

        String safeAllergyInfo = StringUtils.hasText(allergyInfo) ? allergyInfo : null;
        String safeNutritionInfo = StringUtils.hasText(nutritionInfo) ? nutritionInfo : null;

        validation(dayOfWeek, menuName, safeAllergyInfo, safeNutritionInfo);

        this.dayOfWeek = dayOfWeek;
        this.menuName = menuName;
        this.allergyInfo = safeAllergyInfo;
        this.nutritionInfo = safeNutritionInfo;
    }


    public void update(DayOfWeek dayOfWeek, String menuName, String allergyInfo,
                       String nutritionInfo) {
        DayOfWeek nextDayOfWeek = dayOfWeek != null ? dayOfWeek : this.dayOfWeek;
        String nextMenuName = menuName != null ? menuName : this.menuName;
        String nextAllergyInfo = allergyInfo != null
                ? (StringUtils.hasText(allergyInfo) ? allergyInfo : null)
                : this.allergyInfo;

        String nextNutritionInfo = nutritionInfo != null
                ? (StringUtils.hasText(nutritionInfo) ? nutritionInfo : null)
                : this.nutritionInfo;

        validation(nextDayOfWeek, nextMenuName, nextAllergyInfo, nextNutritionInfo);

        this.dayOfWeek = nextDayOfWeek;
        this.menuName = nextMenuName;
        this.allergyInfo = nextAllergyInfo;
        this.nutritionInfo = nextNutritionInfo;
    }

    private void validation(DayOfWeek dayOfWeek, String menuName, String allergyInfo,
                            String nutritionInfo) {

        if (dayOfWeek == null) {
            throw new IllegalArgumentException("요일 선택은 필수입니다.");
        }
        if (!StringUtils.hasText(menuName)) {
            throw new IllegalArgumentException("메뉴 이름은 필수입니다.");
        } else if (menuName.length() > 255) {
            throw new IllegalArgumentException("메뉴 이름은 길이가 255이하여야 합니다.");
        }
        if (allergyInfo != null && allergyInfo.length() > 255) {
            throw new IllegalArgumentException("알레르기 정보 길이는 255 이하여야합니다.");
        }
        if (nutritionInfo != null && nutritionInfo.length() > 255) {
            throw new IllegalArgumentException("영양성분 정보 길이는 255 이하여야합니다.");
        }

    }

    void assignProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("상품은 필수입니다.");
        }
        this.product = product;
    }

}
