package com.idolu.product.presentation.product.response;

import com.idolu.product.domain.product.type.PeriodUnitCode;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class SubscriptionTermDto {

    private Integer contractPeriod; // 계약 기간

    private PeriodUnitCode contractPeriodUnitCode; // 계약 기간 단위

    private Integer servicePeriod; // 서비스 제공주기

    private PeriodUnitCode servicePeriodUnitCode; // 서비스 제공주기 단위
}
