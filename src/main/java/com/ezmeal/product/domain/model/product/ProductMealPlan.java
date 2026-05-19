package com.ezmeal.product.domain.model.product;

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
@Table(name = "p_product_meal_plan")
public class ProductMealPlan extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
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

    //s3이미지 키
    @Column(name = "image_key", length = 500)
    private String imageKey;

    // 요일별 식단 생성
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


    //혹시 나중에 개별 수정 하게 될 경우를 위해 남겨둠
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

    //검증
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

    //상품에 요일별식단을 연결하는데 이미 연결된 상품이 있나 검증 후 연결
    void assignProduct(Product product) {
        if (product == null) {
            throw new IllegalArgumentException("상품은 필수입니다.");
        }
        if (this.product != null && this.product != product) {
            throw new IllegalArgumentException("이미 다른 상품에 연결된 식단입니다.");
        }
        this.product = product;
    }

    public void updateImageKey(String imageKey) {
        if (!StringUtils.hasText(imageKey)) {
            throw new IllegalArgumentException("식단 이미지 키는 필수입니다.");
        }

        this.imageKey = imageKey;
    }
}
