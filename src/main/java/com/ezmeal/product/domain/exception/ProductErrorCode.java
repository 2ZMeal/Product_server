package com.ezmeal.product.domain.exception;

import com.ezmeal.common.exception.ErrorCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@Getter
@RequiredArgsConstructor
public enum ProductErrorCode implements ErrorCode {

    PRODUCT_NOT_FOUND(HttpStatus.NOT_FOUND, "PRODUCT_404", "상품을 찾을 수 없습니다."),
    PRODUCT_MEAL_PLAN_REQUIRED(HttpStatus.BAD_REQUEST, "PRODUCT_MEAL_PLAN_400", "요일별 식단은 최소 1개 이상 필요합니다."),
    PRODUCT_INVALID_REQUEST(HttpStatus.BAD_REQUEST, "PRODUCT_400", "잘못된 상품 요청입니다."),
    PRODUCT_ACCESS_DENIED(HttpStatus.FORBIDDEN, "PRODUCT_403", "상품에 접근할 권한이 없습니다."),
    PRODUCT_ALREADY_EXISTS(HttpStatus.CONFLICT, "PRODUCT_409", "이미 존재하는 상품입니다."),
    PRODUCT_MEAL_PLAN_DUPLICATED_DAY(HttpStatus.CONFLICT, "PRODUCT_MEAL_PLAN_409", "같은 요일의 식단은 중복 등록할 수 없습니다."),
    COMPANY_NOT_FOUND(HttpStatus.NOT_FOUND, "COMPANY_404", "업체를 찾을 수 없습니다."),
    PRODUCT_ORDER_QUANTITY_INVALID(HttpStatus.BAD_REQUEST, "PRODUCT_ORDER_QUANTITY_400", "주문 수량은 1개 이상이어야 합니다."),
    PRODUCT_ORDER_QUANTITY_EXCEEDED(HttpStatus.CONFLICT, "PRODUCT_ORDER_QUANTITY_409", "주문 가능 수량이 부족합니다."),
    PRODUCT_RECOMMENDATION_LOGIN_REQUIRED(HttpStatus.UNAUTHORIZED, "PRODUCT_RECOMMENDATION_401",
            "추천 상품 조회는 로그인이 필요합니다.");

    private final HttpStatus status;
    private final String code;
    private final String message;

}
